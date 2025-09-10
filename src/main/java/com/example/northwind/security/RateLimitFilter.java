package com.example.northwind.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
	private static final long WINDOW_MS = 10_000; // 10s
	private static final int MAX_REQUESTS = 100; // per IP per window

	private static class Counter { long windowStart = Instant.now().toEpochMilli(); int count = 0; }
	private final Map<String, Counter> ipCounters = new ConcurrentHashMap<>();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (HttpMethod.GET.matches(request.getMethod())) {
			String ip = request.getRemoteAddr();
			Counter c = ipCounters.computeIfAbsent(ip, k -> new Counter());
			long now = Instant.now().toEpochMilli();
			if (now - c.windowStart > WINDOW_MS) { c.windowStart = now; c.count = 0; }
			c.count++;
			if (c.count > MAX_REQUESTS) {
				response.setStatus(429);
				response.getWriter().write("Too Many Requests");
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}

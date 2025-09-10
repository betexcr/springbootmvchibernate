package com.example.northwind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.northwind.security.JwtAuthFilter;
import com.example.northwind.security.RateLimitFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class SecurityConfig {
	private final JwtAuthFilter jwtAuthFilter;
	private final RateLimitFilter rateLimitFilter;
	public SecurityConfig(JwtAuthFilter jwtAuthFilter, RateLimitFilter rateLimitFilter) { this.jwtAuthFilter = jwtAuthFilter; this.rateLimitFilter = rateLimitFilter; }
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/products/**", "/api/categories/**", "/api/suppliers/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/customers/**", "/api/orders/**", "/api/employees/**").hasAnyRole("STAFF", "ADMIN")
				.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
				.requestMatchers("/actuator/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}

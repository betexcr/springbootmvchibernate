package com.example.northwind.controller;

import com.example.northwind.security.JwtService;
import com.example.northwind.security.DbUserDetailsService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
	private final JwtService jwtService;
	private final DbUserDetailsService userDetailsService;
	private final AuthenticationManager authenticationManager;
	public AuthController(JwtService jwtService, DbUserDetailsService userDetailsService, AuthenticationManager authenticationManager) { this.jwtService = jwtService; this.userDetailsService = userDetailsService; this.authenticationManager = authenticationManager; }

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest req) {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username, req.password));
		SecurityContextHolder.getContext().setAuthentication(auth);
		var userDetails = userDetailsService.loadUserByUsername(req.username);
		java.util.List<String> roles = userDetails.getAuthorities().stream().map(a -> a.getAuthority().replace("ROLE_","" )).toList();
		String token = jwtService.generateToken(req.username, Map.of("roles", roles), 3600);
		return ResponseEntity.ok(Map.of("access_token", token, "token_type", "Bearer"));
	}

	@PostMapping("/refresh")
	public ResponseEntity<Map<String, String>> refresh(@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) return ResponseEntity.status(401).body(Map.of("error","missing_token"));
		String old = authHeader.substring(7);
		var claims = jwtService.parse(old);
		String username = claims.getSubject();
		java.util.List<String> roles = (java.util.List<String>) claims.get("roles");
		String token = jwtService.generateToken(username, Map.of("roles", roles), 3600);
		return ResponseEntity.ok(Map.of("access_token", token, "token_type", "Bearer"));
	}

	public static class LoginRequest {
		@NotBlank public String username;
		@NotBlank public String password;
	}
}

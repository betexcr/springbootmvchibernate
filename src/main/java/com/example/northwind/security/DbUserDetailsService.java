package com.example.northwind.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DbUserDetailsService implements UserDetailsService {
	private final UserAccountRepository repo;
	public DbUserDetailsService(UserAccountRepository repo){this.repo = repo;}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount ua = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		List<GrantedAuthority> authorities = Arrays.stream(ua.getRoles().split(","))
			.map(r -> new SimpleGrantedAuthority("ROLE_" + r.trim()))
			.collect(Collectors.toList());
		return new User(ua.getUsername(), ua.getPassword(), ua.isEnabled(), true, true, true, authorities);
	}
}

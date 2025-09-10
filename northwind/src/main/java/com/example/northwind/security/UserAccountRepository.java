package com.example.northwind.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
	Optional<UserAccount> findByUsername(String username);
}

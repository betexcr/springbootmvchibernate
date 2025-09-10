package com.example.northwind.service;

import com.example.northwind.entity.Customer;
import com.example.northwind.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository repository;

	public Page<Customer> list(Pageable pageable) { return repository.findAll(pageable); }
	public Customer get(String id) { return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found")); }

	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public Customer create(Customer c) { return repository.save(c); }
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public Customer update(String id, Customer c) { Customer existing = get(id); c.setId(existing.getId()); return repository.save(c); }
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(String id) { repository.deleteById(id); }
}

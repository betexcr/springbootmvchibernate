package com.example.northwind.service;

import com.example.northwind.entity.Employee;
import com.example.northwind.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
	private final EmployeeRepository repository;

	public Page<Employee> list(Pageable pageable) { return repository.findAll(pageable); }
	public Employee get(Integer id) { return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found")); }

	@PreAuthorize("hasRole('ADMIN')")
	public Employee create(Employee e) { return repository.save(e); }
	@PreAuthorize("hasRole('ADMIN')")
	public Employee update(Integer id, Employee e) { Employee existing = get(id); e.setId(existing.getId()); return repository.save(e); }
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(Integer id) { repository.deleteById(id); }
}

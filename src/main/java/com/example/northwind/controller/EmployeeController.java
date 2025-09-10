package com.example.northwind.controller;

import com.example.northwind.entity.Employee;
import com.example.northwind.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
	private final EmployeeService service;
	@GetMapping public Page<Employee> list(Pageable p){return service.list(p);}    
	@GetMapping("/{id}") public Employee get(@PathVariable Integer id){return service.get(id);}    
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public Employee create(@RequestBody Employee e){return service.create(e);}    
	@PutMapping("/{id}") public Employee update(@PathVariable Integer id,@RequestBody Employee e){return service.update(id,e);}    
	@DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Integer id){service.delete(id);}    
}

package com.example.northwind.controller;

import com.example.northwind.entity.Customer;
import com.example.northwind.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
	private final CustomerService service;
	@GetMapping public Page<Customer> list(Pageable p){return service.list(p);}    
	@GetMapping("/{id}") public Customer get(@PathVariable String id){return service.get(id);}    
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public Customer create(@RequestBody Customer c){return service.create(c);}    
	@PutMapping("/{id}") public Customer update(@PathVariable String id,@RequestBody Customer c){return service.update(id,c);}    
	@DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable String id){service.delete(id);}    
}

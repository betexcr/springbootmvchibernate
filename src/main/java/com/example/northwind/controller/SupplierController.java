package com.example.northwind.controller;

import com.example.northwind.entity.Supplier;
import com.example.northwind.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
	private final SupplierService service;
	@GetMapping public Page<Supplier> list(Pageable p){return service.list(p);}    
	@GetMapping("/{id}") public Supplier get(@PathVariable Integer id){return service.get(id);}    
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public Supplier create(@RequestBody Supplier s){return service.create(s);}    
	@PutMapping("/{id}") public Supplier update(@PathVariable Integer id,@RequestBody Supplier s){return service.update(id,s);}    
	@DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Integer id){service.delete(id);}    
}

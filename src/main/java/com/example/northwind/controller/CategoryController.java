package com.example.northwind.controller;

import com.example.northwind.entity.Category;
import com.example.northwind.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService service;
	@GetMapping public Page<Category> list(Pageable p){return service.list(p);}    
	@GetMapping("/{id}") public Category get(@PathVariable Integer id){return service.get(id);}    
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public Category create(@RequestBody Category c){return service.create(c);}    
	@PutMapping("/{id}") public Category update(@PathVariable Integer id,@RequestBody Category c){return service.update(id,c);}    
	@DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Integer id){service.delete(id);}    
}

package com.example.northwind.service;

import com.example.northwind.entity.Category;
import com.example.northwind.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository repo;
	public Page<Category> list(Pageable p){return repo.findAll(p);}    
	public Category get(Integer id){return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));}
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public Category create(Category c){return repo.save(c);}    
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public Category update(Integer id, Category c){ c.setId(id); return repo.save(c);}    
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public void delete(Integer id){repo.deleteById(id);}    
}

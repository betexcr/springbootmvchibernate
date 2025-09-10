package com.example.northwind.service;

import com.example.northwind.entity.Supplier;
import com.example.northwind.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierService {
	private final SupplierRepository repo;
	public Page<Supplier> list(Pageable p){return repo.findAll(p);}    
	public Supplier get(Integer id){return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier not found"));}
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public Supplier create(Supplier s){return repo.save(s);}    
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public Supplier update(Integer id, Supplier s){ s.setId(id); return repo.save(s);}    
	@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
	public void delete(Integer id){repo.deleteById(id);}    
}

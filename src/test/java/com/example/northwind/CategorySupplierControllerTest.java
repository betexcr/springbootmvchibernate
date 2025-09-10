package com.example.northwind;

import com.example.northwind.controller.CategoryController;
import com.example.northwind.controller.SupplierController;
import com.example.northwind.entity.Category;
import com.example.northwind.entity.Supplier;
import com.example.northwind.security.JwtAuthFilter;
import com.example.northwind.service.CategoryService;
import com.example.northwind.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = {CategoryController.class, SupplierController.class})
@AutoConfigureMockMvc(addFilters = false)
class CategorySupplierControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean CategoryService categoryService;
    @MockBean SupplierService supplierService;
    @MockBean JwtAuthFilter jwtAuthFilter;

    @Test
    void listCategories_ok() throws Exception {
        Page<Category> page = new PageImpl<>(List.of(), PageRequest.of(0,10), 0);
        Mockito.when(categoryService.list(Mockito.any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories").param("page","0").param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void listSuppliers_ok() throws Exception {
        Page<Supplier> page = new PageImpl<>(List.of(), PageRequest.of(0,10), 0);
        Mockito.when(supplierService.list(Mockito.any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/suppliers").param("page","0").param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

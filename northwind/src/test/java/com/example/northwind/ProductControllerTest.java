package com.example.northwind;

import com.example.northwind.controller.ProductController;
import com.example.northwind.entity.Product;
import com.example.northwind.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.example.northwind.security.JwtAuthFilter;

import java.util.List;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean ProductService productService;
    @MockBean JwtAuthFilter jwtAuthFilter;

    @Test
    void listProducts_withFilters_ok() throws Exception {
        Page<Product> page = new PageImpl<>(List.of(), PageRequest.of(0,10), 0);
        Mockito.when(productService.list(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products").param("q","tea").param("page","0").param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

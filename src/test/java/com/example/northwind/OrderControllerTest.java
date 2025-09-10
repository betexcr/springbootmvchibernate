package com.example.northwind;

import com.example.northwind.controller.OrderController;
import com.example.northwind.service.OrderDetailService;
import com.example.northwind.service.OrderService;
import com.example.northwind.entity.Order;
import com.example.northwind.dto.OrderSummaryDto;
import com.example.northwind.security.JwtAuthFilter;
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

import java.math.BigDecimal;
import java.util.List;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean OrderService orderService;
    @MockBean OrderDetailService orderDetailService;
    @MockBean JwtAuthFilter jwtAuthFilter;

    @Test
    void listOrders_withFilters_ok() throws Exception {
        Page<Order> page = new PageImpl<>(List.of(), PageRequest.of(0,10), 0);
        Mockito.when(orderService.list(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders").param("customerId","ALFKI").param("page","0").param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void orderSummary_ok() throws Exception {
        Mockito.when(orderService.get(Mockito.anyInt())).thenReturn(new Order());
        Mockito.when(orderService.computeTotal(Mockito.anyInt())).thenReturn(BigDecimal.ZERO);
        Mockito.when(orderDetailService.list(Mockito.anyInt())).thenReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/10248/summary"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

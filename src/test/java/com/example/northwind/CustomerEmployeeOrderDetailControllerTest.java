package com.example.northwind;

import com.example.northwind.controller.CustomerController;
import com.example.northwind.controller.EmployeeController;
import com.example.northwind.controller.OrderDetailController;
import com.example.northwind.entity.Customer;
import com.example.northwind.entity.Employee;
import com.example.northwind.entity.OrderDetail;
import com.example.northwind.security.JwtAuthFilter;
import com.example.northwind.service.CustomerService;
import com.example.northwind.service.EmployeeService;
import com.example.northwind.service.OrderDetailService;
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

@WebMvcTest(controllers = {CustomerController.class, EmployeeController.class, OrderDetailController.class})
@AutoConfigureMockMvc(addFilters = false)
class CustomerEmployeeOrderDetailControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean CustomerService customerService;
    @MockBean EmployeeService employeeService;
    @MockBean OrderDetailService orderDetailService;
    @MockBean JwtAuthFilter jwtAuthFilter;

    @Test
    void listCustomers_ok() throws Exception {
        Page<Customer> page = new PageImpl<>(List.of(), PageRequest.of(0,10), 0);
        Mockito.when(customerService.list(Mockito.any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers").param("page","0").param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void listEmployees_ok() throws Exception {
        Page<Employee> page = new PageImpl<>(List.of(), PageRequest.of(0,10), 0);
        Mockito.when(employeeService.list(Mockito.any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees").param("page","0").param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void orderDetails_list_and_total_ok() throws Exception {
        Page<OrderDetail> page = new PageImpl<>(List.of(), PageRequest.of(0,5), 0);
        Mockito.when(orderDetailService.list(Mockito.anyInt(), Mockito.any())).thenReturn(page);
        Mockito.when(orderDetailService.summaryTotal(Mockito.anyInt())).thenReturn(BigDecimal.ZERO);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/10248/details").param("page","0").param("size","5"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/10248/details/summary/total"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

package com.java.spring_boot.controller;

import com.java.spring_boot.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ProductController.class})
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @MockitoBean
    private ProductServiceImpl productService;

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void shouldReturnActiveProducts() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }

    @Test
    void shouldReturnActiveProducts() throws Exception{
        BDDMockito.given(productService.getAllProducts()).willReturn(List.of());
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }
}

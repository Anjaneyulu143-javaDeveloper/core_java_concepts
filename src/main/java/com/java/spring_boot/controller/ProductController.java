package com.java.spring_boot.controller;

import com.java.spring_boot.model.Product;
import com.java.spring_boot.service.ProductServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllCustomers() {
        return ResponseEntity.ok(this.productServiceImpl.getAllProducts());

    }
}



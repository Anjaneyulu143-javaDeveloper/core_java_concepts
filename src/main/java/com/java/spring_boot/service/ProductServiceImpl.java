package com.java.spring_boot.service;

import com.java.spring_boot.model.Product;
import com.java.spring_boot.repository.productRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl {

    private final productRepository productRepo;

    public ProductServiceImpl(productRepository productRepo) {
        this.productRepo=productRepo;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts(){
        return this.productRepo.findAll().stream().filter(a->!a.isDisabled()).toList();
    }
}

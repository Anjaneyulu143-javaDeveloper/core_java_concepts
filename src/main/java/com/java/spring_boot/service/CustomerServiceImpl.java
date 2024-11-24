package com.java.spring_boot.service;

import com.java.spring_boot.model.Customer;
import com.java.spring_boot.repository.customerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl {

    private final customerRepository customerRepo;

    public CustomerServiceImpl(customerRepository customerRepo) {
        this.customerRepo=customerRepo;
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return this.customerRepo.findAll();
    }
}

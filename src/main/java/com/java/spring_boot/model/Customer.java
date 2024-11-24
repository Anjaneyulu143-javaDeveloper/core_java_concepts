package com.java.spring_boot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Setter
@Getter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "customer_id_generator")
    @SequenceGenerator(name="customer_id_generator",sequenceName = "customer_id_seq")
    private Long customerId;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String name;
}

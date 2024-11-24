package com.java.spring_boot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_sequence")
    private Long productId;

    @Column(nullable = false,unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private boolean disabled;
}

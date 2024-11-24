package com.java.spring_boot.service;

import com.java.spring_boot.model.Product;
import com.java.spring_boot.repository.productRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

//    @Mock
//    private productRepository productRepo;
//
//    @InjectMocks
//    private ProductServiceImpl productService;

    private productRepository productRepo;
    private ProductServiceImpl productService;

    @BeforeEach
    void setup(){
        productRepo= Mockito.mock(productRepository.class);
        productService = new ProductServiceImpl(productRepo);
    }

    @Test
    void ShouldReturnActiveProducts() {
        // Arrange
        Product p1 = new Product(1L, "p-name1", "p-description1", BigDecimal.TEN,false);
        Product p2 = new Product(2L, "p-name2", "p-description2", BigDecimal.TEN,true);
        // acceptation
        BDDMockito.given(productRepo.findAll()).willReturn(List.of(p1, p2));

        //Act
        List<Product> products = productService.getAllProducts();

        //Assert
        assertThat(products).hasSize(1);
        assertThat(products.getFirst().getProductId()).isEqualTo(1L);
    }
}

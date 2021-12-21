package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Inquiry;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.repositories.InquiryRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InquiryServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    InquiryRepository inquiryRepository;

    @Mock
    EmailService emailService;

    @InjectMocks
    InquiryService inquiryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveInquiry() {
        Product product = Product.builder()
                .id(1L)
                .amountInStock(1)
                .description("product")
                .build();

        Inquiry inquiry = Inquiry.builder()
                .id(1L)
                .product(product)
                .build();


    }

    @Test
    void getInquiryById() {
    }

}
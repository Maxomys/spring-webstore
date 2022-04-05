package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.InquiryDto;
import com.github.maxomys.webstore.api.mappers.InquiryMapper;
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

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InquiryServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    InquiryRepository inquiryRepository;

    @Mock
    EmailService emailService;

    InquiryServiceImpl inquiryService;

    @BeforeEach
    void setUp() {
        inquiryService = new InquiryServiceImpl(productRepository, inquiryRepository, emailService, new InquiryMapper());
    }

    @Test
    void saveInquiryTest() {
        Product product = Product.builder()
                .id(1L)
                .amountInStock(1)
                .description("product")
                .inquiries(new HashSet<>())
                .build();

        InquiryDto inquiryDto = InquiryDto.builder()
                .id(1L)
                .productId(1L)
                .build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        inquiryService.saveInquiry(inquiryDto);

        assertTrue(product.getInquiries().size() > 0);
    }

    @Test
    void getInquiryByIdExceptionTest() {
        when(inquiryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> inquiryService.getInquiryById(1L));
    }

}

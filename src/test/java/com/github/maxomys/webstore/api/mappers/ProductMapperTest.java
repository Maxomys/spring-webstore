package com.github.maxomys.webstore.api.mappers;

import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProductMapperImpl.class})
class ProductMapperTest {

    @Autowired
    ProductMapper productMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void productToProductDto() {
        User user = User.builder()
                .id(1L)
                .username("Joe")
                .build();
        Image image = Image.builder()
                .id(1L)
                .build();
        Category category = Category.builder()
                .id(1L)
                .description("Category")
                .build();
        Inquiry inquiry = Inquiry.builder()
                .id(1L)
                .build();
        Inquiry inquiry2 = Inquiry.builder()
                .id(2L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("A Product")
                .user(user)
                .images(List.of(image))
                .category(category)
                .inquiries(Set.of(inquiry, inquiry2))
                .build();

        ProductDto dto = productMapper.productToProductDto(product);

        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getUserId());
        assertEquals("Joe", dto.getUsername());
        assertEquals("Category", dto.getCategoryName());
        assertEquals(1, dto.getImageIds().size());
        assertEquals(2, dto.getInquiryIds().size());
    }

}

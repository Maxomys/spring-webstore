package com.github.maxomys.webstore.api.mappers;

import lombok.RequiredArgsConstructor;
import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.auth.UserService;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.services.CategoryService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductDto productToProductDto(Product product) {      
        return ProductDto.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .uniqueAddresses(product.getUniqueAddresses())
            .userName(product.getUser().getUsername())
            .userId(product.getUser().getId())
            .description(product.getDescription())
            .imageIds(product.getImages().stream()
                .map(image -> image.getId())
                .collect(Collectors.toList()))
            .creatorName(product.getCreatorName())
            .phoneNumber(product.getPhoneNumber())
            .categoryName(product.getCategory().getDescription())
            .categoryId(product.getCategory().getId())
            .inquiryIds(product.getInquiries().stream()
                .map(inquiry -> inquiry.getId())
                .collect(Collectors.toList()))
            .build();
    }

    public Product productDtoToProduct(ProductDto dto) {
        return Product.builder()
            .name(dto.getName())
            .price(dto.getPrice())
            .user(userService.getUserById(dto.getUserId()))
            .description(dto.getDescription())
            .creatorName(dto.getCreatorName())
            .phoneNumber(dto.getPhoneNumber())
            .category(categoryService.findById(dto.getCategoryId()))
            .build();
    }

}

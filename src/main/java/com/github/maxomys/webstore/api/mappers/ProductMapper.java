package com.github.maxomys.webstore.api.mappers;

import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    
    public ProductDto productToProductDto(Product product) {
        return ProductDto.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .uniqueAddresses(product.getUniqueAddresses())
            .username(product.getUser().getUsername())
            .userId(product.getUser().getId())
            .description(product.getDescription())
            .imageIds(product.getImages().stream()
                .map(image -> image.getId())
                .collect(Collectors.toList()))
            .phoneNumber(product.getPhoneNumber())
            .categoryName(product.getCategory().getDescription())
            .categoryId(product.getCategory().getId())
            .inquiryIds(product.getInquiries().stream()
                .map(inquiry -> inquiry.getId())
                .collect(Collectors.toList()))
            .amountInStock(product.getAmountInStock())
            .build();
    }

    public Product productDtoToProduct(ProductDto dto) {
        return Product.builder()
            .name(dto.getName())
            .price(dto.getPrice())
            .description(dto.getDescription())
            .phoneNumber(dto.getPhoneNumber())
            .amountInStock(dto.getAmountInStock())
            .build();
    }

}

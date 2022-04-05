package com.github.maxomys.webstore.api.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class ProductDto {
    
    private Long id;
    private String name;
    private Integer price;
    private String createdAt;
    private Integer amountInStock;
    private Set<String> uniqueAddresses;
    private Long userId;
    private String username;
    private String description;
    private List<Long> imageIds;
    private String phoneNumber;
    private Long categoryId;
    private String categoryName;
    private List<Long> inquiryIds;

}

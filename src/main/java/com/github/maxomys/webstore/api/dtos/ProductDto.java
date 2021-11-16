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
    private Set<String> uniqueAddresses;
    private String userName;
    private Long userId;
    private String description;
    private List<Long> imageIds;
    private String creatorName;
    private String phoneNumber;
    private String categoryName;
    private Long categoryId;
    private List<Long> inquiryIds;

}

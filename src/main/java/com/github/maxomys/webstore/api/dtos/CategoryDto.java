package com.github.maxomys.webstore.api.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryDto {
    
    private Long id;
    private String description;
    private List<Long> productIds;

}

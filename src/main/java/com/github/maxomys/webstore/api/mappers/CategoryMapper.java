package com.github.maxomys.webstore.api.mappers;

import lombok.RequiredArgsConstructor;
import com.github.maxomys.webstore.api.dtos.CategoryDto;
import com.github.maxomys.webstore.domain.Category;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    
    public CategoryDto categoryToCategoryDto(Category category) {
        return CategoryDto.builder()
            .id(category.getId())
            .description(category.getDescription())
            .productIds(category.getProducts().stream()
                .map(product -> product.getId())
                .collect(Collectors.toList()))
            .build();
    }

    public Category categoryDtoToCategory(CategoryDto dto) {
        return Category.builder()
            .description(dto.getDescription())
            .build();
    }

}

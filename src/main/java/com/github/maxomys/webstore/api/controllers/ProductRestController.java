package com.github.maxomys.webstore.api.controllers;

import lombok.RequiredArgsConstructor;
import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.api.mappers.ProductMapper;
import com.github.maxomys.webstore.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    @GetMapping("/all")
    public List<ProductDto> getAllProducts() {
        return productService.getProducts().stream()
            .map(productMapper::productToProductDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) {
        return productMapper.productToProductDto(productService.findById(productId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductDto productDto) {
        productService.saveProduct(productMapper.productDtoToProduct(productDto));
    }

    @DeleteMapping("/{productId}")
    public void deleteProductById(@PathVariable Long productId) {
        productService.deleteById(productId);
    }

}

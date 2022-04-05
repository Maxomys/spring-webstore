package com.github.maxomys.webstore.api.controllers;

import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/all")
    public List<ProductDto> getAllProducts() {
        return productService.getProducts();
    }

    @GetMapping("/my")
    public List<ProductDto> getAllProductsForCurrentUser() {
        return productService.getAllProductsForCurrentUser();
    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) {
        return productService.findById(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.saveProduct(productDto);
        return savedProduct;
    }

    @DeleteMapping("/{productId}")
    public void deleteProductById(@PathVariable Long productId) {
        productService.deleteById(productId);
    }

    @PutMapping
    public void updateProduct(ProductDto productDto) {
        productService.updateProduct(productDto);
    }

}

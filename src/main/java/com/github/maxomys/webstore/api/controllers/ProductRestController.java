package com.github.maxomys.webstore.api.controllers;

import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/page")
    public Page<ProductDto> getAllProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PageRequest request;

        if (sortDir.equals("desc")) {
            request = PageRequest.of(page, size, Sort.by(sort).descending());
        } else {
            request = PageRequest.of(page, size, Sort.by(sort).ascending());
        }

        return productService.getProductDtoPage(request);
    }

    @GetMapping("/category/{categoryId}/page")
    public Page<ProductDto> getProductsInCategoryPaginated(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PageRequest request;

        if (sortDir.equals("desc")) {
            request = PageRequest.of(page, size, Sort.by(sort).descending());
        } else {
            request = PageRequest.of(page, size, Sort.by(sort).ascending());
        }

        return productService.getProductDtosByCategoryIdPaginated(categoryId, request);
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

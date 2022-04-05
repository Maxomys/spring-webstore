package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductDto> getProducts();

    List<Product> getLatestProducts(Integer numberOfProducts);

    Page<Product> getProductsPaginated(Pageable pageable);

    List<ProductDto> getAllProductsForCurrentUser();

    Page<Product> getProductsByCategoryIdPaginated(Long categoryId, Pageable pageable);

    ProductDto findById(Long id);

    ProductDto saveProduct(ProductDto dto);

    Product saveProduct(Product product);

    ProductDto updateProduct(ProductDto product);

    void deleteById(Long id);

    Product addRequestToProduct(Product product, String remoteAddress);

}

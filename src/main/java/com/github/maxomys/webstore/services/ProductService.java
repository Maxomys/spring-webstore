package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    List<Product> getLatestProducts(Integer numberOfProducts);

    Page<Product> getProductsPaginated(Pageable pageable);

    List<Product> getAllProductsForCurrentUser();

    Page<Product> getProductsByCategoryIdPaginated(Long categoryId, Pageable pageable);

    Product findById(Long id);

    Product saveProduct(Product product);

    Product updateProduct(Product product);

    void deleteById(Long id);

    Product addRequestToProduct(Product product, String remoteAddress);

}

package com.github.maxomys.webstore.repositories;

import com.github.maxomys.webstore.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);

    List<Product> findAllByUser_Username(String username);

    List<Product> findAllByNameContainingIgnoreCase(String name);

}

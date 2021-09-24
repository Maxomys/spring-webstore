package com.github.maxomys.webstore.repositories;

import com.github.maxomys.webstore.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {



}

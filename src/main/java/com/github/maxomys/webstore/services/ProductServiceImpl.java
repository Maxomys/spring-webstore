package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Category;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.repositories.CategoryRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getLatestProducts(Integer numberOfProducts) {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.size() <= numberOfProducts) {
            return allProducts;
        }

        List<Product> latestProducts = new ArrayList<>();
        for (int i = allProducts.size() - numberOfProducts; i < allProducts.size(); i++) {
            latestProducts.add(allProducts.get(i));
        }

        return latestProducts;
    }

    @Override
    public Page<Product> getProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getProductsByCategoryIdPaginated(Long categoryId, Pageable pageable) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(!categoryOptional.isPresent()) {
            throw new RuntimeException("Category not found!");
        }

        return productRepository.findAllByCategoryId(categoryOptional.get().getId(), pageable);
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (!productOptional.isPresent()) {
            throw new ResourceNotFoundException("Product not found!");
        }

        return productOptional.get();
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        imageService.deleteImagesByProductId(id);

        productRepository.deleteById(id);
    }

    @Override
    public Product addRequestToProduct(Product product, String remoteAddress) {
        Optional<String> addressOptional = product.getUniqueAddresses().stream().filter(s -> s.equals(remoteAddress)).findFirst();

        if (addressOptional.isPresent()) {
            return product;
        }

        product.getUniqueAddresses().add(remoteAddress);
        productRepository.save(product);

        return product;
    }

}

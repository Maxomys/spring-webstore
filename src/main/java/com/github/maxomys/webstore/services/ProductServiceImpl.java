package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Category;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.repositories.CategoryRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final PermissionService permissionService;

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
    public List<Product> getAllProductsForCurrentUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return productRepository.findAllByUser_Username(name);
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
        Product savedProduct = productRepository.save(product);
        permissionService.addPermissionForCurrentUser(product.getClass(), product.getId(), BasePermission.ADMINISTRATION);
        return savedProduct;
    }

    @Override
    @PreAuthorize("hasPermission(#product, 'WRITE')")
    public Product updateProduct(Product product) {
        Optional<Product> productOptional = productRepository.findById(product.getId());

        if (productOptional.isEmpty()) {
            return saveProduct(product);
        }

        Product fetchedProduct = productOptional.get();
        fetchedProduct.setName(product.getName());
        fetchedProduct.setPrice(product.getPrice());
        fetchedProduct.setAmountInStock(product.getAmountInStock());

        return productRepository.save(fetchedProduct);
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

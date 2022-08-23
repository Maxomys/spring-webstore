package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.api.mappers.ProductMapper;
import com.github.maxomys.webstore.domain.Category;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.repositories.CategoryRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import com.github.maxomys.webstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final PermissionService permissionService;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
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
    public Page<ProductDto> getProductDtoPage(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::productToProductDto);
    }

    @Override
    public Page<Product> getProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<ProductDto> getAllProductsForCurrentUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return productRepository.findAllByUser_Username(name)
                .stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDto> getProductDtosByCategoryIdPaginated(Long categoryId, Pageable pageable) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(!categoryOptional.isPresent()) {
            throw new RuntimeException("Category not found!");
        }

        return productRepository.findAllByCategoryId(categoryOptional.get().getId(), pageable).map(productMapper::productToProductDto);
    }

    @Override
    public List<ProductDto> searchProductsByName(String name) {
        return productRepository.findAllByNameContainingIgnoreCase(name).stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product not found!");
        }

        return productMapper.productToProductDto(productOptional.get());
    }

    @Override
    public ProductDto saveProduct(ProductDto dto) {
        Product productToSave = productMapper.productDtoToProduct(dto);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        productToSave.setUser(userRepository.findByUsername(username));
        productToSave.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for id: " + dto.getCategoryId())));
        productToSave.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(productToSave);

        permissionService.addPermissionForCurrentUser(savedProduct.getClass(), savedProduct.getId(), BasePermission.READ);
        permissionService.addPermissionForCurrentUser(savedProduct.getClass(), savedProduct.getId(), BasePermission.WRITE);

        return productMapper.productToProductDto(savedProduct);
    }

    @Override
    public Product saveProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        permissionService.addPermissionForCurrentUser(savedProduct.getClass(), savedProduct.getId(), BasePermission.READ);
        permissionService.addPermissionForCurrentUser(savedProduct.getClass(), savedProduct.getId(), BasePermission.WRITE);
        return savedProduct;
    }

    @Override
    @PreAuthorize("hasPermission(@productRepository.findById(#dto.id).orElseThrow(), 'WRITE')")
    public ProductDto updateProduct(ProductDto dto) {
        Optional<Product> productOptional = productRepository.findById(dto.getId());

        if (productOptional.isEmpty()) {
            return saveProduct(dto);
        }

        Product product = productMapper.productDtoToProduct(dto);

        Product fetchedProduct = productOptional.get();
        fetchedProduct.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for id: " + dto.getCategoryId())));
        fetchedProduct.setName(product.getName());
        fetchedProduct.setPrice(product.getPrice());
        fetchedProduct.setAmountInStock(product.getAmountInStock());

        return productMapper.productToProductDto(productRepository.save(fetchedProduct));
    }

    @Override
    @PreAuthorize("hasPermission(@productRepository.findById(#id).orElseThrow(), 'WRITE')")
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

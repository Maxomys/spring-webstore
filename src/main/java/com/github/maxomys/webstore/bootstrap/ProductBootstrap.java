package com.github.maxomys.webstore.bootstrap;

import com.github.maxomys.webstore.auth.UserService;
import com.github.maxomys.webstore.domain.Category;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.domain.Transaction;
import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.repositories.TransactionRepository;
import com.github.maxomys.webstore.services.CategoryService;
import com.github.maxomys.webstore.services.PermissionService;
import com.github.maxomys.webstore.services.ProductService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

@Component
@Profile("!mysql")
public class ProductBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;
    private final PermissionService permissionService;

    public ProductBootstrap(ProductService productService, CategoryService categoryService, UserService userService, PasswordEncoder passwordEncoder, TransactionRepository transactionRepository, PermissionService permissionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.transactionRepository = transactionRepository;
        this.permissionService = permissionService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Authentication authentication = new AnonymousAuthenticationToken("key", "anon", Collections.singleton(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //User
        User userAdmin = new User();
        userAdmin.setUsername("admin");
        userAdmin.setPassword("a");
        userAdmin.setEmail("test@test.pl");

        //User
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("a");
        user1.setEmail("test2@test.pl");

        //User
        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("a");
        user2.setEmail("test3@test.pl");

        userService.createNewUser(userAdmin);
        userService.createNewUser(user1);
        userService.createNewUser(user2);

        //Categories
        Category categorySculptures = new Category();
        categorySculptures.setDescription("Sculptures");

        Category categoryPaintings = new Category();
        categoryPaintings.setDescription("Paintings");

        Category categoryBooks = new Category();
        categoryBooks.setDescription("Books");

        Category categoryMusic = new Category();
        categoryMusic.setDescription("Music");

        categoryService.createNewCategory(categoryBooks);
        categoryService.createNewCategory(categoryMusic);

        //Products
        Product product1 = new Product();
        product1.setUser(userAdmin);
        product1.setName("Product 1");
        product1.setPrice(12);
        product1.setAmountInStock(10);
        product1.setCategory(categoryPaintings);
        product1.setCreatorName("creatorTom");
        product1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        productService.saveProduct(product1);
        permissionService.addPermission("admin", product1.getClass(), product1.getId(), BasePermission.ADMINISTRATION);
        permissionService.addPermission("user1", product1.getClass(), product1.getId(), BasePermission.READ);

        Product product2 = new Product();
        product2.setUser(userAdmin);
        product2.setName("Product 2");
        product2.setPrice(22);
        product2.setCategory(categoryPaintings);
        product2.setCreatorName("creatorJeff");
        product2.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        productService.saveProduct(product2);
        permissionService.addPermission("admin", product2.getClass(), product2.getId(), BasePermission.ADMINISTRATION);

        Product product3 = new Product();
        product3.setUser(userAdmin);
        product3.setName("Product 3");
        product3.setPrice(41);
        product3.setCategory(categoryPaintings);
        product3.setCreatorName("creatorTom");
        product3.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        productService.saveProduct(product3);
        permissionService.addPermission("admin", product3.getClass(), product3.getId(), BasePermission.ADMINISTRATION);
        permissionService.addPermission("user1", product3.getClass(), product3.getId(), BasePermission.READ);

        Product product4 = new Product();
        product4.setUser(userAdmin);
        product4.setName("Product 4");
        product4.setPrice(51);
        product4.setCategory(categoryPaintings);
        product4.setCreatorName("creatorJeff");
        product4.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        productService.saveProduct(product4);
        permissionService.addPermission("admin", product4.getClass(), product4.getId(), BasePermission.ADMINISTRATION);


        transactionRepository.save(Transaction.builder()
            .product(product4)
            .time(new Date())
            .buyer(user1)
            .seller(user2)
            .build());
    }

}

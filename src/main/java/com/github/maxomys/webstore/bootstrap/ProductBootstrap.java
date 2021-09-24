package com.github.maxomys.webstore.bootstrap;

import com.github.maxomys.webstore.auth.UserService;
import com.github.maxomys.webstore.domain.Category;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.services.CategoryService;
import com.github.maxomys.webstore.services.ProductService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("dev")
public class ProductBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ProductBootstrap(ProductService productService, CategoryService categoryService, UserService userService, PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //User
        User userAdmin = new User();
        userAdmin.setUsername("admin");
        userAdmin.setPassword("a");
        userAdmin.setEmail("mistrz7losos@live.com");

        userService.createNewUser(userAdmin);

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
        product1.setCategory(categoryPaintings);
        product1.setCreatorName("creatorTom");
        product1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        productService.saveProduct(product1);

        Product product2 = new Product();
        product2.setUser(userAdmin);
        product2.setName("Product 2");
        product2.setPrice(22);
        product2.setCategory(categoryPaintings);
        product2.setCreatorName("creatorJeff");
        product2.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        productService.saveProduct(product2);

        Product product3 = new Product();
        product3.setUser(userAdmin);
        product3.setName("Product 3");
        product3.setPrice(41);
        product3.setCategory(categoryPaintings);
        product3.setCreatorName("creatorTom");
        product3.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        productService.saveProduct(product3);

        Product product4 = new Product();
        product4.setUser(userAdmin);
        product4.setName("Product 4");
        product4.setPrice(51);
        product4.setCategory(categoryPaintings);
        product4.setCreatorName("creatorJeff");
        product4.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        productService.saveProduct(product4);
    }

}

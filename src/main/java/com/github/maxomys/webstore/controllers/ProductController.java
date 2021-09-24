package com.github.maxomys.webstore.controllers;

import lombok.extern.slf4j.Slf4j;
import com.github.maxomys.webstore.domain.Inquiry;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.services.CategoryService;
import com.github.maxomys.webstore.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Controller
public class ProductController {

    private final int PAGE_SIZE = 9;
    private final String[] SORT_PARAMS = {"name", "price"};

    ProductService productService;
    CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @GetMapping("/products/{productId}")
    public String getProductById(Model model, @PathVariable Long productId, HttpServletRequest request) {
        Product product = productService.addRequestToProduct(productService.findById(productId), request.getRemoteAddr());

        model.addAttribute("product", product);

        Inquiry inquiry = new Inquiry();
        inquiry.setProduct(productService.findById(Long.valueOf(productId)));

        model.addAttribute("inquiry", inquiry);

        return "/products/productDetails";
    }

    @GetMapping("/admin/products/{productId}")
    public String getProductUpdateForm(Model model, @PathVariable Long productId) {
        model.addAttribute("product", productService.findById(productId));
        model.addAttribute("categories", categoryService.getCategories());

        return "/products/productForm";
    }

    @GetMapping("/admin/products/{productId}/requests")
    public String getProductRequests(Model model, @PathVariable Long productId) {
        model.addAttribute("product", productService.findById(productId));

        return "/admin/productRequests";
    }

    @GetMapping("/admin/products/new")
    public String getProductForm(Model model) {
        model.addAttribute("product", new Product());

        model.addAttribute("categories", categoryService.getCategories());

        return "/products/productForm";
    }

    @GetMapping("/products")
    public String getAllProductsPage(Model model, @RequestParam Optional<Integer> pageNumber,
                                      @RequestParam Optional<String> sortBy, @RequestParam Optional<String> sortDirection) {

        Sort.Order sortOrder;

        if (sortDirection.orElse("ASC").equals("DESC")) {
            sortOrder = Sort.Order.desc(sortBy.orElse("name"));
        } else {
            sortOrder = Sort.Order.asc(sortBy.orElse("name"));
        }

        Page<Product> productPage = productService.getProductsPaginated(PageRequest.of(pageNumber.orElse(0), PAGE_SIZE, Sort.by(sortOrder)));

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("productPage", productPage);
        model.addAttribute("categories", categoryService.getCategories());

        return "/products/all";
    }

    @GetMapping("/categories/{categoryId}/products")
    public String getProductsByCategory(Model model, @PathVariable Long categoryId, @RequestParam Optional<Integer> pageNumber,
                                        @RequestParam Optional<String> sortBy, @RequestParam Optional<String> sortDirection) {

        Sort.Order sortOrder;

        if (sortDirection.orElse("ASC").equals("DESC")) {
            sortOrder = Sort.Order.desc(sortBy.orElse("name"));
        } else {
            sortOrder = Sort.Order.asc(sortBy.orElse("name"));
        }

        Page<Product> productPage = productService.getProductsByCategoryIdPaginated(categoryId,
                PageRequest.of(pageNumber.orElse(0), PAGE_SIZE, Sort.by(sortOrder)));

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("productPage", productPage);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryName", categoryService.getCategories()
                .stream().filter(category -> category.getId().equals(categoryId)).findFirst().get().getDescription());

        return "/products/productsInCategory";
    }

    @PostMapping("/admin/products")
    public String saveOrUpdate(@ModelAttribute("product") Product product,
                               Authentication authentication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });

            return "/products/productForm";
        }

        product.setUser((User) authentication.getPrincipal());
        Product savedProduct = productService.saveProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products")
    public String getAllProductsAdmin(Model model, Authentication authentication) {
        User activeUser = (User) authentication.getPrincipal();

        model.addAttribute("products", productService.getProducts());
        model.addAttribute("user", activeUser);

        return "/admin/products";
    }

    @PostMapping("/products/{productId}")
    public String deleteProduct(@PathVariable String productId) {
        productService.deleteById(Long.valueOf(productId));

        return "redirect:/admin/products";
    }

}

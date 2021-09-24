package com.github.maxomys.webstore.controllers;

import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.services.InquiryService;
import com.github.maxomys.webstore.services.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final int LATEST_NUMBER = 3;

    private final InquiryService inquiryService;
    private final ProductService productService;

    public IndexController(InquiryService inquiryService, ProductService productService) {
        this.inquiryService = inquiryService;
        this.productService = productService;
    }

    @GetMapping({"", "/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("latest", productService.getLatestProducts(LATEST_NUMBER));

        return "/index";
    }

    @GetMapping({"/admin", "/admin/"})
    public String getAdminPage() {
        return "redirect:/admin/index";
    }

    @GetMapping("/admin/index")
    public String getAdminIndex(Model model, Authentication authentication) {
        User activeUser = (User) authentication.getPrincipal();

        model.addAttribute("user", activeUser);
        model.addAttribute("inquiries", inquiryService.getInquiries());

        return "/admin/index";
    }

    @GetMapping("/about")
    public String getAboutPage() {
	return "/about";
    }

}

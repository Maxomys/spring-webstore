package com.github.maxomys.webstore.controllers;

import com.github.maxomys.webstore.domain.Image;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.services.ImageService;
import com.github.maxomys.webstore.services.ProductService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Profile("mvc")
public class ImageController {

    private final ImageService imageService;
    private final ProductService productService;

    public ImageController(ImageService imageService, ProductService productService) {
        this.imageService = imageService;
        this.productService = productService;
    }

    @GetMapping("/admin/products/{productId}/image")
    public String showUploadForm(@PathVariable String productId, Model model) {
        model.addAttribute("product", productService.findById(Long.valueOf(productId)));

        return "/imageForm";
    }

    @ResponseBody
    @GetMapping(value = "/products/{productId}/thumbnail", produces = "image/jpg")
    public FileSystemResource renderProductThumbnail(@PathVariable String productId) {
        Product product = productService.findById(Long.valueOf(productId));

        if (product.getImages().size() > 0) {
            Image image = product.getImages().get(0);

            return imageService.getThumbnailById(image.getId());
        }
        return null;
    }

    @ResponseBody
    @GetMapping(value = "/image/{imageId}", produces = "image/jpeg")
    public FileSystemResource renderImageByImageId(@PathVariable String imageId) {
        return imageService.getImageById(Long.valueOf(imageId));
    }

    @ResponseBody
    @GetMapping(value = "/image/{imageId}/thumbnail", produces = "image/jpeg")
    public FileSystemResource renderImageThumbnailByImageId(@PathVariable String imageId) {
        return imageService.getThumbnailById(Long.valueOf(imageId));
    }

    @PostMapping("/admin/products/{productId}/image")
    public String handleImagePost(@PathVariable String productId, @RequestParam("imagefile") MultipartFile multipartFile) {
        imageService.saveImage(Long.valueOf(productId), multipartFile);

        return "redirect:/admin/products/" + productId + "/images";
    }

    @GetMapping("/admin/products/{productId}/images")
    public String getImagesByProductId(@PathVariable Long productId, Model model) {
        Product product = productService.findById(productId);

        if (product == null) {
            throw new ResourceNotFoundException("Product not found!");
        }

        List<Image> images = product.getImages();
        model.addAttribute("images", images);
        model.addAttribute("productId", productId);

        return "/admin/imageList";
    }

    @PostMapping("/images/{imageId}")
    public String deleteImageById(@PathVariable Long imageId, @ModelAttribute("productId") Long productId) {
        imageService.deleteImageById(imageId);

        return "redirect:/admin/products/" + productId + "/images";
    }

}

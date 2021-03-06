package com.github.maxomys.webstore.api.controllers;

import com.github.maxomys.webstore.services.ImageService;
import com.github.maxomys.webstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageRestController {

    private final ImageService imageService;
    private final ProductService productService;

    @GetMapping(value = "/api/image/{imageId}", produces = "image/jpg")
    public byte[] renderImageByImageId(@PathVariable Long imageId) {
        return imageService.getImageById(imageId);
    }

    @GetMapping(value = "/api/image/{imageId}/thumbnail", produces = "image/jpg")
    public byte[] renderThumbnailByImageId(@PathVariable Long imageId) {
        return imageService.getThumbnailById(imageId);
    }

    @PostMapping("/api/product/{productId}/image")
    @ResponseStatus(HttpStatus.CREATED)
    public void handleImagePost(@PathVariable Long productId, @RequestParam("imagefile") MultipartFile multipartFile) {
        imageService.saveImage(productId, multipartFile);
    }

}

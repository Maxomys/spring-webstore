package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Image;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.repositories.ImageRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile({"default", "mysql"})
public class ImageServiceImpl implements ImageService {

    @Value("${resources.directory}")
    String RESOURCES_DIR;

    ProductRepository productRepository;
    ImageRepository imageRepository;
    ResourceLoader resourceLoader;

    public ImageServiceImpl(ProductRepository productRepository, ImageRepository imageRepository, ResourceLoader resourceLoader) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void saveImage(Long productId, MultipartFile file) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            throw new RuntimeException("Product not found!");
        }

        Product product = productOptional.get();

        Image newImage = new Image();
        newImage.setFileName(UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
        newImage.setThumbnailFileName(UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename()));

        //Create a thumbnail
        try {
            BufferedImage uploadedImage = ImageIO.read(file.getInputStream());
            BufferedImage thumbnail = Scalr.resize(uploadedImage, Scalr.Mode.FIT_TO_WIDTH, 360, 360);
            File thumbnailFile = new File(RESOURCES_DIR + newImage.getThumbnailFileName());
            ImageIO.write(thumbnail, "jpg", thumbnailFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path imageFile = Paths.get(RESOURCES_DIR + newImage.getFileName());

        try {
            Files.write(imageFile, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        newImage.setProduct(product);
        product.getImages().add(newImage);
        productRepository.save(product);
    }

    @Override
    public byte[] getImageById(Long imageId) {
        Optional<Image> imageOptional = imageRepository.findById(imageId);

        if (imageOptional.isEmpty()) {
            throw new RuntimeException("Image not found!");
        }

        Image image = imageOptional.get();

        try {
            return Files.readAllBytes(Path.of(RESOURCES_DIR, image.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] getThumbnailById(Long imageId) {
        Optional<Image> imageOptional = imageRepository.findById(imageId);

        if (imageOptional.isEmpty()) {
            throw new RuntimeException("Image not found!");
        }

        Image image = imageOptional.get();

        try {
            return Files.readAllBytes(Path.of(RESOURCES_DIR + "\\", image.getThumbnailFileName()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteImageById(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image Not Found!"));

        File imageFile = new File(RESOURCES_DIR + image.getFileName());
        File thumbnailFile = new File(RESOURCES_DIR + image.getThumbnailFileName());
        imageFile.delete();
        thumbnailFile.delete();

        imageRepository.deleteById(imageId);
    }

    @Override
    public void deleteImagesByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("ProductNotFound"));

        product.getImages().forEach(image -> {
            File imageFile = new File(RESOURCES_DIR + image.getFileName());
            File thumbnailFile = new File(RESOURCES_DIR + image.getThumbnailFileName());
            imageFile.delete();
            thumbnailFile.delete();
        });
    }

}

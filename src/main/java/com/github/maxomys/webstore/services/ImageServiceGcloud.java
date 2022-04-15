package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Image;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.exceptions.ServerErrorException;
import com.github.maxomys.webstore.repositories.ImageRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile("cloud-storage")
@Primary
@RequiredArgsConstructor
public class ImageServiceGcloud implements ImageService {

    public static final String BUCKET_NAME = "webstore-images";

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final Storage storage;

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

            //Save to gcp storage bucket
            Bucket bucket = storage.get(BUCKET_NAME);
            bucket.create(newImage.getFileName(), file.getInputStream());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", outputStream);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            bucket.create(newImage.getThumbnailFileName(), inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerErrorException("Images not saved", e);
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

        Bucket bucket = storage.get(BUCKET_NAME);
        Blob blob = bucket.get(image.getFileName());

        return blob.getContent();
    }

    @Override
    public byte[] getThumbnailById(Long imageId) {
        Optional<Image> imageOptional = imageRepository.findById(imageId);

        if (imageOptional.isEmpty()) {
            throw new RuntimeException("Image not found!");
        }

        Image image = imageOptional.get();

        Bucket bucket = storage.get(BUCKET_NAME);
        Blob blob = bucket.get(image.getThumbnailFileName());

        return blob.getContent();
    }

    @Override
    public void deleteImageById(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image Not Found!"));

        Bucket bucket = storage.get(BUCKET_NAME);

        Blob imageBlob = bucket.get(image.getFileName());
        imageBlob.delete();
        Blob thumbnailBlob = bucket.get(image.getThumbnailFileName());
        thumbnailBlob.delete();

        imageRepository.deleteById(imageId);
    }

    @Override
    public void deleteImagesByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("ProductNotFound"));

        Bucket bucket = storage.get(BUCKET_NAME);

        product.getImages().forEach(image -> {
            Blob imageBlob = bucket.get(image.getFileName());
            imageBlob.delete();
            Blob thumbnailBlob = bucket.get(image.getThumbnailFileName());
            thumbnailBlob.delete();
        });
    }

}

package com.github.maxomys.webstore.services;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveImage(Long productId, MultipartFile file);

    byte[] getImageById(Long imageId);

    byte[] getThumbnailById(Long imageId);

    void deleteImageById(Long imageId);

    void deleteImagesByProductId(Long productId);
}

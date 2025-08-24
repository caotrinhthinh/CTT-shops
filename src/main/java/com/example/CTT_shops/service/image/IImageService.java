package com.example.CTT_shops.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.CTT_shops.dto.ImageDto;
import com.example.CTT_shops.model.Image;

public interface IImageService {
    Image getImageById(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);

    void updateImage(MultipartFile file, Long imageId);
}

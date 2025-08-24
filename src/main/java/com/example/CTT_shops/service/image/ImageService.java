package com.example.CTT_shops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.CTT_shops.dto.ImageDto;
import com.example.CTT_shops.exceptions.ResourceNotFoundException;
import com.example.CTT_shops.model.Image;
import com.example.CTT_shops.model.Product;
import com.example.CTT_shops.repository.ImageRepository;
import com.example.CTT_shops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("No image found with id: " + id);
        });
    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImageDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                // Lưu ảnh vào DB (1 lần duy nhất)
                Image savedImage = imageRepository.save(image);

                // Build download URL động dựa trên ID đã sinh
                String downloadUrl = "/api/v1/images/image/download/" + savedImage.getId();

                // Không cần save lại DB, chỉ set vào DTO trả về
                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownLoadUrl(downloadUrl);

                savedImageDto.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
            }
        }

        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

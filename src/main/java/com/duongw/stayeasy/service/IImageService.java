package com.duongw.stayeasy.service;

import com.duongw.stayeasy.dto.response.entity.ImageResponseDTO;
import com.duongw.stayeasy.model.Images;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    ImageResponseDTO convertImagetoImageDTO(Images image);
    Images convertImageDTOtoImage(ImageResponseDTO image);

    List<ImageResponseDTO> getImagesByRoomId(Long roomId);

    Images getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageResponseDTO> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile files, Long imageId);
}

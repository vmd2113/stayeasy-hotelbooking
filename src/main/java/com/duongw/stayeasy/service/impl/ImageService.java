package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.dto.response.entity.ImageResponseDTO;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.Images;
import com.duongw.stayeasy.model.Room;
import com.duongw.stayeasy.repository.ImageRepository;
import com.duongw.stayeasy.service.IImageService;
import com.duongw.stayeasy.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IRoomService roomService;
    @Override
    public ImageResponseDTO convertImagetoImageDTO(Images image) {
        ImageResponseDTO imageResponseDTO = new ImageResponseDTO();
        imageResponseDTO.setId(image.getId());
        imageResponseDTO.setFileName(image.getFileName());
        imageResponseDTO.setFileType(image.getFileType());
        imageResponseDTO.setDownloadUrl(image.getDownloadUrl());
        imageResponseDTO.setPreviewUrl("/api/v1/images/image/preview/" + image.getId()); // URL hiển thị
        return imageResponseDTO;
    }

    @Override
    public Images convertImageDTOtoImage(ImageResponseDTO imageResponseDTO) {
        Images image = new Images();
        image.setId(imageResponseDTO.getId());
        image.setFileName(imageResponseDTO.getFileName());
        image.setFileType(imageResponseDTO.getFileType());
        image.setDownloadUrl(imageResponseDTO.getDownloadUrl());
        return image;
    }

    @Override
    public List<ImageResponseDTO> getImagesByRoomId(Long roomId) {
        Room room = roomService.getRoomById(roomId); // Lấy phòng theo ID
        List<Images> images = imageRepository.findByRoomImagesId(roomId); // Tìm tất cả ảnh theo room
        List<ImageResponseDTO> imageResponseDTOS = new ArrayList<>();

        for (Images image : images) {
            ImageResponseDTO imageResponseDTO = convertImagetoImageDTO(image);
            imageResponseDTOS.add(imageResponseDTO);
        }

        return imageResponseDTOS; // Trả về danh sách DTO
    }



    @Override
    public Images getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Image is not found"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public List<ImageResponseDTO> saveImages(List<MultipartFile> files, Long roomId) {
        Room room = roomService.getRoomById(roomId);
        List<ImageResponseDTO> imageResponseDTOS = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Images image = new Images();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(file.getBytes()); // Sử dụng byte array

                image.setRoomImages(room);
                Images savingImage = imageRepository.save(image); // Lưu hình ảnh

                String downloadUrl = "/api/v1/images/image/download/" + savingImage.getId(); // Tạo URL sau khi lưu
                savingImage.setDownloadUrl(downloadUrl);
                imageRepository.save(savingImage); // Cập nhật lại URL tải xuống

                ImageResponseDTO imageResponseDTO = convertImagetoImageDTO(savingImage);
                imageResponseDTOS.add(imageResponseDTO);
            } catch (Exception e) {
                throw new RuntimeException("Failed to save image: " + e.getMessage());
            }
        }
        return imageResponseDTOS; // Trả về danh sách DTO
    }


    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Images image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(file.getBytes()); // Cập nhật bằng byte array
            imageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update image: " + e.getMessage());
        }
    }

}

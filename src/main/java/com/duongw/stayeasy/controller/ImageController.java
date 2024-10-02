package com.duongw.stayeasy.controller;

import com.duongw.stayeasy.dto.response.entity.ImageResponseDTO;
import com.duongw.stayeasy.dto.response.ApiResponse;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.Images;
import com.duongw.stayeasy.service.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;


    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<?>> getImagesByRoomId(@PathVariable Long roomId) {
        try {
            List<ImageResponseDTO> images = imageService.getImagesByRoomId(roomId);
            ApiResponse<List<ImageResponseDTO>> response = new ApiResponse<>(true, "Images retrieved successfully", HttpStatus.OK.value(), images);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<List<ImageResponseDTO>> response = new ApiResponse<>(false, "Room not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/image/upload")
    public ResponseEntity<ApiResponse<?>> saveImage(@RequestParam List<MultipartFile> files,
                                                                 @RequestParam(name = "room-id") Long roomId) {
        try {
            List<ImageResponseDTO> imageResponseDTOS = imageService.saveImages(files, roomId);
            ApiResponse<List<ImageResponseDTO>> response = new ApiResponse<>(true, "Upload image success", HttpStatus.OK.value(), imageResponseDTOS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<List<ImageResponseDTO>> response = new ApiResponse<>(false, "Upload image failed, room not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/image/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable(name = "id") Long imageId) throws SQLException {
        Images image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping(path = "/image/preview/{id}")
    public ResponseEntity<Resource> previewImage(@PathVariable(name = "id") Long imageId) throws SQLException {
        Images image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType())) // Đặt loại nội dung đúng với loại tệp
                .body(resource);
    }


    @PutMapping(path = "/image/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateImage(@PathVariable(name = "id") Long imageId,
                                                             @RequestParam MultipartFile file) {
        try {
            imageService.updateImage(file, imageId);
            Images updatedImage = imageService.getImageById(imageId);
            ImageResponseDTO imageResponseDTO = imageService.convertImagetoImageDTO(updatedImage);
            ApiResponse<ImageResponseDTO> response = new ApiResponse<>(true, "Image updated successfully", HttpStatus.OK.value(), imageResponseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<ImageResponseDTO> response = new ApiResponse<>(false, "Image not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/image/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteImage(@PathVariable(name = "id") Long imageId) {
        try {
            imageService.deleteImageById(imageId);
            ApiResponse<Void> response = new ApiResponse<>(true, "Image deleted successfully", HttpStatus.OK.value(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Image not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

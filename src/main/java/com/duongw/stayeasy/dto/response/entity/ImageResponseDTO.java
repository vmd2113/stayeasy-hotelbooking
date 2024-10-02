package com.duongw.stayeasy.dto.response.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ImageResponseDTO {

    private Long id;
    private String fileName;
    private String fileType;
    private String downloadUrl;
    private String previewUrl;



}

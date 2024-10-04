package com.duongw.stayeasy.dto.response.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserResponseDTO {
    private String username;
    private String email;
    private String roleName;

}
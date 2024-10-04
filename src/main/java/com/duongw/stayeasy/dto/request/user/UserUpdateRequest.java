package com.duongw.stayeasy.dto.request.user;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String password;

    private String username;

    private String rewritePassword;

    private String name;
    private String phoneNumber;
    private String address;
}

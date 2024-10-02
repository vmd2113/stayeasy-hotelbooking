package com.duongw.stayeasy.dto.request.user;

import lombok.Data;

@Data
public class UserUpdateRequest {
    String email;
    String password;
}

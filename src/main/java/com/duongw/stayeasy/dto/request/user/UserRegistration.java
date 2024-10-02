package com.duongw.stayeasy.dto.request.user;

import lombok.Data;

@Data
public class UserRegistration {


    private String username;
    private String password;
    private String rewritePassword;
    private String email;

}





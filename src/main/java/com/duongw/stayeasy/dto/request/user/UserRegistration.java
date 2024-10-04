package com.duongw.stayeasy.dto.request.user;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserRegistration {


    private String username;
    private String password;
    private String rewritePassword;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;

}





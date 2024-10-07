package com.duongw.stayeasy.dto.request.user;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistration {


    private String username;
    private String password;
    private String rewritePassword;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

}





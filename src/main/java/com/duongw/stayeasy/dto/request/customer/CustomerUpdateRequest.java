package com.duongw.stayeasy.dto.request.customer;

import lombok.Data;

@Data
public class CustomerUpdateRequest {
    private String name;
    private String phoneNumber;
    private String address;
}

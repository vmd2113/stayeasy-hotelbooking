package com.duongw.stayeasy.dto.request.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class CustomerUpdateRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
}

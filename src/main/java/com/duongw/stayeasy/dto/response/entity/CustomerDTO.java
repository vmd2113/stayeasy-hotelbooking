package com.duongw.stayeasy.dto.response.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerDTO {

    private Long id;

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private List<BookingRoomResponseDTO> bookingRoomList;
    private UserResponseDTO user;


}

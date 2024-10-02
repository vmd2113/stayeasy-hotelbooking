package com.duongw.stayeasy.dto.request.customer;

import com.duongw.stayeasy.dto.response.entity.BookingRoomResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDetail {
    private String username;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;

    private List<BookingRoomResponseDTO> list;

}

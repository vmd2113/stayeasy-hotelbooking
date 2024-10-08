package com.duongw.stayeasy.dto.response.entity;

import com.duongw.stayeasy.enums.RoomStatus;
import com.duongw.stayeasy.model.RoomCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder

public class RoomResponseDTO {

    private Long id;

    private String roomCode;

    private RoomStatus roomStatus;

    private BigDecimal roomPrice;

    private String description;

    private RoomCategory roomCategory;

    List<ImageResponseDTO> imageResponseDTOS;

    List<BookingRoomResponseDTO> bookingRoomResponseDTOList;
}

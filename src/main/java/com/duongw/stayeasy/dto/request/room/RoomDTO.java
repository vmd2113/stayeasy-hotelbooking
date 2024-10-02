package com.duongw.stayeasy.dto.request.room;

import com.duongw.stayeasy.dto.response.entity.BookingRoomResponseDTO;
import com.duongw.stayeasy.dto.response.entity.ImageResponseDTO;
import com.duongw.stayeasy.enums.RoomStatus;
import com.duongw.stayeasy.model.BookingRoom;
import com.duongw.stayeasy.model.RoomCategory;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RoomDTO {


    private Long id;

    private String roomCode;

    private RoomStatus roomStatus;

    private BigDecimal roomPrice;

    private String description;

    private RoomCategory roomCategory;

    List<ImageResponseDTO> imageResponseDTOS;

    List<BookingRoomResponseDTO> bookingRoomResponseDTOList;
}

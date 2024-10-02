package com.duongw.stayeasy.dto.request.room;

import com.duongw.stayeasy.enums.RoomStatus;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class UpdateRoomDTO {


    private String roomCode;
    private String roomStatus;
    private BigDecimal roomPrice;
    private String description;
    private Long roomCategoryId;
}

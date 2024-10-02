package com.duongw.stayeasy.dto.request.room;


import com.duongw.stayeasy.model.RoomCategory;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateRoomDTO {

    private Long id;

    @NotBlank(message = "Room code cannot be blank")
    @Size(min = 3, max = 10, message = "Room code must be between 3 and 10 characters")
    private String roomCode;

    @NotNull(message = "Room status cannot be null")
    private String roomStatus = "AVAILABLE";

    @NotNull(message = "Room price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Room price must be greater than 0")
    private BigDecimal roomPrice;

    private String description;
    private Long roomCategoryId;

}

package com.duongw.stayeasy.dto.request.bookingroom;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder

public class CreateBookingDTO {

    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfAdults;
    private int numberOfChildren;
    private int totalNumberOfGuest;
    private String bookingRoomStatus;
    private String bookingConfirmationCode;
    private Long customerId;
}




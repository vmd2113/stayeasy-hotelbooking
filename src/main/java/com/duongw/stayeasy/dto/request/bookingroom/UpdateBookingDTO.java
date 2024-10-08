package com.duongw.stayeasy.dto.request.bookingroom;

import java.time.LocalDate;

public class UpdateBookingDTO {


    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfAdults;
    private int numberOfChildren;
    private int totalNumberOfGuest;
    private String bookingRoomStatus;
    private String bookingConfirmationCode;
    private Long customerId;
}

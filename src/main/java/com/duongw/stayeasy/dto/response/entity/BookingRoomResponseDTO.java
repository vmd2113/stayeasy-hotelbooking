package com.duongw.stayeasy.dto.response.entity;

import com.duongw.stayeasy.enums.BookingRoomStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BookingRoomResponseDTO {

    private Long id;

    private BookingRoomStatus bookingRoomStatus;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private int numberOfAdults;

    private int numberOfChildren;

    private int totalNumberOfGuest;

    private String bookingConfirmationCode;

    private CustomerDTO customerDTO;

    private RoomResponseDTO roomResponseDTO;

    public BookingRoomResponseDTO(Long id, BookingRoomStatus bookingRoomStatus, LocalDate checkInDate, LocalDate checkOutDate, int numberOfAdults, int numberOfChildren, int totalNumberOfGuest, String bookingConfirmationCode) {
        this.id = id;
        this.bookingRoomStatus = bookingRoomStatus;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
        this.totalNumberOfGuest = totalNumberOfGuest;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}

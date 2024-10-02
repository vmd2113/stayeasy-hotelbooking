package com.duongw.stayeasy.model;

import com.duongw.stayeasy.enums.BookingRoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "booking_rooms")
public class BookingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_room_status")
    private BookingRoomStatus bookingRoomStatus;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(name = "number_of_adults")
    private int numberOfAdults;

    @Column(name = "number_of_children")
    private int numberOfChildren;

    @Column(name = "total_guest")
    private int totalNumberOfGuest;

    @Column(name = "confirmation_code")
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne()
    @JoinColumn(name = "room_id")
    private Room room;

    public void calculateTotalNumberOfGuest() {
        this.totalNumberOfGuest = this.numberOfAdults + this.numberOfChildren;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numberOfAdults = numOfAdults;
        calculateTotalNumberOfGuest();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numberOfChildren = numOfChildren;
        calculateTotalNumberOfGuest();
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }


}

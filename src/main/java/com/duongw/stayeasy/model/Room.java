package com.duongw.stayeasy.model;

import com.duongw.stayeasy.enums.BookingRoomStatus;
import com.duongw.stayeasy.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor

@Table(name= "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_code", unique = true)
    private String roomCode;


    @Enumerated(EnumType.STRING)
    @Column(name = "room_status")
    private RoomStatus roomStatus;

    @Column(name = "room_price")
    private BigDecimal roomPrice;

    @Column(name = "description")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private RoomCategory roomCategory;

    @OneToMany(mappedBy = "room", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BookingRoom> bookingRoomList;

    @OneToMany(mappedBy = "roomImages", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Images> imagesList;

    public Room() {
        this.bookingRoomList = new ArrayList<>();
    }

    public void addBookingRoom(BookingRoom bookingRoom) {
        if (bookingRoomList == null) {
            bookingRoomList = new ArrayList<>();

        }
        if(this.roomStatus.equals("AVAILABLE")){
            bookingRoomList.add(bookingRoom);
            bookingRoom.setRoom(this);
            bookingRoom.setBookingRoomStatus(BookingRoomStatus.PENDING);
            String bookingCode = RandomStringUtils.randomNumeric(10);
            bookingRoom.setBookingConfirmationCode(bookingCode);
        }

    }

    public void removeBookingRoom(BookingRoom bookingRoom) {
        if (bookingRoomList == null) {
            bookingRoomList = new ArrayList<>();
        }
        if (bookingRoomList.contains(bookingRoom)) {
            bookingRoomList.remove(bookingRoom);
        }

    }

}

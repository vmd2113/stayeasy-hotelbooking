package com.duongw.stayeasy.model;

import com.duongw.stayeasy.enums.BookingRoomStatus;
import com.duongw.stayeasy.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BookingRoom> bookingRoomList;

    public Customer() {
        this.bookingRoomList = new ArrayList<>();
    }

    public void addBookingRoom(BookingRoom bookingRoom) {
        if (bookingRoomList == null) {
            bookingRoomList = new ArrayList<>();

        }
        if (bookingRoom.getRoom().getRoomStatus().equals("AVAILABLE")) {
            bookingRoomList.add(bookingRoom);
            bookingRoom.setCustomer(this);
            String bookingCode = RandomStringUtils.randomNumeric(10);
            bookingRoom.setBookingRoomStatus(BookingRoomStatus.PENDING);
            bookingRoom.setBookingConfirmationCode(bookingCode);
        } else {
            throw new IllegalStateException("Room is not available for booking.");
        }

    }


    // Xóa phòng đặt
    public void removeBookingRoom(BookingRoom bookingRoom) {
        if (!bookingRoomList.remove(bookingRoom)) {
            throw new IllegalArgumentException("BookingRoom does not exist.");
        }
    }

    // Hủy đặt phòng
    public void cancelBookingRoom(BookingRoom bookingRoom) {
        //TODO: điều kiện để đặt phòng:
        // - thực hiện kiểm tra ngày CheckIn của BookingRoom sau ngày hiện tại hai ngày, thì mới thực cho hủy
        // - thực hiện kiểm tra trạng thái đặt phòng (nếu trong trạng thái pending thì cho phép thực hiện hủy đặt phòng)

        if (isCancelable(bookingRoom)) {
            bookingRoom.setBookingRoomStatus(BookingRoomStatus.CANCELED);
        } else {
            throw new IllegalStateException("Booking cannot be canceled.");
        }
    }

    // Điều kiện kiểm tra khi hủy đặt phòng
    private boolean isCancelable(BookingRoom bookingRoom) {
        // Kiểm tra trạng thái đặt phòng
        if (!bookingRoom.getBookingRoomStatus().equals(BookingRoomStatus.PENDING)) {
            return false;
        }

        // Kiểm tra ngày Check-in
        LocalDate currentDate = LocalDate.now();
        LocalDate checkInDate = bookingRoom.getCheckInDate();

        return checkInDate != null && checkInDate.isAfter(currentDate.plusDays(2));
    }

    // Check-out phòng
    public void checkOutBookingRoom(BookingRoom bookingRoom) {

        //TODO: điều kiện khi thực hiện trả phòng
        // - phòng sẽ thực hiện tự động trả phòng khi quá thời gian checkout
        // - khi khách hàng thực hiện trả phòng thì set phòng trở lại trạng thái maintain

        if (isCheckOutAllowed(bookingRoom)) {
            bookingRoom.setBookingRoomStatus(BookingRoomStatus.CHECK_OUT);
            bookingRoom.getRoom().setRoomStatus(RoomStatus.BEING_MAINTAINED);
        } else {
            throw new IllegalStateException("Cannot check out room.");
        }
    }

    // Điều kiện kiểm tra khi trả phòng
    private boolean isCheckOutAllowed(BookingRoom bookingRoom) {
        LocalDate currentDate = LocalDate.now();
        LocalDate checkOutDate = bookingRoom.getCheckOutDate();

        return checkOutDate != null && !currentDate.isBefore(checkOutDate);
    }


}

package com.duongw.stayeasy.service;

import com.duongw.stayeasy.dto.request.bookingroom.CreateBookingDTO;
import com.duongw.stayeasy.dto.response.entity.BookingRoomResponseDTO;
import com.duongw.stayeasy.model.BookingRoom;

import java.util.List;

public interface IBookingRoomService {
    BookingRoomResponseDTO convertBookingRoomToBookingRoomResponseDTO(BookingRoom bookingRoom);

    BookingRoom convertBookingRoomResponseDTOToBookingRoom(BookingRoomResponseDTO bookingRoomResponseDTO);

    BookingRoomResponseDTO createBookingRoom(Long customerId, CreateBookingDTO createBookingDTO); // Tạo đặt phòng cho customer
    void cancelBookingRoom(Long customerId, Long bookingRoomId); // Hủy đặt phòng của customer
    BookingRoomResponseDTO getCustomerBookingRoom(Long customerId, Long bookingRoomId); // Xem thông tin đặt phòng của customer
    List<BookingRoomResponseDTO> getAllCustomerBookingRooms(Long customerId); // Lấy danh sách đặt phòng của customer
    List<BookingRoomResponseDTO> getAllBookingRooms(); // Xem tất cả đặt phòng
    BookingRoomResponseDTO updateBookingRoomStatus(Long bookingRoomId, String status); // Cập nhật trạng thái đặt phòng

    void deleteBookingRoom(Long customerId, Long bookingRoomId); // Xóa đặt phòng
    void deleteBookingRoomById(Long bookingRoomId); // Xóa đặt phòng






}

package com.duongw.stayeasy.service;

import com.duongw.stayeasy.dto.request.customer.CustomerDetail;
import com.duongw.stayeasy.dto.request.customer.CustomerUpdateRequest;
import com.duongw.stayeasy.dto.response.entity.BookingRoomResponseDTO;
import com.duongw.stayeasy.model.BookingRoom;
import com.duongw.stayeasy.model.Customer;

import java.util.List;

public interface ICustomerService {

    Customer addBookingRoom(Long customerId, Long roomId, BookingRoom bookingRoom);
    Customer removeBookingRoom(Long customerId, Long bookingRoomId);
    Customer cancelBookingRoom(Long customerId, Long bookingRoomId);
    Customer checkOutBookingRoom(Long customerId, Long bookingRoomId);

    // Các thao tác với Customer
    Customer updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest);

    void deleteCustomer(Long customerId);

    // Phương thức cho vai trò STAFF
    List<CustomerDetail> getAllCustomers();
    Customer getCustomerById(Long customerId);
    List<BookingRoomResponseDTO> getCustomerBookingRooms(Long customerId);
    BookingRoom getBookingRoomById(Long bookingRoomId);








}

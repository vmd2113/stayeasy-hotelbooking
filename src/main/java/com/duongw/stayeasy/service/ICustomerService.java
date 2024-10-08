package com.duongw.stayeasy.service;

import com.duongw.stayeasy.dto.request.customer.CustomerDetail;
import com.duongw.stayeasy.dto.request.customer.CustomerUpdateRequest;
import com.duongw.stayeasy.dto.response.PageResponse;
import com.duongw.stayeasy.dto.response.entity.BookingRoomResponseDTO;
import com.duongw.stayeasy.dto.response.entity.CustomerDTO;
import com.duongw.stayeasy.model.BookingRoom;
import com.duongw.stayeasy.model.Customer;

import java.util.List;

public interface ICustomerService {

    Customer saveCustomer(Long userId, CustomerDTO customerDTO);
    Customer addBookingRoom(Long customerId, Long roomId, BookingRoom bookingRoom);
    Customer removeBookingRoom(Long customerId, Long bookingRoomId);
    Customer cancelBookingRoom(Long customerId, Long bookingRoomId);
    Customer checkOutBookingRoom(Long customerId, Long bookingRoomId);

    // Các thao tác với Customer
    Customer updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest);

    void deleteCustomer(Long customerId);

    CustomerDTO checkCustomerPhoneNumber(Long customerId);

    Customer convertToCustomer(CustomerDTO customerDTO);

    // Phương thức cho vai trò STAFF
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Long customerId);
    List<BookingRoomResponseDTO> getCustomerBookingRooms(Long customerId);
    PageResponse<?> getAllCustomersWithSortBy(int pageNo, int pageSize, String sortBy);

    PageResponse<?> getAllCustomersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);

    PageResponse<?> getAllCustomersAndSearchWithPagingAndSorting(int pageNo, int pageSize, String search, String sortBy);










}

package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.dto.request.customer.CustomerDetail;
import com.duongw.stayeasy.dto.request.customer.CustomerUpdateRequest;
import com.duongw.stayeasy.dto.response.entity.BookingRoomResponseDTO;
import com.duongw.stayeasy.dto.response.entity.CustomerDTO;
import com.duongw.stayeasy.enums.BookingRoomStatus;
import com.duongw.stayeasy.enums.RoomStatus;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.BookingRoom;
import com.duongw.stayeasy.model.Customer;
import com.duongw.stayeasy.model.Room;
import com.duongw.stayeasy.model.User;
import com.duongw.stayeasy.repository.BookingRoomRepository;
import com.duongw.stayeasy.repository.CustomerRepository;
import com.duongw.stayeasy.repository.RoomRepository;
import com.duongw.stayeasy.repository.UserRepository;
import com.duongw.stayeasy.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public Customer saveCustomer(Long userId, CustomerDTO customerDTO) {
        // Step 1: Retrieve the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Step 2: Create a new Customer instance and set its properties
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setAddress(customerDTO.getAddress());

        // Step 3: Set the relationship between user and customer
        customer.setUser(user);
        user.setCustomer(customer);

        // Step 4: Save the customer and user
        customerRepository.save(customer);
        userRepository.save(user);

        return customer;
    }





    @Override
    public Customer addBookingRoom(Long customerId, Long roomId, BookingRoom bookingRoom) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));


        if (!room.getRoomStatus().equals("AVAILABLE")) {
            throw new IllegalStateException("Room is not available for booking");
        }

        bookingRoom.setCustomer(customer);
        bookingRoom.setRoom(room);
        bookingRoom.setBookingRoomStatus(BookingRoomStatus.PENDING);
        bookingRoom.setBookingConfirmationCode(RandomStringUtils.randomNumeric(10));

        room.addBookingRoom(bookingRoom);
        customer.addBookingRoom(bookingRoom);

        return bookingRoomRepository.save(bookingRoom).getCustomer();
    }


    @Override
    public Customer removeBookingRoom(Long customerId, Long bookingRoomId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        BookingRoom bookingRoom = bookingRoomRepository.findById(bookingRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("BookingRoom not found"));

        if (!customer.getBookingRoomList().contains(bookingRoom)) {
            throw new IllegalArgumentException("BookingRoom does not belong to this customer");
        }

        customer.removeBookingRoom(bookingRoom);
        bookingRoomRepository.delete(bookingRoom);
        return customerRepository.save(customer);
    }

    @Override
    public Customer cancelBookingRoom(Long customerId, Long bookingRoomId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        BookingRoom bookingRoom = bookingRoomRepository.findById(bookingRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("BookingRoom not found"));


        customer.cancelBookingRoom(bookingRoom);
        bookingRoom.setBookingRoomStatus(BookingRoomStatus.CANCELED);
        bookingRoomRepository.save(bookingRoom);
        return customerRepository.save(customer);
    }

    @Override
    public Customer checkOutBookingRoom(Long customerId, Long bookingRoomId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        BookingRoom bookingRoom = bookingRoomRepository.findById(bookingRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("BookingRoom not found"));


        customer.checkOutBookingRoom(bookingRoom);
        bookingRoom.setBookingRoomStatus(BookingRoomStatus.CHECK_OUT);
        bookingRoom.getRoom().setRoomStatus(RoomStatus.BEING_MAINTAINED);
        bookingRoomRepository.save(bookingRoom);
        roomRepository.save(bookingRoom.getRoom());

        return customerRepository.save(customer);
    }



    @Override
    public Customer updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {


        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        existingCustomer.setFirstName(customerUpdateRequest.getFirstName());
        existingCustomer.setLastName(customerUpdateRequest.getLastName());
        existingCustomer.setPhoneNumber(customerUpdateRequest.getPhoneNumber());
        existingCustomer.setAddress(customerUpdateRequest.getAddress());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customerRepository.delete(customer);
    }

    @Override
    public List<CustomerDetail> getAllCustomers() {
        return null;
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
    }

    @Override
    public List<BookingRoomResponseDTO> getCustomerBookingRooms(Long customerId) {
        Customer customer = getCustomerById(customerId);
        List<BookingRoom> bookingRooms = customer.getBookingRoomList();
        List<BookingRoomResponseDTO> bookingRoomResponseDTOList = new ArrayList<>();
        return null;
    }

    @Override
    public BookingRoom getBookingRoomById(Long bookingRoomId) {
        return null;
    }
}

package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.dto.request.customer.CustomerUpdateRequest;
import com.duongw.stayeasy.dto.response.PageResponse;
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setAddress(customerDTO.getAddress());

        customer.setUser(user);
        user.setCustomer(customer);

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
    public CustomerDTO checkCustomerPhoneNumber(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if(customerRepository.existsByPhoneNumber(customer.getPhoneNumber())){
            throw new ResourceNotFoundException("Phone number already exists");
        }
        return convertToCustomerDTO(customer);
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .username(customer.getUser().getUsername())
                .email(customer.getUser().getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .build();
    }

    @Override
    public Customer convertToCustomer(CustomerDTO customerDTO) {
        User user = userRepository.findByUsername(customerDTO.getUsername());

        return Customer.builder()
                .user(user)
                .lastName(customerDTO.getLastName())
                .firstName(customerDTO.getFirstName())
                .phoneNumber(customerDTO.getPhoneNumber())
                .address(customerDTO.getAddress())
                .build();
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(customer -> convertToCustomerDTO(customer)).toList();
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
        return convertToCustomerDTO(customer);
    }

    @Override
    public List<BookingRoomResponseDTO> getCustomerBookingRooms(Long customerId) {
        Customer customer =customerRepository.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
        List<BookingRoom> bookingRooms = customer.getBookingRoomList();
        List<BookingRoomResponseDTO> bookingRoomResponseDTOList = new ArrayList<>();
        for (BookingRoom bookingRoom : bookingRooms) {
            BookingRoomResponseDTO bookingRoomResponseDTO = BookingRoomResponseDTO.builder()
                    .id(bookingRoom.getId())
                    .bookingRoomStatus(bookingRoom.getBookingRoomStatus())
                    .checkInDate(bookingRoom.getCheckInDate())
                    .checkOutDate(bookingRoom.getCheckOutDate())
                    .numberOfAdults(bookingRoom.getNumberOfAdults())
                    .numberOfChildren(bookingRoom.getNumberOfChildren())
                    .totalNumberOfGuest(bookingRoom.getTotalNumberOfGuest())
                    .bookingConfirmationCode(bookingRoom.getBookingConfirmationCode())
                    .customerDTO(convertToCustomerDTO(bookingRoom.getCustomer()))
                    .roomId(bookingRoom.getRoom().getId())
                    .build();
            bookingRoomResponseDTOList.add(bookingRoomResponseDTO);
        }
        return bookingRoomResponseDTOList;

    }



    //TODO: thực hiện viết custom query - search customer
    @Override
    public PageResponse<?> getAllCustomersWithSortBy(int pageNo, int pageSize, String sortBy) {
        return null;
    }

    @Override
    public PageResponse<?> getAllCustomersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        return null;
    }

    @Override
    public PageResponse<?> getAllCustomersAndSearchWithPagingAndSorting(int pageNo, int pageSize, String search, String sortBy) {
        return null;
    }


}

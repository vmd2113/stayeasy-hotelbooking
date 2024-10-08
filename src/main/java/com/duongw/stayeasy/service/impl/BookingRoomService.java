package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.dto.request.bookingroom.CreateBookingDTO;
import com.duongw.stayeasy.dto.response.entity.BookingRoomResponseDTO;
import com.duongw.stayeasy.dto.response.entity.CustomerDTO;
import com.duongw.stayeasy.enums.BookingRoomStatus;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.BookingRoom;
import com.duongw.stayeasy.model.Customer;
import com.duongw.stayeasy.repository.BookingRoomRepository;
import com.duongw.stayeasy.service.IBookingRoomService;
import com.duongw.stayeasy.service.ICustomerService;
import com.duongw.stayeasy.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor


public class BookingRoomService implements IBookingRoomService {

    private final BookingRoomRepository bookingRoomRepository;
    private final ICustomerService customerService;
    private final IRoomService roomService;

    private String generateBookingConfirmationCode() {
        return RandomStringUtils.randomNumeric(10);
    }


    @Override
    public BookingRoomResponseDTO convertBookingRoomToBookingRoomResponseDTO(BookingRoom bookingRoom) {
        BookingRoomResponseDTO bookingRoomResponseDTO = new BookingRoomResponseDTO();
        bookingRoomResponseDTO.setId(bookingRoom.getId());
        bookingRoomResponseDTO.setBookingRoomStatus(bookingRoom.getBookingRoomStatus());
        bookingRoomResponseDTO.setCheckInDate(bookingRoom.getCheckInDate());
        bookingRoomResponseDTO.setCheckOutDate(bookingRoom.getCheckOutDate());
        bookingRoomResponseDTO.setNumberOfAdults(bookingRoom.getNumberOfAdults());
        bookingRoomResponseDTO.setNumberOfChildren(bookingRoom.getNumberOfChildren());
        bookingRoomResponseDTO.setTotalNumberOfGuest(bookingRoom.getTotalNumberOfGuest());
        bookingRoomResponseDTO.setBookingConfirmationCode(bookingRoom.getBookingConfirmationCode());
        bookingRoomResponseDTO.setRoomId(bookingRoom.getRoom().getId());
        bookingRoomResponseDTO.setCustomerDTO(customerService.getCustomerById(bookingRoom.getCustomer().getId()));

        return bookingRoomResponseDTO;
    }

    @Override
    public BookingRoom convertBookingRoomResponseDTOToBookingRoom(BookingRoomResponseDTO bookingRoomResponseDTO) {

        CustomerDTO customerDTO = customerService.getCustomerById(bookingRoomResponseDTO.getCustomerDTO().getId());
        Customer customer = customerService.convertToCustomer(customerDTO);
        return BookingRoom.builder()
                .id(bookingRoomResponseDTO.getId())
                .bookingRoomStatus(bookingRoomResponseDTO.getBookingRoomStatus())
                .checkInDate(bookingRoomResponseDTO.getCheckInDate())
                .checkOutDate(bookingRoomResponseDTO.getCheckOutDate())
                .numberOfAdults(bookingRoomResponseDTO.getNumberOfAdults())
                .numberOfChildren(bookingRoomResponseDTO.getNumberOfChildren())
                .totalNumberOfGuest(bookingRoomResponseDTO.getTotalNumberOfGuest())
                .bookingConfirmationCode(bookingRoomResponseDTO.getBookingConfirmationCode())
                .room(roomService.getRoomById(bookingRoomResponseDTO.getRoomId()))
                .customer(customer)
                .build();
    }


    @Override
    public BookingRoomResponseDTO createBookingRoom(Long customerId, CreateBookingDTO createBookingDTO) {

        String bookingConfirmationCode = generateBookingConfirmationCode();
        BookingRoomResponseDTO bookingRoomResponseDTO = new BookingRoomResponseDTO();
        bookingRoomResponseDTO.setId(createBookingDTO.getRoomId());
        bookingRoomResponseDTO.setBookingRoomStatus(BookingRoomStatus.valueOf(createBookingDTO.getBookingRoomStatus()));
        bookingRoomResponseDTO.setCheckInDate(createBookingDTO.getCheckInDate());
        bookingRoomResponseDTO.setCheckOutDate(createBookingDTO.getCheckOutDate());
        bookingRoomResponseDTO.setNumberOfAdults(createBookingDTO.getNumberOfAdults());
        bookingRoomResponseDTO.setNumberOfChildren(createBookingDTO.getNumberOfChildren());
        bookingRoomResponseDTO.setTotalNumberOfGuest(createBookingDTO.getTotalNumberOfGuest());
        bookingRoomResponseDTO.setBookingConfirmationCode(bookingConfirmationCode);
        bookingRoomResponseDTO.setRoomId(createBookingDTO.getRoomId());
        bookingRoomResponseDTO.setCustomerDTO(customerService.getCustomerById(customerId));

        return bookingRoomResponseDTO;

    }

    @Override
    public void cancelBookingRoom(Long customerId, Long bookingRoomId) {

        BookingRoom bookingRoom = bookingRoomRepository.findById(bookingRoomId).orElseThrow(() -> new ResourceNotFoundException("BookingRoom not found"));
        bookingRoom.setBookingRoomStatus(BookingRoomStatus.CANCELED);
        bookingRoomRepository.save(bookingRoom);
        customerService.cancelBookingRoom(customerId, bookingRoomId);
    }

    @Override
    public BookingRoomResponseDTO getCustomerBookingRoom(Long customerId, Long bookingRoomId) {
        BookingRoom bookingRoom = bookingRoomRepository.findById(bookingRoomId).orElseThrow(() -> new ResourceNotFoundException("BookingRoom not found"));
        BookingRoomResponseDTO bookingRoomResponseDTO = convertBookingRoomToBookingRoomResponseDTO(bookingRoom);
        return bookingRoomResponseDTO;
    }

    @Override
    public List<BookingRoomResponseDTO> getAllCustomerBookingRooms(Long customerId) {
        CustomerDTO customer = customerService.getCustomerById(customerId);
        List<BookingRoomResponseDTO> bookingRooms = customer.getBookingRoomList();
        return bookingRooms;
    }

    @Override
    public List<BookingRoomResponseDTO> getAllBookingRooms() {
        return bookingRoomRepository.findAll().stream().map(bookingRoom -> convertBookingRoomToBookingRoomResponseDTO(bookingRoom)).toList();
    }

    @Override
    public BookingRoomResponseDTO updateBookingRoomStatus(Long bookingRoomId, String status) {
        BookingRoom bookingRoom = bookingRoomRepository.findById(bookingRoomId).orElseThrow(() -> new ResourceNotFoundException("BookingRoom not found"));
        bookingRoom.setBookingRoomStatus(BookingRoomStatus.valueOf(status));
        bookingRoomRepository.save(bookingRoom);
        return convertBookingRoomToBookingRoomResponseDTO(bookingRoom);
    }

    @Override
    public void deleteBookingRoom(Long customerId, Long bookingRoomId) {
        BookingRoom bookingRoom = bookingRoomRepository.findById(bookingRoomId).orElseThrow(() -> new ResourceNotFoundException("BookingRoom not found"));
        bookingRoomRepository.delete(bookingRoom);
        customerService.removeBookingRoom(customerId, bookingRoomId);
    }

    @Override
    public void deleteBookingRoomById(Long bookingRoomId) {
        BookingRoom bookingRoom = bookingRoomRepository.findById(bookingRoomId).orElseThrow(()-> new ResourceNotFoundException("Not found"));
        bookingRoomRepository.delete(bookingRoom);

    }
}

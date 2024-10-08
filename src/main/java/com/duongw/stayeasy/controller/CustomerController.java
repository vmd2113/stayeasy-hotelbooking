package com.duongw.stayeasy.controller;


import com.duongw.stayeasy.configuration.AppConstant;
import com.duongw.stayeasy.dto.request.customer.CustomerUpdateRequest;
import com.duongw.stayeasy.dto.response.ApiResponse;
import com.duongw.stayeasy.dto.response.PageResponse;
import com.duongw.stayeasy.dto.response.entity.BookingRoomResponseDTO;
import com.duongw.stayeasy.dto.response.entity.CustomerDTO;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.BookingRoom;
import com.duongw.stayeasy.model.Customer;
import com.duongw.stayeasy.service.ICustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = AppConstant.API_PREFIX_CUSTOMER)
@RequiredArgsConstructor

public class CustomerController {


    private final ICustomerService customerService;

    // add and check in booking room
    @PostMapping(path = "/{customer_id}/booking_room/{room_id}")
    public ResponseEntity<ApiResponse<?>> checkInBookingRoom(@RequestBody BookingRoom bookingRoom, @PathVariable("room_id") Long roomId, @PathVariable("customer_id") Long customerId) {
        try {
            Customer customer = customerService.addBookingRoom(customerId, roomId, bookingRoom);
            ApiResponse<Customer> response = new ApiResponse<>(true, "Check in booking room success", HttpStatus.OK.value(), customer);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Check in booking room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Check in booking room failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }


    @PatchMapping(path = "/{customer_id}/booking_room/{booking_room}/checkout")
    public ResponseEntity<ApiResponse<?>> checkOutBookingRoom(@PathVariable("booking_room") Long bookingRoomId, @PathVariable("customer_id") Long customerId) {
        try {
            Customer customer = customerService.checkOutBookingRoom(customerId, bookingRoomId);
            ApiResponse<Customer> response = new ApiResponse<>(true, "Check out booking room success", HttpStatus.OK.value(), customer);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Check out booking room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Check out booking room failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{customer_id}/booking_room/{booking_room}/remove")
    public ResponseEntity<ApiResponse<?>> removeBookingRoom(@PathVariable("booking_room") Long bookingRoomId, @PathVariable("customer_id") Long customerId) {
        try {
            Customer customer = customerService.removeBookingRoom(customerId, bookingRoomId);
            ApiResponse<Customer> response = new ApiResponse<>(true, "Remove booking room success", HttpStatus.OK.value(), customer);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Remove booking room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Remove booking room failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{customer_id}/booking_room/{booking_room}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelBookingRoom(@PathVariable("booking_room") Long bookingRoomId, @PathVariable("customer_id") Long customerId) {
        try {
            Customer customer = customerService.cancelBookingRoom(customerId, bookingRoomId);
            ApiResponse<Customer> response = new ApiResponse<>(true, "cancel booking room success", HttpStatus.OK.value(), customer);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "cancel booking room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "cancel booking room failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{customer_id}")
    public ResponseEntity<ApiResponse<?>> updateCustomer(@PathVariable("customer_id") Long customerId, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        try {
            Customer customer = customerService.updateCustomer(customerId, customerUpdateRequest);
            ApiResponse<Customer> response = new ApiResponse<>(true, "Update customer success", HttpStatus.OK.value(), customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Update customer failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Update customer failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/{customer_id}/check_phone_number")
    public ResponseEntity<ApiResponse<?>> checkCustomerPhoneNumber(@PathVariable("customer_id") Long customerId) {
        try {
            CustomerDTO customerDTO = customerService.checkCustomerPhoneNumber(customerId);
            ApiResponse<CustomerDTO> response = new ApiResponse<>(true, "Check customer phone number success", HttpStatus.OK.value(), customerDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Check customer phone number failed " + e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Check customer phone number failed " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/list-all-customers")
    public ResponseEntity<ApiResponse<?>> getAllCustomers() {
        try {
            List<CustomerDTO> customerList = customerService.getAllCustomers();
            ApiResponse<List<CustomerDTO>> response = new ApiResponse<>(true, "Get all customer success", HttpStatus.OK.value(), customerList);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Get all customer failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{customer_id}/booking_rooms")
    public ResponseEntity<ApiResponse<?>> getCustomerBookingRooms(@PathVariable("customer_id") Long customerId) {
        try {
            List<BookingRoomResponseDTO> bookingRoomList = customerService.getCustomerBookingRooms(customerId);
            ApiResponse<List<BookingRoomResponseDTO>> response = new ApiResponse<>(true, "Get customer booking room success", HttpStatus.OK.value(), bookingRoomList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Get customer booking room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Get customer booking room failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/list-all-customers/search")

    public ResponseEntity<ApiResponse<?>> getAllCustomerBySearch(@RequestParam(value = "page_no", defaultValue = "0", required = false) int pageNo,
                                                                 @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
                                                                 @RequestParam(value = "sort_by", required = false) String sortBy,
                                                                 @RequestParam(value = "search_key", required = false) String searchKey) {


        try {
            PageResponse<?> customers = customerService.getAllCustomersWithSortBy(pageNo, pageSize, sortBy);
            ApiResponse<PageResponse<?>> response = new ApiResponse<>(true, "Get all customer success", HttpStatus.OK.value(), customers);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Get all customer failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/list-all-customers/search-with-paging-and-sorting")
    public ResponseEntity<ApiResponse<?>> getAllCustomersWithSortByMultipleColumns(@RequestParam(value = "page_no", defaultValue = "0", required = false) int pageNo,
                                                                                   @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
                                                                                   @RequestParam(value = "sort_by", required = false) String sortBy,
                                                                                   @RequestParam(value = "search_key", required = false) String searchKey) {


        try {
            PageResponse<?> customers = customerService.getAllCustomersWithSortByMultipleColumns(pageNo, pageSize, sortBy, searchKey);
            ApiResponse<PageResponse<?>> response = new ApiResponse<>(true, "Get all customer success", HttpStatus.OK.value(), customers);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Get all customer failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/list-all-customers-and-search-with-paging-and-sorting")
    public ResponseEntity<ApiResponse<?>> getAllCustomersAndSearchWithPagingAndSorting(@RequestParam(value = "page_no", defaultValue = "0", required = false) int pageNo,
                                                                                       @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
                                                                                       @RequestParam(value = "sort_by", required = false) String sortBy,
                                                                                       @RequestParam(value = "search_key", required = false) String searchKey) {


        try {
            PageResponse<?> customers = customerService.getAllCustomersAndSearchWithPagingAndSorting(pageNo, pageSize, sortBy, searchKey);
            ApiResponse<PageResponse<?>> response = new ApiResponse<>(true, "Get all customer success", HttpStatus.OK.value(), customers);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Get all customer failed", HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/{customerId}")
    public ResponseEntity<ApiResponse<?>> getCustomerById(@PathVariable("customerId") Long id) {
        try {
            CustomerDTO customerDTO = customerService.getCustomerById(id);
            ApiResponse<CustomerDTO> response = new ApiResponse<>(true, "Get customer success", HttpStatus.OK.value(), customerDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            ApiResponse<Customer> response = new ApiResponse<>(false, "Get customer failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<CustomerDTO> response = new ApiResponse<>(false, "Get customer failed", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<ApiResponse<?>> deleteCustomerById(@PathVariable("customerId") Long id) {
        try {
            customerService.deleteCustomer(id);
            ApiResponse<?> response = new ApiResponse<>(true, "Delete category success", HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "Room category not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

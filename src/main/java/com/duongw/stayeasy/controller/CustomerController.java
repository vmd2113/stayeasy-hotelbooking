package com.duongw.stayeasy.controller;


import com.duongw.stayeasy.dto.response.ApiResponse;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.RoomCategory;
import com.duongw.stayeasy.service.ICustomerService;
import com.duongw.stayeasy.service.impl.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/customers")
@RequiredArgsConstructor

public class CustomerController {



    private final ICustomerService customerService;

    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<ApiResponse<?>> deleteCustomerById(@PathVariable("customerId") Long id){
        try {
            customerService.deleteCustomer(id);
            ApiResponse<RoomCategory> response = new ApiResponse<>(true, "Delete category success", HttpStatus.OK.value());
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

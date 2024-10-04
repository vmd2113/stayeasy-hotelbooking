package com.duongw.stayeasy.controller;

import com.duongw.stayeasy.dto.request.user.UserLogin;
import com.duongw.stayeasy.dto.request.user.UserRegistration;
import com.duongw.stayeasy.dto.request.user.UserUpdateRequest;
import com.duongw.stayeasy.dto.response.ApiResponse;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.RoomCategory;
import com.duongw.stayeasy.model.User;
import com.duongw.stayeasy.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    // đăng nhập
    public ResponseEntity<ApiResponse<?>> login(@RequestBody UserLogin userLogin) {
        return null;
    }

    // đăng kí
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register( @RequestBody UserRegistration userRegistration) {


        try {
            User user = userService.createUser(userRegistration);
            ApiResponse<?> response = new ApiResponse<>(true, "Register successful", HttpStatus.CREATED.value(), user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<?> response = new ApiResponse<>(false, e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
    }

    //update use account
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUserAccount(@RequestBody UserUpdateRequest userUpdateRequest, @PathVariable(name = "userId") Long userId) {

        try {
            User user = userService.updateUser(userUpdateRequest, userId);
            ApiResponse<?> response = new ApiResponse<>(true, "Update successful", HttpStatus.CREATED.value(), user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ResourceNotFoundException exception) {
            ApiResponse<?> response = new ApiResponse<>(false, "User not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception exception) {
            ApiResponse<?> response = new ApiResponse<>(false, "Something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // disable account
    @PatchMapping("/status/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUserStatus(@RequestParam boolean status, @PathVariable(name = "userId") Long userId) {
        User updatedUser = userService.changeUserStatus(status, userId);
        ApiResponse<?> response = new ApiResponse<>(true, "Update successful", HttpStatus.OK.value(), updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // delete account
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> deleteUserAccount(@PathVariable(name = "userId") Long userId) {

        try {
            userService.deleteUser(userId);
            ApiResponse<RoomCategory> response = new ApiResponse<>(true, "Delete user success", HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "User category not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get list user
    @GetMapping(path = "list-all-users")
    public ResponseEntity<ApiResponse<?>> getListUser(){
        try {
            List<User> list = userService.getAllUsers();
            ApiResponse<List<?>> response = new ApiResponse<>(true, "Get all user success", HttpStatus.OK.value(), list);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "list-all-users-by-role")
    public ResponseEntity<ApiResponse<?>> getListUserByRole(@RequestParam(name = "role") String roleName){
        try {
            List<User> list = userService.getUsersByRole(roleName);
            ApiResponse<List<?>> response = new ApiResponse<>(true, "Get all user success", HttpStatus.OK.value(), list);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get user by id
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable(name = "userId") Long userId) {

        try {
            User user = userService.getUserById(userId);
            ApiResponse<?> response = new ApiResponse<>(true, "Get user successful", HttpStatus.OK.value(), user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            ApiResponse<?> response = new ApiResponse<>(false, "User not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception exception) {
            ApiResponse<?> response = new ApiResponse<>(false, "Something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get user by search (search keyword [include email/ username])
    //TODO: custome query - search user
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> getUserBySearch(@RequestBody UserUpdateRequest userUpdateRequest, @PathVariable(name = "userId") Long userId) {

        return null;
    }

    // existed by email

    @GetMapping("/check-email/{userId}")
    public ResponseEntity<ApiResponse<?>> checkUserEmail(@PathVariable(name = "userId") Long userId) {
        User user = userService.getUserById(userId);
        String email = user.getEmail();
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Email existence checked" + exists, HttpStatus.OK.value(), null));
    }

    @GetMapping("/check-username/{userId}")
    public ResponseEntity<ApiResponse<?>> checkUserUsername(@PathVariable(name = "userId") Long userId) {
        User user = userService.getUserById(userId);
        String username = user.getUsername();
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Email existence checked" + exists, HttpStatus.OK.value(), null));

    }


}

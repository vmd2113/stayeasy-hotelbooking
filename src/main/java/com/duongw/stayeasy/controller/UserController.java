package com.duongw.stayeasy.controller;

import com.duongw.stayeasy.configuration.AppConstant;
import com.duongw.stayeasy.dto.request.user.UserLogin;
import com.duongw.stayeasy.dto.request.user.UserRegistration;
import com.duongw.stayeasy.dto.request.user.UserUpdateRequest;
import com.duongw.stayeasy.dto.response.ApiResponse;
import com.duongw.stayeasy.dto.response.PageResponse;
import com.duongw.stayeasy.dto.response.entity.UserResponseDTO;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.RoomCategory;
import com.duongw.stayeasy.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = AppConstant.API_PREFIX_USER)
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    // đăng nhập
    public ResponseEntity<ApiResponse<?>> login(@RequestBody UserLogin userLogin) {
        return null;
    }


    @Operation(
            method = "POST",
            summary = "User registration",
            description = "Send a request via this API to create a new user and a new customer (default role is customer)",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "User successfully registered",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or user already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserRegistration userRegistration) {

        try {
            UserResponseDTO userResponseDTO = userService.createUser(userRegistration);
            ApiResponse<?> response = new ApiResponse<>(true, "Register successful", HttpStatus.CREATED.value(), userResponseDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<?> response = new ApiResponse<>(false, e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
    }

    @Operation(
            method = "PUT",
            summary = "Update user account",
            description = "Update the user account details for the specified user ID. Requires updated user information.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User account successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUserAccount(@RequestBody UserUpdateRequest userUpdateRequest, @PathVariable(name = "userId") Long userId) {

        try {
            UserResponseDTO user = userService.updateUser(userUpdateRequest, userId);
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

    @Operation(
            method = "PATCH",
            summary = "Update user status",
            description = "Update the status of the user account (active or inactive) based on the provided user ID and status.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User status successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @PatchMapping("/status/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUserStatus(@RequestParam boolean status, @PathVariable(name = "userId") Long userId) {
        UserResponseDTO updatedUser = userService.changeUserStatus(status, userId);
        ApiResponse<?> response = new ApiResponse<>(true, "Update successful", HttpStatus.OK.value(), updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            method = "DELETE",
            summary = "Delete user account",
            description = "Delete the user account associated with the provided user ID. Returns success message if deletion is successful.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User successfully deleted",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
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

    @Operation(
            method = "GET",
            summary = "Get all users",
            description = "Retrieve a list of all registered users.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "List of users retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "No users found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @GetMapping(path = "list-all-users")
    public ResponseEntity<ApiResponse<?>> getListUser() {
        try {
            List<UserResponseDTO> list = userService.getAllUsers();
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

    @Operation(
            method = "GET",
            summary = "Get users by role",
            description = "Retrieve a list of users filtered by the specified role.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "List of users by role retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "No users found for the specified role",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )

    @GetMapping(path = "list-all-users-by-role")
    public ResponseEntity<ApiResponse<?>> getListUserByRole(@RequestParam(name = "role") String roleName) {
        try {
            List<UserResponseDTO> list = userService.getUsersByRole(roleName);
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

    @Operation(
            method = "GET",
            summary = "Get user by ID",
            description = "Retrieve user details based on the provided user ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User found and retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable(name = "userId") Long userId) {

        try {
            UserResponseDTO user = userService.getUserById(userId);
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

    @Operation(
            method = "GET",
            summary = "Get all users",
            description = "Retrieve user details based on the provided user ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User found and retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<?>> getAllUser(@RequestParam(value = "page_no", defaultValue = "0", required = false) int pageNo,
                                                     @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
                                                     @RequestParam(value = "sort_by", required = false) String sortBy) {


        try {
            PageResponse<?> users = userService.getAllUsersWithSortBy(pageNo, pageSize, sortBy);
            ApiResponse<PageResponse<?>> response = new ApiResponse<>(true, "Get all user success", HttpStatus.OK.value(), users);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(
            method = "GET",
            summary = "Get all users",
            description = "Retrieve a list of all registered users, with sorting "
    )
    @GetMapping("/list-user-and-search-with-paging-and-sorting")
    public ResponseEntity<ApiResponse<?>> getAllUserMultipleColumns(@RequestParam(value = "page_no", defaultValue = "0", required = false) int pageNo,
                                                                    @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
                                                                    @RequestParam(value = "sort_by", required = false) String sortBy) {


        try {
            PageResponse<?> users = userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sortBy);
            ApiResponse<PageResponse<?>> response = new ApiResponse<>(true, "Get all user success", HttpStatus.OK.value(), users);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            method = "GET",
            summary = "Get user by search",
            description = "get user by search (search keyword [include email/ username])",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User found and retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    //TODO: custome query - search user
    @GetMapping("/search")

    public ResponseEntity<ApiResponse<?>> getAllUserBySearch(@RequestParam(value = "page_no", defaultValue = "0", required = false) int pageNo,
                                                             @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
                                                             @RequestParam(value = "sort_by", required = false) String sortBy,
                                                             @RequestParam(value = "search_key", required = false) String searchKey) {


        try {
            PageResponse<?> users = userService.getAllUsersAndSearchWithPagingAndSorting(pageNo, pageSize, searchKey, sortBy);
            ApiResponse<PageResponse<?>> response = new ApiResponse<>(true, "Get all user success", HttpStatus.OK.value(), users);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // existed by email

    @Operation(
            method = "GET",
            summary = "check user email",
            description = "check user email"

    )
    @GetMapping("/check-email/{userId}")
    public ResponseEntity<ApiResponse<?>> checkUserEmail(@PathVariable(name = "userId") Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        String email = user.getEmail();
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Email existence checked" + exists, HttpStatus.OK.value(), null));
    }

    @Operation(
            method = "GET",
            summary = "check user username",
            description = "check user username"

    )

    @GetMapping("/check-username/{userId}")
    public ResponseEntity<ApiResponse<?>> checkUserUsername(@PathVariable(name = "userId") Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        String username = user.getUsername();
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Email existence checked" + exists, HttpStatus.OK.value(), null));

    }


}

package com.duongw.stayeasy.controller;

import com.duongw.stayeasy.dto.response.ApiResponse;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.RoomCategory;
import com.duongw.stayeasy.service.IRoomCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final IRoomCategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllRoomCategory() {
        try {
            List<RoomCategory> roomCategoryList = categoryService.getAllRoomCategories();
            ApiResponse<List<RoomCategory>> response = new ApiResponse<>(true, "Get all room categories success", HttpStatus.OK.value(), roomCategoryList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "Room categories not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> getRoomCategoryById(@PathVariable(name = "id") Long categoryId) {
        try {
            List<RoomCategory> roomCategoryList = categoryService.getAllRoomCategories();
            ApiResponse<List<RoomCategory>> response = new ApiResponse<>(true, "Ge room categories success", HttpStatus.OK.value(), roomCategoryList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "Room categories not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping()
    public ResponseEntity<ApiResponse<?>> addCategory(@RequestBody RoomCategory category) {
        try {
            RoomCategory roomCategory = categoryService.addRoomCategory(category);
            ApiResponse<RoomCategory> response = new ApiResponse<>(true, "Add category success", HttpStatus.OK.value(), roomCategory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> updateCategory(@RequestBody RoomCategory category, @PathVariable(name = "id") Long categoryId) {
        try {
            RoomCategory roomCategory = categoryService.updateRoomCategory(category, categoryId);
            ApiResponse<RoomCategory> response = new ApiResponse<>(true, "Update category success", HttpStatus.OK.value(), roomCategory);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "Room category not found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable(name = "id") Long categoryId) {
        try {
            categoryService.deleteRoomCategory(categoryId);
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

package com.duongw.stayeasy.controller;

import com.duongw.stayeasy.dto.request.room.CreateRoomDTO;
import com.duongw.stayeasy.dto.request.room.RoomDTO;
import com.duongw.stayeasy.dto.request.room.UpdateRoomDTO;
import com.duongw.stayeasy.dto.response.ApiResponse;
import com.duongw.stayeasy.enums.RoomStatus;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.Room;
import com.duongw.stayeasy.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/rooms")
@RequiredArgsConstructor

public class RoomController {

    private final IRoomService roomService;


    @PostMapping()
    public ResponseEntity<ApiResponse<?>> createRoom(@RequestBody CreateRoomDTO roomDTO) {
        try {
            Room room = roomService.createRoom(roomDTO);

            ApiResponse<?> response = new ApiResponse<>(true, "Create room success", HttpStatus.OK.value(), room);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Create room fall, not found category", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Create room fall, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> updateRoom(@RequestBody UpdateRoomDTO updateRoomDTO, @PathVariable(name = "id") Long roomId) {

        try {
            Room room = roomService.updateRoom(updateRoomDTO, roomId);
            RoomDTO roomDTO1 = roomService.convertRoomToRoomDTO(room);

            ApiResponse<RoomDTO> response = new ApiResponse<>(true, "Update room success", HttpStatus.OK.value(), roomDTO1);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Update room fail, not found category/room", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Create room fall, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> changeRoomStatus(@PathVariable(name = "id") Long roomId, @RequestParam(name = "roomStatus") RoomStatus roomStatus) {
        try {
            Room room = roomService.changeRoomStatus(roomId, roomStatus);
            RoomDTO roomDTO1 = roomService.convertRoomToRoomDTO(room);
            ApiResponse<RoomDTO> response = new ApiResponse<>(true, "Change room status success", HttpStatus.OK.value(), roomDTO1);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Change room status fail, not found room", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Create room fall, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRoomById(@PathVariable(name = "id") Long roomId) {
        try {
            roomService.deleteRoomById(roomId);
            ApiResponse<Room> response = new ApiResponse<>(true, "Delete room success", HttpStatus.OK.value(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Delete failed, not found room", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/list-all-rooms")
    public ResponseEntity<ApiResponse<?>> getAllRooms() {
        try {
            List<Room> roomList = roomService.getAllRooms();
            List<RoomDTO> roomDTOList = roomService.convertListRoomToRoomDTO(roomList);
            ApiResponse<?> response = new ApiResponse<>(true, "Get all room success", HttpStatus.OK.value(), roomDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get all room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Create room fall, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/list-all-rooms/categories/{id}")

    public ResponseEntity<ApiResponse<?>> getAllRoomsByCategoryId(@PathVariable(name = "id") Long categoryId) {
        try {
            List<Room> roomList = roomService.getAllRoomsByCategory(categoryId);
            List<RoomDTO> roomDTOList = roomService.convertListRoomToRoomDTO(roomList);

            ApiResponse<?> response = new ApiResponse<>(true, "Get all room success", HttpStatus.OK.value(), roomDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get all room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get room failed, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/list-all-rooms/categories")
    public ResponseEntity<ApiResponse<?>> getAllRoomsByCategoryName(@RequestParam(name = "categoryName") String name) {
        try {
            List<Room> roomList = roomService.getAllRoomsByCategory(name);
            List<RoomDTO> roomDTOList = roomService.convertListRoomToRoomDTO(roomList);

            ApiResponse<?> response = new ApiResponse<>(true, "Get all room success", HttpStatus.OK.value(), roomDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get all room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get room fall, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{roomId}")
    public ResponseEntity<ApiResponse<?>> getRoomByRoomId(@PathVariable(name = "roomId") Long roomId) {
        try {
            Room room = roomService.getRoomById(roomId);
            RoomDTO roomDTO = roomService.convertRoomToRoomDTO(room);
            ApiResponse<?> response = new ApiResponse<>(true, "Get room success", HttpStatus.OK.value(), roomDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get room fall, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/")
    public ResponseEntity<ApiResponse<?>> getRoomByRoomCode(@RequestParam(name = "roomCode") String code) {
        try {
            Room room = roomService.findRoomByRoomCode(code);
            RoomDTO roomDTO = roomService.convertRoomToRoomDTO(room);
            ApiResponse<?> response = new ApiResponse<>(true, "Get room success", HttpStatus.OK.value(), roomDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<?> response = new ApiResponse<>(false, "Get room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get room fall, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/list-all-rooms-by-price")
    public ResponseEntity<ApiResponse<?>> getRoomBetweenRoomPrice(@RequestParam(name = "minPrice") double minPrice, @RequestParam(name = "maxPrice") double maxPrice) {
        try {
            List<Room> rooms = roomService.searchRoomByPriceRange(minPrice, maxPrice);
            List<RoomDTO> roomDTOList = roomService.convertListRoomToRoomDTO(rooms);

            ApiResponse<?> response = new ApiResponse<>(true, "Get list room success", HttpStatus.OK.value(), roomDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get list room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get room fall, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/search")
    public ResponseEntity<ApiResponse<?>> getRoomBySearchDescription(@RequestParam(name = "keyword") String keyword) {
        try {
            List<Room> rooms = roomService.searchRoomsByDescription(keyword);
            List<RoomDTO> roomDTOList = roomService.convertListRoomToRoomDTO(rooms);

            ApiResponse<?> response = new ApiResponse<>(true, "Get list room success", HttpStatus.OK.value(), roomDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get list room failed", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Get room fall, something was wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

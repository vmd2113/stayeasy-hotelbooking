package com.duongw.stayeasy.service;


import com.duongw.stayeasy.dto.request.room.CreateRoomDTO;
import com.duongw.stayeasy.dto.request.room.RoomDTO;
import com.duongw.stayeasy.dto.request.room.UpdateRoomDTO;
import com.duongw.stayeasy.enums.RoomStatus;
import com.duongw.stayeasy.model.Room;

import java.util.List;

public interface IRoomService {

    List<RoomDTO> convertListRoomToRoomDTO(List<Room> roomList);

    RoomDTO convertRoomToRoomDTO(Room room);

    Room createRoom(CreateRoomDTO roomDTO);

    List<Room> getAllRooms();

    List<Room> getAllRoomsByCategory(Long categoryId);

    List<Room> getAllRoomsByCategory(String categoryName);

    Room getRoomById(Long id);

    Room updateRoom(UpdateRoomDTO updateRoomDTO, Long id);

    Room changeRoomStatus(Long id, RoomStatus status);

    void deleteRoomById(Long id);

    Room findRoomByRoomCode(String roomCode);

    List<Room> searchRoomByPriceRange(double minPrice, double maxPrice);

    List<Room> searchRoomsByDescription(String keyword);





}

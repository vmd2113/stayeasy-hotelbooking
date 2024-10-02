package com.duongw.stayeasy.service;

import com.duongw.stayeasy.model.RoomCategory;

import java.util.List;

public interface IRoomCategoryService {
    List<RoomCategory> getAllRoomCategories();
    RoomCategory getRoomCategoryById(Long id);
    RoomCategory addRoomCategory(RoomCategory roomCategory);
    RoomCategory updateRoomCategory(RoomCategory romRoomCategory, Long id);
    void deleteRoomCategory(Long id);

}

package com.duongw.stayeasy.repository;

import com.duongw.stayeasy.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    Room findByRoomCode(String code);
    List<Room> findByRoomCategoryId(Long id);
    List<Room> findByRoomCategoryCategoryName(String name);
    List<Room> findByRoomPriceBetween(double min, double max);


}

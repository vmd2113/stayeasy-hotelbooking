package com.duongw.stayeasy.repository;

import com.duongw.stayeasy.model.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoom, Long> {

}

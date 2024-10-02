package com.duongw.stayeasy.repository;

import com.duongw.stayeasy.model.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<RoomCategory, Long> {
}

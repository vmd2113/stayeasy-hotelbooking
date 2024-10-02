package com.duongw.stayeasy.repository;

import com.duongw.stayeasy.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Images, Long> {

    List<Images> findByRoomImagesId(Long id);
}

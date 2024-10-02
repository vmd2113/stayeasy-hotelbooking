package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.RoomCategory;
import com.duongw.stayeasy.repository.CategoryRepository;
import com.duongw.stayeasy.service.IRoomCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomCategoryService implements IRoomCategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<RoomCategory> getAllRoomCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public RoomCategory getRoomCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Room category with " + id + " not found"));
    }

    @Override
    public RoomCategory addRoomCategory(RoomCategory roomCategory) {
        // Thêm RoomCategory mới vào database
        return categoryRepository.save(roomCategory);
    }

    @Override
    public RoomCategory updateRoomCategory(RoomCategory updatedRoomCategory, Long id) {
        // Cập nhật RoomCategory dựa trên ID, nếu không tìm thấy ném ngoại lệ
        return categoryRepository.findById(id).map(roomCategory -> {
            roomCategory.setCategoryName(updatedRoomCategory.getCategoryName());
            return categoryRepository.save(roomCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("Room category not found with id: " + id));
    }

    @Override
    public void deleteRoomCategory(Long id) {
       // Xóa RoomCategory dựa trên ID, nếu không tìm thấy ném ngoại lệ
        RoomCategory roomCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room category not found with id: " + id));
        categoryRepository.delete(roomCategory);

    }
}

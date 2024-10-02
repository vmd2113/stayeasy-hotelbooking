package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.dto.request.room.CreateRoomDTO;
import com.duongw.stayeasy.dto.request.room.RoomDTO;
import com.duongw.stayeasy.dto.request.room.UpdateRoomDTO;
import com.duongw.stayeasy.dto.response.entity.BookingRoomResponseDTO;
import com.duongw.stayeasy.dto.response.entity.ImageResponseDTO;
import com.duongw.stayeasy.enums.RoomStatus;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.Room;
import com.duongw.stayeasy.model.RoomCategory;
import com.duongw.stayeasy.repository.CategoryRepository;
import com.duongw.stayeasy.repository.RoomRepository;
import com.duongw.stayeasy.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final CategoryRepository categoryRepository;



    @Override
    public List<RoomDTO> convertListRoomToRoomDTO(List<Room> roomList){
        List<RoomDTO> roomDTOList = roomList.stream().map(room -> convertRoomToRoomDTO(room)).toList();
        return roomDTOList;
    }
    @Override
    public RoomDTO convertRoomToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomCode(room.getRoomCode());
        roomDTO.setRoomStatus(room.getRoomStatus());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setDescription(room.getDescription());

        // Chuyển đổi RoomCategory
        roomDTO.setRoomCategory(room.getRoomCategory());
        String previewUrl = "/api/v1/images/image/preview";
        // Chuyển đổi danh sách Images thành ImageResponseDTO
        List<ImageResponseDTO> imageResponseDTOs = room.getImagesList().stream()
                .map(image -> new ImageResponseDTO(image.getId(), image.getFileType(), image.getFileName(), image.getDownloadUrl(), previewUrl +"/"+ image.getId()))
                .collect(Collectors.toList());
        roomDTO.setImageResponseDTOS(imageResponseDTOs);

        // Chuyển đổi danh sách BookingRoom thành BookingRoomResponseDTO
        List<BookingRoomResponseDTO> bookingRoomResponseDTOs = room.getBookingRoomList().stream()
                .map(bookingRoom -> new BookingRoomResponseDTO(
                        bookingRoom.getId(),
                        bookingRoom.getBookingRoomStatus(),
                        bookingRoom.getCheckInDate(),
                        bookingRoom.getCheckOutDate(),
                        bookingRoom.getNumberOfAdults(),
                        bookingRoom.getNumberOfChildren(),
                        bookingRoom.getTotalNumberOfGuest(),
                        bookingRoom.getBookingConfirmationCode()))
                .collect(Collectors.toList());
        roomDTO.setBookingRoomResponseDTOList(bookingRoomResponseDTOs);

        return roomDTO;
    }

    @Override
    public Room createRoom(CreateRoomDTO roomDTO) {
        RoomCategory roomCategoryFound = (RoomCategory) categoryRepository.findById(roomDTO.getRoomCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Room room = new Room();


        room.setRoomCode(roomDTO.getRoomCode());
        room.setRoomPrice(roomDTO.getRoomPrice());
        room.setRoomStatus(RoomStatus.valueOf(roomDTO.getRoomStatus()));
        room.setDescription(roomDTO.getDescription());

        room.setRoomCategory(roomCategoryFound);
        roomCategoryFound.getRoomList().add(room);

        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getAllRoomsByCategory(Long categoryId) {
        return roomRepository.findByRoomCategoryId(categoryId);
    }

    @Override
    public List<Room> getAllRoomsByCategory(String categoryName) {
        return roomRepository.findByRoomCategoryCategoryName(categoryName);
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with " + id + " not found!"));

    }

    @Override
    public Room updateRoom(UpdateRoomDTO updatedRoom, Long id) {
        return roomRepository.findById(id).map(room -> {
            room.setRoomPrice(updatedRoom.getRoomPrice());
            room.setRoomCategory(categoryRepository.findById(updatedRoom.getRoomCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Not found category")));
            room.setRoomStatus(RoomStatus.valueOf(updatedRoom.getRoomStatus()));
            room.setDescription(updatedRoom.getDescription());
            room.setRoomCode(updatedRoom.getRoomCode());
            return roomRepository.save(room);
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @Override
    public Room changeRoomStatus(Long id, RoomStatus status) {
        return roomRepository.findById(id).map(room -> {
            room.setRoomStatus(status);
            return roomRepository.save(room);
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @Override
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        roomRepository.delete(room);
    }

    @Override
    public Room findRoomByRoomCode(String roomCode) {
        return roomRepository.findByRoomCode(roomCode);
    }

    @Override
    public List<Room> searchRoomByPriceRange(double minPrice, double maxPrice) {
        return roomRepository.findByRoomPriceBetween(minPrice, maxPrice);
    }

    //TODO: thực hiện custom query
    @Override
    public List<Room> searchRoomsByDescription(String keyword) {
        return null;
    }

//    private PageResponse<?> convertToPageResponse(Page<User> users, Pageable pageable) {
//        List<UserDetailResponse> response = users.stream().map(user -> UserDetailResponse.builder()
//                .id(user.getId())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .email(user.getEmail())
//                .phone(user.getPhone())
//                .build()).toList();
//        return PageResponse.builder()
//                .pageNo(pageable.getPageNumber())
//                .pageSize(pageable.getPageSize())
//                .total(users.getTotalPages())
//                .items(response)
//                .build();
//    }
}

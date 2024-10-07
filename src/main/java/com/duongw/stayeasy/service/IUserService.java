package com.duongw.stayeasy.service;

import com.duongw.stayeasy.dto.request.user.UserLogin;
import com.duongw.stayeasy.dto.request.user.UserRegistration;
import com.duongw.stayeasy.dto.request.user.UserUpdateRequest;
import com.duongw.stayeasy.dto.response.PageResponse;
import com.duongw.stayeasy.dto.response.entity.UserResponseDTO;
import com.duongw.stayeasy.model.Role;
import com.duongw.stayeasy.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserResponseDTO createUser(UserRegistration userRegistration);

    UserResponseDTO login(UserLogin userLogin);

    UserResponseDTO updateUser(UserUpdateRequest user, Long userId);

    void deleteUser(Long userId);

    UserResponseDTO getUserById(Long userId);

    UserResponseDTO getUserByUsername(String username);

    UserResponseDTO getUserByEmail(String email);


    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<UserResponseDTO> getAllUsers();

    List<UserResponseDTO> searchUser(String keyword);

    UserResponseDTO changeUserStatus(boolean status, Long userId);


    List<UserResponseDTO> getUsersByRole(String roleName);


    // Optional methods as per your application requirements (not required)
    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);

    PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);

    PageResponse<?> getAllUsersAndSearchWithPagingAndSorting(int pageNo, int pageSize, String search, String sortBy);

    PageResponse<?> advanceSearchWithCriteria(int pageNo, int pageSize, String sortBy, String address, String... search);

    PageResponse<?> advanceSearchWithSpecifications(Pageable pageable, String[] user, String[] address);

    // Additional methods as per your application requirements
}

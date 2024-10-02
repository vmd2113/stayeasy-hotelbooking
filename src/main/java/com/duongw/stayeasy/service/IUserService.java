package com.duongw.stayeasy.service;

import com.duongw.stayeasy.dto.request.user.UserRegistration;
import com.duongw.stayeasy.dto.request.user.UserUpdateRequest;
import com.duongw.stayeasy.model.Role;
import com.duongw.stayeasy.model.User;

import java.util.List;

public interface IUserService {
    User createUser(UserRegistration userRegistration);

    User updateUser(UserUpdateRequest user);

    void deleteUser(Long userId);

    User getUserById(Long userId);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> searchUser(String keyword);
    User changeUserStatus (boolean status, Long userId);



    // Additional methods as per your application requirements
}

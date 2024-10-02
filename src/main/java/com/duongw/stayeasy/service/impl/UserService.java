package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.dto.request.user.UserRegistration;
import com.duongw.stayeasy.dto.request.user.UserUpdateRequest;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.Customer;
import com.duongw.stayeasy.model.Role;
import com.duongw.stayeasy.model.User;
import com.duongw.stayeasy.repository.CustomerRepository;
import com.duongw.stayeasy.repository.RoleRepository;
import com.duongw.stayeasy.repository.UserRepository;
import com.duongw.stayeasy.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;

    @Override
    public User createUser(UserRegistration userRegistration) {
        Role role = roleRepository.findByName("ROLE_CUSTOMER");
        Set<Role> roles = new HashSet<>();

        if(role!= null){
            roles.add(role);
            Customer customer = new Customer();
            User user = new User();
            user.setRoles(roles);
            user.setCustomer(customer);
            user.setEmail(userRegistration.getEmail());
            user.setPassword(userRegistration.getPassword());
            user.setEnabled(true);
            user.setUsername(userRegistration.getUsername());

            userRepository.save(user);
            return user;

        }
        return null;


    }

    //TODO: thắc mắc khi người dùng với với trò customer thực hiện update user thì thực hiện update bên UserService hay CustomerService
    @Override
    public User updateUser(UserUpdateRequest user) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);

    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // TODO: thực hiện viết custom query - search trên cả 2 trường là username và email
    @Override
    public List<User> searchUser(String keyword) {
        return null;
    }

    @Override
    public User changeUserStatus(boolean status, Long userId) {
        User user = getUserById(userId);
        user.setEnabled(status);
        return userRepository.save(user);

    }
}

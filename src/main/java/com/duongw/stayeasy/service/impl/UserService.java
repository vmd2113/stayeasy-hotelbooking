package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.dto.request.user.UserLogin;
import com.duongw.stayeasy.dto.request.user.UserRegistration;
import com.duongw.stayeasy.dto.request.user.UserUpdateRequest;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.Customer;
import com.duongw.stayeasy.model.Role;
import com.duongw.stayeasy.model.User;
import com.duongw.stayeasy.repository.CustomerRepository;
import com.duongw.stayeasy.repository.RoleRepository;
import com.duongw.stayeasy.repository.UserRepository;
import com.duongw.stayeasy.service.ICustomerService;
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
    private final ICustomerService customerService;


    @Override
    public User createUser(UserRegistration userRegistration) {
        Role role = roleRepository.findByName("ROLE_CUSTOMER");
        Set<Role> roles = new HashSet<>();

        if (role != null) {
            roles.add(role);

            // Create User without customer initially
            User user = new User();
            user.setRoles(roles);
            user.setEmail(userRegistration.getEmail());
            user.setPassword(userRegistration.getPassword());
            user.setEnabled(true);
            user.setUsername(userRegistration.getUsername());

            // Save the user first to generate user_id
            user = userRepository.save(user);

            // Create Customer and link it to the already saved user
            Customer customer = new Customer();
            customer.setAddress(userRegistration.getAddress());
            customer.setName(userRegistration.getName());
            customer.setPhoneNumber(userRegistration.getPhoneNumber());

            // Set the bidirectional relationship
            customer.setUser(user);
            user.setCustomer(customer);

            // Now save the customer after user has been persisted
//            customerService.

            // Update the user with the newly saved customer and save it again if necessary
            userRepository.save(user);

            return user;
        }

        return null;
    }






    //TODO: thực hiện login và validation input
    @Override
    public User login(UserLogin userLogin) {
        return null;
    }

    // thực hiện update user - tạm thời cho phép thay đổi email - chưa thực hiện verify)
    @Override
    public User updateUser(UserUpdateRequest user, Long userId) {
        User user1 = getUserById(userId);

        user1.setEmail(user.getEmail());
        return userRepository.save(user1);
    }

    @Override
    public void deleteUser(Long userId) {

        // Retrieve the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Customer customer = user.getCustomer();
        customerService.deleteCustomer(customer.getId());
        // The customer will be deleted automatically because of orphanRemoval=true
        userRepository.delete(user);
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

    @Override
    public List<User> getUsersByRole(String roleName) {
        return roleRepository.findUsersByRoleName(roleName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

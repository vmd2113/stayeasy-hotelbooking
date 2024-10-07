package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.configuration.AppConstant;
import com.duongw.stayeasy.dto.request.user.UserLogin;
import com.duongw.stayeasy.dto.request.user.UserRegistration;
import com.duongw.stayeasy.dto.request.user.UserUpdateRequest;
import com.duongw.stayeasy.dto.response.PageResponse;
import com.duongw.stayeasy.dto.response.entity.CustomerDTO;
import com.duongw.stayeasy.dto.response.entity.UserResponseDTO;
import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.Customer;
import com.duongw.stayeasy.model.Role;
import com.duongw.stayeasy.model.User;
import com.duongw.stayeasy.repository.RoleRepository;
import com.duongw.stayeasy.repository.SearchUserRepository;
import com.duongw.stayeasy.repository.UserRepository;
import com.duongw.stayeasy.service.ICustomerService;
import com.duongw.stayeasy.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor

@Slf4j

public class UserService implements IUserService {

    final String LIKE_FORMAT = "%%%s%%%";


    private final UserRepository userRepository;
    private final SearchUserRepository searchUserRepository;
    private final RoleRepository roleRepository;
    private final ICustomerService customerService;


    @Override
    public UserResponseDTO createUser(UserRegistration userRegistration) {
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
            customer.setFirstName(userRegistration.getFirstName());
            customer.setLastName(userRegistration.getLastName());
            customer.setPhoneNumber(userRegistration.getPhoneNumber());

            // Set the bidirectional relationship
            customer.setUser(user);
            user.setCustomer(customer);

            // Now save the customer after user has been persisted
//            customerService.

            // Update the user with the newly saved customer and save it again if necessary
            userRepository.save(user);
            UserResponseDTO userResponseDTO = convertToUserResponseDTO(user);

            return userResponseDTO;
        }
        return null;


    }


    //TODO: thực hiện login và validation input
    @Override
    public UserResponseDTO login(UserLogin userLogin) {
        return null;
    }

    // thực hiện update user - tạm thời cho phép thay đổi email - chưa thực hiện verify)
    @Override
    public UserResponseDTO updateUser(UserUpdateRequest user, Long userId) {
        User user1 = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user1.setEmail(user.getEmail());
        userRepository.save(user1);
        return convertToUserResponseDTO(user1);


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
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return convertToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return convertToUserResponseDTO(user);
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
    public List<UserResponseDTO> searchUser(String keyword) {
        return null;
    }

    @Override
    public UserResponseDTO changeUserStatus(boolean status, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToUserResponseDTO(user);

    }

    @Override
    public List<UserResponseDTO> getUsersByRole(String roleName) {
        return roleRepository.findUsersByRoleName(roleName).stream().map(user -> convertToUserResponseDTO(user)).toList();
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> convertToUserResponseDTO(user)).toList();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc

            Pattern pattern = Pattern.compile(AppConstant.SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }


        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        Page<User> users = userRepository.findAll(pageable);
        return convertToPageResponse(users, pageable);
    }

    // Optional methods as per your application requirements (not required)
    @Override
    public PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int page = 0;
        if (pageNo > 0) page = pageNo - 1;
        List<Sort.Order> orders = new ArrayList<>();

        if (sorts != null) {
            for (String sortBy : sorts) {
                log.info("sortBy: {}", sortBy);
                // firstName:asc|desc
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    if (matcher.group(3).equalsIgnoreCase("asc")) {
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(orders));
        Page<User> users = userRepository.findAll(pageable);
        return convertToPageResponse(users, pageable);
    }

    @Override
    public PageResponse<?> getAllUsersAndSearchWithPagingAndSorting(int pageNo, int pageSize, String search, String sortBy) {
        return searchUserRepository.searchUser(pageNo, pageSize, search, sortBy);
    }

    @Override
    public PageResponse<?> advanceSearchWithCriteria(int pageNo, int pageSize, String sortBy, String address, String... search) {
        return null;
    }

    @Override
    public PageResponse<?> advanceSearchWithSpecifications(Pageable pageable, String[] user, String[] address) {
        return null;
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .username(customer.getUser().getUsername())
                .email(customer.getUser().getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .build();
    }

    public UserResponseDTO convertToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRoles().stream().map(Role::getName).toList())
                .customerDTO(convertToCustomerDTO(user.getCustomer()))
                .build();

    }

    public User convertToUser(UserResponseDTO userResponseDTO) {
        return User.builder()
                .username(userResponseDTO.getUsername())
                .email(userResponseDTO.getEmail())
                .customer(convertToCustomer(userResponseDTO.getCustomerDTO()))
                .build();
    }

    public Customer convertToCustomer(CustomerDTO customerDTO) {
        return Customer.builder()
                .lastName(customerDTO.getLastName())
                .firstName(customerDTO.getFirstName())
                .phoneNumber(customerDTO.getPhoneNumber())
                .address(customerDTO.getAddress())
                .build();
    }

    /**
     * Convert Page<User> to PageResponse
     *
     * @param users
     * @param pageable
     * @return
     */
    private PageResponse<?> convertToPageResponse(Page<User> users, Pageable pageable) {
        List<UserResponseDTO> response = users.stream().map(user -> UserResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRoles().stream().map(Role::getName).toList())
                .customerDTO(convertToCustomerDTO(user.getCustomer()))
                .build()).toList();
        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .total(users.getTotalPages())
                .items(response)
                .build();
    }


}

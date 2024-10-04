package com.duongw.stayeasy.repository;

import com.duongw.stayeasy.model.Role;
import com.duongw.stayeasy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<User> getUserByRoles(Role role);
}

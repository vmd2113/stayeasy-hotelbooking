package com.duongw.stayeasy.service.impl;

import com.duongw.stayeasy.exception.ResourceNotFoundException;
import com.duongw.stayeasy.model.Role;
import com.duongw.stayeasy.model.User;
import com.duongw.stayeasy.repository.RoleRepository;
import com.duongw.stayeasy.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor


public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;


    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }


}

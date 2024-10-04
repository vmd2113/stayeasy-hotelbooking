package com.duongw.stayeasy.service;

import com.duongw.stayeasy.model.Role;
import com.duongw.stayeasy.model.User;

import java.util.List;

public interface IRoleService {
    Role getRoleById(Long id);
    Role getRoleByName(String name);


}

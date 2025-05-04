package com.electricity.system.service;

import com.electricity.system.model.User;

public interface UserService {
    User save(User user);
    User findByUsername(String username);
}

package com.asgerkrabbe.maintenancelog.service;

import com.asgerkrabbe.maintenancelog.entity.*;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> findByUsername(String username);
}



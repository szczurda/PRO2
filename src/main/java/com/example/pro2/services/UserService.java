package com.example.pro2.services;

import com.example.pro2.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
   User saveUser(User user);
}

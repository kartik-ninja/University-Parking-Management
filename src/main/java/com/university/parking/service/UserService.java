package com.university.parking.service;

import com.university.parking.dto.UserRegistrationDto;
import com.university.parking.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void save(User user);
    User save(UserRegistrationDto registrationDto);
    boolean emailExists(String email);
    boolean universityIdExists(String universityId);
    User getUserByEmail(String email);
    User getUserById(Long id);
    User update(User user);
}

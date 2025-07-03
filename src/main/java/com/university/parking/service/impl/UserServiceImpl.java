package com.university.parking.service.impl;

import com.university.parking.dto.UserRegistrationDto;
import com.university.parking.model.Role;
import com.university.parking.model.User;
import com.university.parking.repository.RoleRepository;
import com.university.parking.repository.UserRepository;
import com.university.parking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JavaMailSender javaMailSender;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User save(UserRegistrationDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUniversityId(dto.getUniversityId());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(false);

        String code = generateVerificationCode();
        user.setVerificationCode(code);

        Role role = roleRepository.findByName("ROLE_" + dto.getRole());
        user.setRoles(Collections.singletonList(role));

        User savedUser = userRepository.save(user);
        sendVerificationEmail(savedUser.getEmail(), code);

        return savedUser;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit code
    }

    private void sendVerificationEmail(String email, String code) {
        String subject = "Verify your email - Parking Management";
        String body = "Your verification code is: " + code;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message); // Inject JavaMailSender
    }


    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean universityIdExists(String universityId) {
        return userRepository.existsByUniversityId(universityId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("Invalid credentials");

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(), // ← this line makes it work
                true, true, true,
                user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(r.getName()))
                        .toList()
        );
    }

    @Override
    public User update(User user) {
        return userRepository.save(user); // simple update
    }

}

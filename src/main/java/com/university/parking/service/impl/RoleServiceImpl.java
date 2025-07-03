package com.university.parking.service.impl;

import com.university.parking.model.Role;
import com.university.parking.repository.RoleRepository;
import com.university.parking.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @PostConstruct
    public void seedRoles() {
        if (roleRepository.findByName("ROLE_ADMIN") == null)
            roleRepository.save(new Role(null, "ROLE_ADMIN"));
        if (roleRepository.findByName("ROLE_STUDENT") == null)
            roleRepository.save(new Role(null, "ROLE_STUDENT"));
        if (roleRepository.findByName("ROLE_TEACHER") == null)
            roleRepository.save(new Role(null, "ROLE_TEACHER"));
        if (roleRepository.findByName("ROLE_AUTHORITY") == null)
            roleRepository.save(new Role(null, "ROLE_AUTHORITY"));
    }
}

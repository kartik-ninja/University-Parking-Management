package com.university.parking.service;

import com.university.parking.model.Role;

public interface RoleService {
    Role findByName(String name);
    void seedRoles(); // Auto-creates if missing
}

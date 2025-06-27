package com.university.parking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;

    @NotEmpty(message = "Confirm Password is required")
    private String confirmPassword;

    @NotEmpty(message = "University ID is required")
    private String universityId;

    @NotEmpty(message = "Role is required")
    private String role; // Options: STUDENT, TEACHER, AUTHORITY
}

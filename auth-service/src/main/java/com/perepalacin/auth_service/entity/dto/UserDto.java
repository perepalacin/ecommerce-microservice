package com.perepalacin.auth_service.entity.dto;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Value
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    @Email(message = "Please provide a valid email")
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Password is required")
    private String password;
}

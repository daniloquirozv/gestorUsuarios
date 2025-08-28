package com.gestusers.gestorUsers.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 60)
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private String firstName;
    private String lastName;
    private Set<String> roles;

}

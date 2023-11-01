package com.example.projetcv.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull(message = "email msut not be null")
    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be valid")
    String email;

    @NotNull(message = "email msut not be null")
    @NotBlank(message = "email must not be blank")
    String password;

}

package com.example.projetcv.dto;

import com.example.projetcv.model.CV;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserSignupDto {

    @NotNull(message = "name must not be null")
    @NotBlank(message = "name must not be blank")
    private String name;

    @NotNull(message = "firsName must not be null")
    @NotBlank(message = "firsName must not be blank")
    private String firstName;

    @Email(message = "email must be correct")
    @NotNull(message = "email must not be null")
    @NotBlank(message = "email must not be blank")
    private String email;

    private String website;

    @NotNull(message = "birthday must not be null")
    private LocalDate birthday;

    @NotNull(message = "password must not be null")
    @NotBlank(message = "password must not be blank")
    @Size(min = 2, message = "password must not be too short")
    private String password;

    @NotNull(message = "passwordConfirm must not be null")
    private String passwordConfirm;

    @AssertTrue(message = "Password and password confirmation must match")
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(passwordConfirm);
    }


}

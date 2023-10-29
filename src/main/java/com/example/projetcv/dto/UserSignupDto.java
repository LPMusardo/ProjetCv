package com.example.projetcv.dto;

import com.example.projetcv.model.CV;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupDto {

    @NotNull(message = "name must not be null")
    @NotBlank(message = "name must not be blank")
    private String name;

    @NotNull(message = "firsName must not be null")
    @NotBlank(message = "firsName must not be blank")
    private String firstName;

    @Email(message = "email must be correct")
    @NotNull(message = "email must not be null")
    private String email;

    private String website;

    private LocalDate birthday;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String passwordConfirm;

    @AssertTrue(message = "Password and password confirmation must match")
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(passwordConfirm);
    }


}

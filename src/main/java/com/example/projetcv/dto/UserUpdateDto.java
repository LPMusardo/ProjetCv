package com.example.projetcv.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    /*null is valid*/
    @Size(min = 2, message = "name is to short")
    private String name;

    /*null is valid*/
    @Size(min = 2, message = "firstName is to short")
    private String firstName;

    /*null is valid*/
    @Email(message = "email must be correct")
    private String email;

    private String website;

    private LocalDate birthday;

    private String password;

    private String passwordConfirm;

    /*null is valid*/
    @AssertTrue(message = "Password and password confirmation must match")
    public boolean isPasswordPresentAndConfirmed() {
        if (password==null) return true;
        return password.equals(passwordConfirm);
    }


}

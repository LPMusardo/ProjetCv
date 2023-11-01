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
    @Size(min = 1, message = "email must be blank")
    private String email;

    private String website;

    /*null is valid*/
    private LocalDate birthday;

    private String password;

    private String passwordConfirm;

    /*null is valid*/
    @AssertTrue(message = "Password and password confirmation must match")
    public boolean isPasswordPresentAndConfirmed() {
        if (password==null) return true;
        return password.equals(passwordConfirm);
    }

    @AssertTrue(message = "Password must not be too short")
    public boolean PasswordSize() {
        if (password==null) return true;
        return password.length()>2 && password.equals(passwordConfirm);
    }


}

package com.example.projetcv.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserUpdateDto {

    /*null is valid*/
    @Size(min = 2, message = "name is too short")
    private String name;

    /*null is valid*/
    @Size(min = 2, message = "firstName is too short")
    private String firstName;

    /*null is valid*/
    @Email(message = "email must be correct")
    @Size(min = 1, message = "email must not be blank")
    private String email;

    private String website;

    /* because of the casting in LocalDate, if birthday is present, it must be valid*/
    private LocalDate birthday;

    private String password;

    /*null is valid if password is null too*/
    private String passwordConfirm;



    /******************************************************************
     * METHOD NAME MUST BEGIN WITH "is" like in "isPasswordLongEnough"
     * otherwise you will cry
     /******************************************************************/
    /*null is valid*/
    @AssertTrue(message = "Password and password confirmation must match")
    public boolean isPasswordPresentAndConfirmed() {
        if (password==null) return true;
        return password.equals(passwordConfirm);
    }

    /*null is valid*/
    @AssertTrue(message = "Password must not be too short")
    public boolean isPasswordLongEnough() {
        if (password==null) return true;
        return password.length()>2 && password.equals(passwordConfirm);
    }


}

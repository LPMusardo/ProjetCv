package com.example.projetcv.web;

import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;


/**
 * L'API d'authentification
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(this.getClass().getName());



    //-----------------------------------------------------------------------

    @GetMapping("/test")
    public String refresh(@AuthenticationPrincipal UserDetails userDetails) {
        return "You are: "+userDetails.getAuthorities();
    }

    @GetMapping("/users")
    public UserSafeDto[] getAllUsers() {
        return userService.getAllUsers();
    }


}

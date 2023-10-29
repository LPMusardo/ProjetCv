package com.example.projetcv.web;

import com.example.projetcv.dto.SafeUserDto;
import com.example.projetcv.model.User;
import com.example.projetcv.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    private ModelMapper modelMapper = new ModelMapper();


    //-----------------------------------------------------------------------

    @GetMapping("/test")
    public String refresh(@AuthenticationPrincipal UserDetails userDetails) {
        return "You are: "+userDetails.getAuthorities();
    }

    @GetMapping("/users")
    public List<User> getAllUsers(HttpServletRequest req) {
        return userService.getAllUsers();
    }


}

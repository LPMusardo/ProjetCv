package com.example.projetcv.web;

import com.example.projetcv.dto.SafeUserDto;
import com.example.projetcv.model.User;
import com.example.projetcv.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

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
    public SafeUserDto[] getAllUsers() {
        return userService.getAllUsers();
    }

   @GetMapping("/users/page")
    public Object getPageUsers(@PageableDefault(page = 0, size = 5) Pageable pageable) {
        return userService.getPageUsers(pageable);
    }


}

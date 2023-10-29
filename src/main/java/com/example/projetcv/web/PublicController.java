package com.example.projetcv.web;


import com.example.projetcv.dao.UserRepository;
import com.example.projetcv.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/public")
public class PublicController {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;


    //-----------------------------------------------------------------------------

    @GetMapping("/test")
    public String refresh(@AuthenticationPrincipal UserDetails userDetails) {
        return "You are: " + ((userDetails==null)? "[]":userDetails.getAuthorities());
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }


}

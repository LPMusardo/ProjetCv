package com.example.projetcv.web;

import com.example.projetcv.dto.LoginDto;
import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(AuthenticationController.class.getName());


    //-----------------------------------------------------------------------------------

    /**
     * Authentification et récupération d'un JWT
     */
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }


    /**
     * Supprimer le JWT de la whitelist
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest req) {
        return userService.logout(req);
    }



    /**
     * Récupérer un nouveau JWT
     */
    @GetMapping("/refresh")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req);
    }


    /**
     * Récupérer des informations sur l'utilisateur courant
     */
    @GetMapping(value = "/me")
    public UserSafeDto whoami(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.whoami(userDetails);
    }



}
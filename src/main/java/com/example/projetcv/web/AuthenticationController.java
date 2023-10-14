package com.example.projetcv.web;

import com.example.projetcv.model.Person;
import com.example.projetcv.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;


@RestController
@RequestMapping("/api/auth")
@Profile("usejwt")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    private ModelMapper modelMapper = new ModelMapper();

    //-----------------------------------------------------------------------------------


    /**
     * Authentification et récupération d'un JWT
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        logger.info("login: " + username + " " + password);
        return userService.login(username, password);
    }

    /**
     * Ajoutez une entrée GET /logout qui permet d'oublier le JWT en question.
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest req) {
        return userService.logout(req);
    }


    /**
     * Ajouter un utilisateur
     */
    @PostMapping("/signup")
    public String signup(@RequestBody Person user) {
        return userService.signup(modelMapper.map(user, Person.class));
    }


    /**
     * Récupérer un nouveau JWT
     */
    @GetMapping("/refresh")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }


    /**
     * Récupérer des informations sur l'utilisateur courant
     */
    @GetMapping(value = "/me")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Person whoami(HttpServletRequest req) {
        return modelMapper.map(userService.whoami(req), Person.class);
    }



    //-----------------------------------------------------------------------------------
    // TODO: Déplacer dans le controlleur adapté
    /**
     * Supprimer un utilisateur
     */
    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(@PathVariable String username) {
        System.out.println("delete " + username);
        userService.delete(username);
        return username;
    }

    /**
     * Récupérer des informations sur un utilisateur
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Person search(@PathVariable String username) {
        return modelMapper.map(userService.search(username), Person.class);
    }





}
package com.example.projetcv.web;


import com.example.projetcv.model.User;
import com.example.projetcv.security.JwtHelper;
import com.example.projetcv.service.UserService;
import io.jsonwebtoken.JwtHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    JwtHelper jwtTokenProvider;



    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ModelMapper modelMapper = new ModelMapper();



    @GetMapping
    public List<User> getAllUsers(HttpServletRequest req) {
        return userService.getAllUsers();
    }

    @Transactional
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id, HttpServletRequest req) {
        return userService.getUserById(id);
    }

    @DeleteMapping
    public void deleteUser(HttpServletRequest req) {
        String username = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req));
        userService.delete(username);

    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.signup(user);
    }

    @PatchMapping
    public User updateUser(@RequestBody User user){
        return userService.update(user);
    }


}

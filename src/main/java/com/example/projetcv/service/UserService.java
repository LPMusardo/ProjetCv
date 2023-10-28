package com.example.projetcv.service;

import com.example.projetcv.dao.UserRepository;
import com.example.projetcv.model.User;
import com.example.projetcv.security.JwtHelper;
import com.example.projetcv.security.MyJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Service
@Profile("usejwt")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var user = userRepository.findByEmail(username).get();
            return jwtTokenProvider.createToken(user);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
            //throw new MyJwtException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String logout(HttpServletRequest req) {
        try {
            return jwtTokenProvider.removeToken(jwtTokenProvider.resolveToken(req));
        } catch (Exception e) {
            throw new MyJwtException("Invalid token", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signup(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new MyJwtException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userRepository.save(user);
        return jwtTokenProvider.createToken(user);
    }

    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }

    private User search(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new MyJwtException("The user doesn't exist", HttpStatus.NOT_FOUND));
    }

    public User whoami(HttpServletRequest req) {
        return search(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String email) {
        return jwtTokenProvider.createToken(userRepository.findByEmail(email).get());
    }


    public User update(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id){

         return userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Id " + id + "not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Email " + email + "not found"));

    }


}

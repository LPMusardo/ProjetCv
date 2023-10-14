package com.example.projetcv.service;

import com.example.projetcv.dao.PersonRepository;
import com.example.projetcv.model.Person;
import com.example.projetcv.security.JwtHelper;
import com.example.projetcv.security.MyJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Profile("usejwt")
public class UserService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var user = personRepository.findByEmail(username).get();
            return jwtTokenProvider.createToken(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public String signup(Person user) {
        if (personRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new MyJwtException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        personRepository.save(user);
        return jwtTokenProvider.createToken(user);
    }

    public void delete(String email) {
        personRepository.deleteByEmail(email);
    }

    public Person search(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new MyJwtException("The user doesn't exist", HttpStatus.NOT_FOUND));
    }

    public Person whoami(HttpServletRequest req) {
        return search(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String email) {
        return jwtTokenProvider.createToken(personRepository.findByEmail(email).get());
    }

}

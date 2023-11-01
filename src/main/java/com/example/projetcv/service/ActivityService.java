package com.example.projetcv.service;

import com.example.projetcv.dao.ActivityRepository;
import com.example.projetcv.dao.CVRepository;
import com.example.projetcv.dao.UserRepository;
import com.example.projetcv.dto.UserSignupDto;
import com.example.projetcv.dto.UserUpdateDto;
import com.example.projetcv.exception.MyJwtException;
import com.example.projetcv.exception.NotFoundException;
import com.example.projetcv.model.User;
import com.example.projetcv.security.JwtHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ActivityService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private ActivityRepository activityRepository;

    private ModelMapper modelMapper;

    //-----------------------------------------------------------------------------------


    @PostConstruct
    private void init() {
        modelMapper = new ModelMapper();
    }











}

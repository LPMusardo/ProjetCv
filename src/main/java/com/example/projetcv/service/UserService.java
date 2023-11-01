package com.example.projetcv.service;

import com.example.projetcv.dao.UserRepository;
import com.example.projetcv.dto.*;
import com.example.projetcv.exception.MyJwtException;
import com.example.projetcv.exception.NotFoundException;
import com.example.projetcv.model.User;
import com.example.projetcv.security.JwtHelper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    private ModelMapper modelMapper;

    //-----------------------------------------------------------------------------------


    @PostConstruct
    private void init(){
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        //
        var typemape = modelMapper.createTypeMap(UserSignupDto.class, User.class);
        typemape.addMapping(UserSignupDto::getPassword, User::setPasswordHash);
        //

    }


    //-----------------------------------------------------------------------------------


    public String login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User with mail'" + loginDto.getEmail() + "' not found"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), loginDto.getPassword()));
        return jwtHelper.createToken(user);
    }


    public String logout(HttpServletRequest req) {
        return jwtHelper.removeTokenFromWhiteList(jwtHelper.resolveToken(req));
    }


    public User signup(UserSignupDto userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new MyJwtException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        User user = modelMapper.map(userDTO, User.class);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }


    public User deleteById(String id) {
        User userToDelete = userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new NotFoundException("The user of id"+ id +"doesn't exist", HttpStatus.NOT_FOUND));
        userRepository.deleteById(Long.parseLong(id));
        return userToDelete;
    }


    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("The user of id"+ id +"doesn't exist", HttpStatus.NOT_FOUND));
    }


    public User whoami(UserDetails userDetails) {
        return getUserById(Long.parseLong(userDetails.getUsername()));
    }


    public String refresh(HttpServletRequest req) {
        String initialToken = jwtHelper.resolveToken(req);
        String userId = jwtHelper.getUserId(initialToken);
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(()-> new MyJwtException("User from token not found in database", HttpStatus.UNAUTHORIZED));
        String newToken = jwtHelper.createToken(user);
        jwtHelper.removeTokenFromWhiteList(initialToken);
        return newToken;
    }


    public User update(UserUpdateDto userUpdateDto, UserDetails userDetails) {
        User user = userRepository.findById(Long.parseLong(userDetails.getUsername())).orElseThrow(() -> new UsernameNotFoundException("User with id'" + userDetails.getUsername() + "' not found"));
        logger.info("user to update :\n" + user);
        logger.info("user proposition :\n" + userUpdateDto);
        modelMapper.map(userUpdateDto, user);
        logger.info("user updated :\n" + user);
        return userRepository.save(user);
    }


    public SafeUserDto[] getAllUsers() {
        return new ModelMapper().map(userRepository.findAll(), SafeUserDto[].class);
    }

    public PagedModel<SafeUserDto> getAllUsersWithFilter(String name, String firstName, String activityTitle, Pageable pageable) {
        Page<User> userPage = userRepository.findAllUsersWithFilters(name, firstName, activityTitle, pageable);
        PagedModel<SafeUserDto> userPageHateoas = pagedResourcesAssembler.toModel(userPage, safeUserModelAssembler);
        return userPageHateoas;
    }


    @Autowired
    SafeUserDto.SafeUserDtoAssembler safeUserModelAssembler;

    @Autowired
    PagedResourcesAssembler<User> pagedResourcesAssembler;

    public PagedModel<SafeUserDto> getPageUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        PagedModel<SafeUserDto> result = pagedResourcesAssembler.toModel(userPage, safeUserModelAssembler);
        return result;

    }



}

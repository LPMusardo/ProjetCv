package com.example.projetcv.web;


import com.example.projetcv.dto.SafeUserDto;
import com.example.projetcv.dto.UserSignupDto;
import com.example.projetcv.dto.UserUpdateDto;
import com.example.projetcv.model.User;
import com.example.projetcv.security.JwtHelper;
import com.example.projetcv.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ModelMapper modelMapper = new ModelMapper();


    //------------------------------------------------------------------------------

    @GetMapping
    public SafeUserDto[] getAllUsers(HttpServletRequest req) {
        List<User> users = userService.getAllUsers();
        return modelMapper.map(users, SafeUserDto[].class);
    }


    @GetMapping("/{id}")
    public SafeUserDto getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return modelMapper.map(user, SafeUserDto.class);
    }

    @DeleteMapping
    public ResponseEntity<SafeUserDto> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        User userDeleted = userService.deleteById(userDetails.getUsername());
        return new ResponseEntity<>(modelMapper.map(userDeleted, SafeUserDto.class), HttpStatus.NO_CONTENT);
    }

    @PostMapping()
    public ResponseEntity<SafeUserDto> signup(@Valid @RequestBody UserSignupDto userDTO) {
        User newUser = userService.signup(userDTO);
        return new ResponseEntity<>(modelMapper.map(newUser, SafeUserDto.class), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<SafeUserDto> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, @AuthenticationPrincipal UserDetails userDetails){
        User userUpated = userService.update(userUpdateDto, userDetails);
        return new ResponseEntity<>(modelMapper.map(userUpated, SafeUserDto.class), HttpStatus.ACCEPTED);
    }

}

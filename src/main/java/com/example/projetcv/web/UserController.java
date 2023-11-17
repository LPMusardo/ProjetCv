package com.example.projetcv.web;


import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.dto.UserSignupDto;
import com.example.projetcv.dto.UserUpdateDto;
import com.example.projetcv.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(this.getClass().getName());



    //------------------------------------------------------------------------------


    @GetMapping
    public PagedModel<UserSafeDto> getUsers(@RequestParam(defaultValue = "%") String name,
                                            @RequestParam(defaultValue = "%") String firstName,
                                            @RequestParam(defaultValue = "%") String activityTitle,
                                            @PageableDefault(page = 0, size = 5, direction = Direction.ASC, sort = {"name", "firstName"} ) Pageable pageable) {
        return userService.getAllUsersWithFilter(name, firstName, activityTitle, pageable);
    }


    @GetMapping("/{id}")
    public UserSafeDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @DeleteMapping
    public ResponseEntity<UserSafeDto> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserSafeDto userDeleted = userService.deleteById(userDetails.getUsername());
        return new ResponseEntity<>(userDeleted, HttpStatus.NO_CONTENT);
    }


    @PostMapping()
    public ResponseEntity<UserSafeDto> signup(@Valid @RequestBody UserSignupDto userDTO) {
        UserSafeDto newUser = userService.signup(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    @PatchMapping
    public ResponseEntity<UserSafeDto> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, @AuthenticationPrincipal UserDetails userDetails){
        UserSafeDto userUpdated = userService.update(userUpdateDto, userDetails);
        return new ResponseEntity<>(userUpdated, HttpStatus.ACCEPTED);
    }


}

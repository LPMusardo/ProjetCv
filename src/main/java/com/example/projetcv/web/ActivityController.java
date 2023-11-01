package com.example.projetcv.web;

import com.example.projetcv.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ModelMapper modelMapper = new ModelMapper();


    //------------------------------------------------------------------------------


/*
    @PostMapping
    private void addActivity(){

    }


    @DeleteMapping
    private void deleteActivity(){

    }


    @PatchMapping
    private void updateActivity(){

    }
*/






}

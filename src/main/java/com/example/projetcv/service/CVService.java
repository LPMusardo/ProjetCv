package com.example.projetcv.service;

import com.example.projetcv.dao.ActivityRepository;
import com.example.projetcv.dao.CVRepository;
import com.example.projetcv.dao.UserRepository;
import com.example.projetcv.dto.CvDto;
import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.exception.NotFoundException;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.User;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CVService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    CVRepository cvRepository;


    @Autowired
    UserService userService;

    ModelMapper modelMapper;


    //------------------------------------------------------------------------------

    @PostConstruct
    private void init(){
        this.modelMapper = new ModelMapper();
    }


    //------------------------------------------------------------------------------


    public CV getCvById(Long id) {
        return cvRepository.findById(id).orElseThrow(() -> new NotFoundException("The CV of id "+ id +" doesn't exist", HttpStatus.NOT_FOUND));
    }


/*
    public void deleteCvByUserId(String id) {
        cvRepository.deleteById(Long.parseLong(id));
    }
*/


    public UserSafeDto updateCV(CvDto cvDto, UserDetails userDetails){
        logger.info("Beginning update...");
        User user = userRepository.findById(Long.parseLong(userDetails.getUsername())).orElseThrow(() -> new UsernameNotFoundException("User with id'" + userDetails.getUsername() + "' not found"));

        if(user.getCv() == null){
            CV newCV = new CV();
            user.setCv(newCV);
            newCV.setUser(user);
        }
        logger.info("Cv initial : " + user.getCv());

        logger.info("Cv proposition : " + cvDto);

        List<Activity> newActivities = Arrays.asList(modelMapper.map(cvDto.getActivities(), Activity[].class));
        newActivities.forEach(activity -> activity.setCv(user.getCv()));
        user.getCv().getActivities().clear();
        user.getCv().getActivities().addAll(newActivities);

        User userUpdated = userRepository.save(user);
        logger.info("Cv updated : " + userUpdated.getCv());

        return modelMapper.map(userUpdated, UserSafeDto.class);
    }



}

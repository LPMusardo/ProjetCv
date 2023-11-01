package com.example.projetcv.service;

import com.example.projetcv.dao.ActivityRepository;
import com.example.projetcv.dao.CVRepository;
import com.example.projetcv.dao.UserRepository;
import com.example.projetcv.dto.CvDto;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.User;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    ActivityRepository activityRepository;

    @Autowired
    UserService userService;

    ModelMapper modelMapper;

    //------------------------------------------------------------------------------

    @PostConstruct
    private void init(){
        this.modelMapper = new ModelMapper();
    }



    //------------------------------------------------------------------------------

    public List<CV> getAllCv() {
        return cvRepository.findAll();
    }

    public CV getCvById(Long id) {
        return cvRepository.findById(id).get();
    }

    public void deleteCvByUserId(String id) {
        cvRepository.deleteById(Long.parseLong(id));
    }




    public User updateCV(CvDto cvDto, UserDetails userDetails){
        logger.info("Beginning update...");
        User user = userRepository.findById(Long.parseLong(userDetails.getUsername())).orElseThrow(() -> new UsernameNotFoundException("User with id'" + userDetails.getUsername() + "' not found"));
        logger.info("Cv initial : " + user.getCv());
        logger.info("Cv proposition : " + cvDto);
        List<Activity> newActivities = Arrays.asList(modelMapper.map(cvDto.getActivities(), Activity[].class));
        newActivities.forEach(activity -> activity.setCv(user.getCv()));
        user.getCv().getActivities().clear();
        user.getCv().getActivities().addAll(newActivities);
        User userUpdated = userRepository.save(user);
        logger.info("Cv updated : " + userUpdated.getCv());

        return userUpdated;
    }








/*    public void addActivityToCv(String idUser, Activity activity) {
        long idUserLong = Long.parseLong(idUser);
        CV cv = cvRepository.findByUserId(idUserLong);

        if (cv == null) cv = new CV();

        User user = userService.getUserById(idUserLong);
        cv.setUser(user);
        if (cv.getActivities() == null) {
            cv.setActivities(List.of(activity));
        } else {
            cv.getActivities().add(activity);
        }

        cvRepository.save(cv);
    }*/

/*
    public void removeActivityToCv(String idUser, Long activityId) {
        long idUserLong = Long.parseLong(idUser);
        CV cv = cvRepository.findByUserId(idUserLong);
        if (cv == null) throw new NotFoundException("CV not found", HttpStatus.NOT_FOUND);
        User user = userService.getUserById(idUserLong);
        Activity activity = activityRepository.findById(activityId).orElseThrow(()-> new NotFoundException("Activity not found", HttpStatus.NOT_FOUND));
        cv.getActivities().remove(activity);
        cvRepository.save(cv);
    }
*/

/*    public Activity updateActivity(Long id, Activity updatedActivity) {
        return activityRepository.findById(id)
                .map(activity -> {
                    activity.setDescription(updatedActivity.getDescription());
                    activity.setWebAddress(updatedActivity.getWebAddress());
                    activity.setTitle(updatedActivity.getTitle());
                    activity.setNature(updatedActivity.getNature());
                    activity.setYear(updatedActivity.getYear());
                    return activityRepository.save(activity);
                })
                .orElseThrow(() -> new NotFoundException("Activity not found with id " + id, HttpStatus.NOT_FOUND));
    }*/


}

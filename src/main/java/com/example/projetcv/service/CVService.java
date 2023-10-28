package com.example.projetcv.service;

import com.example.projetcv.dao.ActivityRepository;
import com.example.projetcv.dao.CVRepository;
import com.example.projetcv.exception.NotFoundException;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("usejwt")
public class CVService {

    @Autowired
    CVRepository cvRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    UserService userService;


    public List<CV> getAllCv() {
        return cvRepository.findAll();
    }

    public CV getCvById(Long id) {
        return cvRepository.findById(id).get();
    }

    public void deleteCvByUserId(Long id) {
        cvRepository.deleteByUserId(id);
    }

    public void addActivityToCv(Long idUser, Activity activity) {
        CV cv = cvRepository.findByUserId(idUser);

        if (cv == null) cv = new CV();

        User user = userService.getUserById(idUser);
        cv.setUser(user);
        if (cv.getActivities() == null) {
            cv.setActivities(List.of(activity));
        } else {
            cv.getActivities().add(activity);
        }

        cvRepository.save(cv);
    }

    public void removeActivityToCv(Long idUser, Long activityId) {
        CV cv = cvRepository.findByUserId(idUser);
        if (cv == null) throw new NotFoundException("CV not found");
        User user = userService.getUserById(idUser);
        Activity activity = activityRepository.findById(activityId).orElseThrow(()-> new NotFoundException("Activity not found"));
        cv.getActivities().remove(activity);
        cvRepository.save(cv);
    }

    public Activity updateActivity(Long id, Activity updatedActivity) {
        return activityRepository.findById(id)
                .map(activity -> {
                    activity.setDescription(updatedActivity.getDescription());
                    activity.setWebAddress(updatedActivity.getWebAddress());
                    activity.setTitle(updatedActivity.getTitle());
                    activity.setNature(updatedActivity.getNature());
                    activity.setYear(updatedActivity.getYear());
                    return activityRepository.save(activity);
                })
                .orElseThrow(() -> new NotFoundException("Activity not found with id " + id));
    }


}

package com.example.projetcv.dao;

import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private UserRepository userRepository;

    Logger logger = Logger.getLogger(this.getClass().getName());

    @BeforeEach
    public void cleanDatabase() {
        userRepository.deleteAll();
        cvRepository.deleteAll();
        activityRepository.deleteAll();
    }

    //---------------------------------CRUD TESTS---------------------------------

    @Test
    public void createActivityTest() {
        User user = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();
        userRepository.save(user);
        CV cv = CV.builder()
                .user(user)
                .build();
        cvRepository.save(cv);
        Activity activity = Activity.builder()
                .cv(cv)
                .year(2022)
                .nature(Nature.PROJECT)
                .title("Project Title")
                .build();
        Activity savedActivity = activityRepository.save(activity);
        logger.info("savedActivity: " + savedActivity);

        assertThat(savedActivity).isNotNull();
        assertThat(savedActivity.getId()).isNotNull();
    }

    @Test
    public void readActivityTest() {
        User user = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();
        userRepository.save(user);
        CV cv = CV.builder()
                .user(user)
                .build();
        cvRepository.save(cv); //save
        Activity activity = Activity.builder()
                .cv(cv)
                .year(2022)
                .nature(Nature.PROJECT)
                .title("Project Title")
                .build();
        Activity savedActivity = activityRepository.save(activity);
        Optional<Activity> retrievedActivity = activityRepository.findById(savedActivity.getId());

        assertThat(retrievedActivity).isPresent();
        assertThat(retrievedActivity.get().getTitle()).isEqualTo(activity.getTitle());
    }

    @Test
    public void updateActivityTest() {
        User user = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();
        userRepository.save(user);
        CV cv = CV.builder()
                .user(user)
                .build();
        cvRepository.save(cv); //save
        Activity activity = Activity.builder()
                .cv(cv)
                .year(2022)
                .nature(Nature.PROJECT)
                .title("Project Title")
                .build();
        Activity savedActivity = activityRepository.save(activity);
        savedActivity.setTitle("Updated Project Title");
        activityRepository.save(savedActivity);
        Optional<Activity> updatedActivity = activityRepository.findById(savedActivity.getId());

        assertThat(updatedActivity).isPresent();
        assertThat(updatedActivity.get().getTitle()).isEqualTo("Updated Project Title");
    }

    @Test
    public void deleteActivityTest() {
        User user = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();
        userRepository.save(user);
        CV cv = CV.builder()
                .user(user)
                .build();
        CV savedCV = cvRepository.save(cv); //save
        Activity activity = Activity.builder()
                .cv(cv)
                .year(2022)
                .nature(Nature.PROJECT)
                .title("Project Title")
                .build();
        Activity savedActivity = activityRepository.save(activity);


        Optional<CV> retrievedCV = cvRepository.findById(savedCV.getId());
        assertThat(retrievedCV).isPresent();
        assertEquals(1, retrievedCV.get().getActivities().size() );

        retrievedCV.get().getActivities().removeIf(a -> a.getId().equals(savedActivity.getId()));  // Delete
        cvRepository.save(retrievedCV.get());                                                      //
        Optional<Activity> retrievedActivity = activityRepository.findById(savedActivity.getId());
        Optional<CV> emptiedCV = cvRepository.findById(cv.getId());
        assertThat(retrievedActivity).isNotPresent();
        assertThat(emptiedCV).isPresent();
        assertEquals(retrievedCV.get().getActivities().size(), 0);
    }



}

package com.example.projetcv.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@SpringBootTest
public class CVRepositoryTest {

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private UserRepository userRepository;

    Logger logger = Logger.getLogger(this.getClass().getName());

    @BeforeEach
    public void cleanDatabase() {
        cvRepository.deleteAll();
        userRepository.deleteAll();
    }

    //---------------------------------CRUD TESTS---------------------------------

    @Test
    public void createCVTest() {
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
        logger.info("savedCV: " + savedCV);

        assertThat(savedCV).isNotNull();
        assertThat(savedCV.getId()).isNotNull();
    }

    @Test
    public void readCVTest() {
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
        CV savedCV = cvRepository.save(cv);
        Optional<CV> retrievedCV = cvRepository.findById(savedCV.getId()); //Read


        assertThat(retrievedCV).isPresent();
        assertThat(retrievedCV.get().getUser().getName()).isEqualTo(user.getName());
    }

    @Test
    public void updateCVTest() {
        User userA = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();
        userRepository.save(userA);
        CV cv = CV.builder()
                .user(userA)
                .build();
        CV savedCV = cvRepository.save(cv);

        savedCV.setActivities(List.of(Activity.builder().cv(cv).year(2022).nature(Nature.PROJECT).title("Project Title").build()));
        cvRepository.save(savedCV); // Update

        Optional<CV> updatedCV = cvRepository.findById(savedCV.getId());
        logger.info("updatedCV: " + updatedCV);

        assertThat(updatedCV).isPresent();
        assertThat(updatedCV.get().getActivities().get(0).getTitle()).isEqualTo("Project Title");
    }

        @Test
        public void deleteCVTest() {
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
            CV savedCV = cvRepository.save(cv);

            user.setCv(null);             // Delete
            userRepository.save(user);  //

            Optional<CV> retrievedCV = cvRepository.findById(savedCV.getId());
            Optional<User> retrievedUser = userRepository.findById(user.getId());

            assertThat(retrievedCV).isNotPresent();
            assertThat(retrievedUser).isPresent();
    }



}

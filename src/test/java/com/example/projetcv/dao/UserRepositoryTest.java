package com.example.projetcv.dao;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.projetcv.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;


@SpringBootTest
public class UserRepositoryTest {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void cleanuserTable(){
        userRepository.deleteAll();
    }

    //---------------------------------CRUD TESTS---------------------------------

    @Test
    public void createusertest() {
        User user = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("anemail@email.com")
                .passwordHash("lehash")
                .build();
        User savedUser = userRepository.save(user);
        logger.info("savedUser: " + savedUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void readuserTest() {
        User user = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("anemail@email.com")
                .passwordHash("lehash")
                .build();
        User savedUser = userRepository.save(user);
        Optional<User> founduser = userRepository.findById(savedUser.getId());

        assertThat(founduser).isPresent();
        assertThat(founduser.get().getName()).isEqualTo(savedUser.getName());
    }


    //@Autowired
    //EntityManager entityManager;

    @Test
    @Transactional
    public void updateuserTest() {
        User user = User.builder()
                .name("Johnnnnnn")
                .firstName("Doeeeeee")
                .birthday(LocalDate.now())
                .email("johnnnnnnn@email.com")
                .passwordHash("hasssssssss")
                .build();
        userRepository.saveAndFlush(user);

        Optional<User> retrieveduser = userRepository.findAll().stream().findFirst();
        assertThat(retrieveduser).isPresent();
        retrieveduser.get().setFirstName("Jane");


        userRepository.saveAndFlush(retrieveduser.get());
        Optional<User> updateduser = userRepository.findById(user.getId());

        assertThat(updateduser).isPresent();
        assertThat(updateduser.get().getFirstName()).isEqualTo("Jane");
    }

    @Test
    public void deleteuserTest() {
        User user = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("anemail@email.com")
                .passwordHash("lehash")
                .build();
        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getId());
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isNotPresent();
    }




}

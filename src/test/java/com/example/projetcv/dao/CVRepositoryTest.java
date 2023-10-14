package com.example.projetcv.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@SpringBootTest
public class CVRepositoryTest {

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private PersonRepository personRepository;

    Logger logger = Logger.getLogger(this.getClass().getName());

    @BeforeEach
    public void cleanDatabase() {
        cvRepository.deleteAll();
        personRepository.deleteAll();
    }

    //---------------------------------CRUD TESTS---------------------------------

    @Test
    public void createCVTest() {
        Person person = Person.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();
        personRepository.save(person);
        CV cv = CV.builder()
                .person(person)
                .build();
        CV savedCV = cvRepository.save(cv); //save
        logger.info("savedCV: " + savedCV);

        assertThat(savedCV).isNotNull();
        assertThat(savedCV.getId()).isNotNull();
    }

    @Test
    public void readCVTest() {
        Person person = Person.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();
        personRepository.save(person);
        CV cv = CV.builder()
                .person(person)
                .build();
        CV savedCV = cvRepository.save(cv);
        Optional<CV> retrievedCV = cvRepository.findById(savedCV.getId()); //Read


        assertThat(retrievedCV).isPresent();
        assertThat(retrievedCV.get().getPerson().getName()).isEqualTo(person.getName());
    }

    @Test
    public void updateCVTest() {
        Person personA = Person.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();
        personRepository.save(personA);
        CV cv = CV.builder()
                .person(personA)
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
            Person person = Person.builder()
                    .name("John")
                    .firstName("Doe")
                    .birthday(LocalDate.now())
                    .email("john.doe@example.com")
                    .passwordHash("lehash")
                    .build();
            personRepository.save(person);

            CV cv = CV.builder()
                    .person(person)
                    .build();
            CV savedCV = cvRepository.save(cv);

            person.setCv(null);             // Delete
            personRepository.save(person);  //

            Optional<CV> retrievedCV = cvRepository.findById(savedCV.getId());
            Optional<Person> retrievedPerson = personRepository.findById(person.getId());

            assertThat(retrievedCV).isNotPresent();
            assertThat(retrievedPerson).isPresent();
    }



}

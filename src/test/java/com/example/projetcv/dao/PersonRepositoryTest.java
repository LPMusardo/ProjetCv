package com.example.projetcv.dao;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.projetcv.model.Person;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;


@SpringBootTest
public class PersonRepositoryTest {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void cleanPersonTable(){
        personRepository.deleteAll();
    }

    //---------------------------------CRUD TESTS---------------------------------

    @Test
    public void createPersontest() {
        Person person = Person.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("anemail@email.com")
                .passwordHash("lehash")
                .build();
        Person savedPerson = personRepository.save(person);
        logger.info("savedPerson: " + savedPerson);

        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isNotNull();
    }

    @Test
    public void readPersonTest() {
        Person person = Person.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("anemail@email.com")
                .passwordHash("lehash")
                .build();
        Person savedPerson = personRepository.save(person);
        Optional<Person> foundPerson = personRepository.findById(savedPerson.getId());

        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getName()).isEqualTo(savedPerson.getName());
    }


    //@Autowired
    //EntityManager entityManager;

    @Test
    @Transactional
    public void updatePersonTest() {
        Person person = Person.builder()
                .name("Johnnnnnn")
                .firstName("Doeeeeee")
                .birthday(LocalDate.now())
                .email("johnnnnnnn@email.com")
                .passwordHash("hasssssssss")
                .build();
        personRepository.saveAndFlush(person);

        Optional<Person> retrievedPerson = personRepository.findAll().stream().findFirst();
        assertThat(retrievedPerson).isPresent();
        retrievedPerson.get().setFirstName("Jane");


        personRepository.saveAndFlush(retrievedPerson.get());
        Optional<Person> updatedPerson = personRepository.findById(person.getId());

        assertThat(updatedPerson).isPresent();
        assertThat(updatedPerson.get().getFirstName()).isEqualTo("Jane");
    }

    @Test
    public void deletePersonTest() {
        Person person = Person.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("anemail@email.com")
                .passwordHash("lehash")
                .build();
        Person savedPerson = personRepository.save(person);
        personRepository.deleteById(savedPerson.getId());
        Optional<Person> foundPerson = personRepository.findById(savedPerson.getId());

        assertThat(foundPerson).isNotPresent();
    }




}

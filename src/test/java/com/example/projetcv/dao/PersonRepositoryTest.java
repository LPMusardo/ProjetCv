package com.example.projetcv.dao;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.projetcv.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;


@SpringBootTest
public class PersonRepositoryTest {

    @BeforeEach
    public void cleanPersonTable(){
        personRepository.deleteAll();
    }

    @Autowired
    private PersonRepository personRepository;

    Logger logger = Logger.getLogger(this.getClass().getName());

    //------------------------------------------------------------------------------

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

    @Test
    public void updatePersonTest() {
        Person person = Person.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("anemail@email.com")
                .passwordHash("lehash")
                .build();
        Person savedPerson = personRepository.save(person);
        savedPerson.setFirstName("Jane");
        personRepository.save(savedPerson);
        Optional<Person> updatedPerson = personRepository.findById(savedPerson.getId());

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

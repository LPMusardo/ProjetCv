package com.example.projetcv.service;

import com.example.projetcv.dao.PersonRepository;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.model.Person;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.stripAccents;

@Component
public class PopulateDataBase implements CommandLineRunner {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Random random = new Random();
    Faker faker = new Faker(new Locale("fr-FR"), new Random(42));


    @Override
    public void run(String... args) throws Exception {
        clearDb();
        generateCustomPersons();
        generateRandomPersons(10);
    }

    private void clearDb() {
        personRepository.deleteAll();
    }

    private void generateRandomPersons(int numberOfPerson) {
        String hash = passwordEncoder.encode("password");

        //List<Person> persons = new ArrayList<>();
        for (int i = 0; i < numberOfPerson; i++) {
            String pFirstName = stripAccents(faker.name().firstName());
            String pLastName = stripAccents(faker.name().lastName());

            // required
            var person = Person.builder()
                    .name(stripAccents(faker.name().firstName()))
                    .firstName(stripAccents(faker.name().lastName()))
                    .birthday(LocalDate.ofEpochDay(randomInt(0, 1003502741))) //2001
                    .email(faker.internet().emailAddress(pFirstName.toLowerCase()+i))
                    .passwordHash(hash)
                    .website(pFirstName+"."+pLastName+".com")
                    .build();
            // optional
            if(i%2==0) {
                person.setRoles(Set.of("USER"));
            }
            if(i%10!=0) {
                CV cv = generateCv(person);
                person.setCv(cv);
            }

            personRepository.save(person);
        }

    }

    private CV generateCv(Person person) {
        CV cv = CV.builder()
                .person(person)
                .build();

        //TODO random number of activities
        Activity activity = Activity.builder()
                .cv(cv)
                .year(randomInt(2000, 2023))
                .nature(Nature.randomNature())
                .title("Project Title")
                .build();
        cv.setActivities(List.of(activity));

        return cv;
    }


    private void generateCustomPersons(){
        Person amdinLP = Person.builder()
                .firstName("lp")
                .name("mu")
                .birthday(LocalDate.now())
                .email("lpmusardo@gmail.com")
                .roles(Set.of("ADMIN", "USER"))
                .passwordHash(passwordEncoder.encode("password"))
                .website("https://www.linkedin.com/in/lpmusardo/")
                .build();
        Person userMaxime = Person.builder()
                .firstName("maxime")
                .name("gu")
                .birthday(LocalDate.now())
                .email("maxime@gmail.com")
                .roles(Set.of("USER"))
                .passwordHash(passwordEncoder.encode("password"))
                .website("https://www.linkedin.com/in/maxime/")
                .build();
        personRepository.save(amdinLP);
        personRepository.save(userMaxime);
    }



    public int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }


}

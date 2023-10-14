package com.example.projetcv.service;

import com.example.projetcv.dao.PersonRepository;
import com.example.projetcv.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class PopulateDataBase implements CommandLineRunner {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
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










}

package com.example.projetcv.web;


import com.example.projetcv.dao.PersonRepository;
import com.example.projetcv.model.Person;
import com.example.projetcv.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/public")
public class PublicController {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    PersonRepository personRepository;




    @GetMapping("/test")
    public String refresh(HttpServletRequest req) {
        return "Vous etes public";
    }

    @GetMapping("/users")
    public List<Person> users(HttpServletRequest req) {
        return personRepository.findAll();
    }




}

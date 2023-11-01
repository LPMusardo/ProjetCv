package com.example.projetcv.dto;

import com.example.projetcv.model.CV;
import com.example.projetcv.model.User;
import com.example.projetcv.web.UserController;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
public class SafeUserDto  extends  RepresentationModel<SafeUserDto>{

    private String name;

    private String firstName;

    private String email;

    private String website;

    private LocalDate birthday;

    private CV cv;


    @Component
    public static class SafeUserDtoAssembler extends RepresentationModelAssemblerSupport<User, SafeUserDto> {
        public SafeUserDtoAssembler() {
            super(UserController.class, SafeUserDto.class);
        }

        @Override
        public SafeUserDto toModel(User entity) {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(entity, SafeUserDto.class);
        }
    }





}

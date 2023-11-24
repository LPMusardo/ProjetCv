package com.example.projetcv.dto;

import com.example.projetcv.model.CV;
import com.example.projetcv.model.User;
import com.example.projetcv.web.UserController;
import lombok.Data;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
public class UserSafeDto extends RepresentationModel<UserSafeDto> {

    private Long id;

    private String name;

    private String firstName;

    private String email;

    private String website;

    private LocalDate birthday;

    private CV cv;


    @Component
    public static class SafeUserDtoAssembler extends RepresentationModelAssemblerSupport<User, UserSafeDto> {
        public SafeUserDtoAssembler() {
            super(UserController.class, UserSafeDto.class);
        }

        @Override
        @NonNull
        public UserSafeDto toModel(@NonNull User entity) {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(entity, UserSafeDto.class);
        }
    }


}

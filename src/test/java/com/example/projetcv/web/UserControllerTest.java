package com.example.projetcv.web;

import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.dto.UserSignupDto;
import com.example.projetcv.dto.UserUpdateDto;
import com.example.projetcv.exception.NotFoundException;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.model.User;
import com.example.projetcv.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        // Initialisez votre UserSafeDto
        UserSafeDto user = new UserSafeDto();
        CV cv = new CV();
        List<Activity> activities = new ArrayList<Activity>();

        Activity activity = new Activity();
        activity.setYear(2020);
        activity.setTitle("Test");
        activity.setNature(Nature.PROJECT);
        activity.setWebAddress("www.test.com");
        activity.setDescription("Description of the test");
        activities.add(activity);
        cv.setActivities(activities);
        user.setCv(cv);
        user.setId(1L);
        user.setName("John Doe");
        given(this.userService.getUserById(1L)).willReturn(user);
        given(this.userService.getUserById(2L)).willThrow(new NotFoundException("The user of id 2 doesn't exist", HttpStatus.NOT_FOUND));

    }

    @Test
    @WithMockUser
    public void testGetUser() throws Exception {
        mvc.perform(get("/api/users/{id}", 1L)
                        .with(user("user").password("password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"name\": \"John Doe\"}"));
    }

    @Test
    @WithMockUser
    public void testGetUserNotFound() throws Exception {
        mvc.perform(get("/api/users/2")
                        .with(user("user").password("password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("The user of id 2 doesn't exist")));
    }


    @Test
    public void testDeleteUserNotConnected() throws Exception {
        mvc.perform(delete("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(status().reason(containsString("Access Denied")));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    public void testDeleteUserNotExist() throws Exception {
        given(this.userService.deleteById(anyString())).willThrow(new NotFoundException("The user of id 2 doesn't exist", HttpStatus.NOT_FOUND));
        mvc.perform(delete("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("The user of id 2 doesn't exist")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    public void testDeleteUserConnectedUser() throws Exception {
        mvc.perform(delete("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSignUpNotConnected() throws Exception {
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(status().reason(containsString("Access Denied")));
    }


    @Test
    @WithMockUser
    public void testSignUpConnectedNoBody() throws Exception {
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required request body is missing")));
    }


    @Test
    @WithMockUser
    public void testSignUpConnected() throws Exception {
        UserSignupDto userDTO = new UserSignupDto();
        userDTO.setName("ValidName");
        userDTO.setEmail("validemail@example.com");
        userDTO.setBirthday(LocalDate.of(2000, 9, 13));
        userDTO.setPassword("password");
        userDTO.setPasswordConfirm("password");
        userDTO.setFirstName("ValidFirstName");

        UserSafeDto safeUser = new UserSafeDto();
        safeUser.setName("ValidName");
        safeUser.setEmail("validemail@example.com");
        safeUser.setBirthday(LocalDate.of(2000, 9, 13));
        safeUser.setFirstName("ValidFirstName");
        safeUser.setId(1L);

        // set fields..
        given(userService.signup(any(UserSignupDto.class))).willReturn(safeUser);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"ValidName\",\"firstName\":\"ValidFirstName\",\"email\":\"validemail@example.com\",\"website\":null,\"birthday\":\"2000-09-13\",\"cv\":null}"));
    }

    @Test
    public void testUpdateUserNotConnected() throws Exception {
        mvc.perform(patch("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(status().reason(containsString("Access Denied")));

    }

    @Test
    @WithMockUser
    public void testUpdateUserConnectedNoBody() throws Exception {
        mvc.perform(patch("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("body")));

    }


    @Test
    @WithMockUser
    public void testUpdateUserConnected() throws Exception {
        UserUpdateDto userDTO = new UserUpdateDto();
        userDTO.setName("ValidName");
        userDTO.setEmail("validemail@example.com");
        userDTO.setBirthday(LocalDate.of(2000, 9, 13));
        userDTO.setPassword("password");
        userDTO.setPasswordConfirm("password");
        userDTO.setFirstName("ValidFirstName");

        UserSafeDto safeUser = new UserSafeDto();
        safeUser.setName("ValidName");
        safeUser.setEmail("validemail@example.com");
        safeUser.setBirthday(LocalDate.of(2000, 9, 13));
        safeUser.setFirstName("ValidFirstName");
        safeUser.setId(1L);
        given(userService.update(any(UserUpdateDto.class), any(UserDetails.class))).willReturn(safeUser);

        mvc.perform(patch("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().json("{\"id\":1,\"name\":\"ValidName\",\"firstName\":\"ValidFirstName\",\"email\":\"validemail@example.com\",\"website\":null,\"birthday\":\"2000-09-13\",\"cv\":null}"));

    }

    // TODO
    @Test
    public void testSearchWithArgs() throws Exception {
        UserSafeDto userSafeDto = new UserSafeDto();
        // Perform and validate request
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk());
                //.andExpect(content().json("{\"content\":[" + objectMapper.writeValueAsString(userSafeDto) + "],\"page\":0,\"pageSize\":1,\"totalElements\":1}"));
    }
}



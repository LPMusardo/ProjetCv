package com.example.projetcv.api.autorisation;

import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.exception.NotFoundException;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.service.UserService;

import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;


import static org.mockito.ArgumentMatchers.anyLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

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

}

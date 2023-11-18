package com.example.projetcv.web;

import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.exception.NotFoundException;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.service.CVService;
import com.example.projetcv.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CvControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CVService cvService;

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
        given(this.cvService.getCvById(1L)).willReturn(cv);
        given(this.cvService.getCvById(2L)).willThrow(new NotFoundException("The user of id 2 doesn't exist", HttpStatus.NOT_FOUND));

    }

    @Test
    @WithMockUser
    public void testGetCvById() throws Exception {
        mvc.perform(get("/api/cvs/{id}", 1L)
                        .with(user("user").password("password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":null,\"activities\":[{\"id\":null,\"year\":2020,\"nature\":\"PROJECT\",\"title\":\"Test\",\"description\":\"Description of the test\",\"webAddress\":\"www.test.com\"}]}"));

    }



}

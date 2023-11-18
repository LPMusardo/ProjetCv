package com.example.projetcv.web;

import com.example.projetcv.dto.ActivityDto;
import com.example.projetcv.dto.CvDto;
import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.exception.NotFoundException;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.service.CVService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
        given(this.cvService.getCvById(2L)).willThrow(new NotFoundException("The CV of id 2 doesn't exist", HttpStatus.NOT_FOUND));

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

    @Test
    public void testGetCvByIdNotConnected() throws Exception {
        mvc.perform(get("/api/cvs/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":null,\"activities\":[{\"id\":null,\"year\":2020,\"nature\":\"PROJECT\",\"title\":\"Test\",\"description\":\"Description of the test\",\"webAddress\":\"www.test.com\"}]}"));

    }

    @Test
    @WithMockUser
    public void testGetCvByIdNotExisting() throws Exception {
        mvc.perform(get("/api/cvs/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("The CV of id 2 doesn't exist"));
    }

    @Test
    public void testUpdateCvNotConnected() throws Exception {
        mvc.perform(patch("/api/cvs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("Access Denied"));

    }

    @Test
    @WithMockUser
    public void testUpdateCvNoBody() throws Exception {
        mvc.perform(patch("/api/cvs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required request body is missing")));

    }

    @Test
    @WithMockUser
    public void testUpdateCv() throws Exception {
        CvDto cvDto = new CvDto();
        List<ActivityDto> activities = new ArrayList<ActivityDto>();

        ActivityDto activity = new ActivityDto();
        activity.setYear(2020);
        activity.setTitle("Test");
        activity.setNature(Nature.PROJECT);
        activity.setWebAddress("www.test.com");
        activity.setDescription("Description of the test");
        activities.add(activity);
        cvDto.setActivities(activities);


        UserSafeDto safeUser = new UserSafeDto();
        safeUser.setName("ValidName");
        safeUser.setEmail("validemail@example.com");
        safeUser.setBirthday(LocalDate.of(2000, 9, 13));
        safeUser.setFirstName("ValidFirstName");
        safeUser.setId(1L);

        given(cvService.updateCV(any(CvDto.class), any(UserDetails.class))).willReturn(safeUser);

        mvc.perform(patch("/api/cvs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cvDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"ValidName\",\"firstName\":\"ValidFirstName\",\"email\":\"validemail@example.com\",\"website\":null,\"birthday\":\"2000-09-13\",\"cv\":null}"));

    }


}

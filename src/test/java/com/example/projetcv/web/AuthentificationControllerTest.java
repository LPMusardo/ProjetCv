package com.example.projetcv.web;

import com.example.projetcv.dto.LoginDto;
import com.example.projetcv.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthentificationControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    public void testLoginNoBody() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required request body is missing")));

    }

    @Test
    public void testLoginNoBodyNotConnected() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required request body is missing")));
    }

    @Test
    public void testLogin() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("max@gmail.com");
        loginDto.setPassword("pswd");

        given(userService.login(any(LoginDto.class))).willReturn("JWT");
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required request body is missing")));
    }


}

package com.example.projetcv.web.security;

import com.example.projetcv.dto.LoginDto;
import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.exception.MyJwtException;
import com.example.projetcv.exception.NotFoundException;
import com.example.projetcv.security.JwtHelper;
import com.example.projetcv.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthentificationControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @MockBean
    private JwtHelper jwtHelper;

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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("JWT"));
    }

    @Test
    public void testLogoutNotConnected() throws Exception {
        mvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("Access Denied"));

    }

    @Test
    @WithMockUser
    public void testLogoutNoHeader() throws Exception {
        mvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Something went wrong"));

    }


    @Test
    @WithMockUser
    public void testLogoutConnected() throws Exception {
        String expectedResponse = "Logout successful";
        given(userService.logout(any(HttpServletRequest.class))).willReturn(expectedResponse);
        mvc.perform(get("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

    }


    @Test
    @WithMockUser
    public void testRefreshWithConnectedUser() throws Exception {
        String expectedResponse = "Token refreshed successfully";
        given(userService.refresh(any(HttpServletRequest.class))).willReturn(expectedResponse);

        mvc.perform(get("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testRefreshWithUnconnectedUser() throws Exception {
        String expectedResponse = "Access Denied";
        given(userService.refresh(any(HttpServletRequest.class))).willReturn(expectedResponse);

        mvc.perform(get("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(status().reason(expectedResponse));
    }

    @Test
    @WithMockUser
    public void testRefreshWithBadToken() throws Exception {
        given(userService.refresh(any(HttpServletRequest.class))).willThrow(new MyJwtException("User from token not found in database", HttpStatus.UNAUTHORIZED));

        mvc.perform(get("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("User from token not found in database"));
    }


    @Test
    @WithMockUser
    public void testWhoAmIWithConnectedUser() throws Exception {
        UserSafeDto user = new UserSafeDto();

        given(userService.whoami(any(UserDetails.class))).willReturn(user);

        mvc.perform(get("/api/auth/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":null,\"name\":null,\"firstName\":null,\"email\":null,\"website\":null,\"birthday\":null,\"cv\":null}"));
    }

    @Test
    public void testWhoAmIWithUnconnectedUser() throws Exception {
        mvc.perform(get("/api/auth/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("Access Denied"));
    }


}

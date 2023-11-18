package com.example.projetcv.web.validation;

import com.example.projetcv.security.UserDetailsServiceImpl;
import com.example.projetcv.service.UserService;
import com.example.projetcv.web.AuthentificationController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



/**
 **  Validation test of the DTOs with the "@Valid" of the controllers
 **/
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthentificationController.class)
@ComponentScan(basePackages = {"com.example.projetcv.security"})
class AuthenticationControllerTest {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserService userService;


    @BeforeEach
    void setUp() {
        when(userService.login(any())).thenReturn("the_token");
    }

    //-------------------------------------------------------------------

    @Test
    @SneakyThrows
    void goodLoginTest() {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\" :\"lpmusardo@gmail.com\",\"password\": \"password\"}"))
                        .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                        .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void NoPasswordLoginTest() {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\" :\"lpmusardo@gmail.com\"}"))
                        .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void NoEmailLoginTest() {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\": \"password\"}"))
                        .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void emptyBodyLoginTest() {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                        .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                        .andExpect(status().isBadRequest());
    }



}
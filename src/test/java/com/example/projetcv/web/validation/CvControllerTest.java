package com.example.projetcv.web.validation;

import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.security.UserDetailsServiceImpl;
import com.example.projetcv.service.CVService;
import com.example.projetcv.web.CvController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



/**
 **  Validation test of the DTOs with the "@Valid" of the controllers
 **/
@ExtendWith(SpringExtension.class)
@WebMvcTest(CvController.class)
@ComponentScan(basePackages = {"com.example.projetcv.security"})
class CvControllerTest {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private CVService cvService;


    @BeforeEach
    void setUp() {
        UserSafeDto userSafeDto = new UserSafeDto();
        when(cvService.updateCV(any(), any())).thenReturn(userSafeDto);
    }



    //------------------------------OK TESTS------------------------------

    @Test
    @SneakyThrows
    @WithMockUser
    void goodFullUpdateCvTest() {
        String goodJsonPayloadUpdateCv = """
                {
                    "activities": [
                        {
                            "year": 2009,
                            "nature": "EDUCATION",
                            "title": "OUIII TEST ",
                            "description": "oui oui",
                            "webAddress": "www.xn--tho-colin-c4a.com"
                        }
                    ]
                }""";
        mvc.perform(patch("/api/cvs")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(goodJsonPayloadUpdateCv))
                .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                .andExpect(status().isCreated());
    }


    @Test
    @SneakyThrows
    @WithMockUser
    void goodEmptyArrayUpdateCvTest() {
        String goodJsonPayloadUpdateCv = """
                {
                    "activities": []
                }""";
        mvc.perform(patch("/api/cvs")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodJsonPayloadUpdateCv))
                .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void goodMinimalInfoActivityUpdateCvTest() {
        String goodJsonPayloadUpdateCv = """
                {
                    "activities": [
                        {
                            "nature": "EDUCATION",
                            "title": "OUIII TEST "
                        }
                    ]
                }""";
        mvc.perform(patch("/api/cvs")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodJsonPayloadUpdateCv))
                .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                .andExpect(status().isCreated());
    }


    //------------------------------KO TESTS------------------------------


    private static Stream<Arguments> koUpdateCvTestInputs() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of("""
                {
                }"""),
            Arguments.of("""
                {
                    "activities": [
                        {
                            "title": "OUIII TEST "
                        }
                    ]
                }"""),
            Arguments.of("""
                {
                    "activities": [
                        {
                            "nature": "EDUCATION",
                        }
                    ]
                }"""),
            Arguments.of("")
        );
    }

    @SneakyThrows
    @WithMockUser
    @ParameterizedTest
    @MethodSource("koUpdateCvTestInputs")
    void koUpdateCvTest(String JsonPayloadUpdateCv) {
        mvc.perform(patch("/api/cvs")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonPayloadUpdateCv))
                .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }

    //------------------------------------------------------------

/*    @Test
    @SneakyThrows
    @WithMockUser
    void emptyUpdateCvTest() {
        String goodJsonPayloadUpdateCv = "";
        mvc.perform(patch("/api/cvs")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(goodJsonPayloadUpdateCv))
                .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }


    @Test
    @SneakyThrows
    @WithMockUser
    void emptyJsonObjectUpdateCvTest() {
        String goodJsonPayloadUpdateCv = """
                {
                }""";
        mvc.perform(patch("/api/cvs")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodJsonPayloadUpdateCv))
                .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }


    @Test
    @SneakyThrows
    @WithMockUser
    void missingNatureUpdateCvTest() {
        String goodJsonPayloadUpdateCv = """
                {
                    "activities": [
                        {
                            "title": "OUIII TEST "
                        }
                    ]
                }""";
        mvc.perform(patch("/api/cvs")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodJsonPayloadUpdateCv))
                .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }


    @Test
    @SneakyThrows
    @WithMockUser
    void missingTitleUpdateCvTest() {
        String goodJsonPayloadUpdateCv = """
                {
                    "activities": [
                        {
                            "nature": "EDUCATION",
                        }
                    ]
                }""";
        mvc.perform(patch("/api/cvs")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodJsonPayloadUpdateCv))
                .andDo(result -> logger.info("MESSAGE: "+result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());
    }*/




}

package com.example.projetcv.web.validation;

import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.security.UserDetailsServiceImpl;
import com.example.projetcv.service.UserService;
import com.example.projetcv.web.UserController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import({com.example.projetcv.security.JwtHelper.class, com.example.projetcv.security.SecurityConfiguration.class})
class UserControllerTest {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        UserSafeDto userSafeDto = new UserSafeDto();
        when(userService.createUser(any())).thenReturn(userSafeDto);
    }

    //------------------------------------------------------------------------------


    private static Stream<Arguments> signupTestInputs() {
        Stream<Arguments> goodInputs = Stream.of(
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "website" : "www.apple.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isCreated(), "good full input"), Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isCreated(), "good minimal input")
        );
        Stream<Arguments> badInputs = Stream.of(
                Arguments.of("", status().isBadRequest(), "empty"),
                Arguments.of("""
                        {
                        }""", status().isBadRequest(), "empty object"),
                Arguments.of("""
                        {
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "missing name"),
                Arguments.of("""
                        {
                            "name": "",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "blank name"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "email" : "atest@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "missing firstName"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "",
                            "email" : "atest@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "blank firstName"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "missing email"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "blank email"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "bad format email"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "missing birthday"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "birthday" : "2099-99",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "bad format birthday"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "missing password"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "",
                            "passwordConfirm" : "thepassword"
                        }""", status().isBadRequest(), "blank password"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "t",
                            "passwordConfirm" : "t"
                        }""", status().isBadRequest(), "too short password"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword"
                        }""", status().isBadRequest(), "missing passwordConfirm"),
                Arguments.of("""
                        {
                            "name": "Anametest",
                            "firstName": "AfirstNameTest",
                            "email" : "atest@gmail.com",
                            "website" : "www.apple.com",
                            "birthday" : "2023-10-29T11:22:05.953Z",
                            "password" : "thepassword",
                            "passwordConfirm" : "thepasswordthepassword"
                        }""", status().isBadRequest(), "password and passwordConfirm mismatch")
        );
        return Stream.concat(goodInputs, badInputs);
    }


    @SneakyThrows
    @WithMockUser
    @ParameterizedTest
    @MethodSource("signupTestInputs")
    void dtoValidationSignupTest(String jsonPayloadSignup, ResultMatcher expectedStatus, String testName) {
        logger.info("Test: " + testName);
        mvc.perform(post("/api/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonPayloadSignup))
            .andDo(result -> logger.info("MESSAGE: " + result.getResponse().getContentAsString()))
            .andExpect(expectedStatus);
    }



    //------------------------------------------------------------------------------

    private static Stream<Arguments> updateUserTestInputs() {
        Stream<Arguments> goodInputs = Stream.of(
            Arguments.of("""
                {
                    "name": "newNameTest",
                    "firstName": "newFirstNameTest",
                    "email": "emailTest@email.com",
                    "website": "www.apple.com",
                    "birthday": "2023-10-29T11:22:05.953Z",
                    "password": "passwordTest",
                    "passwordConfirm": "passwordTest"
                }""", status().isAccepted(), "good full input"),
                Arguments.of("""
                {
                    "name": "newNameTest",
                    "firstName": "newFirstNameTest",
                    "email": "emailTest@email.com",
                    "website": "www.apple.com",
                    "birthday": "2023-10-29T11:22:05.953Z"
                }""", status().isAccepted(), "good full input without changing password"),
                Arguments.of("""
                {
                    "name": "newNameTest"
                }""", status().isAccepted(), "good full input"),
                Arguments.of("""
                {
                }""", status().isAccepted(), "good minimal update")
        );
        Stream<Arguments> badInputs = Stream.of(
            Arguments.of("""
                {
                    "name": "n"
                }""", status().isBadRequest(), "name too short"),
            Arguments.of("""
                {
                    "firstName": "n"
                }""", status().isBadRequest(), "firsName too short"),
            Arguments.of("""
                {
                    "email": ""
                }""", status().isBadRequest(), "email blank"),
            Arguments.of("""
                {
                    "email": "@email.com"
                }""", status().isBadRequest(), "email bad format"),
            Arguments.of("""
                {
                    "birthday": "2099-99"
                }""", status().isBadRequest(), "birthday bad format"),
            Arguments.of("""
                {
                    "password": "passwordTest",
                    "passwordConfirm": "passwordTestPasswordTest"
                }""", status().isBadRequest(), "password and passwordConfirm mismatch"),
            Arguments.of("""
                {
                    "password": "p",
                    "passwordConfirm": "p"
                }""", status().isBadRequest(), "password is too short")

        );
        return Stream.concat(goodInputs, badInputs);
    }


    @SneakyThrows
    @WithMockUser
    @ParameterizedTest
    @MethodSource("updateUserTestInputs")
    void dtoValidationUpdateUserTest(String jsonPayloadUpdateUser, ResultMatcher expectedStatus, String testName) {
        logger.info("Test: " + testName);
        mvc.perform(patch("/api/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonPayloadUpdateUser))
            .andDo(result -> logger.info("MESSAGE: " + result.getResponse().getContentAsString()))
            .andExpect(expectedStatus);
    }


}
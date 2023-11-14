package com.example.projetcv.service;

import com.example.projetcv.dao.UserRepository;
import com.example.projetcv.dto.LoginDto;
import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.dto.UserSignupDto;
import com.example.projetcv.dto.UserUpdateDto;
import com.example.projetcv.model.User;
import com.example.projetcv.security.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private User testUser;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtHelper jwtHelper;

    @BeforeEach
    void setUp() {
        this.testUser = User.builder()
            .id(123L)
            .roles(Set.of("USER"))
            .name("John")
            .firstName("Doe")
            .birthday(LocalDate.now())
            .email("username@email.com")
            .passwordHash("hashdepassword")
            .build();
        User testUser2 = User.builder()
            .id(456L)
            .name("John2")
            .firstName("Doe2")
            .birthday(LocalDate.now())
            .email("anemail2@email.com")
            .passwordHash("hash2")
            .build();
        when(userRepository.findByEmail("username@email.com")).thenReturn(java.util.Optional.ofNullable(testUser));
        when(userRepository.findById(123L)).thenReturn(java.util.Optional.ofNullable(testUser));
        when(userRepository.findByEmail("anemail2@email.com")).thenReturn(java.util.Optional.ofNullable(testUser2));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findAll()).thenReturn(List.of(testUser, testUser2));
        when(userRepository.findAllUsersWithFilters(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(testUser)));
        //when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(testUser));

        UserDetails testUserDetail = org.springframework.security.core.userdetails.User
                .withUsername("999")
                .password(passwordEncoder.encode("password"))
                .authorities(List.of("USER").stream().map(SimpleGrantedAuthority::new).toList())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
        when(userDetailsService.loadUserByUsername(any())).thenReturn(testUserDetail);

    }



    /*The class to test*/
    @Autowired
    private UserService userService;


    //----------------------------------------------------

    @Test
    void login() {
        assertDoesNotThrow(()->{
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail("username@email.com");
            loginDto.setPassword("password");
            userService.login(loginDto);
        });
    }

    @Test
    void logout() {
        assertDoesNotThrow(()->{
            HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
            when(req.getHeader("Authorization")).thenReturn("Bearer token");
            userService.logout(req);
        });
    }

    @Test
    void signup() {
        UserSignupDto userDTO = new UserSignupDto();
        userDTO.setName("John");
        userDTO.setFirstName("Doe");
        userDTO.setBirthday(LocalDate.now());
        userDTO.setEmail("newemail@email.com");
        userDTO.setPassword("password");
        userDTO.setPasswordConfirm("password");
        when(userRepository.findByEmail("newemail@email.com")).thenReturn(java.util.Optional.ofNullable(null));
        assertDoesNotThrow(()->{
            UserSafeDto userSafeDto = userService.signup(userDTO);
            assertEquals(userSafeDto.getName(), userDTO.getName());
        });
    }

    @Test
    void deleteById() {
        assertDoesNotThrow(()->{
            UserSafeDto userSafeDto = userService.deleteById("123");
            assertEquals(userSafeDto.getName(), testUser.getName());
        });
    }

    @Test
    void getUserById() {
        assertDoesNotThrow(()->{
            UserSafeDto userSafeDto = userService.getUserById(123L);
            assertEquals(userSafeDto.getName(), testUser.getName());
        });
    }

    @Test
    @WithMockUser(username = "123")
    void whoami() {
        assertDoesNotThrow(()->{
            UserSafeDto userSafeDto = userService.whoami((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            assertEquals(userSafeDto.getName(), testUser.getName());
        });
    }

    @Test
    void refresh() throws InterruptedException {
        String initialToken = jwtHelper.createToken(testUser);
        Thread.sleep(1000); //sinon les 2 tokens sont exactement identiques
        logger.info("initialToken: "+initialToken);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer "+initialToken);
        //
        assertDoesNotThrow(()->{
            String newToken = userService.refresh(req);
            assertTrue(newToken.length()>100);
            assertNotEquals(initialToken, newToken);
        });
    }

    @Test
    @WithMockUser(username = "123")
    void update() {
        UserDetails userDetails =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("LeNouveauNom");
        userUpdateDto.setFirstName("LeNouveauPrenom");
        UserSafeDto userSafeDto = userService.update(userUpdateDto, userDetails);
        assertEquals(userSafeDto.getName(), userUpdateDto.getName());
    }

    @Test
    void getAllUsers() {
        UserSafeDto[] users = userService.getAllUsers();
        assertTrue(users.length==2);
        assertTrue(users[0].getName().equals("John"));
        assertTrue(users[1].getName().equals("John2"));
    }

    @Test
    void getAllUsersWithFilter() {
        PagedModel<UserSafeDto> users = userService.getAllUsersWithFilter("John", "Doe", "", null);
        assertTrue(users.getContent().size()==1);
        assertTrue(users.hasLink("self"));
    }
}
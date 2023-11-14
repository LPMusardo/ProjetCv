package com.example.projetcv.service;

import com.example.projetcv.dao.CVRepository;
import com.example.projetcv.dao.UserRepository;
import com.example.projetcv.dto.ActivityDto;
import com.example.projetcv.dto.CvDto;
import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.model.User;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CVServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private CVRepository cvRepository;

    @Mock
    private UserDetails userDetails;

    static CvDto cvDto;

    @InjectMocks
    private CVService cvService;


    @BeforeAll
    public static void before() {
        cvDto = new CvDto();
        List<ActivityDto> activities = new ArrayList<>();
        ActivityDto activityDto1 = ActivityDto.builder()
                .nature(Nature.EDUCATION)
                .title("the title of the activity 1")
                .year(2001)
                .description("the description of the activity 1")
                .webAddress("the web address of the activity 1")
                .build();
        ActivityDto activityDto2 = ActivityDto.builder()
                .nature(Nature.EDUCATION)
                .title("the title of the activity 2")
                .year(2002)
                .description("the description of the activity 2")
                .webAddress("the web address of the activity 2")
                .build();
        activities.add(activityDto1);
        activities.add(activityDto2);
        cvDto.setActivities(activities);
    }

    @BeforeEach
    public void beforeEach() {
        cvService.modelMapper = new ModelMapper();
    }


    //-------------------------------------------------------------------------------

    @Test
    public void testUpdateExistingCV() {
        // Créer un utilisateur factice pour le test
        User user = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();
        CV newCV = new CV();
        user.setCv(newCV);
        newCV.setUser(user);
        Activity activity = Activity.builder()
                .cv(newCV)
                .year(2022)
                .nature(Nature.PROJECT)
                .title("will be replaced with the update")
                .build();
        newCV.getActivities().add(activity);

        // setup Mocks
        when(userDetails.getUsername()).thenReturn("123");
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Appeler la méthode à tester
        UserSafeDto result = cvService.updateCV(cvDto, userDetails);

        // Vérifier
        assertEquals(user.getId(), result.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any());
        assertEquals(result.getCv().getActivities().size(), 2);
        assertEquals(result.getCv().getActivities().get(0).getTitle(), "the title of the activity 1");
        assertEquals(result.getCv().getActivities().get(1).getTitle(), "the title of the activity 2");
    }


    @Test
    public void testUpdateNullCV() {
        // Créer un utilisateur factice pour le test
        User user = User.builder()
                .name("John")
                .firstName("Doe")
                .birthday(LocalDate.now())
                .email("john.doe@example.com")
                .passwordHash("lehash")
                .build();

        // setup Mocks
        when(userDetails.getUsername()).thenReturn("123");
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Appeler la méthode à tester
        UserSafeDto result = cvService.updateCV(cvDto, userDetails);

        // Vérifier
        assertEquals(user.getId(), result.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any());
        assertEquals(result.getCv().getActivities().size(), 2);
        assertEquals(result.getCv().getActivities().get(0).getTitle(), "the title of the activity 1");
        assertEquals(result.getCv().getActivities().get(1).getTitle(), "the title of the activity 2");
    }



}

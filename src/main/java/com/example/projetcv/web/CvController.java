package com.example.projetcv.web;

import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.security.JwtHelper;
import com.example.projetcv.service.CVService;
import com.example.projetcv.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/cvs")
public class CvController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private CVService cvService;


    //-----------------------------------------------------------------------

    @GetMapping
    public List<CV> getAllCvs() {
        return cvService.getAllCv();
    }

    @GetMapping("/{id}")
    public CV getCv(@PathVariable long id) {
        return cvService.getCvById(id);

    }


/*    @DeleteMapping
    public ResponseEntity<String> deleteCv(@AuthenticationPrincipal UserDetails userDetails) {
        cvService.deleteCvByUserId(userDetails.getUsername());
        return new ResponseEntity<>("You deleted  your Cv with id : " + userDetails.getUsername(), HttpStatus.NO_CONTENT);
    }*/


/*    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String addActivity(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Activity activity){
        cvService.addActivityToCv(userDetails.getUsername() , activity);
        return "activity added";
    }*/

/*    @PatchMapping("/{idActivity}")
    public Activity updateCv(@PathVariable Long idActivity, @RequestBody Activity activity){
        return  cvService.updateActivity(idActivity , activity);

    }*/

/*    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String deleteActivity(@AuthenticationPrincipal UserDetails userDetails, Long idActivity){
        cvService.removeActivityToCv(userDetails.getUsername(),idActivity);
        return "";

    }*/



}

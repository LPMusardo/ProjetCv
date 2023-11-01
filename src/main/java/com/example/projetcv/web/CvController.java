package com.example.projetcv.web;

import com.example.projetcv.dto.CvDto;
import com.example.projetcv.dto.UserSafeDto;
import com.example.projetcv.model.CV;
import com.example.projetcv.service.CVService;
import com.example.projetcv.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/cvs")
public class CvController {

    @Autowired
    private UserService userService;

    private final Logger logger = Logger.getLogger(this.getClass().getName());


    @Autowired
    private CVService cvService;


    //-----------------------------------------------------------------------


//    @GetMapping
//    public List<CV> getAllCvs() {
//        return cvService.getAllCv();
//    }


    @GetMapping("/{id}")
    public CV getCv(@PathVariable long id) {
        return cvService.getCvById(id);
    }


/*    @DeleteMapping
    public ResponseEntity<String> deleteCv(@AuthenticationPrincipal UserDetails userDetails) {
        cvService.deleteCvByUserId(userDetails.getUsername());
        return new ResponseEntity<>("CV deleted", HttpStatus.NO_CONTENT);
    }*/


    @PatchMapping()
    public ResponseEntity<UserSafeDto> updateCv(@Valid @RequestBody CvDto cvDto, @AuthenticationPrincipal UserDetails userDetails){
        logger.info("from controller from updateCv()");
        return new ResponseEntity<>(cvService.updateCV(cvDto, userDetails), HttpStatus.CREATED);
    }


}

package com.example.projetcv.web;

import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.security.JwtHelper;
import com.example.projetcv.service.CVService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/cvs")
public class CvController {

    @Autowired
    private JwtHelper jwtTokenProvider;
    @Autowired
    private CVService cvService;



    @GetMapping
    public List<CV> getAllCvs() {
        return cvService.getAllCv();
    }

    @GetMapping("/{id}")
    public CV getCv(@PathVariable("id") Long id) {
        return cvService.getCvById(id);

    }
// TODO :
/*    @DeleteMapping
    public String deleteCv() {
        Long idUser = jwtTokenProvider.getId(jwtTokenProvider.resolveToken(req));
        cvService.deleteCvByUserId(idUser);
        return "You deleted  your Cv with id : " + idUser;
    }*/

    @PostMapping
    public String addActivity(HttpServletRequest req,@RequestBody Activity activity){
        Long idUser = jwtTokenProvider.getId(jwtTokenProvider.resolveToken(req));
        cvService.addActivityToCv(idUser , activity);
        return "activity added";
    }

    @PatchMapping("/{idActivity}")
    public Activity updateCv(@PathVariable Long idActivity, @RequestBody Activity activity){

        return  cvService.updateActivity(idActivity , activity);


    }

    @DeleteMapping
    public String deleteActivity(HttpServletRequest req,Long idActivity){
        Long idUser = jwtTokenProvider.getId(jwtTokenProvider.resolveToken(req));

        cvService.removeActivityToCv(idUser,idActivity);
        return "";

    }



}

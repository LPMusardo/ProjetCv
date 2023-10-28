package com.example.projetcv.dao;


import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CVRepository extends JpaRepository<CV, Long> {

    void deleteByUserId(Long userId);
    CV findByUserId(Long userId);


/*    @Query("SELECT cv FROM CV cv JOIN cv.activities a WHERE a.nature = :nature")
    List<CV> findCvWithActivitesOfNature(@Param("nature") Nature nature);*/
}

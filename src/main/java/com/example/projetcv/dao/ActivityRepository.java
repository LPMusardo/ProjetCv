package com.example.projetcv.dao;


import com.example.projetcv.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ActivityRepository extends JpaRepository<Activity, Long> {

}

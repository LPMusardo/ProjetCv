package com.example.projetcv.dao;


import com.example.projetcv.model.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CVRepository extends JpaRepository<CV, Long> {
}

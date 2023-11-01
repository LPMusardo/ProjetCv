package com.example.projetcv.dao;

import com.example.projetcv.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Long deleteByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN u.cv cv " +
            "LEFT JOIN cv.activities activity " +
            "WHERE LOWER(COALESCE(u.name, '')) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(COALESCE(u.firstName, '')) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
            "AND LOWER(COALESCE(activity.title, '')) LIKE LOWER(CONCAT('%', :activityTitle, '%'))")
    Page<User> findAllUsersWithFilters(String name, String firstName, String activityTitle, Pageable pageable);

}

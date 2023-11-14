package com.example.projetcv.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CV {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonBackReference
    @ToString.Exclude
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;


    @Builder.Default
    @JsonManagedReference
    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();

}

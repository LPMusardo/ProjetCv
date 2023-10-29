package com.example.projetcv.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonBackReference


    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CV cv;


    @Basic
    private int year;


    @Basic
    @Column(nullable = false)
    private Nature nature;


    @Basic
    @Column(nullable = false)
    private String title;


    @Basic
    private String description;


    @Basic
    private String webAddress;


}

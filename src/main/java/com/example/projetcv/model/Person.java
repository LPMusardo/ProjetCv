package com.example.projetcv.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(com.example.projetcv.model.Views.Public.class)
    @Column(nullable = false)
    private String name;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private String firstName;

    @JsonView(Views.Public.class)
    @Basic
    @Column(unique=true, nullable = false)
    private String email;

    @JsonView(Views.Public.class)
    @Basic
    private String website;

    @JsonView(Views.Internal.class)
    @Basic
    @Column(nullable = false)
    private LocalDate birthday;

    @JsonView(Views.Internal.class)
    @Basic
    @Column(nullable = false)
    private String passwordHash;


    // ATTENTION : One-to-one n'est en réalité jamais LAZY
    // SRC: https://stackoverflow.com/questions/1444227/how-can-i-make-a-jpa-onetoone-relation-lazy
    //
    // mappedby est utilisé dans l'entité qui n'est pas le propriétaire de la relation et qui ne contient pas la clé étrangère
    @JsonView(Views.Public.class)
    @OneToOne(optional = true, mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private CV cv;

    @ElementCollection(fetch = FetchType.EAGER)
    Set<String> roles;

}

package com.example.projetcv.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.Set;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {


    @ElementCollection(fetch = FetchType.EAGER)
    Set<String> roles;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(nullable = false)
    @Size(min = 2, message = "name is to short")
    private String name;


    @Column(nullable = false)
    @Size(min = 2, message = "firstName is to short")
    private String firstName;


    @Basic
    @Column(unique = true, nullable = false)
    @NotBlank(message = "email must not be blank in db")
    private String email;


    @Basic
    private String website;


    @Basic
    @Column(nullable = false)
    private LocalDate birthday;


    @Basic
    @Column(nullable = false)
    private String passwordHash;


    // ATTENTION : One-to-one n'est en réalité jamais LAZY
    // SRC: https://stackoverflow.com/questions/1444227/how-can-i-make-a-jpa-onetoone-relation-lazy
    //
    // mappedby est utilisé dans l'entité qui n'est pas le propriétaire de la relation et qui ne contient pas la clé étrangère
    @JsonManagedReference
    @OneToOne(optional = true, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private CV cv;


}

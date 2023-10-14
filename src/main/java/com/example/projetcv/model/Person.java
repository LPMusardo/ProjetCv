package com.example.projetcv.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Can't be blank")
    @JsonView(com.example.projetcv.model.Views.Public.class)
    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    //@NotBlank(message = "Can't be blank")
    //@JsonView(Views.Public.class)
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Can't be blank")
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

    //cascade = CascadeType.ALL  : pour que la suppression de la personne entraine la suppression du cv
    // mappedby est utilisé dans l'entité qui n'est pas le propriétaire de la relation et qui ne contient pas la clé étrangère
    @JsonView(Views.Public.class)
    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    private CV cv;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = false)
    Set<String> roles = Set.of();

}

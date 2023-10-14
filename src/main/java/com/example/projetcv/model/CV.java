package com.example.projetcv.model;

import com.fasterxml.jackson.annotation.JsonView;
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

  @JsonView(Views.Internal.class)
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @JsonView(Views.Internal.class)
  @ToString.Exclude
  @OneToOne(optional=false, fetch = FetchType.EAGER)
  private Person person;

  @JsonView(Views.Public.class)
  @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private List<Activity> activities = new ArrayList<>();
}

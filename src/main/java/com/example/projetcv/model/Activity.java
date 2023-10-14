package com.example.projetcv.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

  @JsonView(Views.Internal.class)
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @JsonView(Views.Internal.class)
  @ToString.Exclude
  @ManyToOne(optional=false)
  private CV cv;

  @JsonView(Views.Public.class)
  @Basic
  private int year;

  @JsonView(Views.Public.class)
  @Basic
  @Column(nullable = false)
  private Nature nature;

  @JsonView(Views.Public.class)
  @Basic
  @Column(nullable = false)
  private String title;

  @JsonView(Views.Public.class)
  @Basic
  private String description;

  @JsonView(Views.Public.class)
  @Basic
  private String webAddress;
}

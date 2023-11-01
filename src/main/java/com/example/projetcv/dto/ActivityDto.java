package com.example.projetcv.dto;

import com.example.projetcv.model.Nature;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDto {


    @NotNull(message = "nature must not be null")
    private Nature nature;


    @NotNull(message = "itle must not be null")
    @NotBlank(message = "title must not be blank")
    private String title;


    private int year;


    private String description;


    private String webAddress;


}

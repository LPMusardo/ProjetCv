package com.example.projetcv.dto;

import com.example.projetcv.model.Activity;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CvDto {

    @Valid
    @NotNull(message = "activities must not be null")
    private List<ActivityDto> activities;

}

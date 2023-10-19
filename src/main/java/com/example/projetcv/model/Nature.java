package com.example.projetcv.model;

import java.util.Random;

public enum Nature {
    EDUCATION, EXPERIENCE, PROJECT, OTHER;

    private static final Random random = new Random();


    public static Nature randomNature()  {
        Nature[] natures = values();
        return natures[random.nextInt(natures.length)];
    }




}

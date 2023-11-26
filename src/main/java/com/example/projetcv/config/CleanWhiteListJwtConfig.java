package com.example.projetcv.config;

import com.example.projetcv.security.JwtWhitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
@ConditionalOnBean(JwtWhitelist.class)
public class CleanWhiteListJwtConfig {

    @Autowired
    private JwtWhitelist jwtWhitelist;


    /*Time in milliseconds*/
    @Scheduled(fixedDelay = 10000, initialDelay = 15000)
    public void scheduleFixedRateWithInitialDelayTask() {
        jwtWhitelist.cleanExpiredTokens();
    }


}

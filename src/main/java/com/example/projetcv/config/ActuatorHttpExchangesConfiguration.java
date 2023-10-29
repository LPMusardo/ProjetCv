package com.example.projetcv.config;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class ActuatorHttpExchangesConfiguration {
    @Bean
    public HttpExchangeRepository httpTraceRepository()
    {
        return new InMemoryHttpExchangeRepository();
    }
}
package com.example.projetcv.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Profile("usejwt")
@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;


    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Pas de vérification CSRF (cross site request forgery)
        http.csrf().disable();

        // Spring security de doit gérer les sessions
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Déclaration des end-points
        http.authorizeRequests().requestMatchers("/api/auth/login").permitAll()
                .and()
                .authorizeRequests().requestMatchers("/api/auth/signup").authenticated()
                .and()
                .authorizeRequests().requestMatchers("/api/auth/**").authenticated()
                .and()
                .authorizeRequests().requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                .and()
                .authorizeRequests().requestMatchers(HttpMethod.GET,"/api/users/**").permitAll()
                .and()
                .authorizeRequests().requestMatchers(HttpMethod.DELETE, "/api/users").authenticated()
                .and()
                .authorizeRequests().requestMatchers(HttpMethod.PATCH, "/api/users").authenticated()
                .and()
                .authorizeRequests().requestMatchers(HttpMethod.POST, "/api/users").authenticated()
                .anyRequest().permitAll();

        // Pas vraiment nécessaire
        //http.exceptionHandling().accessDeniedPage("/login");

        // Optional, if you want to test the API from a browser
        //http.httpBasic();

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}

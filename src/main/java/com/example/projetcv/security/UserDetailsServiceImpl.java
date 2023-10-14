package com.example.projetcv.security;

import com.example.projetcv.dao.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;


@Service
@Profile("usejwt")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = personRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User '" + email + "' not found"));
        return User
                .withUsername(email)
                .password(user.getPasswordHash())
                .authorities(user.getRoles().stream().map(SimpleGrantedAuthority::new).toList())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }



}

package com.example.projetcv.security;


import com.example.projetcv.exception.MyJwtException;
import com.example.projetcv.model.User;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Création/vérification/gestion d'un JWT
 */
@Service
public class JwtHelper {

    private Logger logger = Logger.getLogger(JwtHelper.class.getName());

    @Value("${com.example.projetcv.jwt.use-whitelist}")
    private boolean useWhitelist;

    @Autowired(required = false)
    JwtWhitelist whitelist;


    @Value("${security.jwt.token.secret-key:SaS7FS7dvnMP1RwJT/zcTmLAUd07vICgJTzpB1MFtnY=}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:6000000}")
    private long validityInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsServiceImpl;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    //---------------------------------------------------------------------------------------------------


    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("auth", user.getRoles().stream().filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        byte[] decodedKey = DatatypeConverter.parseBase64Binary(secretKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
        String token = Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(originalKey)// Use the SecretKey instance here
                .compact();
        if (useWhitelist) whitelist.addToken(token);
        logger.info("token created " + token);
        return token;
    }


    public Authentication getAuthentication(String token) throws MyJwtException {
        try {
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(getUserId(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (Exception e) {
            throw new MyJwtException("User from token not found in database", HttpStatus.UNAUTHORIZED);
        }
    }


    public String getUserId(String token) {
        byte[] decodedKey = DatatypeConverter.parseBase64Binary(secretKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
        return Jwts.parserBuilder() // Use parserBuilder instead of parser
                .setSigningKey(originalKey) // Use the SecretKey instance here
                .build() // Build the parser
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }


    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) return null;
        return bearerToken.substring("Bearer ".length());
    }


    public boolean validateToken(String token) throws MyJwtException {
        logger.info("Before validate token: " + token);
        try {
            byte[] decodedKey = DatatypeConverter.parseBase64Binary(secretKey);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
            Jwts.parserBuilder() // use parserBuilder instead of parser
                    .setSigningKey(originalKey) // use the SecretKey instance here
                    .build() // create a parser instance by calling build
                    .parseClaimsJws(token);
        } catch (Exception e) {
            logger.info("Invalid or expired token");
            throw new MyJwtException("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }
        if (useWhitelist && !whitelist.containsToken(token)) {
            logger.info("Invalid or expired token");
            throw new MyJwtException("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        logger.info("Token is valid: " + token);
        return true;
    }


    public String removeTokenFromWhiteList(String token) {
        if (useWhitelist) whitelist.removeToken(token);
        return "token removed " + token;
    }


}

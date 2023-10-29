package com.example.projetcv.security;


import com.example.projetcv.exception.MyJwtException;
import com.example.projetcv.model.User;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    public ArrayList<String> whiteList = new ArrayList<>();


    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    //---------------------------------------------------------------------------------------------------


    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("auth", user.getRoles().stream().filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        String token = Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
        whiteList.add(token);
        logger.info("token created " + token);
        return token;
    }


    public Authentication getAuthentication(String token) throws MyJwtException {
        try{
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(getUserId(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }catch (Exception e){
            throw new MyJwtException("User from token not found in database", HttpStatus.UNAUTHORIZED);
        }
    }


    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) return null;
        return bearerToken.substring("Bearer ".length());
    }


    public boolean validateToken(String token) throws MyJwtException{
        logger.info("Before validate token: " + token);
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (Exception e) {
            logger.info("Invalid or expired token");
            throw new MyJwtException("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }
        if (!whiteList.contains(token)) {
            logger.info("Invalid or expired token");
            throw new MyJwtException("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        logger.info("Token is valid: " + token);
        return true;
    }


    public String removeTokenFromWhiteList(String token) {
        whiteList.remove(token);
        return "token removed " + token;
    }


    public void cleanWhiteList() {
        for (int i = 0; i < whiteList.size(); i++) {
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(whiteList.get(i)).getBody().getExpiration();
            } catch (Exception e) {
                whiteList.remove(i);
            }
        }
        logger.info("whitelist size: " + whiteList.size());
    }


//    public Jwt<Header, Claims> getTokenInfo(String token) {
//        String tokenWithoutSign = token.substring(0,token.lastIndexOf("."));
//        return Jwts.parser().parseClaimsJwt(tokenWithoutSign);
//    }

}

package com.example.projetcv.security;


import com.example.projetcv.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
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
@Profile("usejwt")
public class JwtHelper {

    private Logger logger = Logger.getLogger(JwtHelper.class.getName());

    public ArrayList<String> whiteList = new ArrayList<String>();

    /**
     * Par simplicité, nous stockons la clef de manière statique. il est sans doute
     * préférable d'avoir un autre API (sur un serveur de configuration) qui nous
     * fournisse la clé.
     */
    @Value("${security.jwt.token.secret-key:SaS7FS7dvnMP1RwJT/zcTmLAUd07vICgJTzpB1MFtnY=}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:6000000}")
    private long validityInMilliseconds; // 60 seconds

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    //---------------------------------------------------------------------------------------------------


    public String createToken(User user) {

        Claims claims = Jwts.claims().setSubject("user");
        claims.put("mail", user.getEmail());
        claims.put("id", user.getId());
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
        System.out.println("token created " + token);
        logger.info("token created " + token);
        return token;
    }


    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("mail",String.class);
    }

    public String getId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("id",String.class);
    }


    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // remove token from white list
    public String removeToken(String token) {
        whiteList.remove(token);
        return "token removed " + token;
    }


    public boolean validateToken(String token) {
        System.out.println("Before validate token: " + token);
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (whiteList.contains(token)) {
                return true;
            }
            throw new MyJwtException("Expired or invalid JWT token A", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MyJwtException | IllegalArgumentException e) {
            throw new MyJwtException("Expired or invalid JWT token B", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void clean() {
        for (int i = 0; i < whiteList.size(); i++) {
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(whiteList.get(i)).getBody().getExpiration();
            } catch (Exception e) {
                whiteList.remove(i);
            }
        }
        System.out.println("size: " + whiteList.size());
    }


}

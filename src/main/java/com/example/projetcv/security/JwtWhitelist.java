package com.example.projetcv.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;
import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnProperty(name = "com.example.projetcv.jwt.use-whitelist", havingValue = "true")
public class JwtWhitelist {

    private final Logger logger = Logger.getLogger(JwtWhitelist.class.getName());

    @Value("${security.jwt.token.secret-key:SaS7FS7dvnMP1RwJT/zcTmLAUd07vICgJTzpB1MFtnY=}")
    private String secretKey;

    private final ArrayList<String> whiteList = new ArrayList<>();

    //-----------------------------------------------------------------------------------

    public void addToken(String token) {
        this.whiteList.add(token);
    }

    public boolean containsToken(String token) {
        return this.whiteList.contains(token);
    }

    public void removeToken(String token) {
        this.whiteList.remove(token);
    }

    public void cleanExpiredTokens() {
        logger.info("cleaning whitelist");
        Iterator<String> iterator = whiteList.iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            try {
                Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getExpiration();
            } catch (Exception e) {
                logger.info(e.getMessage());
                iterator.remove();
            }
        }
        logger.info("whitelist size: " + whiteList.size());
    }
}

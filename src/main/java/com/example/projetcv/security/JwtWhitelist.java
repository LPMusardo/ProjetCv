package com.example.projetcv.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;


import java.util.ArrayList;
import java.util.logging.Logger;

@Component
@ConditionalOnProperty(name = "com.example.projetcv.jwt.use-whitelist", havingValue = "true")
public class JwtWhitelist {

    private final Logger logger = Logger.getLogger(JwtHelper.class.getName());


    @Value("${security.jwt.token.secret-key:SaS7FS7dvnMP1RwJT/zcTmLAUd07vICgJTzpB1MFtnY=}")
    private String secretKey;


    private final ArrayList<String> whiteList = new ArrayList<>();


    //-----------------------------------------------------------------------------------

    public boolean addToken(String token){
        return this.whiteList.add(token);
    }

    public boolean containsToken(String token){
        return this.whiteList.contains(token);
    }

    public boolean removeToken(String token){
        return this.whiteList.remove(token);
    }


    public void cleanExpiredTokens() {
        logger.info("cleaning whitelist");
        for (int i = 0; i < whiteList.size(); i++) {
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(whiteList.get(i)).getBody().getExpiration();
            } catch (Exception e) {
                whiteList.remove(i);
            }
        }
        logger.info("whitelist size: " + whiteList.size());
    }




}

package com.FileExplorer.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Service
public class JwtTokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);
    private static RsaKeyProperties rsaKeys;

    public JwtTokenUtils(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    public static String getMyUsername() {
        try {
            Map<String, Object> tokenMap = decode(getToken());
//            System.out.println(tokenMap);
            return tokenMap.get("sub").toString();
        } catch (Exception e) {
            logger.error("JwtTokenUtils:debugPrint exception: " + e);
        }
        return "";
    }

    private static String getToken() {
        return getAuthorizationHeader().split(" ")[1];
    }

    private static String getAuthorizationHeader() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    private static Map<String, Object> decode(String token) {
        try {
            JwtDecoder jwtDecoder = jwtDecoder();
            Jwt jwt = jwtDecoder.decode(token);
            Map<String, Object> claimsStr = jwt.getClaims();
            return claimsStr;
        } catch (Exception e) {
            throw new InvalidBearerTokenException("Cannot convert access token to JSON", e);
        }
    }

    static JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }
}

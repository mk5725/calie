package com.mk.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("userId","15aacca42542");
        Map<String, String> map2 = new HashMap<>();
        map.put("userId","1554ca2a542");
        Map<String, String> map3 = new HashMap<>();
        map.put("userId","155");
        String token = JwtUtils.getToken(map);
        String token2 = JwtUtils.getToken(map2);
        String token3 = JwtUtils.getToken(map3);
        System.out.println(token);
        System.out.println(token2);
        System.out.println(token3);
    }
}

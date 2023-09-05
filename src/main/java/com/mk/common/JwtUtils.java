package com.mk.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

// Token工具类
public class JwtUtils {
    private static final String SLAT = "!%D3$#GR^*&L";

    // 生成Token
    public static String getToken(Map<String,String> map){
        // 设置默认过期时间(3天)
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 1);

        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        String token = builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SLAT));

        return token;
    }

    // 验证Token
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(SLAT)).build().verify(token);
    }

    // 获取Token 解签信息
    public static DecodedJWT getDecoded(String token){
        return JWT.require(Algorithm.HMAC256(SLAT)).build().verify(token);
    }
}

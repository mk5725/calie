package com.mk.config;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Random;

/**
 *  生成验证码
 */
@Slf4j
public class Code {
    public static String getCode(int n){
        Random random = new Random(System.currentTimeMillis());
        StringBuffer stringBuffer = new StringBuffer(n);
        for (int i=0;i<n;i++){
            stringBuffer.append(random.nextInt(9));
        }
        log.info("生成验证码：{}", String.valueOf(stringBuffer));
        return String.valueOf(stringBuffer);
    }
}

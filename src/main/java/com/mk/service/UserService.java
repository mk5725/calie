package com.mk.service;

import com.mk.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserService {
    //登录验证
    Boolean loginCheck(Map map, HttpServletRequest request);

    // 发送验证码
    String sendMsg(User user);

}

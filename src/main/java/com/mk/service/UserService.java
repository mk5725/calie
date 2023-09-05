package com.mk.service;

import com.mk.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    //登录验证
    Boolean loginCheck(User user, HttpServletRequest request, HttpServletResponse response);

}

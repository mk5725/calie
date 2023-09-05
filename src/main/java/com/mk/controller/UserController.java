package com.mk.controller;

import com.mk.common.R;
import com.mk.pojo.User;
import com.mk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 登录验证
    @PostMapping("/login")
    public R loginCheck(@RequestBody User user, HttpServletRequest request, HttpServletResponse response){

        userService.loginCheck(user, request, response);
        return R.SUCCESS();
    }

    // 退出登录
    @PostMapping("/loginout")
    public R logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("employee");
        return R.SUCCESS();
    }
}

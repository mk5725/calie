package com.mk.controller;

import com.mk.common.R;
import com.mk.pojo.User;
import com.mk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 登录验证
    @PostMapping("/login")
    public R loginCheck(@RequestBody Map map, HttpServletRequest request){
        if (userService.loginCheck(map, request)) {
            return R.SUCCESS();
        }
        return R.ERROR("验证码无效！");
    }
    // 获取验证码
    @PostMapping("/sendMsg")
    public R sendMsg(@RequestBody User user){
        String code = userService.sendMsg(user);
        return R.SUCCESS(code);
    }

    // 退出登录
    @PostMapping("/loginout")
    public R logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("employee");
        return R.SUCCESS();
    }
}

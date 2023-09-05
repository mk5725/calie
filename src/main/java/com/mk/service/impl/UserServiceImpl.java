package com.mk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mk.common.JwtUtils;
import com.mk.mapper.UserMapper;
import com.mk.pojo.User;
import com.mk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    //登录验证
    public Boolean loginCheck(User user, HttpServletRequest request, HttpServletResponse response){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, user.getPhone());
        User u = userMapper.selectOne(wrapper);
        if (u == null) {
            userMapper.insert(user);  // user主键自动填充
            u = user;
        }
        request.getSession().setAttribute("user", u.getId());

        // 向客户端响应cookie
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(user.getId()));  // 将登录用户的主键Id存入token的payload中
        String token = JwtUtils.getToken(map);            // 获取token
//        Cookie cookie = new Cookie("userId", token);
        Cookie cookie = new Cookie("userId", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain(request.getServerName());
//        response.addCookie(cookie);                       // 添加cookie

        return null;
    }
}

package com.mk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mk.common.JwtUtils;
import com.mk.config.Code;
import com.mk.mapper.UserMapper;
import com.mk.pojo.User;
import com.mk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //登录验证
    public Boolean loginCheck(Map map, HttpServletRequest request){
        // 验证码验证
        if (redisTemplate.hasKey((String) map.get("phone"))) {  // 判断验证码是否存在
            String redisCode = redisTemplate.opsForValue().get(map.get("phone"));
            if (!redisCode.equals(map.get("code")))
                return false;
            ValueOperations valueOperations = redisTemplate.opsForValue();
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, map.get("phone"));
            User user = userMapper.selectOne(wrapper);
            if (user == null) {
                User u = new User();
                u.setPhone((String) map.get("phone"));
                userMapper.insert(u);  // user主键自动填充
                user.setId(u.getId());
            }
            request.getSession().setAttribute("user", user.getId());

            // 删除验证码
            redisTemplate.delete((String) map.get("phone"));
            return true;
        }
        return false;
    }


    // 发送验证码
    @Override
    public String sendMsg(User user){
        String code = Code.getCode(4);    // 生成4位验证码
        // 缓存验证码
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(user.getPhone(), code, 5*60L, TimeUnit.SECONDS);
        return code;
    }
}

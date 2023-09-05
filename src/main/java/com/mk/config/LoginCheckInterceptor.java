package com.mk.config;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mk.common.CustomServiceException;
import com.mk.common.JwtUtils;
import com.mk.common.MyThreadLocal;
import com.mk.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 登录验证拦截器
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断当前请求是否携带userId的cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
//            Long userId = flagUserId(cookies);
        }

        // 判断session是否存在
        if (request.getSession().getAttribute("employee") != null){
            log.info("userId: employee");
            Object employee = request.getSession().getAttribute("employee");
            MyThreadLocal.setValue((Long)employee);
//            MyThreadLocal.setValue(userId);
            return true;
        }
        if (request.getSession().getAttribute("user") != null){
            log.info("添加userId: user");
                Object employee = request.getSession().getAttribute("user");
            MyThreadLocal.setValue((Long)employee);
//            MyThreadLocal.setValue(userId);
            return true;
        }

        log.info("登录拦截器生效，拦截路径：{}", request.getRequestURI());
        response.getWriter().write(JSON.toJSONString(R.ERROR("NOTLOGIN")));
        return false;
    }

    private Long flagUserId(Cookie[] cookies){
        for (Cookie cookie : cookies) {
            if ("userId".equals(cookie.getName())){
                String token = cookie.getValue();
                try {
                    DecodedJWT decoded = JwtUtils.getDecoded(token);
                    Long userId = decoded.getClaim("userId").asLong();
                    return userId;
                }catch (Exception e){
                    e.printStackTrace();
                    throw new CustomServiceException("Token验证异常！");
                }
            }
        }
        return null;
    }
}

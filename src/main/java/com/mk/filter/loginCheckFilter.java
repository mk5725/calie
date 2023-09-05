package com.mk.filter;

import com.alibaba.fastjson2.JSON;
import com.mk.common.R;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  检查用户是否登录
 */
@Slf4j
//@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class loginCheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("loginCheckFilter过滤器初始化...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 定义不需要处理的请求路径
        String[] strUrl = {"/login", "/logout", "/backend", "/front", "/"};


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取本次请求的路径
        String requestURI = request.getRequestURI();
        log.info("拦截到请求: {}", requestURI);

        // 判断本次请求是否需要处理
        boolean check = check(strUrl, requestURI);
        if (check){
            log.info("本次请求不需要处理 {}", requestURI);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 判断登录状态
        if (request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户ID为 {}", request.getSession().getAttribute("employee"));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.ERROR("NoLogin")));
//        response.sendRedirect("/backend/page/login/login.html");
        return;
    }
    @Override
    public void destroy() {
    }
    public boolean check(String[] strUrl, String requestURI){
        for (String s : strUrl) {
            if (requestURI.contains("/index.html")) return false;
            if (requestURI.contains(s)) return true;
        }
        return false;
    }
}

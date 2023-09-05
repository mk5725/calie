package com.mk.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

//    @Autowired
    private HttpServletRequest request;

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", MyThreadLocal.getValue());
        metaObject.setValue("updateUser", MyThreadLocal.getValue());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        log.info((String.valueOf(request.getSession().getAttribute("employee"))));
//        log.info("updateFill线性id: {}", Thread.currentThread().getId());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", MyThreadLocal.getValue());
    }
}

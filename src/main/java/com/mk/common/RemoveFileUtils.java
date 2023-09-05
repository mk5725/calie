package com.mk.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

// 删除客户端上传文件的工具类
@Slf4j
@Component
public class RemoveFileUtils {
    @Value("${reggie.basePath}")
    private String basePath ;

    // 删除上传的图片
    public void removeImg(String name){
        File file = new File(basePath + name);
        if (file.exists() && file.isFile()){
            log.info("删除图片: {}", basePath+name);
            file.delete();
        }
    }
}

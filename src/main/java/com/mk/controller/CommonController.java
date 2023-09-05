package com.mk.controller;

import com.mk.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.basePath}")
    private String basePath;

    // 处理文件上传
    @PostMapping("/upload")
    public R upload(@RequestPart("file") MultipartFile file){
        log.info("上传文件根路径：{} | 原始文件名： {}", basePath, file.getOriginalFilename());
        // 转存上传的文件
        String saveFileName = saveUploadByFile(file);
//        saveUploadByOutputStream(file);
        return R.SUCCESS("", saveFileName);
    }
    // 处理文件下载
    @GetMapping("/download")
    public R download(@RequestParam("name") String name, HttpServletResponse response){
        //输入流，通过输入流读取文件内容
        FileInputStream fileInputStream = null;
        ServletOutputStream servletOutputStream = null;
        byte[] bytes = new byte[1024 * 20];
        try {
            servletOutputStream = response.getOutputStream();
//            response.setContentType("image/jpeg");
            fileInputStream = new FileInputStream(new File(basePath + name));
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != (-1)){
                //输出流，通过输出流将文件写回浏览器
                servletOutputStream.write(bytes, 0, len);
                servletOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (servletOutputStream != null){
                try {
                    servletOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return R.SUCCESS();
    }


    // 以File对象形式，转存文件
    public String saveUploadByFile(MultipartFile file){
        // 判断basePath目录是否存在
        isBasePath(basePath);
        String fileName = file.getOriginalFilename();
        String uid = UUID.randomUUID().toString().replace("-", "");
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        log.info("原始文件后缀： {}", suffix);
        log.info("转存文件绝对路径名： {}", basePath+uid+suffix);
        File saveFile = new File(basePath + uid + suffix);
        // 转存文件
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uid + suffix;
    }
    // 以二进制流形式 转存文件
    public void saveUploadByOutputStream(MultipartFile file){
        // 判断basePath目录是否存在
        isBasePath(basePath);
        FileOutputStream outputStream = null;
        try {
            byte[] bytes = file.getBytes();
            outputStream = new FileOutputStream(basePath + file.getOriginalFilename());
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 判断基础目录路径是否存在 若不存在则新建
    public void isBasePath(String basePath){
        File file = new File(basePath);
        if (!file.exists()) file.mkdirs();
    }
}

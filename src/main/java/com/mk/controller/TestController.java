package com.mk.controller;

import com.mk.common.RemoveFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RemoveFileUtils removeFileUtils;

    @GetMapping("/{name}")
    public String test1(@PathVariable("name") String name){
        removeFileUtils.removeImg(name);
        return "OK";
    }
}

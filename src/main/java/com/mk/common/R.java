package com.mk.common;

import lombok.Data;

import java.util.Map;

@Data  // 必须添加 *****
public class R {
    private Integer code;
    private String msg;
    private Object data;
    private Map map;  // 临时动态数据存储

    public R() {
    }
    public R(String msg) {
        this.msg = msg;
    }
    public R(String msg, Object data) {
        this.msg = msg;
        this.data = data;
    }
    public R(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static R ERROR(){
        return R.ERROR("操作失败-_-!");
    }
    public static R ERROR(String msg){
        return new R(0, msg, null);
    }
    public static R SUCCESS(){
        return R.SUCCESS("操作成功^_^");
    }
    public static R SUCCESS(String msg){
        return R.SUCCESS(msg, null);
    }
    public static R SUCCESS(Object data){
        return R.SUCCESS("操作成功^_^", data);
    }
    public static R SUCCESS(String msg, Object data){
        return new R(1, msg, data);
    }


}

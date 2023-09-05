package com.mk.common;


public class MyThreadLocal {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setValue(Long id){
        System.out.println("MyThread  当前线程Id:" + Thread.currentThread().getId());
        System.out.println("setValue: " + id);
        threadLocal.set(id);
    }
    public static Long getValue(){
        return threadLocal.get();
    }

}

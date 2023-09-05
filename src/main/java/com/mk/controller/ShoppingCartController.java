package com.mk.controller;

import com.mk.common.R;
import com.mk.pojo.ShoppingCart;
import com.mk.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    // 获取购物车内商品的集合
    @GetMapping("/list")
    public R listShoppingCart(){
        return R.SUCCESS(shoppingCartService.listShoppingCart());
    }

    // 添加购物车信息
    @PostMapping("/add")
    public R saveShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return R.SUCCESS(shoppingCartService.saveShoppingCart(shoppingCart));
    }

    // 移除购物车信息
    @PostMapping("/sub")
    public R removeShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return R.SUCCESS(shoppingCartService.removeShoppingCart(shoppingCart));
    }

    // 清空购物车信息
    @DeleteMapping("/clean")
    public R clearShoppingCart(){
        shoppingCartService.clearShoppingCart();
        return R.SUCCESS();
    }
}

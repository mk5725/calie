package com.mk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mk.common.R;
import com.mk.pojo.ShoppingCart;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    // 获取购物车内商品的集合
    List<ShoppingCart> listShoppingCart();
    // 添加购物车信息
    ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);
    // 移除购物车信息
    ShoppingCart removeShoppingCart(ShoppingCart shoppingCart);
    // 清空购物车信息
    void clearShoppingCart();
}

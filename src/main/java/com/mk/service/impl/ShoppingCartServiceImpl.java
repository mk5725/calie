package com.mk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.common.MyThreadLocal;
import com.mk.common.R;
import com.mk.mapper.ShoppingCartMapper;
import com.mk.pojo.ShoppingCart;
import com.mk.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    // 获取购物车信息
    @Override
    public List<ShoppingCart> listShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(ShoppingCart::getNumber);
        wrapper.eq(ShoppingCart::getUserId, MyThreadLocal.getValue()); // 当前登录用户的购物车信息
        return shoppingCartMapper.selectList(wrapper);
    }

    // 添加购物车信息
    @Override
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart){
        // 判断当前添加的信息是否存在，若存在则数量+1
        LambdaQueryWrapper<ShoppingCart> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ShoppingCart::getUserId, MyThreadLocal.getValue());
        wrapper.eq(ShoppingCart::getName, shoppingCart.getName());
        ShoppingCart selectOne = shoppingCartMapper.selectOne(wrapper);

        if (selectOne != null){
            selectOne.setNumber(selectOne.getNumber() + 1);
            shoppingCartMapper.updateById(selectOne);
            return selectOne;
        }else {
            // 设置当前用户Id, 设置基础数量（可通过公共字段自动填充完成）
            shoppingCart.setUserId(MyThreadLocal.getValue());
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
            LambdaQueryWrapper<ShoppingCart> query = Wrappers.lambdaQuery();
            query.eq(ShoppingCart::getName, shoppingCart.getName());
            return shoppingCartMapper.selectOne(query);
        }
    }
    // 移除购物车信息
    @Override
    public ShoppingCart removeShoppingCart(ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        wrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        // 移除信息
        wrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        ShoppingCart selectOne = shoppingCartMapper.selectOne(wrapper);
        if (selectOne!=null){
            if (selectOne.getNumber() > 1){
                selectOne.setNumber(selectOne.getNumber()-1);
                shoppingCartMapper.updateById(selectOne);
                return selectOne;
            }
            selectOne.setNumber(0);
            shoppingCartMapper.deleteById(selectOne.getId());
            return selectOne;
        }
        return null;
    }


    // 清空购物车信息
    @Override
    public void clearShoppingCart(){
        // 获取当前登录用户Id
        Long userId = MyThreadLocal.getValue();
        LambdaQueryWrapper<ShoppingCart> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartMapper.delete(wrapper);
    }
}

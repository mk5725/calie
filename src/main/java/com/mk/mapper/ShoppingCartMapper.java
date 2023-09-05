package com.mk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mk.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}

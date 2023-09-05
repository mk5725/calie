package com.mk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mk.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderMapper extends BaseMapper<Orders> {
}

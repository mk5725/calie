package com.mk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.common.R;
import com.mk.pojo.Orders;
import com.mk.pojo.dto.OrdersDto;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    // 保存订单信息
    void saveOrder(Orders order);
    // 查询订单信息
    Page<OrdersDto> listUserOrder(Integer pageNo, Integer pageSize);
    // 删除订单信息

    // 分页查询所有订单信息（管理端）
    Page<Orders> page(Integer pageNo, Integer pageSize, String number);

    // 修改定案状态
    void modifierOrderStatus(Orders orders);
}

package com.mk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.common.R;
import com.mk.pojo.Orders;
import com.mk.pojo.dto.OrdersDto;
import com.mk.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 保存订单信息
    @PostMapping("/submit")
    public R saveOrder(@RequestBody Orders order){
        orderService.saveOrder(order);
        return R.SUCCESS();
    }

    // 查看订单信息
    @GetMapping("/userPage")
    public R listUserOrder(@RequestParam(value = "page", required = false) Integer pageNo,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize){
        Page<OrdersDto> page = orderService.listUserOrder(pageNo, pageSize);
        return R.SUCCESS(page);
    }

    // 分页查询所有订单信息（管理端）
    @GetMapping("/page")
    public R page(@RequestParam("page") Integer pageNo,
                  @RequestParam("pageSize") Integer pageSize,
                  @RequestParam(value = "number", required = false) String number,
                  @RequestParam(value = "beginTime", required = false) Object beginTime,
                  @RequestParam(value = "endTime",required = false) Object endTime){
        Page<Orders> page = orderService.page(pageNo, pageSize, number);
        return R.SUCCESS(page);
    }

    // 修改订单状态
    @PutMapping
    public R modifierOrderStatus(@RequestBody Orders orders){
        orderService.modifierOrderStatus(orders);
        return R.SUCCESS();
    }
}

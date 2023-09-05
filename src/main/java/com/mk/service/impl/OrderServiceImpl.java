package com.mk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.common.MyThreadLocal;
import com.mk.mapper.OrderMapper;
import com.mk.pojo.*;
import com.mk.pojo.dto.OrdersDto;
import com.mk.pojo.dto.SetmealDto;
import com.mk.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    // 保存订单信息
    @Override
    @Transactional
    public void saveOrder(Orders order){
        // 设置下单用户Id
        order.setUserId(MyThreadLocal.getValue());
        // 生成订单号Id
        Long orderId = IdWorker.getId();
        order.setId(orderId);
        order.setNumber(String.valueOf(orderId));
        // 设置下单、付款时间，订单状态
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        order.setStatus(Integer.valueOf(2));

        // 获取地址信息
        AddressBook address = addressBookService.getAddressById(order.getAddressBookId());
        order.setPhone(address.getPhone());
        order.setConsignee(address.getConsignee());
        order.setAddress(address.getDetail()+"("+address.getLabel()+")");
        order.setUserName("姓名id:" + String.valueOf(order.getUserId()));

        // 计算总金额
        LambdaQueryWrapper<ShoppingCart> cartWrapper = Wrappers.lambdaQuery();
        cartWrapper.eq(ShoppingCart::getUserId, MyThreadLocal.getValue());
        List<ShoppingCart> cartList = shoppingCartService.list(cartWrapper);// 查询当前用户购物车中的所有信息

        BigDecimal bigDecimal = new BigDecimal("0");
        for (ShoppingCart cart : cartList) {
            BigDecimal amount = cart.getAmount().multiply(new BigDecimal(cart.getNumber().toString()));
            bigDecimal = bigDecimal.add(amount);
        }
        // 设置实收金额
        order.setAmount(bigDecimal);
        // 插入数据
        orderMapper.insert(order);


        // 添加订单详情信息
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : cartList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(cart.getName());
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setAmount(cart.getAmount());
            orderDetail.setDishFlavor(cart.getDishFlavor());

            if (cart.getDishId() != null){
                orderDetail.setDishId(cart.getDishId());
                Dish dish = dishService.getById(cart.getDishId());
                orderDetail.setImage(dish.getImage());

            }else {
                orderDetail.setSetmealId(cart.getSetmealId());
                SetmealDto setMeal = setmealService.getSetmealDyId(cart.getSetmealId());
                orderDetail.setImage(setMeal.getImage());
            }

            orderDetailList.add(orderDetail);
        }
        // 批量保存订单详情
        orderDetailService.saveBatch(orderDetailList);


        // 清楚购物车信息
        shoppingCartService.clearShoppingCart();
    }

    // 查询用户订单信息
    @Override
    @Transactional(readOnly = true)
    public Page<OrdersDto> listUserOrder(Integer pageNo, Integer pageSize){
        Page<Orders> page = new Page<>(pageNo, pageSize);
        Page<OrdersDto> pageDto = new Page<>();

        // 获取当前登录用户id
        Long userId = MyThreadLocal.getValue();
        // 查询用户订单信息
        LambdaQueryWrapper<Orders> orderWrapper = Wrappers.lambdaQuery();
        orderWrapper.eq(Orders::getUserId, userId);
        orderWrapper.orderByDesc(Orders::getCheckoutTime);
        orderMapper.selectPage(page, orderWrapper);

        List<Orders> ordersList = page.getRecords();
        if (ordersList.isEmpty()) return null;

        List<OrdersDto> ordersDtoList = new ArrayList<>();
        for (Orders orders : ordersList) {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(orders, ordersDto);

            // 获取订单详情
            LambdaQueryWrapper<OrderDetail> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(OrderDetail::getOrderId, orders.getId());
            List<OrderDetail> detailList = orderDetailService.list(wrapper);
            ordersDto.setOrderDetails(detailList);  // 设置订单详情

            ordersDtoList.add(ordersDto);
        }
        BeanUtils.copyProperties(page, pageDto, "records");
        pageDto.setRecords(ordersDtoList);
        return pageDto;
    }


    // 分页查询所有订单信息（管理端）
    @Override
    public Page<Orders> page(Integer pageNo, Integer pageSize, String number){
        Page<Orders> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<Orders> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Strings.isNotEmpty(number), Orders::getNumber, number);
        orderMapper.selectPage(page, wrapper);

        return page;
    }

    // 修改订单状态
    @Override
    public void modifierOrderStatus(Orders orders) {
        LambdaQueryWrapper<Orders> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Orders::getId, orders.getId());
        orderMapper.update(orders, wrapper);
    }
}

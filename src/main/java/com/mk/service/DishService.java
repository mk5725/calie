package com.mk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mk.common.R;
import com.mk.pojo.Dish;
import com.mk.pojo.dto.DishDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface DishService extends IService<Dish> {

    // 分页查询所有菜品
    IPage<Dish> getAllPage(int pageNo, int pageSize, String name);
    // 分页查询所有菜品
    IPage<DishDto> getPage(int pageNo, int pageSize, String name);

    // 新增菜品，同时新增菜品口味信息
    Boolean saveDishWithFlavor(DishDto dishDto);

    // 统计关联分类的菜品个数
    Integer getCount(Long id);

    // 根据菜品ID查询菜品信息, 以及菜品的口味信息
    DishDto getDishWithFlavor(Long id);

    // 更新菜品，同时更新菜品口味信息
    Boolean updateDishWithFlavor(DishDto dishDto);

    // 根据菜品分类id, 查询菜品信息
    List<DishDto> getDishByCategoryId(Dish dish);

    // 根据ids批量删除菜品，同时删除菜品口味信息 （图片删除）
    Boolean removeWithFlavor(List<Long> ids);
}

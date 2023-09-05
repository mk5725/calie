package com.mk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.common.CustomServiceException;
import com.mk.mapper.DishMapper;
import com.mk.pojo.Dish;
import com.mk.pojo.DishFlavor;
import com.mk.pojo.dto.DishDto;
import com.mk.service.CategoryService;
import com.mk.service.DishFlavorService;
import com.mk.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;  // 用于查询分类名字

    // 分页查询所有菜品
    @Override
    public IPage<Dish> getAllPage(int pageNo, int pageSize, String name) {
        IPage<Dish> iPage = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(name), Dish::getName, name);
        dishMapper.selectPage(iPage, wrapper);
        return iPage;
    }
//     分页查询所有菜品
    @Override
    @Transactional(readOnly = true)
    public IPage<DishDto> getPage(int pageNo, int pageSize, String name) {
        IPage<Dish> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(name), Dish::getName, name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishMapper.selectPage(page, wrapper);

        IPage<DishDto> pageDto = new Page<>();
        List<Dish> dishList = page.getRecords();
        List<DishDto> dishDtoList = new ArrayList<>();
        // 拷贝page属性
        BeanUtils.copyProperties(page, pageDto, "records");
        for (Dish dish : dishList) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            // 查询每个菜品对应分类的名字
            String categoryName = categoryService.getCategoryById(dish.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            dishDtoList.add(dishDto);  // 向集合中添加 categoryDto
        }
        pageDto.setRecords(dishDtoList);
        log.info(dishDtoList.toString());

        return pageDto;
    }

    // 新增菜品，同时添加菜品口味信息
    @Override
    @Transactional   // 声明事务
    public Boolean saveDishWithFlavor(DishDto dishDto) {
//        Dish dish = dishDto;
        // 新增菜品信息
        dishMapper.insert(dishDto);    // 向上转型
        // 获取菜品id
        Long dishId = dishDto.getId();
        // 设置菜品口味中的菜品Id
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
        // 批量插入菜品口味信息
        dishFlavorService.saveBatch(flavors);
        return true;
    }


    // 更新菜品，同时更新菜品口味信息
    @Override
    @Transactional
    public Boolean updateDishWithFlavor(DishDto dishDto) {
        // 更新菜品信息
        dishMapper.updateById(dishDto);
//        dishMapper.updateById((Dish) dishDto);
        // 获取菜品id
        Long dishId = dishDto.getId();

        // 先删除菜品口味中当前菜品的口味信息，在重新新增口味信息
        LambdaQueryWrapper<DishFlavor> flavorWrapper = new LambdaQueryWrapper<>();
        flavorWrapper.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(flavorWrapper);
        // 重新设置菜品口味中的菜品Id
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
//        if (true) throw new CustomServiceException("测试抛出异常");
        // 批量插入菜品口味信息
        dishFlavorService.saveBatch(flavors);
        return true;
    }

    // 统计 分类信息关联商品的个数
    @Override
    public Integer getCount(Long id){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, id);
        Integer count = dishMapper.selectCount(wrapper);
        log.info("分类信息关联商品的个数：{}", count);
        return count;
    }

    // 根据菜品ID查询菜品信息, 以及菜品的口味信息
    @Override
    public DishDto getDishWithFlavor(Long id) {
        // 根据菜品Id查询菜品口味信息
        LambdaQueryWrapper<DishFlavor> flavorWrapper = new LambdaQueryWrapper<>();
        flavorWrapper.eq(Strings.isNotEmpty(String.valueOf(id)),
                DishFlavor::getDishId, id);
        List<DishFlavor> flavorList = dishFlavorService.list(flavorWrapper);

        // 根据菜品Id查询菜品信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        log.info(dishDto.toString());

        dishDto.setFlavors(flavorList);     // 设置菜品口味

        return dishDto;
    }

    // 根据菜品分类id, 查询菜品信息
    @Override
    @Transactional(readOnly = true)
    public List<DishDto> getDishByCategoryId(Dish dish){
        List<DishDto> arrayList = new ArrayList<>();
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        // 添加查询条件 分来ID
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        // 过滤停售的菜品
        wrapper.eq(Dish::getStatus, (Long)1L);
        // 添加排序条件
        wrapper.orderByAsc(Dish::getSort);
        List<Dish> list = dishMapper.selectList(wrapper);

        list.forEach(d -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(d, dishDto);  // 拷贝对象属性
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
            List<DishFlavor> flavorList = dishFlavorService.list(queryWrapper);  //根据菜品Id查询口味信息
            dishDto.setFlavors(flavorList);
            arrayList.add(dishDto);
        });

        return arrayList;
    }

    // 根据ids批量删除菜品，同时删除菜品口味信息 （图片删除）
    @Override
    @Transactional
    public Boolean removeWithFlavor(List<Long> ids) {
        // 批量删除菜品信息
        dishMapper.deleteBatchIds(ids);

        // 删除菜品口味相关信息
        for (Long id : ids) {
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(wrapper);
        }
        return true;
    }
}

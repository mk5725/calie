package com.mk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.common.CustomServiceException;
import com.mk.common.R;
import com.mk.common.RemoveFileUtils;
import com.mk.mapper.SetmealMapper;
import com.mk.pojo.Setmeal;
import com.mk.pojo.SetmealDish;
import com.mk.pojo.dto.SetmealDto;
import com.mk.service.CategoryService;
import com.mk.service.SetmealDishService;
import com.mk.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;  // 用于查询分类名称
    @Autowired
    private RemoveFileUtils removeFileUtils;  // 用于删除套餐图片

    // 保存套餐信息，同时保存套餐菜品信息。
    @Override
    @Transactional
    public Boolean saveSetmealWithDish(SetmealDto setmealDto) {
        // 保存套餐信息
        setmealMapper.insert(setmealDto);
        // 保存套餐菜品信息
        Long setmealId = setmealDto.getId();
        List<SetmealDish> list = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : list) {
            setmealDish.setSetmealId(setmealId);  // 设置套餐的ID
        }
        // 批量保存
        setmealDishService.saveBatch(list);
        return true;
    }

    @Override
    // 统计 分类信息关联套餐的个数
    public Integer getCount(Long id) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId, id);
        Integer count = setmealMapper.selectCount(wrapper);
        log.info("分类信息关联套餐的个数：{}", count);
        return count;
    }

    // 分页查询所有套餐信息(包括套餐分类名称)
    @Override
    @Transactional(readOnly = true)
    public Page<SetmealDto> getAllPage(int pageNo, int pageSize, String name) {

//        if (name != null) name = name.trim();

        Page<Setmeal> setmealPage = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name.trim());
        setmealMapper.selectPage(setmealPage, wrapper);

        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");

        List<SetmealDto> setmealDtoList = new ArrayList<>();
        List<Setmeal> setmealList = setmealPage.getRecords();
        for (Setmeal setmeal : setmealList) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);   //拷贝属性
            String categoryName = categoryService.getCategoryById(setmeal.getCategoryId()).getName();
            setmealDto.setCategoryName(categoryName);
            setmealDtoList.add(setmealDto);    // 向集合中添加setmealDto对象
        }
        // 向分页结果集records中添加最终数据
        setmealDtoPage.setRecords(setmealDtoList);

        return setmealDtoPage;
    }


    // 修改套餐信息，同时保修改餐菜品信息。
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Boolean updateWithDish(SetmealDto setmealDto){
        String oldImage = setmealMapper.selectById(setmealDto.getId()).getImage();
        String newImage = setmealDto.getImage();
        // 修改套餐信息
        setmealMapper.updateById(setmealDto);

        // 修改套餐菜品的信息(先删除，再添加)
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(wrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);

        // 删除套餐图片
        if (Strings.isNotEmpty(oldImage) && !oldImage.equals(newImage)){
            removeFileUtils.removeImg(oldImage);
        }
        return true;
    }


    // 删除套餐信息，同时删除套餐菜品信息。
    @Override
    @Transactional
    public Boolean removeWithDish(List<Long> ids){
        // 删除套餐图片
        List<String> imgList = new ArrayList<>();
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.selectById(id);
            if (setmeal.getStatus() != 0){
                throw new CustomServiceException("删除失败，当前有正在起售中的套餐 ！");
            }
            imgList.add(setmeal.getImage());
        }
        // 删除套餐图片
        for (String img : imgList) {
            removeFileUtils.removeImg(img);
        }
        // 删除套餐信息
        setmealMapper.deleteBatchIds(ids);
        // 删除套餐菜品信息
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(wrapper);
        return true;
    }

    // 获取套餐信息、菜品信息，回显数据。
    @Override
    @Transactional(readOnly = true)
    public SetmealDto getSetmealDyId(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDto setmealDto = new SetmealDto();
        // 拷贝对象属性
        BeanUtils.copyProperties(setmeal,setmealDto, "setmealDishes");

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishService.list(wrapper);
        // 将查询的菜品信息存入setmealDto
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    // 启停售套餐信息
    @Override
    @Transactional
    public Boolean updateStatus(Integer status, List<Long> ids) {

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId, ids);
        List<Setmeal> setmealList = setmealMapper.selectList(wrapper);
        for (Setmeal setmeal : setmealList) {
            setmeal.setStatus(status);
            setmealMapper.updateById(setmeal);
        }
        return null;
    }

    // 根据分类Id查询套餐信息
    @Override
    public List<Setmeal> listByCategoryId(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
        wrapper.eq(Setmeal::getStatus, setmeal.getStatus());  // 过滤停售
        return setmealMapper.selectList(wrapper);
    }
}

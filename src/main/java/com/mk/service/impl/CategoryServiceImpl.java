package com.mk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.common.CustomServiceException;
import com.mk.mapper.CategoryMapper;
import com.mk.pojo.Category;
import com.mk.pojo.dto.DishDto;
import com.mk.service.CategoryService;
import com.mk.service.DishService;
import com.mk.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    // 分页查询所有分类信息
    @Override
    public IPage<Category> getAllPage(int pageNo, int pageSize) {
        IPage<Category> iPage = new Page<>(pageNo, pageSize);
        // 添加排序条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        categoryMapper.selectPage(iPage, wrapper);
        return iPage;
    }

    // 查询分类信息 ById
    @Override
    public Category getCategoryById(Long id) {
        return categoryMapper.selectById(id);
    }

    // 新增分页信息
    @Override
    public Boolean saveCateGory(Category category) {
        return categoryMapper.insert(category) > 0;
    }

    // 删除分类信息 ById
    @Override
    public Boolean removeCategoryById(Long id) {

        // 删除前检查是否关联商品、套餐
        if (dishService.getCount(id) > 0){
            throw new CustomServiceException("该分类有关联商品信息，删除失败！");
        }
        if (setmealService.getCount(id) > 0){
            throw new CustomServiceException("该分类有关联套餐信息，删除失败！");
        }
        // 都没有关联，删除分类信息
        return categoryMapper.deleteById(id) > 0;
    }

    // 修改分类信息 ById
    @Override
    public Boolean modifierCategoryById(Category category) {
        return categoryMapper.updateById(category) > 0;
    }

}

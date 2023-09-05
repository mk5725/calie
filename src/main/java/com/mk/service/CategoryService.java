package com.mk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mk.pojo.Category;
import com.mk.pojo.dto.DishDto;

public interface CategoryService extends IService<Category> {

    // 分页查询所有分类信息
    IPage<Category> getAllPage(int pageNo, int pageSize);
    // 查询分页信息 ById
    Category getCategoryById(Long id);
    // 新增分页信息
    Boolean saveCateGory(Category category);
    // 删除分类信息 ById
    Boolean removeCategoryById(Long id);
    // 修改分类信息 ById
    Boolean modifierCategoryById(Category category);

}

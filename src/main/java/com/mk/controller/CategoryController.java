package com.mk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mk.common.R;
import com.mk.pojo.Category;
import com.mk.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 分页查询所有分类信息
    @GetMapping("/page")
    public R getAllPage(@RequestParam("page")int pageNo, @RequestParam("pageSize")int pageSize){
        return R.SUCCESS(categoryService.getAllPage(pageNo, pageSize));
    }
    // 查询分类信息 ById
    @GetMapping("/{id}")
    public R getCategoryById(@PathVariable("id") Long id){
        return R.SUCCESS(categoryService.getCategoryById(id));
    }

    // 新增分类信息
    @PostMapping
    public R save(@RequestBody Category category){
        if (categoryService.saveCateGory(category))
            return R.SUCCESS("新增分类信息成功^_^");
        return R.ERROR("新增分类信息失败-_-!");
    }
    // 删除分类信息 ById
    @DeleteMapping
    public R remove(@RequestParam("ids") Long id){
        if (categoryService.removeCategoryById(id))
            return R.SUCCESS("删除分类信息成功^_^");
        return R.ERROR("删除分类信息失败-_-!");
    }
    // 修改分类信息 ById
    @PutMapping
    public R modifier(@RequestBody Category category){
        if (categoryService.modifierCategoryById(category))
            return R.SUCCESS("修改分类信息成功^_^");
        return R.ERROR("修改分类信息失败-_-!");
    }
    // 查询分类信息的菜品分类
    @GetMapping("/list")
    public R getType(@RequestParam(value = "type", required = false) String type){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(type), Category::getType, type);
        wrapper.orderByAsc(Category::getSort); // 添加排序条件
        List<Category> list = categoryService.list(wrapper);
        return R.SUCCESS(list);
    }



}

package com.mk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.common.R;
import com.mk.pojo.Setmeal;
import com.mk.pojo.dto.SetmealDto;
import com.mk.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    // 修改套餐信息，同时保修改餐菜品信息。
    @PutMapping
    public R updateWithDish(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.SUCCESS();
    }

    // 获取套餐信息、菜品信息，回显数据。
    @GetMapping("/{id}")
    public R getSetmeslById(@PathVariable("id") Long id){
        return R.SUCCESS(setmealService.getSetmealDyId(id));
    }

    // 删除套餐信息，同时删除套餐菜品信息。
    @DeleteMapping
    public R removeWithDish(@RequestParam("ids") List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.SUCCESS();
    }
    // 启停售套餐信息
    @PostMapping("/status/{status}")
    public R updateStatus(@PathVariable("status") Integer status,
                          @RequestParam("ids") List<Long> ids){
        setmealService.updateStatus(status, ids);
        return R.SUCCESS();
    }

    // 保存套餐信息，同时保存套餐菜品信息。
    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveSetmealWithDish(setmealDto);
        return R.SUCCESS();
    }

    // 分页查询所有套餐信息(包括套餐分类名称)
    @GetMapping("/page")
    public R getAllPage(@RequestParam("page") int pageNo, @RequestParam("pageSize") int pageSize,
                        @RequestParam(value = "name", required = false, defaultValue = "") String name){
        return R.SUCCESS(setmealService.getAllPage(pageNo, pageSize, name));
    }

    // 根据分类Id查询套餐信息
    @GetMapping("/list")
    public R listByCategoryId(Setmeal setmeal){
        return R.SUCCESS(setmealService.listByCategoryId(setmeal));
    }
}

package com.mk.controller;

import com.mk.common.R;
import com.mk.pojo.Dish;
import com.mk.pojo.dto.DishDto;
import com.mk.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    // 上传文件删除
    // 分页查询所有菜品
    @GetMapping("/page")
    public R getAllPage(@RequestParam("page") int pageNo,
                        @RequestParam("pageSize") int pageSize,
                        @RequestParam(value = "name", required = false) String name) {
//        return R.SUCCESS(dishService.getPage(pageNo, pageSize, name));
        return R.SUCCESS(dishService.getPage(pageNo, pageSize, name));
    }

    // 新增菜品，同时添加菜品口味信息
    @PostMapping
    public R saveDishWithFlavor(@RequestBody DishDto dishDto) {
        if (dishService.saveDishWithFlavor(dishDto))
            return R.SUCCESS("新增菜品成功^_^");
        return R.ERROR("新增菜品失败-_-!");
    }
    // 更新菜品，同时更新菜品口味信息
    @PutMapping
    public R updateDishWithFlavor(@RequestBody DishDto dishDto){
        dishService.updateDishWithFlavor(dishDto);
        return R.SUCCESS();
    }

    // 修改菜品 销售状态
    @PostMapping("/status/{status}")
    public R modifierStatus(@PathVariable("status") Integer status,
                            @RequestParam("ids") List<Long> ids){
        // 根据ids批量获取菜品集合
        List<Dish> dishList = dishService.listByIds(ids);
        for (Dish dish : dishList) {
            dish.setStatus(status);  // 设置菜品状态
        }
        // 批量更新
        dishService.updateBatchById(dishList);
        return R.SUCCESS();
    }
    // 根据菜品ID查询菜品信息, 以及菜品的口味信息
    @GetMapping("/{id}")
    public R getDishById(@PathVariable Long id){
        return R.SUCCESS(dishService.getDishWithFlavor(id));
    }

    // 根据菜品分类id, 查询菜品信息
    @GetMapping("/list")
    public R getDishByCategoryId(Dish dish){
        return R.SUCCESS(dishService.getDishByCategoryId(dish));
    }

    // 根据ids批量删除菜品，同时删除菜品口味信息 （图片删除）
    @DeleteMapping
    public R removeWithFlavor(@RequestParam("ids") List<Long> ids){
        dishService.removeWithFlavor(ids);
        return R.SUCCESS();
    }
}

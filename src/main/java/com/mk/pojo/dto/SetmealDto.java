package com.mk.pojo.dto;

import com.mk.pojo.Setmeal;
import com.mk.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

// 套餐菜品关联表
@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

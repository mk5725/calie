package com.mk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.common.R;
import com.mk.pojo.Setmeal;
import com.mk.pojo.dto.SetmealDto;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


public interface SetmealService {

    // 分页查询所有套餐信息(包括套餐分类名称)
    Page<SetmealDto> getAllPage(int pageNo, int pageSize, String name);

    Boolean saveSetmealWithDish(SetmealDto setmealDto);

    Integer getCount(Long id);

    // 修改套餐信息，同时保修改餐菜品信息。
    Boolean updateWithDish(SetmealDto setmealDto);
    // 删除套餐信息，同时删除套餐菜品信息。
    Boolean removeWithDish(List<Long> ids);
    // 获取套餐信息、菜品信息，回显数据。
    SetmealDto getSetmealDyId(Long id);
    // 启停售套餐信息
    Boolean updateStatus(Integer status, List<Long> ids);
    // 根据分类Id查询套餐信息
    List<Setmeal> listByCategoryId(Setmeal setmeal);
}

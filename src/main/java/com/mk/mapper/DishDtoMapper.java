package com.mk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mk.pojo.dto.DishDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DishDtoMapper {
    IPage<DishDto> page(int pageNo, int pageSize, String name);
}

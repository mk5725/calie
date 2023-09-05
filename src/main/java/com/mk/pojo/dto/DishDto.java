package com.mk.pojo.dto;

import com.mk.pojo.Dish;
import com.mk.pojo.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish implements Serializable {
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

    @Override
    public String toString() {
        return super.toString() + "DishDto{" +
                "flavors=" + flavors +
                ", categoryName='" + categoryName + '\'' +
                ", copies=" + copies +
                '}';
    }
}

package com.mk.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;

@Repository
@Data
public class Category implements Serializable {

    private static final Long serverVersionUID = 1L;

    private Long id;
    private Integer type;
    private String name;
    private Integer sort;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    // 是否删除
    @TableField(exist = false)
    private Integer isDeleted;
}

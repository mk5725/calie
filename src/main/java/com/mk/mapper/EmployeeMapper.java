package com.mk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mk.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("select * from employee where username = #{userName}")
    Employee selectEmployeeByName(String userName);

//    Boolean insertEmployee(Employee employee);

}

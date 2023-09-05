package com.mk.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.common.MyThreadLocal;
import com.mk.mapper.EmployeeMapper;
import com.mk.pojo.Employee;
import com.mk.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee getEmployeeByUserName(String userName) {
//        return employeeMapper.selectEmployeeByName(userName);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strings.isNotBlank(userName), Employee::getUsername, userName);
        Employee employee = employeeMapper.selectOne(wrapper);
        // 判断用户名是否存在
        // 判断用户是否启用
        // 判断密码是否正确
        return employee;
    }

    @Override
    public Boolean saveEmployee(Employee employee) {
        // 设置员工默认登录密码、创建用户ID
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateUser(1L);
//        employee.setUpdateUser(1L);

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        return  employeeMapper.insert(employee) > 0;
    }

    // 分页查询所员工信息
    @Override
    public IPage<Employee> getAllEmployee(int pageNo, int pageSize, String name){

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        // 添加模查询条件
        wrapper.like(Strings.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件 降序
        wrapper.orderByDesc(Employee::getCreateTime);
        IPage<Employee> iPage = new Page<>(pageNo, pageSize);
        employeeMapper.selectPage(iPage, wrapper);
        return iPage;
    }

    // 根据ID查询员工信息
    @Override
    public Employee getEmployeeById(Long id) {
        return employeeMapper.selectById(id);
    }
    // 根据ID更新员工信息
    @Override
    public Boolean modifierEmployeeById(Employee employee) {
        Employee oldEmployee = employeeMapper.selectById(employee.getId());
        // 更新员工状态
        if (oldEmployee.getStatus() != employee.getStatus()){
            oldEmployee.setStatus(employee.getStatus());
            // 更新 更新用户
            oldEmployee.setUpdateUser(MyThreadLocal.getValue());
            return employeeMapper.updateById(oldEmployee) > 0;
        }
        employee.setUpdateUser(MyThreadLocal.getValue());
        return employeeMapper.updateById(employee) > 0;
    }
}


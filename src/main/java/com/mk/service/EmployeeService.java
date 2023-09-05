package com.mk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mk.common.R;
import com.mk.pojo.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {

    Employee getEmployeeByUserName(String userName);

    Boolean saveEmployee(Employee employee);

    IPage<Employee> getAllEmployee(int pageNo, int pageSize, String name);

    Employee getEmployeeById(Long id);

    Boolean modifierEmployeeById(Employee employee);

}

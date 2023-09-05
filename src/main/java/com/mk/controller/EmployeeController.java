package com.mk.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mk.common.MyThreadLocal;
import com.mk.common.R;
import com.mk.pojo.Employee;
import com.mk.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    // 登录验证
    @PostMapping("/login")
    public R login(@RequestBody Employee ResEmployee, HttpServletRequest request){

        Employee employee = employeeService.getEmployeeByUserName(ResEmployee.getUsername());

        // 判断用户名是否存在
        if (employee == null) return R.ERROR("用户不存在！");
        // 判断用户是否启用
        if (!(Integer.valueOf(1).equals(employee.getStatus()))) return R.ERROR("用户已冻结！");

        // 判断密码是否正确
          // 判断前端入参password是否为空
        if (Strings.isNotBlank(ResEmployee.getPassword())){
            String str = DigestUtils.md5DigestAsHex(ResEmployee.getPassword().getBytes());
            if (!str.equals(employee.getPassword())){
                return R.ERROR("密码错误！");
            }else {
//                request.getSession().setAttribute("user"+String.valueOf(Thread.currentThread().getId()), employee.getId());
                request.getSession().setAttribute("employee",employee.getId());
                return R.SUCCESS("登录成功！", employee);
            }
        }
        return R.ERROR();
    }

    // 退出登录
    @PostMapping("/logout")
    public R logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        request.getSession().removeAttribute("user");
        return R.SUCCESS("退出成功");
    }

    // 添加员工信息
    @PostMapping
    public R addEmployee(@RequestBody Employee employee){
        if (employeeService.saveEmployee(employee))
            return R.SUCCESS("添加员工成功^_^");
        return R.ERROR("添加员工失败-_-!");
    }


    // 分页查询所有员工信息
    @GetMapping("/page")
    public R getAllEmployee(@RequestParam(value = "page", defaultValue = "1") int pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                            @RequestParam(value = "name", required = false) String name){
        log.info("EmployeeController.getAll() ---  pageNo: {}，pageSize: {}，name: {}",pageNo,pageSize,name);
        IPage<Employee> iPage = employeeService.getAllEmployee(pageNo, pageSize, name);
        return R.SUCCESS(iPage);
    }


    {
    }

    // 根据ID查询员工信息
    @GetMapping("/{id}")
    public R getEmployeeById(@PathVariable("id") Long id) {
        return R.SUCCESS(employeeService.getEmployeeById(id));
    }
    // 根据ID更新员工信息
    @PutMapping
    public R updateEmployeeById(@RequestBody Employee employee) {
//        log.info("updateEmployeeById线性id: {}", Thread.currentThread().getId());
        if (employeeService.modifierEmployeeById(employee))
            return R.SUCCESS("更新成功^_^");
        return R.ERROR();
    }

}

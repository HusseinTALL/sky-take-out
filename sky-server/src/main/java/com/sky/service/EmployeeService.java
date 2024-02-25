package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

     /**
     *  新增员工
     * @param employeeDTO
     */
    void add(EmployeeDTO employeeDTO);

    /**
     *  员工分页查询
     * @param dto
     * @return
     */
    PageResult page(EmployeePageQueryDTO dto);

    /**
     *  更新员工状态
     * @param status
     * @param id
     */
    void updateStatus(Integer status,Long id);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Employee fingById(Long id);

    /**
     * 更新员工信息
     * @param dto
     */
    void update(EmployeeDTO dto);
}

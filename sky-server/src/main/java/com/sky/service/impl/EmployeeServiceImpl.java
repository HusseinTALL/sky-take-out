package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

//    @Autowired
//    private HttpServletRequest request;
    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());



        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void add(EmployeeDTO employeeDTO) {
        System.out.printf("service的 线程", Thread.currentThread().getName());
        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setStatus(1);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

//        String token = request.getHeader("token");
//        Claims claims = JwtUtil.parseJWT("itcast", token);
//        Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
        Long currentUser = BaseContext.getCurrentId();
        employee.setCreateUser(currentUser);
        employee.setUpdateUser(currentUser);
        employee.setPassword("123456");

        employeeMapper.add(employee);
    }

    /**
     * 员工分页查询
     *
     * @param dto
     * @return
     */
    @Override
    public PageResult page(EmployeePageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(),dto.getPageSize());

        Page<Employee> p = employeeMapper.page(dto);


        return new PageResult(p.getTotal(),p.getResult());
    }

    /**
     * 更新员工状态
     *
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
//        Employee e = new Employee();
//        e.setStatus(status);
//        e.setId(id);
        Employee e = Employee.builder().status(status).id(id).build();

        employeeMapper.update(e);

    }

    /**
     * 根据id查询员工
     *
     * @param id
     * @return
     */
    @Override
    public Employee fingById(Long id) {
        Employee employee = employeeMapper.findById(id);
        if(employee==null){
            return null;
        }
        employee.setPassword("****");
        return employee;
    }

    /**
     * 更新员工信息
     *
     * @param dto
     */
    @Override
    public void update(EmployeeDTO dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto,employee);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }

}

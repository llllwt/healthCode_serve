package com.noodles.healthycode.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.noodles.healthycode.common.lang.Result;
import com.noodles.healthycode.entity.Manager;
import com.noodles.healthycode.entity.Student;
import com.noodles.healthycode.entity.Teacher;
import com.noodles.healthycode.service.ManagerService;
import com.noodles.healthycode.service.StudentService;
import com.noodles.healthycode.service.TeacherService;
import com.noodles.healthycode.util.JwtUtils;
import io.jsonwebtoken.lang.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    @Autowired
    StudentService studentService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    ManagerService managerService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/student_login")
    public Result login(@Validated @RequestBody Student student, HttpServletResponse response){

        Student student1= studentService.getOne(new QueryWrapper<Student>().eq("id", student.getId()));
        if(student1 == null){
            return Result.fail("用户不存在");
        }

        if(!student1.getPassword().equals(student.getPassword()) ){
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(Long.parseLong(student1.getId()));

        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.succ(MapUtil.builder()
                .put("id", student1.getId())
                .put("name", student1.getName())
                .map()
        );
    }

    @PostMapping("/teacher_login")
    public Result login(@Validated @RequestBody Teacher teacher, HttpServletResponse response){

        Teacher teacher1= teacherService.getOne(new QueryWrapper<Teacher>().eq("id", teacher.getId()));
        if(teacher1 == null){
            return Result.fail("用户不存在");
        }

        if(!teacher1.getPassword().equals(teacher.getPassword()) ){
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(Long.parseLong(teacher1.getId()));

        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.succ(MapUtil.builder()
                .put("id", teacher1.getId())
                .put("name", teacher1.getName())
                .map()
        );
    }

    @PostMapping("/admin_login")
    public Result login(@Validated @RequestBody Manager manager, HttpServletResponse response){

        Manager manager1= managerService.getOne(new QueryWrapper<Manager>().eq("id", manager.getId()));
        if(manager1 == null){
            return Result.fail("用户不存在");
        }

        if(!manager1.getPassword().equals(manager.getPassword()) ){
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(Long.parseLong(manager1.getId()));

        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.succ(MapUtil.builder()
                .put("id", manager1.getId())
                .map()
        );
    }


}

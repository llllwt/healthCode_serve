package com.noodles.healthycode.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.noodles.healthycode.common.lang.Result;
import com.noodles.healthycode.entity.Student;
import com.noodles.healthycode.entity.Teacher;
import com.noodles.healthycode.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

}

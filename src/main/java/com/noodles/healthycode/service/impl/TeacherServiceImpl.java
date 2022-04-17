package com.noodles.healthycode.service.impl;

import com.noodles.healthycode.entity.Teacher;
import com.noodles.healthycode.mapper.TeacherMapper;
import com.noodles.healthycode.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

}

package com.noodles.healthycode.service.impl;

import com.noodles.healthycode.entity.Student;
import com.noodles.healthycode.mapper.StudentMapper;
import com.noodles.healthycode.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2022-03-21
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}

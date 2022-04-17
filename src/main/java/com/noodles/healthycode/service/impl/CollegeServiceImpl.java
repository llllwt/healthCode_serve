package com.noodles.healthycode.service.impl;

import com.noodles.healthycode.entity.College;
import com.noodles.healthycode.mapper.CollegeMapper;
import com.noodles.healthycode.service.CollegeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {

}

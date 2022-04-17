package com.noodles.healthycode.service.impl;

import com.noodles.healthycode.entity.Major;
import com.noodles.healthycode.mapper.MajorMapper;
import com.noodles.healthycode.service.MajorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {

}

package com.noodles.healthycode.service.impl;

import com.noodles.healthycode.entity.Healthycode;
import com.noodles.healthycode.mapper.HealthycodeMapper;
import com.noodles.healthycode.service.HealthycodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class HealthycodeServiceImpl extends ServiceImpl<HealthycodeMapper, Healthycode> implements HealthycodeService {

}

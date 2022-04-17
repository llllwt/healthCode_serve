package com.noodles.healthycode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.noodles.healthycode.entity.Backapply;
import com.noodles.healthycode.mapper.BackapplyMapper;
import com.noodles.healthycode.service.BackapplyService;
import org.springframework.stereotype.Service;

@Service
public class BackapplyServiceImpl extends ServiceImpl<BackapplyMapper, Backapply> implements BackapplyService {
}

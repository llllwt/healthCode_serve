package com.noodles.healthycode.service.impl;

import com.noodles.healthycode.entity.Manager;
import com.noodles.healthycode.mapper.ManagerMapper;
import com.noodles.healthycode.service.ManagerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements ManagerService {

}

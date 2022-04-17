package com.noodles.healthycode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.noodles.healthycode.entity.Ioapply;
import com.noodles.healthycode.mapper.IoapplyMapper;
import com.noodles.healthycode.service.IoapplyService;
import org.springframework.stereotype.Service;

@Service
public class IoapplyServiceImpl extends ServiceImpl<IoapplyMapper, Ioapply> implements IoapplyService {
}

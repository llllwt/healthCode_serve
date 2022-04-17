package com.noodles.healthycode.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Backapply implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId
    private String aid;

    private String address;

    private String time;

    private String owner;

    private String state;

    private String pass;

    private String teach;
}

package com.noodles.healthycode.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Ioapply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String oid;

    private String address;

    private String time;

    private String owner;

    private String state;

    private String pass;

    private String teach;
}

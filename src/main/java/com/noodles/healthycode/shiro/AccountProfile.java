package com.noodles.healthycode.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {

    private String id;

    private String idcard;

    private String name;
}

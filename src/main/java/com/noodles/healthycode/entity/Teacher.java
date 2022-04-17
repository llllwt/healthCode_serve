package com.noodles.healthycode.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String idcard;

    private String name;

    private String password;

    private Integer college;

    private String healthycode;


}

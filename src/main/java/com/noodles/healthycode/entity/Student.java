package com.noodles.healthycode.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    private String idcard;

    private String name;

    private String password;

    private Integer classes;

    private String healthycode;

    private Integer major;

    private Integer grade;

    private  String lasttime;

    private Integer instance;

}

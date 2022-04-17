package com.noodles.healthycode.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private Integer root;

    private String password;

    private String teacher;

    private Integer college;


}

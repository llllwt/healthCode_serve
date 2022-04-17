package com.noodles.healthycode.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class College implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotBlank(message = "学院不能为空")
    private String name;


}

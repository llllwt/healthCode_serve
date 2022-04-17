package com.noodles.healthycode.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Classes implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;


}

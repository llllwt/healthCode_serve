package com.noodles.healthycode.entity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Healthyinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String time;

    private String address;

    private String owner;


}

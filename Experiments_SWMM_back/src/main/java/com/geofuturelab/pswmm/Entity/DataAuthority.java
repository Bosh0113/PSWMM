package com.geofuturelab.pswmm.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @Author: David.Xiao
 * @Date: 2019/12/18 17:49
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
public enum DataAuthority {

    PUBLIC("public"),
    PRIVATE("private");

    public String value;
}

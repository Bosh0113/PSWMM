package com.geofuturelab.pswmm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: wangming
 * @Date: 2020-06-09 10:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RptDataDTO {
    String name;
    List<String> properties;
}

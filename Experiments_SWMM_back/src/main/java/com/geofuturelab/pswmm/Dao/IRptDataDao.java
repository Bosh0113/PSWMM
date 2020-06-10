package com.geofuturelab.pswmm.Dao;

import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Entity.RptData;

import java.io.IOException;
import java.util.List;

public interface IRptDataDao {
    RptData readRptFile(String fileName) throws IOException;
    JSONObject rpt2Json(RptData rptData);
    RptData readRptFileByProperties(String fileName, List<String> properties)throws IOException;
}

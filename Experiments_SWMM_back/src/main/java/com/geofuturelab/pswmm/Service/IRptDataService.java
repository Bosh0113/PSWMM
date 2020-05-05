package com.geofuturelab.pswmm.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface IRptDataService {
    JSONObject parseRptData(String name);
    JSONArray extractRptData(String rptName, String attriName, String inpName);
    JSONObject timeSeriesPlot(String objType, String objName, String variable, String rptName);
}

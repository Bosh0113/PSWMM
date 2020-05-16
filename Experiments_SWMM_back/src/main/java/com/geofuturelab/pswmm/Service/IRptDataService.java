package com.geofuturelab.pswmm.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface IRptDataService {
    JSONObject parseRptData(String name, HttpServletRequest request);
//    JSONArray extractRptData(String inpName, String rptName, String attriName, HttpServletRequest request);
    JSONArray floodingNodes(String inpName, String rptName, HttpServletRequest request);
    JSONObject timeSeriesPlot(String objType, String objName, String variable, String inpName, String rptName, HttpServletRequest request);
}

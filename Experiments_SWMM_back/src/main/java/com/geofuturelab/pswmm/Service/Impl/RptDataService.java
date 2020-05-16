package com.geofuturelab.pswmm.Service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.geofuturelab.pswmm.Bean.ResponseCode;
import com.geofuturelab.pswmm.Dao.IInpDataDao;
import com.geofuturelab.pswmm.Dao.IRptDataDao;
import com.geofuturelab.pswmm.Entity.InpData;
import com.geofuturelab.pswmm.Entity.RptData;
import com.geofuturelab.pswmm.Exception.RequestException;
import com.geofuturelab.pswmm.Service.IRptDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RptDataService implements IRptDataService {

    @Resource(name = "rptDataDao")
    IRptDataDao rptDataDao;

    @Resource(name = "inpDataDao")
    IInpDataDao inpDataDao;

    @Override
    public JSONObject parseRptData(String name, HttpServletRequest request) {
        RptData rptData;
        JSONObject jo;
        String servicePath =request.getSession().getServletContext().getRealPath("/")+"data/";
        try {
            rptData = rptDataDao.readRptFile(servicePath + name + ".rpt");
            jo = rptDataDao.rpt2Json(rptData);
        } catch (IOException e) {
            throw  RequestException.fail(ResponseCode.FAIL);
        }
        return jo;
    }

    @Override
    public JSONArray floodingNodes(String inpName, String rptName, HttpServletRequest request) {
        RptData rptData;
        JSONArray arr = new JSONArray();
        InpData inpData;
        try {
            String servicePath =request.getSession().getServletContext().getRealPath("/")+"data/";
            inpData = inpDataDao.readInpFile(servicePath + inpName + ".inp");
            rptData = rptDataDao.readRptFile(servicePath + rptName + ".rpt");
            for (int j = 0; j < inpData.getCoordinates().size(); j++) {
                InpData.Coordinate coord = inpData.getCoordinates().get(j);
                String objName = coord.getNode();

                List<RptData.NodeResult> nodeResults = rptData.getNodeResultsMap().get(objName);
                if (null != nodeResults)
                {
                    JSONObject jo = new JSONObject();
                    List<JSONObject> objValues = new ArrayList<>();
                    String[] objCoordArr = new String[]{String.valueOf(coord.getX_coord()),String.valueOf(coord.getY_coord())};
                    jo.put("name",objName);
                    jo.put("coord",objCoordArr);
                    for (int i = 0; i < nodeResults.size(); i++) {
                        String time = nodeResults.get(i).getTime();
                        String value = nodeResults.get(i).getFlooding();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("time",time);
                        jsonObject.put("flooding",value);
                        objValues.add(jsonObject);
                    }
                    jo.put("data",objValues);
                    arr.add(jo);
                }

            }
        } catch (IOException e) {
        throw  RequestException.fail(ResponseCode.FAIL);
        }
        return arr;
    }

    @Override
    public JSONObject timeSeriesPlot(String objType, String objName, String variable, String inpName, String rptName, HttpServletRequest request){
        JSONObject result = new JSONObject();
        result.put("objName", objName);
        result.put("objType", objType);
        result.put("variable", variable);
        try {
            String servicePath =request.getSession().getServletContext().getRealPath("/")+"data/";
            InpData inpData = inpDataDao.readInpFile(servicePath + inpName + ".inp");
            List<InpData.Raingage> raingages = inpData.getRaingages();
            List<InpData.Timeseries> timeseries = inpData.getTimeseries();
            List<JSONObject> objRains = new ArrayList<>();
            for(InpData.Raingage raingage: raingages){
                JSONObject raingageObj = new JSONObject();
                raingageObj.put("name", raingage.getName());
                for(InpData.Timeseries timeseries1: timeseries){
                    if (timeseries1.getName().equals(raingage.getTimeseries())){
                        raingageObj.put("timeSeries", timeseries1.getDateTimeValues());
                        break;
                    }
                }
                objRains.add(raingageObj);
            }
            result.put("raingages",objRains);
            List<JSONObject> objTimeVariable = new ArrayList<>();
            RptData rptData = rptDataDao.readRptFile(servicePath + rptName + ".rpt");
            switch (objType){
                case "Node":{
                    List<RptData.NodeResult> nodeResults = rptData.getNodeResultsMap().get(objName);
                    if(nodeResults != null){
                        for(RptData.NodeResult nodeResult : nodeResults){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("day",nodeResult.getDate());
                            jsonObject.put("time",nodeResult.getTime());
                            switch (variable){
                                case "Inflow": {
                                    jsonObject.put("variable",nodeResult.getInflow());
                                    break;
                                }
                                case "Flooding": {
                                    jsonObject.put("variable",nodeResult.getFlooding());
                                    break;
                                }
                                case "Depth": {
                                    jsonObject.put("variable",nodeResult.getDepth());
                                    break;
                                }
                                case "Head": {
                                    jsonObject.put("variable",nodeResult.getHead());
                                    break;
                                }
                            }
                            objTimeVariable.add(jsonObject);
                        }
                    }
                    break;
                }
                case "Link":{
                    List<RptData.LinkResult> linkResults = rptData.getLinkResultsMap().get(objName);
                    if(linkResults != null){
                        for(RptData.LinkResult linkResult : linkResults){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("day",linkResult.getDate());
                            jsonObject.put("time",linkResult.getTime());
                            switch (variable){
                                case "Flow": {
                                    jsonObject.put("variable",linkResult.getFlow());
                                    break;
                                }
                                case "Velocity": {
                                    jsonObject.put("variable",linkResult.getVelocity());
                                    break;
                                }
                                case "Depth": {
                                    jsonObject.put("variable",linkResult.getDepth());
                                    break;
                                }
                                case "Capacity": {
                                    jsonObject.put("variable",linkResult.getCapacity());
                                    break;
                                }
                            }
                            objTimeVariable.add(jsonObject);
                        }
                    }
                    break;
                }
                case "Subcatchment":{
                    List<RptData.SubcatchmentResult> subcatchmentResults = rptData.getSubcatchmentResultsMap().get(objName);
                    if(subcatchmentResults != null){
                        for(RptData.SubcatchmentResult subcatchmentResult : subcatchmentResults){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("day",subcatchmentResult.getDate());
                            jsonObject.put("time",subcatchmentResult.getTime());
                            switch (variable){
                                case "Precip": {
                                    jsonObject.put("variable",subcatchmentResult.getPrecip());
                                    break;
                                }
                                case "Losses": {
                                    jsonObject.put("variable",subcatchmentResult.getLosses());
                                    break;
                                }
                                case "Runoff": {
                                    jsonObject.put("variable",subcatchmentResult.getRunoff());
                                    break;
                                }
                            }
                            objTimeVariable.add(jsonObject);
                        }
                    }
                    break;
                }
            }
            result.put("timeVariable", objTimeVariable);
        }catch (IOException e) {
            throw  RequestException.fail(ResponseCode.FAIL);
        }
        return result;
    }
}

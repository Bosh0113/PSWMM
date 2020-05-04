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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RptDataService implements IRptDataService {

    @Resource(name = "rptDataDao")
    IRptDataDao rptDataDao;

    @Resource(name = "inpDataDao")
    IInpDataDao inpDataDao;

    private static final String basePath =  (RptDataService.class.getResource("/").getPath()+"/static/data/").substring(1);

    @Override
    public JSONObject parseRptData(String name) {
        RptData rptData;
        JSONObject jo;
        try {
            rptData = rptDataDao.readRptFile(basePath + name + ".rpt");
            jo = rptDataDao.rpt2Json(rptData);
        } catch (IOException e) {
            throw  RequestException.fail(ResponseCode.FAIL);
        }
        return jo;
    }

    @Override
    public JSONArray extractRptData(String rptName, String attriName, String inpName) {
        RptData rptData;
        JSONArray arr = new JSONArray();
        InpData inpData;
        try {
            inpData = inpDataDao.readInpFile(basePath + inpName);
            rptData = rptDataDao.readRptFile(basePath + rptName + ".rpt");
            for (int j = 0; j < inpData.getCoordinates().size(); j++) {
                JSONObject jo = new JSONObject();
                InpData.Coordinate coord = inpData.getCoordinates().get(j);
                List<JSONObject> objValues = new ArrayList<>();
                String objName = coord.getNode();
                String[] objCoordArr = new String[]{String.valueOf(coord.getX_coord()),String.valueOf(coord.getY_coord())};
                jo.put("name",objName);
                jo.put("coord",objCoordArr);

                List<RptData.NodeResult> nodeResults = rptData.getNodeResultsMap().get(objName);
                if (null != nodeResults)
                {
                    //先写死为depth
                    for (int i = 0; i < nodeResults.size(); i++) {
                        String time = nodeResults.get(i).getTime();
                        String value = nodeResults.get(i).getDepth();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("time",time);
                        jsonObject.put("value",value);
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
}

package com.geofuturelab.pswmm.Controll;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Bean.ResponseCode;
import com.geofuturelab.pswmm.Bean.ResponseResult;
import com.geofuturelab.pswmm.Exception.RequestException;
import com.geofuturelab.pswmm.Service.IInpDataService;
import com.geofuturelab.pswmm.Service.IRptDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/vision")
@Api("可视化数据请求")
public class VisualizationController {
    @Resource(name = "inpDataService")
    IInpDataService inpDataService;

    @Resource(name = "rptDataService")
    IRptDataService rptDataService;

    @GetMapping(value = "/timeSeries")
    @ResponseBody
    @ApiOperation(value="获取时间轴数据")
    public ResponseResult timeSeriesPlot(@RequestParam("objType") String objType,
                                         @RequestParam("objName") String objName,
                                         @RequestParam("variable") String variable,
                                         @RequestParam("inpName") String inpName,
                                         @RequestParam("rptName") String rptName){
        JSONObject arr = null;
        try {
            arr = rptDataService.timeSeriesPlot(objType,objName,variable,inpName,rptName);
        }catch (RequestException exception) {
            ResponseResult.result(exception.getCode());
        }
        return ResponseResult.result(ResponseCode.OK,arr);
    }
}

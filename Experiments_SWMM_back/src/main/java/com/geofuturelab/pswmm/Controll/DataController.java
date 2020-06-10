package com.geofuturelab.pswmm.Controll;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Bean.ResponseCode;
import com.geofuturelab.pswmm.Bean.ResponseResult;
import com.geofuturelab.pswmm.DTO.RptDataDTO;
import com.geofuturelab.pswmm.Entity.DataAuthority;
import com.geofuturelab.pswmm.Exception.RequestException;
import com.geofuturelab.pswmm.Service.IInpDataService;
import com.geofuturelab.pswmm.Service.IRptDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: David.Xiao
 * @Date: 2019/12/6 21:18
 * @Description:
 */
@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/data")
@Api("数据管理")
public class DataController {

   /* *//**//**//**//**
     * @ApiImplicitParam的参数说明：
     *
     * paramType：指定参数放在哪个地方
     *  header：请求参数放置于Request Header，使用@RequestHeader获取
     *  query：请求参数放置于请求地址，使用@RequestParam获取
     *  path：（用于restful接口）-->请求参数的获取：@PathVariable
     *  body：（不常用）
     *  form（不常用）
     *  name：参数名
     * dataType：参数类型  string (this includes dates and files) Integer number boolean array object
     * required：参数是否必须传  true | false
     * value：说明参数的意思
     * defaultValue：参数的默认值
     * Swagger不支持cookie
     *     @ApiOperation(value="上传inp文件",notes = "inp文件、权限、投影信息")
     *     @ApiImplicitParams({
     *             @ApiImplicitParam(name = "authority", value = "private或public", defaultValue = "private", paramType = "form", required = true, dataType = "string"),
     *             @ApiImplicitParam(name = "projection", value = "投影信息", defaultValue = "{\"code\":\"4326\",\"name\":\"WGS 84\",\"extend\":[90,-180,-90,180],\"unit\":\"degree (supplier to define representation)\",\"proj4def\":\" proj=longlat  datum=WGS84  no_defs\"}", paramType = "form", required = true, dataType = "string"),
     *             @ApiImplicitParam(name = "files", value = "inp文件",required = true),
     *             @ApiImplicitParam(name = "instanceId", value = "instanceId", paramType = "form", required = true, dataType = "string"),
     *             @ApiImplicitParam(name = "email", value = "邮箱", paramType = "head", required = true, dataType = "string")
     *     })
     */

    @Resource(name = "inpDataService")
    IInpDataService inpDataService;

    @Resource(name = "rptDataService")
    IRptDataService rptDataService;

    @PostMapping(value = "/inp", headers = "content-type=multipart/form-data")
    @ResponseBody  //返回结果不会被解析为跳转路径，而是直接写入 HTTP response body;直接返回 json 数据
    @ApiOperation(value="上传inp文件",notes = "inp文件、权限、投影信息")
    public ResponseResult uploadFile(@RequestParam("authority") DataAuthority authority, @RequestParam("projection") String projection,
                                     @RequestPart("file") MultipartFile file, @RequestParam("instanceId") String instanceId,
                                     @CookieValue("email") String email){
        // TODO inp文件解析后存到数据库的inpData集合，先将inp文件存放在文件夹，再从文件夹中读inp数据并存放到数据库，再从数据库读取inp数据，转为geojson发给前端，前端显示;
        JSONObject jo = null;
        try{
            String id = inpDataService.saveInpFile(email,instanceId,authority,projection,file);
            jo = inpDataService.queryInpDataById(id);
        }catch (RequestException exception){
            ResponseResult.result(exception.getCode());
        }
        return ResponseResult.result(ResponseCode.OK,jo);
    }

    @GetMapping(value = "/objectNames")
    @ResponseBody
    @ApiOperation(value="获取Inp中对象名列表")
    public ResponseResult getObjectNames(@RequestParam("inpName") String inpName, HttpServletRequest request)
    {
        JSONObject arr = null;
        try {
            arr = inpDataService.getObjectNames(inpName,request);
        }catch (RequestException exception) {
            ResponseResult.result(exception.getCode());
        }
        return ResponseResult.result(ResponseCode.OK,arr);
    }

    @GetMapping(value = "/rpt-all")
    @ResponseBody
    @ApiOperation(value="读取rpt文件")
    public ResponseResult readRptData(@RequestParam("name") String name, HttpServletRequest request)
    {
        JSONObject jo = null;
        try {
             jo = rptDataService.parseRptData(name,request);
        } catch (RequestException exception) {
            ResponseResult.result(exception.getCode());
        }
        return ResponseResult.result(ResponseCode.OK,jo);
    }

    @PostMapping(value = "/rpt")
    @ResponseBody
    @ApiOperation(value = "根据特定的字段来读取rpt文件")
    public ResponseResult readRptDataByName(@RequestBody RptDataDTO rptDataDTO, HttpServletRequest request){
        JSONObject jo = null;
        try{
            jo = rptDataService.parseRptDataByProperties(rptDataDTO.getName(),rptDataDTO.getProperties(),request);
        }catch (RequestException e){
            ResponseResult.result(e.getCode());
        }
        return ResponseResult.result(ResponseCode.OK,jo);
    }

}



package com.geofuturelab.pswmm.Dao;

import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Entity.DataAuthority;
import com.geofuturelab.pswmm.Entity.InpData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: David.Xiao
 * @Date: 2019/12/12 21:40
 * @Description:
 */

public interface IInpDataDao {
    String saveInpFile(String email, String instanceId, MultipartFile mFile)  throws IOException;
    InpData readInpFile(String fileName) throws IOException;
    String insertInpData(InpData inpData);
    void setMetaData(String email, String instance, DataAuthority authority, String projection);
    InpData getInpData();
    InpData queryInpDataById(String id);
    JSONObject inp2Json(InpData inpData);
}

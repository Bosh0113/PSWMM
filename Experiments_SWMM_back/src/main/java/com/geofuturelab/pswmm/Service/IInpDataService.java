package com.geofuturelab.pswmm.Service;

import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Entity.DataAuthority;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: David.Xiao
 * @Date: 2019/12/12 21:26
 * @Description:
 */

public interface IInpDataService {
    String saveInpFile(String email, String instanceId, DataAuthority authority, String projection, MultipartFile file);
    JSONObject queryInpDataById(String id);
    JSONObject getObjectNames(String inpName, HttpServletRequest request);
}

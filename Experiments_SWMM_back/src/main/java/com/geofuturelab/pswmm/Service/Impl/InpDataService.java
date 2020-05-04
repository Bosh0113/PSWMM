package com.geofuturelab.pswmm.Service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Bean.ResponseCode;
import com.geofuturelab.pswmm.Dao.IInpDataDao;
import com.geofuturelab.pswmm.Entity.DataAuthority;
import com.geofuturelab.pswmm.Entity.InpData;
import com.geofuturelab.pswmm.Exception.RequestException;
import com.geofuturelab.pswmm.Service.IInpDataService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author: David.Xiao
 * @Date: 2019/12/12 21:26
 * @Description:
 */
@Service
public class InpDataService implements IInpDataService {

    @Resource(name = "inpDataDao")
    IInpDataDao inpDataDao;

    private static final String basePath = InpDataService.class.getResource("/").getPath()+"\\data";

    /**
     * 将inp文件保存到本地，再读取出来，存到数据库中
     * @param email
     * @param instanceId
     * @param mFile
     * @throws RequestException
     */
    @Override
    public String saveInpFile(String email, String instanceId, DataAuthority authority, String projection, MultipartFile mFile)  {
        String id;
        try {
            String fileName = inpDataDao.saveInpFile(email, instanceId, mFile);
            InpData inpData = inpDataDao.readInpFile(fileName);
            inpDataDao.setMetaData(email,instanceId,authority,projection);
            id = inpDataDao.insertInpData(inpDataDao.getInpData());
        } catch (IOException e) {
            throw  RequestException.fail(ResponseCode.FAIL);
        }
        return id;
    }

    @Override
    public JSONObject queryInpDataById(String id) {
        InpData inpData = inpDataDao.queryInpDataById(id);
        return inpDataDao.inp2Json(inpData);
    }
}

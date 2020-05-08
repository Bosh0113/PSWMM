package com.geofuturelab.pswmm.Service.Impl;

import com.alibaba.fastjson.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: David.Xiao
 * @Date: 2019/12/12 21:26
 * @Description:
 */
@Service
public class InpDataService implements IInpDataService {

    @Resource(name = "inpDataDao")
    IInpDataDao inpDataDao;

    private static final String basePath =  (InpDataService.class.getResource("/").getPath()+"/static/data/").substring(1);

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

    @Override
    public JSONObject getObjectNames(String inpName){
        JSONObject result = new JSONObject();
        try {
            InpData inpData = inpDataDao.readInpFile(basePath + inpName + ".inp");
            List<InpData.Junction> junctions = inpData.getJunctions();
            List<InpData.Outfall> outfalls = inpData.getOutfalls();
            List<String> nodes = new ArrayList<>();
            for(InpData.Junction junction : junctions){
                nodes.add(junction.getName());
            }
            for(InpData.Outfall outfall : outfalls){
                nodes.add(outfall.getName());
            }
            result.put("Node",nodes);
            List<String> links = new ArrayList<>();
            List<InpData.Conduit> conduits = inpData.getConduits();
            for(InpData.Conduit conduit:conduits){
                links.add(conduit.getName());
            }
            result.put("Link",links);
            List<String> subcatchments = new ArrayList<>();
            List<InpData.Subcatchment> subcatchments1 = inpData.getSubcatchments();
            for(InpData.Subcatchment subcatchment:subcatchments1){
                subcatchments.add(subcatchment.getName());
            }
            result.put("Subcatchment",subcatchments);
            List<String> rainGages = new ArrayList<>();
            List<InpData.Raingage> rainGages1 = inpData.getRaingages();
            for(InpData.Raingage rainGage:rainGages1){
                rainGages.add(rainGage.getName());
            }
            result.put("RainGage",rainGages);
        } catch (IOException e) {
            throw  RequestException.fail(ResponseCode.FAIL);
        }
        return result;

    }
}

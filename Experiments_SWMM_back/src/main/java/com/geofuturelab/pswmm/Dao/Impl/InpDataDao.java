package com.geofuturelab.pswmm.Dao.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Bean.ResponseCode;
import com.geofuturelab.pswmm.Dao.IInpDataDao;
import com.geofuturelab.pswmm.Entity.DataAuthority;
import com.geofuturelab.pswmm.Entity.InpData;
import com.geofuturelab.pswmm.Exception.RequestException;
import com.geofuturelab.pswmm.Service.Impl.InpDataService;
import com.geofuturelab.pswmm.vo.InpDataVo;
import lombok.Data;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: David.Xiao
 * @Date: 2019/12/12 21:40
 * @Description:
 */
@Component
public class InpDataDao implements IInpDataDao {
    @Autowired(required = false)
    MongoTemplate mongoTemplate;

    @Value("${spring.data.mongodb.inpDataCollection}")
    private String inpDataCollection;

    private static final String basePath = InpDataService.class.getResource("/").getPath()+"\\data";
    private static ThreadLocal<InpData> inpLocal = new ThreadLocal<>();
    private static ThreadLocal<List<String>> linesLocal = new ThreadLocal<>();

    @Override
    public InpData getInpData()
    {
        return inpLocal.get();
    }

    /**将inp文件存入本地*/
    @Override
    public String saveInpFile(String email,String instanceId, MultipartFile mFile) throws IOException {
        if (mFile == null || email == null || instanceId == null || "".equals(email) || "".equals(instanceId))
            throw RequestException.fail(ResponseCode.FAIL);
        String folderPath = basePath + "\\" + email + "\\" + instanceId + "\\inpData";
        File folder = new File(folderPath);
        if (!folder.exists())
        {
            folder.mkdirs();
        }
        File file = new File(folderPath,mFile.getResource().getFilename());
        mFile.transferTo(file);
        return file.getPath();
    }

    /**从本地读取inp文件*/
    @Override
    public InpData readInpFile(String fileName) throws IOException {
        inpLocal.set(new InpData());
//        System.out.println(Paths.get(fileName));
        linesLocal.set(Files.readAllLines(Paths.get(fileName),Charset.forName("UTF-8")));
        List<String> lines = linesLocal.get();
        for (int cursor = 0; cursor < lines.size(); cursor++) {
            String line = lines.get(cursor);
            switch (line) {
                case "":break;
                case "[TITLE]":
                    cursor = readTitle(cursor);break;
                case "[OPTIONS]":
                    cursor = readOption(cursor);break;
                case "[EVAPORATION]":
                    cursor = readEvaporation(cursor);break;
                case "[RAINGAGES]":
                    cursor = readRaingage(cursor);break;
                case "[SUBCATCHMENTS]":
                    cursor = readSubcatchment(cursor);break;
                case "[SUBAREAS]":
                    cursor = readSubarea(cursor);break;
                case "[INFILTRATION]":
                    cursor = readInfiltration(cursor);break;
                case "[JUNCTIONS]":
                    cursor = readJunction(cursor);break;
                case "[OUTFALLS]":
                    cursor = readOutfall(cursor);break;
                case "[STORAGE]":
                    cursor = readStorage(cursor);break;
                case "[CONDUITS]":
                    cursor = readConduit(cursor);break;
                case "[XSECTIONS]":
                    cursor = readXscetion(cursor);break;
                case "[LOSSES]":
                    cursor = readLosses(cursor);break;
                case "[CURVES]":
                    cursor = readCurve(cursor);break;
                case "[TIMESERIES]":
                    cursor = readTimeseries(cursor);break;
                case "[REPORT]":
                    cursor = readReport(cursor);break;
                case "[TAGS]":
                    cursor = readTag(cursor);break;
                case "[MAP]":
                    cursor = readMap(cursor);break;
                case "[COORDINATES]":
                    cursor = readCoordinate(cursor);break;
                case "[VERTICES]":
                    cursor = readVertice(cursor);break;
                case "[Polygons]":
                    cursor = readPolygons(cursor);break;
                case "[SYMBOLS]":
                    cursor = readSymbol(cursor);break;
                default:
                    break;
            }
        }
        return inpLocal.get();
    }

    /**设置instanceid/email/authority/projection等元数据*/
    @Override
    public void setMetaData(String email, String instanceId, DataAuthority authority, String projection) {
        inpLocal.get().setEmail(email);
        inpLocal.get().setInstanceId(instanceId);
        inpLocal.get().setAuthority(authority);
        inpLocal.get().setProjection(projection);
    }


    /**存入数据库 */
    @Override
    public String insertInpData(InpData inpData) {
        if(inpData == null)
        {
            throw RequestException.fail(ResponseCode.FAIL);
        }
        try
        {
            mongoTemplate.insert(inpData,inpDataCollection);
        }
        catch (Exception e)
        {
            throw RequestException.fail(ResponseCode.FAIL);
        }
        System.out.println(inpData.getId());

        return inpData.getId();
    }


    @Override
    public InpData queryInpDataById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query,InpData.class,inpDataCollection);
    }

    @Override
    public JSONObject inp2Json(InpData inpData) {
        JSONObject jo = new JSONObject();
        JSONObject geoJson = this.getGeoJsonFromInp(this.wrapperInp(inpData));
        JSONObject propJson = this.getPropertyJsonFromInp(inpData);
        jo.put("geoJson",geoJson);
        jo.put("propJson",propJson);
        return jo;
    }

    private JSONObject getPropertyJsonFromInp(InpData inpData){
        InpDataVo vo = new InpDataVo();
        BeanUtils.copyProperties(inpData,vo);
        JSONObject jo = (JSONObject) JSON.toJSON(vo);
        jo.put("geoCenter",getGeoCenter());
        return jo;
    }

    public double[] getGeoCenter(){
        double[] geoCenter = new double[2];
        geoCenter[0] = this.getInpData().getCoordinates().get(0).getX_coord();
        geoCenter[1] = this.getInpData().getCoordinates().get(0).getY_coord();
        return geoCenter;
    }

    /**读取[TITLE]*/
    private int readTitle(int index)
    {
        List<String> lines = linesLocal.get();
        List<String> titles = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            String line = lines.get(index);
            if (line.equals(""))
            {
                index++;
                continue;
            }
            titles.add(line);
            index++;
        }
        inpLocal.get().setTitle(titles);
        return index - 1;
    }

    /**读取[OPTIONS]*/
    private int readOption(int index)
    {
        List<String> lines = linesLocal.get();
        InpData.Option option = new InpData.Option();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            if (line.equals(""))
            {
                index++;
                continue;
            }
            switch(line) {
                case "FLOW_UNITS":
                    option.setFlow_units(tempStr[1]);break;
                case "INFILTRATION":
                    option.setInfiltration(tempStr[1]);break;
                case "FLOW_ROUTING":
                    option.setFlow_routing(tempStr[1]);break;
                case "LINK_OFFSETS":
                    option.setLink_offsets(tempStr[1]);break;
                case "FORCE_MAIN_EQUATION":
                    option.setForce_main_equation(tempStr[1]);break;
                case "IGNORE_RAINFALL":
                    option.setIgnore_rainfall(tempStr[1]);break;
                case "INGORE_SNOWMELT":
                    option.setIgnore_snowmelt(tempStr[1]);break;
                case "IGNORE_GROUNDWATER":
                    option.setIgnore_groundwater(tempStr[1]);break;
                case "IGNORE_ROUTING":
                    option.setIgnore_routing(tempStr[1]);break;
                case "IGNORE_QUALITY":
                    option.setIgnore_quality(tempStr[1]);break;
                case "ALLOW_PONDING":
                    option.setAllow_ponding(tempStr[1]);break;
                case "SKIP_STEADY_STATE":
                    option.setSkip_steady_state(tempStr[1]);break;
                case "START_DATE":
                    option.setStart_date(tempStr[1]);break;
                case "START_TIME":
                    option.setStart_time(tempStr[1]);break;
                case "END_DATE":
                    option.setEnd_date(tempStr[1]);break;
                case "END_TIME":
                    option.setEnd_time(tempStr[1]);break;
                case "REPORT_START_DATE":
                    option.setReport_start_date(tempStr[1]);break;
                case "REPORT_START_TIME":
                    option.setReport_start_time(tempStr[1]);break;
                case "SWEEP_START":
                    option.setSweep_start(tempStr[1]);break;
                case "SWEEP_END":
                    option.setSweep_end(tempStr[1]);break;
                case "DRY_DAYS":
                    option.setDry_days(Float.parseFloat(tempStr[1]));break;
                case "REPORT_STEP":
                    option.setReport_step(tempStr[1]);break;
                case "WET_STEP":
                    option.setWet_step(tempStr[1]);break;
                case "DRY_STEP":
                    option.setDry_step(tempStr[1]);break;
                case "ROUTING_STEP":
                    option.setRouting_step(tempStr[1]);break;
                case "LENGTHENING_STEP":
                    option.setLengthening_step(Float.parseFloat(tempStr[1]));break;
                case "VARIABLE_STEP":
                    option.setVariable_step(Float.parseFloat(tempStr[1]));break;
                case "INERTIAL_DAMPING":
                    option.setInertial_damping(tempStr[1]);break;
                case "NORMAL_FLOW_LIMITED":
                    option.setNormal_flow_limited(tempStr[1]);break;
                case "MIN_SURFAREA":
                    option.setMin_surfarea(Float.parseFloat(tempStr[1]));break;
                case "MIN_SLOPE":
                    option.setMin_slope(Float.parseFloat(tempStr[1]));break;
                //SWMM手册里没有，但是INP文件中出现的
                case "MAX_TRIALS":
                    option.setMax_trials(Float.parseFloat(tempStr[1]));break;
                case "HEAD_TOLERANCE":
                    option.setHead_tolerance(Float.parseFloat(tempStr[1]));break;
                case "SYS_FLOW_TOL":
                    option.setSys_flow_tol(Float.parseFloat(tempStr[1]));break;
                case "LAT_FLOW_TOL":
                    option.setLat_flow_tol(Float.parseFloat(tempStr[1]));break;
                default: break;
            }
            index++;
        }
        inpLocal.get().setOption(option);
        return index - 1;
    }

    /**读取[EVAPORATION]*/
    private int readEvaporation(int index)
    {

        InpData.Evaporation evaporation=new InpData.Evaporation();
        List<String> lines = linesLocal.get();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            String line = lines.get(index);
            if (line.equals(""))
            {
                index++;
                continue;
            }
            String[] tempStr=line.split("[ ]+");
            switch (tempStr[0]){
                case "CONSTANT":
                    evaporation.setConstant(Float.parseFloat(tempStr[1]));
                    evaporation.setSelected_source("CONSTANT");
                    break;
                case "TIMESERIES":
                    evaporation.setTimeseries(tempStr[1]);
                    evaporation.setSelected_source("TIMESERIES");
                    break;
                case "FILE":
                    evaporation.setFile((float[]) ConvertUtils.convert(tempStr,float.class));
                    evaporation.setSelected_source("FILE");
                    break;
                case "MONTHLY":
                    evaporation.setMonthly((float[]) ConvertUtils.convert(tempStr,float.class));
                    evaporation.setSelected_source("MONTHLY");
                    break;
                case "TEMPERATURE":
                    evaporation.setTemperature(tempStr[1]);
                    evaporation.setSelected_source("TEMPERATURE");
                    break;
                case "RECOVERY":
                    evaporation.setRecovery(tempStr[1]);
                    break;
                case "DRY_ONLY":
                    evaporation.setDryonly(tempStr[1]);
                    break;
                default:break;
            }
            index++;
        }
        inpLocal.get().setEvaporation(evaporation);
        return index - 1;
    }

    /**读取Description*/
    private int readDescription(int index, InpData.AbstractInpPart part)
    {
        List<String> lines = linesLocal.get();
        ArrayList<String> descriptions = new ArrayList<>();
        while (lines.get(index).startsWith(";"))
        {
            descriptions.add(lines.get(index).substring(1));
            index++;
        }
        part.setDescription(descriptions);
        return index;
    }

    /**忽略注释*/
    private int skipComment(int index,List<String> lines)
    {
        while (lines.get(index).startsWith(";;"))
        {
            index++;
        }
        return index;
    }

    /**读取[RAINGAGES]*/
    private int readRaingage(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Raingage> raingages = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Raingage raingage = new InpData.Raingage();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            index = readDescription(index,raingage);
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            raingage.setName(tempStr[0]);
            raingage.setFormat(tempStr[1]);
            raingage.setInterval(tempStr[2]);
            raingage.setScf(Float.parseFloat(tempStr[3]));
            raingage.setDatasource(tempStr[4]);
            if (raingage.getDatasource().equals("TIMESERIES"))
            {
                raingage.setTimeseries(tempStr[5]);
            }
            else if (raingage.getDatasource().equals("FILE"))
            {
                raingage.setFilename(tempStr[5]);
                raingage.setStationid(tempStr[6]);
                raingage.setRainunits(tempStr[7]);
            }
            raingages.add(raingage);
            index++;
        }
        inpLocal.get().setRaingages(raingages);
        return index - 1;
    }

    /**读取[SUBCATCHMENTS]*/
    private int readSubcatchment(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Subcatchment> subcatchments = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Subcatchment subcatchment = new InpData.Subcatchment();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            index = readDescription(index,subcatchment);
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            subcatchment.setName(tempStr[0]);
            subcatchment.setRaingage(tempStr[1]);
            subcatchment.setOutlet(tempStr[2]);
            subcatchment.setArea(Float.parseFloat(tempStr[3]));
            subcatchment.setImperv(Float.parseFloat(tempStr[4]));
            subcatchment.setWidth(Float.parseFloat(tempStr[5]));
            subcatchment.setSlope(Float.parseFloat(tempStr[6]));
            subcatchment.setCurblen(Float.parseFloat(tempStr[7]));

            if (tempStr.length>8 && !tempStr[8].equals(""))
            {
                subcatchment.setSnowpack(tempStr[8]);
            }
            else
            {
                subcatchment.setSnowpack("");
            }
            subcatchments.add(subcatchment);
            index++;
        }
        inpLocal.get().setSubcatchments(subcatchments);
        return index - 1;
    }

    /**读取[SUBAREAS]*/
    private int readSubarea(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Subarea> subareas = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Subarea subarea = new InpData.Subarea();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            subarea.setName(tempStr[0]);
            subarea.setN_imperv(Float.parseFloat(tempStr[1]));
            subarea.setN_perv(Float.parseFloat(tempStr[2]));
            subarea.setS_imperv(Float.parseFloat(tempStr[3]));
            subarea.setS_perv(Float.parseFloat(tempStr[4]));
            subarea.setPctzero(Float.parseFloat(tempStr[5]));
            subarea.setRouteto(tempStr[6]);
            if (tempStr.length>7 && !tempStr[7].equals(""))
            {
                subarea.setPctrouted(Float.parseFloat(tempStr[7]));
            }

            subareas.add(subarea);
            index++;
        }
        inpLocal.get().setSubareas(subareas);
        return index - 1;
    }

    /**读取[INFILTRATION]*/
    private int readInfiltration(int index)
    {
        List<String> lines = linesLocal.get();
        ArrayList<InpData.Infiltration> infiltrations=new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        switch (inpLocal.get().getOption().getInfiltration())
        {
            case "HORTON":
            case "MODIFIED_HORTON":
                while (index < lines.size() && !nextSection(lines.get(index)))
                {
                    InpData.Infiltration infiltration = new InpData.Infiltration();
                    if (lines.get(index).equals(""))
                    {
                        index++;
                        continue;
                    }
                    String line = lines.get(index);
                    String[] tempStr=line.split("[ ]+");
                    infiltration.setName(tempStr[0]);
                    infiltration.setMaxrate(Float.parseFloat(tempStr[1]));
                    infiltration.setMinrate(Float.parseFloat(tempStr[2]));
                    infiltration.setDecay(Float.parseFloat(tempStr[3]));
                    infiltration.setDrytime(Float.parseFloat(tempStr[4]));
                    infiltration.setMaxinfil(Float.parseFloat(tempStr[5]));
                    infiltrations.add(infiltration);
                    index++;
                }
                break;
            case "GREEN_AMPT":
                while (index < lines.size() && !nextSection(lines.get(index)))
                {
                    InpData.Infiltration infiltration = new InpData.Infiltration();
                    if (lines.get(index).equals(""))
                    {
                        index++;
                        continue;
                    }
                    String line = lines.get(index);
                    String[] tempStr=line.split("[ ]+");
                    infiltration.setName(tempStr[0]);
                    infiltration.setSuction(Float.parseFloat(tempStr[1]));
                    infiltration.setConductivity(Float.parseFloat(tempStr[2]));
                    infiltration.setDrytime(Float.parseFloat(tempStr[3]));
                    infiltrations.add(infiltration);
                    index++;
                }
                break;
            case "CURVE_NUMBER":
                while (index < lines.size() && !nextSection(lines.get(index)))
                {
                    InpData.Infiltration infiltration = new InpData.Infiltration();
                    if (lines.get(index).equals(""))
                    {
                        index++;
                        continue;
                    }
                    String line = lines.get(index);
                    String[] tempStr=line.split("[ ]+");
                    infiltration.setName(tempStr[0]);
                    infiltration.setCurveNumber(Float.parseFloat(tempStr[1]));
                    infiltration.setConductivity(Float.parseFloat(tempStr[2]));
                    infiltration.setDrytime(Float.parseFloat(tempStr[3]));
                    infiltrations.add(infiltration);
                    index++;
                }
            default:
                break;
        }
        inpLocal.get().setInfiltration(infiltrations);
        return index - 1;
    }

    /**读取[JUNCTIONS]*/
    private int readJunction(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Junction> junctions = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Junction junction=new InpData.Junction();

            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            index = readDescription(index,junction);
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            junction.setName(tempStr[0]);
            junction.setInvert(Float.parseFloat(tempStr[1]));
            junction.setMaxdepth(Float.parseFloat(tempStr[2]));
            junction.setInitdepth(Float.parseFloat(tempStr[3]));
            junction.setSurdepth(Float.parseFloat(tempStr[4]));
            junction.setAponded(Float.parseFloat(tempStr[5]));
            junctions.add(junction);
            index++;
        }
        inpLocal.get().setJunctions(junctions);
        return index - 1;
    }

    /**读取[OUTFALLS]*/
    private int readOutfall(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Outfall> outfalls = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Outfall outfall = new InpData.Outfall();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            index = readDescription(index,outfall);
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            outfall.setName(tempStr[0]);
            outfall.setInvert(Float.parseFloat(tempStr[1]));
            outfall.setType(tempStr[2]);

            if ((outfall.getType()).equals("FREE") || (outfall.getType()).equals("NORMAL"))
            {
                outfall.setStagedata("");
                outfall.setGated(tempStr[3]);
            }
            if ((outfall.getType()).equals("FIXED") || (outfall.getType()).equals("TIDAL") || (outfall.getType()).equals("TIMESERIES"))
            {
                outfall.setStagedata(tempStr[3]);
                outfall.setGated(tempStr[4]);
            }
            outfalls.add(outfall);
            index++;
        }
        inpLocal.get().setOutfalls(outfalls);
        return index - 1;
    }

    /**读取[STORAGE]*/
    private int readStorage(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Storage> storages = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Storage storage = new InpData.Storage();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            index = readDescription(index,storage);
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            storage.setName(tempStr[0]);
            storage.setInvert(Float.parseFloat(tempStr[1]));
            storage.setMaxdepth(Float.parseFloat(tempStr[2]));
            storage.setInitdepth(Float.parseFloat(tempStr[3]));
            storage.setShape(tempStr[4]);

            if ((storage.getShape()).equals("TABULAR"))
            {
                storage.setCurvename(tempStr[5]);
                storage.setPonded(Float.parseFloat(tempStr[6]));
                storage.setFevap(Float.parseFloat(tempStr[7]));
                if (tempStr.length>8 && !tempStr[8].equals(""))
                {
                    storage.setSuction_head(Float.parseFloat(tempStr[8]));
                    storage.setConductivity(Float.parseFloat(tempStr[9]));
                    storage.setInitial_deficit(Float.parseFloat(tempStr[10]));
                }
            }
            else if ((storage.getShape()).equals("FUNCTIONAL"))
            {
                float[] tempParam=new float[3];
                tempParam[0]=(Float.parseFloat(tempStr[5]));
                tempParam[1]=(Float.parseFloat(tempStr[6]));
                tempParam[2]=(Float.parseFloat(tempStr[7]));
                storage.setParam(tempParam);
                storage.setPonded(Float.parseFloat(tempStr[8]));
                storage.setFevap(Float.parseFloat(tempStr[9]));
                if (tempStr.length>10 && !tempStr[8].equals(""))
                {
                    storage.setSuction_head(Float.parseFloat(tempStr[10]));
                    storage.setConductivity(Float.parseFloat(tempStr[11]));
                    storage.setInitial_deficit(Float.parseFloat(tempStr[12]));
                }
            }
            storages.add(storage);
            index++;
        }
        inpLocal.get().setStorages(storages);
        return index - 1;
    }

    /**读取[CONDUITS]*/
    private int readConduit(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Conduit> conduits = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Conduit conduit = new InpData.Conduit();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            index = readDescription(index,conduit);
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            conduit.setName(tempStr[0]);
            conduit.setFromnode(tempStr[1]);
            conduit.setTonode(tempStr[2]);
            conduit.setLength(Float.parseFloat(tempStr[3]));
            conduit.setRoughness(Float.parseFloat(tempStr[4]));
            conduit.setInoffset(Float.parseFloat(tempStr[5]));
            conduit.setOutoffset(Float.parseFloat(tempStr[6]));
            conduit.setInitflow(Float.parseFloat(tempStr[7]));
            conduit.setMaxflow(Float.parseFloat(tempStr[8]));
            conduits.add(conduit);
            index++;
        }
        inpLocal.get().setConduits(conduits);
        return index - 1;
    }

    /**读取[XSECTIONS]*/
    private int readXscetion(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Xscetion> xscetions = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Xscetion xscetion = new InpData.Xscetion();

            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            xscetion.setName(tempStr[0]);
            xscetion.setShape(tempStr[1]);
            xscetion.setGeom1(Float.parseFloat(tempStr[2]));
            xscetion.setGeom2(Float.parseFloat(tempStr[3]));
            xscetion.setGeom3(Float.parseFloat(tempStr[4]));
            xscetion.setGeom4(Float.parseFloat(tempStr[5]));
            if(tempStr.length>6)
            {
                xscetion.setBarrels(Float.parseFloat(tempStr[6]));
            }
            if (tempStr.length>7 && !tempStr[7].equals(""))
            {
                xscetion.setCulvertcode(tempStr[7]);
            }
            xscetions.add(xscetion);
            index++;
        }
        inpLocal.get().setXscetions(xscetions);
        return index - 1;
    }

    /**读取[LOSSES]*/
    private int readLosses(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Losses> losses = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Losses loss = new InpData.Losses();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            loss.setName(tempStr[0]);
            loss.setEntry_loss_coeff(Float.parseFloat(tempStr[1]));
            loss.setExit_loss_coeff(Float.parseFloat(tempStr[2]));
            loss.setAvg_loss_coeff(Float.parseFloat(tempStr[3]));
            loss.setFlap_gate(tempStr[4]);
            loss.setSeepage_loss_rate(Float.parseFloat(tempStr[5]));
            losses.add(loss);
            index++;
        }
        inpLocal.get().setLosses(losses);
        return index - 1;
    }

    /**读取[CURVES]*/
    private int readCurve(int index)
    {
        int row = 0;
        List<String> lines = linesLocal.get();
        List<InpData.Curve> curves = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Curve curve = new InpData.Curve();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            index = readDescription(index,curve);
            List<InpData.CurveXY> coordinates = new ArrayList<>();
            while (!lines.get(index).startsWith(";") && !lines.get(index).equals(""))
            {
                String line = lines.get(index);
                String[] tempStr=line.split("[ ]+");
                if (row == 0)
                {
                    curve.setCurve(tempStr[0]);
                    curve.setType(tempStr[1]);
                    InpData.CurveXY curveXY = new InpData.CurveXY();
                    curveXY.setX_value(Float.parseFloat(tempStr[2]));
                    curveXY.setY_value(Float.parseFloat(tempStr[3]));
                    coordinates.add(curveXY);
                    row++;
                }
                else
                {
                    InpData.CurveXY curveXY = new InpData.CurveXY();
                    curveXY.setX_value(Float.parseFloat(tempStr[1]));
                    curveXY.setY_value(Float.parseFloat(tempStr[2]));
                    coordinates.add(curveXY);
                }
                index++;
            }
            curve.setCoordinates(coordinates);
            curves.add(curve);
            index++;
            row = 0;
        }
        inpLocal.get().setCurves(curves);
        return index - 1;
    }

    /**读取[TIMESERIES]*/
    private int readTimeseries(int index) {
        int row = 0;
        List<String> lines = linesLocal.get();
        List<InpData.Timeseries> timeseries = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index))) {
            InpData.Timeseries series = new InpData.Timeseries();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            index = readDescription(index,series);
            ArrayList<InpData.DateTimeValue> dateTimeValues = new ArrayList<>();
            while (!lines.get(index).startsWith(";") && !lines.get(index).equals("")){
                InpData.DateTimeValue dateTimeValue = new InpData.DateTimeValue();
                String line = lines.get(index);
                String[] tempStr = line.split("[ ]+");
                if (row == 0) {
                    series.setName(tempStr[0]);

                }
                if (tempStr.length == 4) {
                    dateTimeValue.setDate(tempStr[1]);
                    dateTimeValue.setTime(tempStr[2]);
                    dateTimeValue.setValue(Float.parseFloat(tempStr[3]));
                } else if (tempStr.length == 3) {
                    dateTimeValue.setTime(tempStr[1]);
                    dateTimeValue.setValue(Float.parseFloat(tempStr[2]));
                }
                dateTimeValues.add(dateTimeValue);
                row++;
                index++;
            }
            series.setDateTimeValues(dateTimeValues);
            timeseries.add(series);
            row = 0;
            index++;
        }
        inpLocal.get().setTimeseries(timeseries);
        return index - 1;
    }

    /**读取[REPORT]*/
    private int readReport(int index)
    {
        List<String> lines = linesLocal.get();
        InpData.Report report = new InpData.Report();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            switch (tempStr[0])
            {
                case "INPUT":
                    report.setInput(tempStr[1]);break;
                case "CONTROLS":
                    report.setControls(tempStr[1]);break;
                case "CONTINUITY":
                    report.setContinuity(tempStr[1]);break;
                case "FLOWSTATS":
                    report.setFlowstats(tempStr[1]);break;
                case "SUBCATCHMENTS":
                case "NODES":
                case "LINKS":
                    break;
                default:break;

            }
            index++;
        }
        inpLocal.get().setReport(report);
        return index - 1;
    }

    /**读取[TAGS]*/
    private int readTag(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Tag> tags = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Tag tag = new InpData.Tag();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            tag.setCategory(tempStr[0]);
            tag.setTagId(tempStr[1]);
            tag.setContent(tempStr[2]);
            tags.add(tag);
            index++;
        }
        inpLocal.get().setTag(tags);
        return index - 1;
    }

    /**读取[MAP]*/
    private int readMap(int index)
    {
        InpData.Map map = new InpData.Map();
        List<String> lines = linesLocal.get();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            if (tempStr[0].equals("DIMENSIONS"))
            {
                float[] tempDimensions=new float[tempStr.length-1];
                for (int i=0;i<tempDimensions.length;i++)
                {
                    tempDimensions[i]=Float.parseFloat(tempStr[i+1]);
                }
                map.setDimensions(tempDimensions);
            }
            else if (tempStr[0].equals("Units"))
            {
                map.setUnits(tempStr[1]);
            }
            index++;
        }
        inpLocal.get().setMap(map);
        return index - 1;
    }

    /**读取[COORDINATES]*/
    private int readCoordinate(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Coordinate> coordinates = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Coordinate coordinate = new InpData.Coordinate();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            coordinate.setNode(tempStr[0]);
            coordinate.setX_coord(Double.parseDouble(tempStr[1]));
            coordinate.setY_coord(Double.parseDouble(tempStr[2]));
            coordinates.add(coordinate);
            index++;
        }
        inpLocal.get().setCoordinates(coordinates);
        return index - 1;
    }

    /**读取[VERTICES]*/
    private int readVertice(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Vertice> vertices = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Vertice vertice = new InpData.Vertice();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            vertice.setLink(tempStr[0]);
            vertice.setX_coord(Double.parseDouble(tempStr[1]));
            vertice.setY_coord(Double.parseDouble(tempStr[2]));
            vertices.add(vertice);
            index++;
        }
        inpLocal.get().setVertices(vertices);
        return index - 1;
    }

    /**读取[Polygons]*/
    private int readPolygons(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Polygon> polygons = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Polygon polygon = new InpData.Polygon();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            polygon.setPolygon(tempStr[0]);
            polygon.setX_coord(Float.parseFloat(tempStr[1]));
            polygon.setY_coord(Float.parseFloat(tempStr[2]));
            polygons.add(polygon);
            index++;
        }
        inpLocal.get().setPolygons(polygons);
        return index - 1;
    }


    /**读取[SYMBOLS]*/
    private int readSymbol(int index)
    {
        List<String> lines = linesLocal.get();
        List<InpData.Symbol> symbols = new ArrayList<>();
        index++;
        index = skipComment(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index)))
        {
            InpData.Symbol symbol = new InpData.Symbol();
            if (lines.get(index).equals(""))
            {
                index++;
                continue;
            }
            String line = lines.get(index);
            String[] tempStr=line.split("[ ]+");
            symbol.setGage(tempStr[0]);
            symbol.setX_coord(Float.parseFloat(tempStr[1]));
            symbol.setY_coord(Float.parseFloat(tempStr[2]));
            symbols.add(symbol);
            index++;
        }
        inpLocal.get().setSymbols(symbols);
        return index - 1;
    }


    /**判断游标是否到了下一节*/
    private boolean nextSection(String line)
    {
        return line.startsWith("[");
    }

    /**
     * 对Inp中涉及到坐标的属性数据包装为键值对的形式，为了查询方便
     * subcatchment conduit junction storage outfall
     * */
    public JSONObject wrapperInp(InpData inpData){
        JSONObject jo = new JSONObject();

        JSONObject subcatchJo = new JSONObject();
        for (int i = 0; i < inpData.getSubcatchments().size(); i++) {
            JSONObject subcatchmentJo = (JSONObject) JSON.toJSON(inpData.getSubcatchments().get(i));
            JSONObject subareaJo = (JSONObject) JSON.toJSON(inpData.getSubareas().get(i));
            JSONObject infiltrationJo = (JSONObject) JSON.toJSON(inpData.getInfiltration().get(i));
            JSONObject mergeJo = new JSONObject();
            mergeJo.putAll(subcatchmentJo);
            mergeJo.putAll(subareaJo);
            mergeJo.putAll(infiltrationJo);
            subcatchJo.put(inpData.getSubcatchments().get(i).getName(),mergeJo);
        }
        jo.put("subcatchment",subcatchJo);

        JSONObject conduitJo = new JSONObject();
        for (int i = 0; i < inpData.getConduits().size(); i++) {
            JSONObject condJo = (JSONObject) JSON.toJSON(inpData.getConduits().get(i));
            JSONObject xsectionJo = (JSONObject) JSON.toJSON(inpData.getXscetions().get(i));
            JSONObject mergeJo = new JSONObject();
            mergeJo.putAll(condJo);
            mergeJo.putAll(xsectionJo);
            conduitJo.put(inpData.getConduits().get(i).getName(),mergeJo);
        }
        if (null != inpData.getLosses())
        {
            for (int i = 0; i < inpData.getLosses().size(); i++) {
                JSONObject jsonObject = conduitJo.getJSONObject(inpData.getLosses().get(i).getName());
                jsonObject.putAll((JSONObject) JSON.toJSON(inpData.getLosses().get(i)));
                conduitJo.put(inpData.getLosses().get(i).getName(),jsonObject);
            }
        }
        jo.put("conduit",conduitJo);

        JSONObject junctionJo = new JSONObject();
        for (int i = 0; i < inpData.getJunctions().size(); i++) {
            JSONObject juncJo = (JSONObject) JSON.toJSON(inpData.getJunctions().get(i));
            junctionJo.put(inpData.getJunctions().get(i).getName(),juncJo);
        }
        jo.put("junction",junctionJo);

        JSONObject storageJo = new JSONObject();
        if (null != inpData.getStorages())
        {
            for (int i = 0; i < inpData.getStorages().size(); i++) {
                JSONObject storJo = (JSONObject) JSON.toJSON(inpData.getStorages().get(i));
                storageJo.put(inpData.getStorages().get(i).getName(),storJo);
            }
            jo.put("storage",storageJo);
        }


        JSONObject outfallJo = new JSONObject();
        if (null != inpData.getOutfalls())
        {
            for (int i = 0; i < inpData.getOutfalls().size(); i++) {
                JSONObject outJo = (JSONObject) JSON.toJSON(inpData.getOutfalls().get(i));
                outfallJo.put(inpData.getOutfalls().get(i).getName(),outJo);
            }
            jo.put("outfall",outfallJo);
        }


        return jo;
    }

    /**将coordinates转成geojson*/
    public JSONObject getGeoJsonFromInp(JSONObject wrapperedInp)
    {


        JSONObject jo = new JSONObject();
        jo.put("type","FeatureCollection");
        // 点
        List<InpData.Coordinate> coordinateList = this.getInpData().getCoordinates();
        List<JSONObject> features = new ArrayList<>();
        if(coordinateList!=null) {
            for (int i = 0; i < coordinateList.size(); i++) {
                JSONObject feature = new JSONObject();
                feature.put("type", "Feature");

                JSONObject geometry = new JSONObject();
                geometry.put("type", "Point");
                double[] coordinate = new double[]{coordinateList.get(i).getX_coord(), coordinateList.get(i).getY_coord()};
                geometry.put("coordinates", coordinate);
                feature.put("geometry", geometry);

                JSONObject properties = new JSONObject();
                String name = coordinateList.get(i).getNode();
                properties.put("name", name);
                if (wrapperedInp.getJSONObject("junction").containsKey(name))
                {
                    properties.put("geoType","junction");
                    properties.putAll(wrapperedInp.getJSONObject("junction").getJSONObject(name));
                }
                else if (null != wrapperedInp.getJSONObject("storage") && wrapperedInp.getJSONObject("storage").containsKey(name))
                {
                    properties.put("geoType","storage");
                    properties.putAll(wrapperedInp.getJSONObject("storage").getJSONObject(name));
                }
                else if (wrapperedInp.getJSONObject("outfall").containsKey(name))
                {
                    properties.put("geoType","outfall");
                    properties.putAll(wrapperedInp.getJSONObject("outfall").getJSONObject(name));
                }
                feature.put("properties", properties);

                features.add(feature);
            }
        }
        // 线

        List<InpData.Conduit> conduitList = this.getInpData().getConduits();
        if(conduitList!=null) {
            for (int i = 0; i < conduitList.size(); i++) {
                JSONObject feature = new JSONObject();
                feature.put("type", "Feature");

                JSONObject geometry = new JSONObject();
                geometry.put("type", "LineString");

                Polyline polyline = getPolylineCoordinates(conduitList.get(i));
                double[][] coordinates = {{polyline.getFrom_x(), polyline.getFrom_y()}, {polyline.getTo_x(), polyline.getTo_y()}};
                geometry.put("coordinates", coordinates);
                feature.put("geometry", geometry);

                JSONObject properties = new JSONObject();
                String name = conduitList.get(i).getName();
                properties.put("name", name);
                if (wrapperedInp.getJSONObject("conduit").containsKey(name))
                {
                    properties.put("geoType","conduit");
                    properties.putAll(wrapperedInp.getJSONObject("conduit").getJSONObject(name));
                }
                feature.put("properties", properties);

                features.add(feature);
            }
        }

        // 面
        List<Polygon> polygonList = getPolygonCoordinates();
        //生成polygon的geojson
        if(polygonList!=null) {
            for (int i = 0; i < polygonList.size(); i++) {
                JSONObject feature = new JSONObject();
                feature.put("type", "Feature");

                JSONObject geometry = new JSONObject();
                geometry.put("type", "Polygon");

                Polygon polygon = polygonList.get(i);
                double[][][] coordinates = new double[1][polygon.getCoordinateList().size()][2];
                for (int j = 0; j < polygon.getCoordinateList().size(); j++) {
                    coordinates[0][j] = new double[2];
                    coordinates[0][j][0] = polygon.getCoordinateList().get(j).getX();
                    coordinates[0][j][1] = polygon.getCoordinateList().get(j).getY();
                }

                geometry.put("coordinates", coordinates);
                feature.put("geometry", geometry);

                JSONObject properties = new JSONObject();
                String name = polygonList.get(i).getName();
                properties.put("name", name);
                if (wrapperedInp.getJSONObject("subcatchment").containsKey(name))
                {
                    properties.put("geoType","subcatchment");
                    properties.putAll(wrapperedInp.getJSONObject("subcatchment").getJSONObject(name));
                }
                feature.put("properties", properties);
                features.add(feature);
            }
        }
        jo.put("features",features);
        //TODO
        return jo;
    }

    @Data
    private class Polyline{
        private String name;
        private double from_x;
        private double from_y;
        private double to_x;
        private double to_y;
    }

    private Polyline getPolylineCoordinates(InpData.Conduit line)
    {
        Polyline polyline=new Polyline();
        String from_node=line.getFromnode();
        String to_node=line.getTonode();
        polyline.setName(line.getName());
        for (int i=0;i < inpLocal.get().getCoordinates().size();i++)
        {
            InpData.Coordinate point=inpLocal.get().getCoordinates().get(i);
            if (from_node.equals(point.getNode()))
            {
                polyline.setFrom_x(point.getX_coord());
                polyline.setFrom_y(point.getY_coord());
            }
            else if (to_node.equals(point.getNode()))
            {
                polyline.setTo_x(point.getX_coord());
                polyline.setTo_y(point.getY_coord());
            }
        }
        return polyline;
    }

    @Data
    private class Point{
        private double x;
        private double y;
    }

    @Data
    private class Polygon{
        private String name;
        private List<Point> coordinateList;
    }
    private List<Polygon> getPolygonCoordinates()
    {
        List<InpData.Polygon> polygonList=inpLocal.get().getPolygons();
        List<Polygon> polygons=new ArrayList<>();
        List<JSONObject> jsonPolygonObjects=new ArrayList<>();
        //求出polygon的个数及每个polygon对应的点
        for (int i=0;i<polygonList.size();i++)
        {
            Polygon polygon = new Polygon();
            polygon.setName(polygonList.get(i).getPolygon());
            List<Point> pointList=new ArrayList<>();
            while (i<polygonList.size()-1 && polygonList.get(i).getPolygon().equals(polygonList.get(i + 1).getPolygon())) {
                Point point=new Point();
                point.setX(polygonList.get(i).getX_coord());
                point.setY(polygonList.get(i).getY_coord());
                pointList.add(point);
                i++;
            }
            Point point=new Point();
            point.setX(polygonList.get(i).getX_coord());
            point.setY(polygonList.get(i).getY_coord());
            pointList.add(point);
            polygon.setCoordinateList(pointList);
            polygons.add(polygon);

        }
        return polygons;
    }
}

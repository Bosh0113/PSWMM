package com.geofuturelab.pswmm.vo;

import com.geofuturelab.pswmm.Entity.DataAuthority;
import com.geofuturelab.pswmm.Entity.InpData;
import lombok.Data;

import java.util.List;

/**
 * @Author: David.Xiao
 * @Date: 2019/12/19 09:54
 * @Description:
 */
@Data
public class InpDataVo {
    /** 元数据*/
    private String id;

    private String email;

    private String instanceId;

    private String projection;

    private DataAuthority authority;

    //TITLE
    private List<String> title;

    //OPTIONS
    @Data
    public static class Option
    {
        private String flow_units = "CFS";
        private String infiltration = "HORTON";
        private String flow_routing = "KINWAVE";
        private String link_offsets = "DEPTH";
        private String force_main_equation = "H-W";
        private String ignore_rainfall = "NO";

        private String ignore_snowmelt = "NO";  //新添加
        private String ignore_groundwater = "NO"; //新添加
        private String ignore_routing = "NO";
        private String ignore_quality= "NO";   //新添加
        private String allow_ponding= "NO";
        private String skip_steady_state = "NO";
        private String start_date = "1/1/2002";
        private String start_time = "0:00:00";
        private String end_date = "1/1/2002";
        private String end_time = "24:00:00";
        private String report_start_date = "1/1/2002";
        private String report_start_time = "0:00:00";
        private String sweep_start = "1/1";
        private String sweep_end = "12/31";
        private float dry_days = 0;
        private String report_step = "0:15:00";
        private String wet_step = "0:05:00";
        private String dry_step = "1:00:00";
        private String routing_step = "0:05:00";
        private float lengthening_step = 0;
        private float variable_step = 0;
        private String inertial_damping = "PARTIAL";
        private String normal_flow_limited = "BOTH";
        private float min_surfarea = 1.167f;
        private float min_slope = 0;
        //SWMM手册里没有，但是INP文件中出现的
        private float max_trials = 8;
        private float head_tolerance = 0.0015f;
        private float sys_flow_tol = 5;
        private float lat_flow_tol = 5;
    }
    private InpData.Option option;

    //EVAPORATION
    @Data
    public static class Evaporation
    {
        private String selected_source = "CONSTANT";//从CONSTANT,TIMESERIES,FILE,MONTHLY,TEMPERATURE中选哪个
        private float constant = 0.0f;
        private String timeseries;
        private float[] file;
        private float[] monthly;
        private String temperature;
        private String recovery;
        private String dryonly = "NO";
    }
    private InpData.Evaporation evaporation;

    public abstract static class AbstractInpPart
    {
        abstract public void setDescription(List<String> description);
    }

    //RAINGAGES
    @Data
    public static class Raingage extends InpData.AbstractInpPart
    {
        private List<String> description;
        private String gage;
        private String format = "INTENSITY";
        private String interval = "1:00";
        private float scf = 1.0f;
        private String datasource = "TIMESERIES";
        private String timeseries; //改变
        private String filename;
        private String stationid;
        private String rainunits;
    }
    private List<InpData.Raingage> raingages;


    //SUBCATCHMENTS
    @Data
    public static class Subcatchment extends InpData.AbstractInpPart
    {
        private List<String> description;
        private String subcatchment;
        private String raingage;
        private String outlet;
        private float area;
        private float imperv;
        private float width;
        private float slope;
        private float curblen;
        private String snowpack;
    }
    private List<InpData.Subcatchment> subcatchments;


    //SUBAREAS
    @Data
    public static class Subarea
    {
        private String subcatchment;
        private float n_imperv;
        private float n_perv;
        private float s_imperv;
        private float s_perv;
        private float pctzero;
        private String routeto = "OUTLET";
        private float pctrouted = 100;
    }
    private List<InpData.Subarea> subareas;


    //INFILTRATION
    @Data
    public static class Infiltration
    {
        private String subcatchment;
        // Horton
        private float maxrate;
        private float minrate;
        private float decay;
        private float drytime;
        private float maxinfil;
        // green ampt
        private float suction;
        private float conductivity;
        private float initialDeficit;
        // curve number
        private float curveNumber;
    }
    private List<InpData.Infiltration> infiltration;


    //JUNCTIONS
    @Data
    public static class Junction extends InpData.AbstractInpPart
    {
        private List<String> description;
        private String junction;
        private float invert;
        private float maxdepth = 0;
        private float initdepth = 0;
        private float surdepth = 0;
        private float aponded = 0;
    }
    private List<InpData.Junction> junctions;


    //Outfalls
    @Data
    public static class Outfall extends InpData.AbstractInpPart
    {
        private List<String> description;
        private String outfall;
        private float invert;
        private String type;
        private String stagedata;
        private String gated;
    }
    private List<InpData.Outfall> outfalls;


    //STORAGE
    @Data
    public static class Storage extends InpData.AbstractInpPart
    {
        private List<String> description;
        private String storage;
        private float invert;
        private float maxdepth;
        private float initdepth;
        private String shape;
        private String curvename;
        private float[] param = new float[3];
        private float ponded = 0;
        private float fevap = 0;
        //        private String seepage_exist;   //YES OR NO？
        private float suction_head;
        private float conductivity;
        private float initial_deficit;
    }
    private List<InpData.Storage> storage;


    //CONDUITS
    @Data
    public static class Conduit extends InpData.AbstractInpPart
    {
        private List<String> description;
        private String conduit;
        private String fromnode;
        private String tonode;
        private float length;
        private float roughness;
        private float inoffset;
        private float outoffset;
        private float initflow = 0;
        private float maxflow;
    }
    private List<InpData.Conduit> conduits;

    //XSECTIONS
    @Data
    public static class Xscetion
    {
        private String link;
        private String shape;
        private float geom1;
        private float geom2;
        private float geom3;
        private float geom4;
        private int barrels;
        private String culvertcode;
    }
    private List<InpData.Xscetion> xscetions;

    //LOSSES
    @Data
    public static class Losses
    {
        private String culvert;
        private float entry_loss_coeff;
        private float exit_loss_coeff;
        private float avg_loss_coeff;
        private String flap_gate = "NO";
        private float seepage_loss_rate;
    }
    private List<InpData.Losses> losses;


    //CURVES
    @Data
    public static class CurveXY
    {
        private float x_value;
        private float y_value;
    }

    @Data
    public static class Curve extends InpData.AbstractInpPart
    {
        private String curve;
        private String type;
        private List<String> description;
        private List<InpData.CurveXY> coordinates;
    }
    private List<InpData.Curve> curves;


    //TIMESERIES
    @Data
    public static class DateTimeValue
    {
        private String date;
        private String time;
        private float value;
    }

    @Data
    public static class Timeseries extends InpData.AbstractInpPart
    {
        private String name;
        private List<String> description;
        private List<InpData.DateTimeValue> dateTimeValues;
    }
    private List<InpData.Timeseries> timeseries;

    //REPORT
    @Data
    public static class Report
    {
        private String input = "NO";
        private String continuity = "YES";
        private String flowstats = "YES";
        private String controls = "NO";
        private String subcatchments = "ALL";
        private String nodes = "ALL";
        private String links = "ALL";
    }
    private InpData.Report report;

    //TAGS
    @Data
    public static class Tag
    {
        private String category;
        private String tagId;
        private String content;
    }
    private List<InpData.Tag> tag;


    //MAP
    @Data
    public static class Map
    {
        private float[] dimensions = new float[4];
        private String units;
    }
    private InpData.Map map;


    //SYMBOLS
    @Data
    public static class Symbol
    {
        private String gage;
        private double x_coord;
        private double y_coord;
    }
    private List<InpData.Symbol> symbols;
}

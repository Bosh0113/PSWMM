package com.geofuturelab.pswmm.Dao.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Dao.IRptDataDao;
import com.geofuturelab.pswmm.Entity.RptData;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RptDataDao implements IRptDataDao {
    private static ThreadLocal<RptData> rptLocal = new ThreadLocal<>();
    private static ThreadLocal<List<String>> linesLocal = new ThreadLocal<>();
    @Override
    public RptData readRptFile(String fileName) throws IOException{
        //读取数据并进行处理，先尝试传统方式
        RptData rptData = new RptData();
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
        String line;
        while ((line = bufferedReader.readLine()) != null){
            switch (line.trim()) {
                case "":
                    break;
                case "Subcatchment Runoff Summary":
                    //去掉一行
                    bufferedReader.readLine();
                    skipTitle(bufferedReader);
                    List<RptData.SubcatchmentRunoffSummary> subcatchmentRunoffSummaries = readSubcatchmentRunoffSummary(bufferedReader);
                    rptData.setSubcatchmentRunoffSummaries(subcatchmentRunoffSummaries);
                    break;
                case "Node Depth Summary":
                    bufferedReader.readLine();
                    skipTitle(bufferedReader);
                    List<RptData.NodeDepthSummary> nodeDepthSummaries = readNodeDepthSummary(bufferedReader);
                    rptData.setNodeDepthSummaries(nodeDepthSummaries);
                    break;
                case "Node Inflow Summary":
                    bufferedReader.readLine();
                    skipTitle(bufferedReader);
                    List<RptData.NodeInflowSummary> nodeInflowSummaries = readNodeInflowSummary(bufferedReader);
                    rptData.setNodeInflowSummaries(nodeInflowSummaries);
                    break;
                case "Node Flooding Summary":
                    bufferedReader.readLine();
                    skipTitle(bufferedReader);
                    List<RptData.NodeFloodingSummary> nodeFloodingSummaries = readNodeFloodingSummary(bufferedReader);
                    rptData.setNodeFloodingSummaries(nodeFloodingSummaries);
                    break;
                case "Node Surcharge Summary":
                    bufferedReader.readLine();
                    skipTitle(bufferedReader);
                    List<RptData.NodeSurchargeSummary> nodeSurchargeSummaries = readNodeSurchargeSummary(bufferedReader);
                    rptData.setNodeSurchargeSummaries(nodeSurchargeSummaries);
                    break;
                case "Storage Volume Summary":
                    bufferedReader.readLine();
                    skipTitle(bufferedReader);
                    List<RptData.StorageVolumeSummary> storageVolumeSummaries = readStorageVolumeSummary(bufferedReader);
                    rptData.setStorageVolumeSummaries(storageVolumeSummaries);
                    break;
                case "Outfall Loading Summary":
                    bufferedReader.readLine();
                    skipTitle(bufferedReader);
                    List<RptData.OutfallLoadingSummary> outfallLoadingSummaries = readOutfallLoadingSummary(bufferedReader);
                    rptData.setOutfallLoadingSummaries(outfallLoadingSummaries);
                    break;
                case "Link Flow Summary":
                    bufferedReader.readLine();
                    skipTitle(bufferedReader);
                    List<RptData.LinkFlowSummary> linkFlowSummaries = readLinkFlowSummary(bufferedReader);
                    rptData.setLinkFlowSummaries(linkFlowSummaries);
                    break;
                case "Conduit Surcharge Summary":
                    bufferedReader.readLine();
                    skipTitle(bufferedReader);
                    List<RptData.ConduitSurchargeSummary> conduitSurchargeSummaries = readConduitSurchargeSummary(bufferedReader);
                    rptData.setConduitSurchargeSummaries(conduitSurchargeSummaries);
                    break;
                case "Subcatchment Results":
                    bufferedReader.readLine();
                    String line2 = skipTitle(bufferedReader);
                    Map<String, List<RptData.SubcatchmentResult>> subcatchmentResults = readSubcatchmentResults(bufferedReader,line2);
                    rptData.setSubcatchmentResultsMap(subcatchmentResults);
                    break;
                case "Node Results":
                    bufferedReader.readLine();
                    String line3 = skipTitle(bufferedReader);
                    Map<String, List<RptData.NodeResult>> nodeResults = readNodeResults(bufferedReader, line3);
                    rptData.setNodeResultsMap(nodeResults);
                    break;
                case "Link Results":
                    bufferedReader.readLine();
                    String line4 = skipTitle(bufferedReader);
                    Map<String, List<RptData.LinkResult>> linkResults = readLinkResults(bufferedReader, line4);
                    rptData.setLinkResultsMap(linkResults);
                    break;
                default:
                    break;
            }
        }
        return rptData;
    }

    @Override
    public RptData readRptFileByProperties(String fileName, List<String> properties)throws IOException {
        //读取数据并进行处理，先尝试传统方式
        RptData rptData = new RptData();
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
        String line;
        while ((line = bufferedReader.readLine()) != null){
            switch (line.trim()) {
                case "":
                    break;
                case "Subcatchment Runoff Summary":
                    if(properties.contains("Subcatchment Runoff Summary")){
                        //去掉一行
                        bufferedReader.readLine();
                        skipTitle(bufferedReader);
                        List<RptData.SubcatchmentRunoffSummary> subcatchmentRunoffSummaries = readSubcatchmentRunoffSummary(bufferedReader);
                        rptData.setSubcatchmentRunoffSummaries(subcatchmentRunoffSummaries);
                    }
                    break;
                case "Node Depth Summary":
                    if(properties.contains("Node Depth Summary")){
                        bufferedReader.readLine();
                        skipTitle(bufferedReader);
                        List<RptData.NodeDepthSummary> nodeDepthSummaries = readNodeDepthSummary(bufferedReader);
                        rptData.setNodeDepthSummaries(nodeDepthSummaries);
                    }
                    break;
                case "Node Inflow Summary":
                    if(properties.contains("Node Inflow Summary")){
                        bufferedReader.readLine();
                        skipTitle(bufferedReader);
                        List<RptData.NodeInflowSummary> nodeInflowSummaries = readNodeInflowSummary(bufferedReader);
                        rptData.setNodeInflowSummaries(nodeInflowSummaries);
                    }
                    break;
                case "Node Flooding Summary":
                    if(properties.contains("Node Flooding Summary")){
                        bufferedReader.readLine();
                        skipTitle(bufferedReader);
                        List<RptData.NodeFloodingSummary> nodeFloodingSummaries = readNodeFloodingSummary(bufferedReader);
                        rptData.setNodeFloodingSummaries(nodeFloodingSummaries);
                    }
                    break;
                case "Node Surcharge Summary":
                    if(properties.contains("Node Surcharge Summary")){
                        bufferedReader.readLine();
                        skipTitle(bufferedReader);
                        List<RptData.NodeSurchargeSummary> nodeSurchargeSummaries = readNodeSurchargeSummary(bufferedReader);
                        rptData.setNodeSurchargeSummaries(nodeSurchargeSummaries);
                    }
                    break;
                case "Storage Volume Summary":
                    if(properties.contains("Storage Volume Summary")){
                        bufferedReader.readLine();
                        skipTitle(bufferedReader);
                        List<RptData.StorageVolumeSummary> storageVolumeSummaries = readStorageVolumeSummary(bufferedReader);
                        rptData.setStorageVolumeSummaries(storageVolumeSummaries);
                    }
                    break;
                case "Outfall Loading Summary":
                    if(properties.contains("Outfall Loading Summary")){
                        bufferedReader.readLine();
                        skipTitle(bufferedReader);
                        List<RptData.OutfallLoadingSummary> outfallLoadingSummaries = readOutfallLoadingSummary(bufferedReader);
                        rptData.setOutfallLoadingSummaries(outfallLoadingSummaries);
                    }
                    break;
                case "Link Flow Summary":
                    if(properties.contains("Link Flow Summary")){
                        bufferedReader.readLine();
                        skipTitle(bufferedReader);
                        List<RptData.LinkFlowSummary> linkFlowSummaries = readLinkFlowSummary(bufferedReader);
                        rptData.setLinkFlowSummaries(linkFlowSummaries);
                    }
                    break;
                case "Conduit Surcharge Summary":
                    if(properties.contains("Conduit Surcharge Summary")){
                        bufferedReader.readLine();
                        skipTitle(bufferedReader);
                        List<RptData.ConduitSurchargeSummary> conduitSurchargeSummaries = readConduitSurchargeSummary(bufferedReader);
                        rptData.setConduitSurchargeSummaries(conduitSurchargeSummaries);
                    }
                    break;
                case "Subcatchment Results":
                    if(properties.contains("Subcatchment Results")){
                        bufferedReader.readLine();
                        String line2 = skipTitle(bufferedReader);
                        Map<String, List<RptData.SubcatchmentResult>> subcatchmentResults = readSubcatchmentResults(bufferedReader,line2);
                        rptData.setSubcatchmentResultsMap(subcatchmentResults);
                    }
                    break;
                case "Node Results":
                    if(properties.contains("Node Results")){
                        bufferedReader.readLine();
                        String line3 = skipTitle(bufferedReader);
                        Map<String, List<RptData.NodeResult>> nodeResults = readNodeResults(bufferedReader, line3);
                        rptData.setNodeResultsMap(nodeResults);
                    }
                    break;
                case "Link Results":
                    if(properties.contains("Link Results")){
                        bufferedReader.readLine();
                        String line4 = skipTitle(bufferedReader);
                        Map<String, List<RptData.LinkResult>> linkResults = readLinkResults(bufferedReader, line4);
                        rptData.setLinkResultsMap(linkResults);
                    }
                    break;
                default:
                    break;
            }
        }
        return rptData;
    }

    private String skipTitle(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine().trim();
        while (line.equals("") || line.startsWith("*")){
            line = bufferedReader.readLine().trim();
            if(line.startsWith("-") && line.endsWith("-")){
                line = bufferedReader.readLine().trim();
                while (!(line.startsWith("-") && line.endsWith("-"))){
                    line = bufferedReader.readLine().trim();
                }
            }
        }
        return line;
    }

    private void skipTitle2(BufferedReader bufferedReader)throws IOException {
        String line = bufferedReader.readLine().trim();
        if (line.startsWith("-")){
            line = bufferedReader.readLine().trim();
            while (!line.startsWith("-")){
                line = bufferedReader.readLine().trim();
            }
        }
    }

    @Override
    public JSONObject rpt2Json(RptData rptData)
    {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(rptData);
        return jsonObject;
    }

    private List<RptData.SubcatchmentRunoffSummary> readSubcatchmentRunoffSummary(BufferedReader bufferedReader) throws IOException {
        List<RptData.SubcatchmentRunoffSummary>  subcatchmentRunoffSummaries = new ArrayList<>();
        String line = bufferedReader.readLine().trim();
        while (line != null && !nextSection(line.trim())){
            if(line.equals("")){
                line = bufferedReader.readLine().trim();
                continue;
            }
            RptData.SubcatchmentRunoffSummary subcatchmentRunoffSummary = new RptData.SubcatchmentRunoffSummary();

            String[] tempStr=line.split("[ ]+");
            subcatchmentRunoffSummary.setSubcatchment(tempStr[0]);
            subcatchmentRunoffSummary.setTotalPrecip(tempStr[1]);
            subcatchmentRunoffSummary.setTotalRunon(tempStr[2]);
            subcatchmentRunoffSummary.setTotalEvap(tempStr[3]);
            subcatchmentRunoffSummary.setTotalInfil(tempStr[4]);
            subcatchmentRunoffSummary.setTotalRunoffMm(tempStr[5]);
            subcatchmentRunoffSummary.setTotalRunoffLtr(tempStr[6]);
            subcatchmentRunoffSummary.setPeakRunoff(tempStr[7]);
            subcatchmentRunoffSummary.setRunoffCoeff(tempStr[8]);
            subcatchmentRunoffSummaries.add(subcatchmentRunoffSummary);
            line = bufferedReader.readLine().trim();
        }
        return subcatchmentRunoffSummaries;
    }

    private List<RptData.NodeDepthSummary> readNodeDepthSummary(BufferedReader bufferedReader) throws IOException {
        List<RptData.NodeDepthSummary>  nodeDepthSummaries = new ArrayList<>();
        String line = bufferedReader.readLine().trim();
        while (line != null && !nextSection(line.trim())){
            if(line.equals("")){
                line = bufferedReader.readLine().trim();
                continue;
            }
            RptData.NodeDepthSummary nodeDepthSummary = new RptData.NodeDepthSummary();
            String[] tempStr=line.split("[ ]+");
            nodeDepthSummary.setNode(tempStr[0]);
            nodeDepthSummary.setType(tempStr[1]);
            nodeDepthSummary.setAverageDepth(tempStr[2]);
            nodeDepthSummary.setMaximumDepth(tempStr[3]);
            nodeDepthSummary.setMaximumHgl(tempStr[4]);
            nodeDepthSummary.setTimeOfMaxOccurrenceDay(tempStr[5]);
            nodeDepthSummary.setTimeOfMaxOccurrenceTime(tempStr[6]);
            nodeDepthSummaries.add(nodeDepthSummary);
            line = bufferedReader.readLine().trim();
        }
        return nodeDepthSummaries;
    }

    private List<RptData.NodeInflowSummary> readNodeInflowSummary(BufferedReader bufferedReader) throws IOException {
        List<RptData.NodeInflowSummary>  nodeInflowSummaries = new ArrayList<>();
        String line = bufferedReader.readLine().trim();
        while (line != null && !nextSection(line.trim())){
            if(line.equals("")){
                line = bufferedReader.readLine().trim();
                continue;
            }
            RptData.NodeInflowSummary nodeInflowSummary = new RptData.NodeInflowSummary();
            String[] tempStr=line.split("[ ]+");
            nodeInflowSummary.setNode(tempStr[0]);
            nodeInflowSummary.setType(tempStr[1]);
            nodeInflowSummary.setMaximumLateralInflow(tempStr[2]);
            nodeInflowSummary.setMaximumTotalInflow(tempStr[3]);
            nodeInflowSummary.setTimeOfMaxOccurrenceDay(tempStr[4]);
            nodeInflowSummary.setTimeOfMaxOccurrenceTime(tempStr[5]);
            nodeInflowSummary.setLateralInflowVolume(tempStr[6]);
            nodeInflowSummary.setTotalInflowVolume(tempStr[7]);
            nodeInflowSummaries.add(nodeInflowSummary);
            line = bufferedReader.readLine().trim();
        }
        return nodeInflowSummaries;
    }

    private List<RptData.NodeFloodingSummary> readNodeFloodingSummary(BufferedReader bufferedReader) throws IOException {
        List<RptData.NodeFloodingSummary>  nodeFloodingSummaries = new ArrayList<>();
        String line = bufferedReader.readLine().trim();
        while (line != null && !nextSection(line.trim())){
            if(line.equals("")){
                line = bufferedReader.readLine().trim();
                continue;
            }
            RptData.NodeFloodingSummary nodeFloodingSummary = new RptData.NodeFloodingSummary();
            String[] tempStr=line.split("[ ]+");
            nodeFloodingSummary.setNode(tempStr[0]);
            nodeFloodingSummary.setHoursFlooded(tempStr[1]);
            nodeFloodingSummary.setMaximumRate(tempStr[2]);
            nodeFloodingSummary.setTimeOfMaxOccurrenceDay(tempStr[3]);
            nodeFloodingSummary.setTimeOfMaxOccurrenceTime(tempStr[4]);
            nodeFloodingSummary.setTotalFloodVolume(tempStr[5]);
            nodeFloodingSummary.setMaximumPondedVolume(tempStr[6]);
            nodeFloodingSummaries.add(nodeFloodingSummary);
            line = bufferedReader.readLine().trim();
        }
        return nodeFloodingSummaries;
    }

    private List<RptData.NodeSurchargeSummary> readNodeSurchargeSummary(BufferedReader bufferedReader) throws IOException {
        List<RptData.NodeSurchargeSummary>  nodeSurchargeSummaries = new ArrayList<>();
        String line = bufferedReader.readLine().trim();
        while (line != null && !nextSection(line.trim())){
            if(line.equals("")){
                line = bufferedReader.readLine().trim();
                continue;
            }
            RptData.NodeSurchargeSummary nodeSurchargeSummary = new RptData.NodeSurchargeSummary();
            String[] tempStr=line.split("[ ]+");
            nodeSurchargeSummary.setNode(tempStr[0]);
            nodeSurchargeSummary.setType(tempStr[1]);
            nodeSurchargeSummary.setHoursSurcharged(tempStr[2]);
            nodeSurchargeSummary.setMaxHeightAboveCrown(tempStr[3]);
            nodeSurchargeSummary.setMinDepthBelowRim(tempStr[4]);
            nodeSurchargeSummaries.add(nodeSurchargeSummary);
            line = bufferedReader.readLine().trim();
        }
        return nodeSurchargeSummaries;
    }

    private List<RptData.StorageVolumeSummary> readStorageVolumeSummary(BufferedReader bufferedReader) throws IOException {
        List<RptData.StorageVolumeSummary>  storageVolumeSummaries = new ArrayList<>();
        String line = bufferedReader.readLine().trim();
        while (line != null && !nextSection(line.trim())){
            if(line.equals("")){
                line = bufferedReader.readLine().trim();
                continue;
            }
            RptData.StorageVolumeSummary storageVolumeSummary = new RptData.StorageVolumeSummary();
            String[] tempStr=line.split("[ ]+");
            storageVolumeSummary.setStorageUnit(tempStr[0]);
            storageVolumeSummary.setAverageVolume(tempStr[1]);
            storageVolumeSummary.setAvgPcntFull(tempStr[2]);
            storageVolumeSummary.setEvapPcntLoss(tempStr[3]);
            storageVolumeSummary.setExfilPcntLoss(tempStr[4]);
            storageVolumeSummary.setMaximumVolume(tempStr[5]);
            storageVolumeSummary.setMaxPcntFull(tempStr[6]);
            storageVolumeSummary.setTimeOfMaxOccurrenceDay(tempStr[7]);
            storageVolumeSummary.setTimeOfMaxOccurrenceTime(tempStr[8]);
            storageVolumeSummary.setMaximumOutflow(tempStr[9]);

            storageVolumeSummaries.add(storageVolumeSummary);
            line = bufferedReader.readLine().trim();
        }
        return storageVolumeSummaries;
    }

    private List<RptData.OutfallLoadingSummary> readOutfallLoadingSummary(BufferedReader bufferedReader) throws IOException {
        List<RptData.OutfallLoadingSummary>  outfallLoadingSummaries = new ArrayList<>();
        String line = bufferedReader.readLine().trim();
        while (line != null && !nextSection(line.trim())){
            if(line.equals("") || line.startsWith("-")){
                line = bufferedReader.readLine().trim();
                continue;
            }
            RptData.OutfallLoadingSummary outfallLoadingSummary = new RptData.OutfallLoadingSummary();
            String[] tempStr=line.split("[ ]+");
            outfallLoadingSummary.setOutfallNode(tempStr[0]);
            outfallLoadingSummary.setFlowFregPcnt(tempStr[1]);
            outfallLoadingSummary.setAvgFlow(tempStr[2]);
            outfallLoadingSummary.setMaxFlow(tempStr[3]);
            outfallLoadingSummary.setTotalVolume(tempStr[4]);
            outfallLoadingSummaries.add(outfallLoadingSummary);
            line = bufferedReader.readLine().trim();
        }
        return outfallLoadingSummaries;
    }

    private List<RptData.LinkFlowSummary> readLinkFlowSummary(BufferedReader bufferedReader) throws IOException {
        List<RptData.LinkFlowSummary>  linkFlowSummaries = new ArrayList<>();
        String line = bufferedReader.readLine().trim();
        while (line != null && !nextSection(line.trim())){
            if(line.equals("")){
                line = bufferedReader.readLine().trim();
                continue;
            }
            RptData.LinkFlowSummary linkFlowSummary = new RptData.LinkFlowSummary();
            String[] tempStr=line.split("[ ]+");
            linkFlowSummary.setLink(tempStr[0]);
            linkFlowSummary.setType(tempStr[1]);
            linkFlowSummary.setMaximumFlow(tempStr[2]);
            linkFlowSummary.setTimeOfMaxOccurrenceDay(tempStr[3]);
            linkFlowSummary.setTimeOfMaxOccurrenceTime(tempStr[4]);
            linkFlowSummary.setMaximumVeloc(tempStr[5]);
            linkFlowSummary.setMaxFullFlow(tempStr[6]);
            linkFlowSummary.setMaxFullDepth(tempStr[7]);
            linkFlowSummaries.add(linkFlowSummary);
            line = bufferedReader.readLine().trim();
        }
        return linkFlowSummaries;
    }

    private List<RptData.ConduitSurchargeSummary> readConduitSurchargeSummary(BufferedReader bufferedReader) throws IOException {
        List<RptData.ConduitSurchargeSummary>  conduitSurchargeSummaries = new ArrayList<>();
        String line = bufferedReader.readLine().trim();
        while (line != null && !nextSection(line.trim())){
            if(line.equals("")){
                line = bufferedReader.readLine().trim();
                continue;
            }
            RptData.ConduitSurchargeSummary conduitSurchargeSummary = new RptData.ConduitSurchargeSummary();
            String[] tempStr=line.split("[ ]+");
            conduitSurchargeSummary.setConduit(tempStr[0]);
            conduitSurchargeSummary.setBothEnds(tempStr[1]);
            conduitSurchargeSummary.setUpstream(tempStr[2]);
            conduitSurchargeSummary.setDnstream(tempStr[3]);
            conduitSurchargeSummary.setHoursAboveFullNormalFlow(tempStr[4]);
            conduitSurchargeSummary.setHoursCapacityLimited(tempStr[5]);
            conduitSurchargeSummaries.add(conduitSurchargeSummary);
            line = bufferedReader.readLine().trim();
        }
        return conduitSurchargeSummaries;
    }

    private Map<String, List<RptData.SubcatchmentResult>> readSubcatchmentResults(BufferedReader bufferedReader, String line) throws IOException {
        Map<String, List<RptData.SubcatchmentResult>> subcatchmentResultsMap = new HashMap<>();
        while (line != null && !nextSection(line.trim())){
            List<RptData.SubcatchmentResult> subcatchmentResults = new ArrayList<>();
            String subcatchName = "";
            if(line.startsWith("<") && line.endsWith(">")){
                String[] nameList = line.split("[ ]+");
                subcatchName = nameList[2];
                skipTitle2(bufferedReader);
            }
            line = bufferedReader.readLine().trim();
            while (line != null && !nextSection2(line) && !nextSection(line)){
                if(line.equals("")){
                    line = bufferedReader.readLine().trim();
                    continue;
                }
                RptData.SubcatchmentResult subcatchmentResult = new RptData.SubcatchmentResult();
                String[] tempStr=line.split("[ ]+");
                subcatchmentResult.setDate(tempStr[0]);
                subcatchmentResult.setTime(tempStr[1]);
                subcatchmentResult.setPrecip(tempStr[2]);
                subcatchmentResult.setLosses(tempStr[3]);
                subcatchmentResult.setRunoff(tempStr[4]);
                subcatchmentResults.add(subcatchmentResult);
                line = bufferedReader.readLine().trim();
            }
            subcatchmentResultsMap.put(subcatchName,subcatchmentResults);
        }
        return subcatchmentResultsMap;
    }

    private Map<String,List<RptData.NodeResult>> readNodeResults(BufferedReader bufferedReader,String line) throws IOException {
        Map<String,List<RptData.NodeResult>> nodeResultsMap = new HashMap<>();
        String key = "";
        while (line != null && !nextSection(line.trim())){
            List<RptData.NodeResult> nodeResults = new ArrayList<>();
            if(line.startsWith("<") && line.endsWith(">")){
                String[] nameList = line.split("[ ]+");
                key = nameList[2];
                skipTitle2(bufferedReader);
            }
            line = bufferedReader.readLine().trim();
            while (line != null && !nextSection2(line) && !nextSection(line)) {
                if (line.equals("")) {
                    line = bufferedReader.readLine().trim();
                    continue;
                }
                RptData.NodeResult nodeResult = new RptData.NodeResult();
                String[] tempStr=line.split("[ ]+");
                nodeResult.setDate(tempStr[0]);
                nodeResult.setTime(tempStr[1]);
                nodeResult.setInflow(tempStr[2]);
                nodeResult.setFlooding(tempStr[3]);
                nodeResult.setDepth(tempStr[4]);
                nodeResult.setHead(tempStr[5]);
                nodeResults.add(nodeResult);
                line = bufferedReader.readLine().trim();
            }
            nodeResultsMap.put(key,nodeResults);
        }
        return nodeResultsMap;
    }

    private Map<String, List<RptData.LinkResult>> readLinkResults(BufferedReader bufferedReader,String line) throws IOException {
        Map<String, List<RptData.LinkResult>> linkResultsMap = new HashMap<>();
        while (line != null && !nextSection(line.trim())){
            String linkName = "";
            List<RptData.LinkResult> linkResults = new ArrayList<>();
            if(line.startsWith("<") && line.endsWith(">")){
                String[] nameList = line.split("[ ]+");
                linkName = nameList[2];
                skipTitle2(bufferedReader);
            }
            line = bufferedReader.readLine().trim();
            while (line != null && !nextSection2(line) && !nextSection(line)) {
                if (line.equals("")) {
                    line = bufferedReader.readLine().trim();
                    continue;
                }
                RptData.LinkResult linkResult = new RptData.LinkResult();
                String[] tempStr=line.split("[ ]+");
                linkResult.setDate(tempStr[0]);
                linkResult.setTime(tempStr[1]);
                linkResult.setFlow(tempStr[2]);
                linkResult.setVelocity(tempStr[3]);
                linkResult.setDepth(tempStr[4]);
                linkResult.setCapacity(tempStr[5]);
                linkResults.add(linkResult);
                line = bufferedReader.readLine().trim();
            }
            linkResultsMap.put(linkName, linkResults);
        }
        return linkResultsMap;
    }

    private int readNodeResults2(int index){
        List<String> lines = linesLocal.get();
        List<RptData.NodeResult> nodeResultsList = new ArrayList<>();
        Map<String,List<RptData.NodeResult>> nodeResultsMap = new HashMap<>();
        String key = "";
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            List<RptData.NodeResult> nodeResults = new ArrayList<>();
            String line = lines.get(index).trim();
            if (line.startsWith("<") && line.endsWith(">"))
            {
                String[] nameList = line.split("[ ]+");
                key = nameList[2];
                index = skipTitle2(index+1,lines);
            }
            while (index < lines.size() && !nextSection2(lines.get(index).trim()) &&!nextSection(lines.get(index).trim()))
            {
                line = lines.get(index).trim();
                if ("".equals(line))
                {
                    index++;
                    continue;
                }
                RptData.NodeResult nodeResult = new RptData.NodeResult();
                String[] tempStr=line.split("[ ]+");
                nodeResult.setDate(tempStr[0]);
                nodeResult.setTime(tempStr[1]);
                nodeResult.setInflow(tempStr[2]);
                nodeResult.setFlooding(tempStr[3]);
                nodeResult.setDepth(tempStr[4]);
                nodeResult.setHead(tempStr[5]);
                nodeResults.add(nodeResult);
                index++;
            }
            for (int i = 0; i < nodeResults.size(); i++) {
                if (Float.parseFloat(nodeResults.get(i).getFlooding())  >= 0.1)
                {
                    nodeResultsMap.put(key,nodeResults);
                    break;
                }
            }

        }
        rptLocal.get().setNodeResultsMap(nodeResultsMap);
        return index;
    }

    private int skipTitle(int index, List<String> lines)
    {
        String line = lines.get(index).trim();
        while (line.equals("") || line.startsWith("*"))
        {
            line = lines.get(++index).trim();
            if (line.startsWith("-") && line.endsWith("-"))
            {
                line = lines.get(++index).trim();
                while (!(line.startsWith("-") && line.endsWith("-")))
                {
                    line = lines.get(++index).trim();
                }
                index++;
            }
        }
        return index;
    }

    private int skipTitle2(int index, List<String> lines)
    {
        String line = lines.get(index).trim();
        if (line.startsWith("-"))
        {
            line = lines.get(++index).trim();
            while (!line.startsWith("-"))
            {
                line = lines.get(++index).trim();
            }
            index++;
        }
        return index;
    }

    private boolean nextSection(String line)
    {
        return (line.startsWith("*")||line.startsWith("Analysis"));
    }

    private boolean nextSection2(String line)
    {
        return (line.startsWith("<"));
    }

}

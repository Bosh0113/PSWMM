package com.geofuturelab.pswmm.Dao.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Dao.IRptDataDao;
import com.geofuturelab.pswmm.Entity.RptData;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    public RptData readRptFile(String fileName) throws IOException {
        rptLocal.set(new RptData());
//        System.out.println(Paths.get(fileName));
        linesLocal.set(Files.readAllLines(Paths.get(fileName),Charset.forName("UTF-8")));
        List<String> lines = linesLocal.get();
        for (int cursor = 0; cursor < lines.size(); cursor++) {
            String line =  (lines.get(cursor)).trim();
            switch (line) {
                case "":
                    break;
                case "Subcatchment Runoff Summary":
                    cursor = readSubcatchmentRunoffSummary(cursor);break;
                case "Node Depth Summary":
                    cursor = readNodeDepthSummary(cursor);break;
                case "Node Inflow Summary":
                    cursor = readNodeInflowSummary(cursor);break;
                case "Node Flooding Summary":
                    cursor = readNodeFloodingSummary(cursor);break;
                case "Node Surcharge Summary":
                    cursor = readNodeSurchargeSummary(cursor);break;
                case "Storage Volume Summary":
                    cursor = readStorageVolumeSummary(cursor);break;
                case "Outfall Loading Summary":
                    cursor = readOutfallLoadingSummary(cursor);break;
                case "Link Flow Summary":
                    cursor = readLinkFlowSummary(cursor);break;
                case "Conduit Surcharge Summary":
                    cursor = readConduitSurchargeSummary(cursor);break;
                case "Subcatchment Results":
                    cursor = readSubcatchmentResults(cursor);break;
                case "Node Results":
                    cursor = readNodeResults(cursor);break;
                case "Link Results":
                    cursor = readLinkResults(cursor);break;
                default:
                    break;
            }
        }
        return rptLocal.get();
    }

    public JSONObject rpt2Json(RptData rptData)
    {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(rptData);
        return jsonObject;
    }

    private int readSubcatchmentRunoffSummary(int index){
        List<String> lines = linesLocal.get();
        List<RptData.SubcatchmentRunoffSummary>  subcatchmentRunoffSummaries = new ArrayList<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String line = lines.get(index).trim();
            if ("".equals(line))
            {
                index++;
                continue;
            }
            RptData.SubcatchmentRunoffSummary subcatchmentRunoffSummary = new RptData.SubcatchmentRunoffSummary();

            String[] tempStr=line.split("[ ]+");
            subcatchmentRunoffSummary.setSubcatchment(tempStr[0]);
            subcatchmentRunoffSummary.setTotalPrecip(tempStr[1]);
            subcatchmentRunoffSummary.setTotalRunon(tempStr[2]);
            subcatchmentRunoffSummary.setTotalEvap(tempStr[3]);
            subcatchmentRunoffSummary.setTotalInfil(tempStr[4]);
//            subcatchmentRunoffSummary.setImpervRunoff(tempStr[5]);
//            subcatchmentRunoffSummary.setPervRunoff(tempStr[6]);
            subcatchmentRunoffSummary.setTotalRunoffMm(tempStr[5]);
            subcatchmentRunoffSummary.setTotalRunoffLtr(tempStr[6]);
            subcatchmentRunoffSummary.setPeakRunoff(tempStr[7]);
            subcatchmentRunoffSummary.setRunoffCoeff(tempStr[8]);
            subcatchmentRunoffSummaries.add(subcatchmentRunoffSummary);
            index++;
        }
        rptLocal.get().setSubcatchmentRunoffSummaries(subcatchmentRunoffSummaries);
        return index;
    }

    private int readNodeDepthSummary(int index){
        List<String> lines = linesLocal.get();
        List<RptData.NodeDepthSummary>  nodeDepthSummaries = new ArrayList<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String line = lines.get(index).trim();
            if ("".equals(line))
            {
                index++;
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
//            nodeDepthSummary.setReportedMaxDepth(tempStr[7]);
            nodeDepthSummaries.add(nodeDepthSummary);
            index++;
        }
        rptLocal.get().setNodeDepthSummaries(nodeDepthSummaries);
        return index;
    }

    private int readNodeInflowSummary(int index){
        List<String> lines = linesLocal.get();
        List<RptData.NodeInflowSummary>  nodeInflowSummaries = new ArrayList<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String line = lines.get(index).trim();
            if ("".equals(line))
            {
                index++;
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
            index++;
        }
        rptLocal.get().setNodeInflowSummaries(nodeInflowSummaries);
        return index;
    }

    private int readNodeFloodingSummary(int index){
        List<String> lines = linesLocal.get();
        List<RptData.NodeFloodingSummary>  nodeFloodingSummaries = new ArrayList<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String line = lines.get(index).trim();
            if ("".equals(line))
            {
                index++;
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
            index++;
        }
        rptLocal.get().setNodeFloodingSummaries(nodeFloodingSummaries);
        return index;
    }

    private int readNodeSurchargeSummary(int index){
        List<String> lines = linesLocal.get();
        List<RptData.NodeSurchargeSummary>  nodeSurchargeSummaries = new ArrayList<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String line = lines.get(index).trim();
            if ("".equals(line))
            {
                index++;
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
            index++;
        }
        rptLocal.get().setNodeSurchargeSummaries(nodeSurchargeSummaries);
        return index;
    }

    private int readStorageVolumeSummary(int index){
        List<String> lines = linesLocal.get();
        List<RptData.StorageVolumeSummary>  storageVolumeSummaries = new ArrayList<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String line = lines.get(index).trim();
            if ("".equals(line))
            {
                index++;
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
            index++;
        }
        rptLocal.get().setStorageVolumeSummaries(storageVolumeSummaries);
        return index;
    }

    private int readOutfallLoadingSummary(int index){
        List<String> lines = linesLocal.get();
        List<RptData.OutfallLoadingSummary>  outfallLoadingSummaries = new ArrayList<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String line = lines.get(index).trim();
            if ("".equals(line) || line.startsWith("-"))
            {
                index++;
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
            index++;
        }
        rptLocal.get().setOutfallLoadingSummaries(outfallLoadingSummaries);
        return index;
    }

    private int readLinkFlowSummary(int index){
        List<String> lines = linesLocal.get();
        List<RptData.LinkFlowSummary>  linkFlowSummaries = new ArrayList<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String line = lines.get(index).trim();
            if ("".equals(line))
            {
                index++;
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
            index++;
        }
        rptLocal.get().setLinkFlowSummaries(linkFlowSummaries);
        return index;
    }

    private int readConduitSurchargeSummary(int index){
        List<String> lines = linesLocal.get();
        List<RptData.ConduitSurchargeSummary>  conduitSurchargeSummaries = new ArrayList<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String line = lines.get(index).trim();
            if ("".equals(line))
            {
                index++;
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
            index++;
        }
        rptLocal.get().setConduitSurchargeSummaries(conduitSurchargeSummaries);
        return index;
    }

    private int readSubcatchmentResults(int index){
        List<String> lines = linesLocal.get();
        Map<String, List<RptData.SubcatchmentResult>> subcatchmentResultsMap = new HashMap<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            List<RptData.SubcatchmentResult> subcatchmentResults = new ArrayList<>();
            String subcatchName = "";
            String line = lines.get(index).trim();
            if (line.startsWith("<") && line.endsWith(">"))
            {
                String[] nameList = line.split("[ ]+");
                subcatchName =nameList[2];
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
                RptData.SubcatchmentResult subcatchmentResult = new RptData.SubcatchmentResult();
                String[] tempStr=line.split("[ ]+");
                subcatchmentResult.setDate(tempStr[0]);
                subcatchmentResult.setTime(tempStr[1]);
                subcatchmentResult.setPrecip(tempStr[2]);
                subcatchmentResult.setLosses(tempStr[3]);
                subcatchmentResult.setRunoff(tempStr[4]);
                subcatchmentResults.add(subcatchmentResult);
                index++;
            }
            subcatchmentResultsMap.put(subcatchName, subcatchmentResults);
        }
        rptLocal.get().setSubcatchmentResultsMap(subcatchmentResultsMap);
        return index;
    }

    private int readNodeResults(int index){
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
            nodeResultsMap.put(key,nodeResults);
        }
        rptLocal.get().setNodeResultsMap(nodeResultsMap);
        return index;
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

    private int readLinkResults(int index){
        List<String> lines = linesLocal.get();
        Map<String, List<RptData.LinkResult>> linkResultsMap = new HashMap<>();
        index++;
        index = skipTitle(index,lines);
        while (index < lines.size() && !nextSection(lines.get(index).trim()))
        {
            String linkName = "";
            List<RptData.LinkResult> linkResults = new ArrayList<>();
            String line = lines.get(index).trim();
            if (line.startsWith("<") && line.endsWith(">"))
            {
                String[] nameList = line.split("[ ]+");
                linkName = nameList[2];
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
                RptData.LinkResult linkResult = new RptData.LinkResult();
                String[] tempStr=line.split("[ ]+");
                linkResult.setDate(tempStr[0]);
                linkResult.setTime(tempStr[1]);
                linkResult.setFlow(tempStr[2]);
                linkResult.setVelocity(tempStr[3]);
                linkResult.setDepth(tempStr[4]);
                linkResult.setCapacity(tempStr[5]);
                linkResults.add(linkResult);
                index++;
            }
            linkResultsMap.put(linkName, linkResults);
        }
        rptLocal.get().setLinkResultsMap(linkResultsMap);
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
        return (line.startsWith("*"));
    }

    private boolean nextSection2(String line)
    {
        return (line.startsWith("<"));
    }

}

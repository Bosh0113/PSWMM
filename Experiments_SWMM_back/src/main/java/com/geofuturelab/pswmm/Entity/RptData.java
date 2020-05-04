package com.geofuturelab.pswmm.Entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RptData {

        @Data
        public static class AnalysisOptions
        {
            public String flow_units;
            public String rainfall_runoff;
            public String rdii;
            public String snowmelt;
            public String groundwater;
            public String flow_routing;
            public String ponding_allowed;
            public String water_quality;
            public String infiltration_method;
            public String flow_routing_method;
            public String starting_date;
            public String ending_date;
            public String antecedent_dry_days;
            public String report_time_step;
            public String wet_time_step;
            public String dry_time_step;
            public String routing_time_step;
        }
        private AnalysisOptions analysisOptions;

        //
        public static class TotalPrecipitation
        {
            public String volume;
            public String depth;
        }
        public static class EvaporationLoss
        {
            public String volume;
            public String depth;
        }
        public static class InfiltrationLoss
        {
            public String volume;
            public String depth;
        }
        public static class SurfaceRunoff
        {
            public String volume;
            public String depth;
        }
        public static class FinalSurfaceStorage
        {
            public String volume;
            public String depth;
        }
        @Data
        public static class RunoffQuantityContinuity
        {
            public TotalPrecipitation totalPrecipitation;
            public EvaporationLoss evaporationLoss;
            public InfiltrationLoss infiltrationLoss;
            public SurfaceRunoff surfaceRunoff;
            public FinalSurfaceStorage finalSurfaceStorage;
            public String continuity_error;
        }
        private RunoffQuantityContinuity runoffQuantityContinuity;

        //
        public static class DryWeatherInflow
        {
            public String hectare_m;
            public String ltr;
        }
        public static class WetWeatherInflow
        {
            public String hectare_m;
            public String ltr;
        }
        public static class GroundWaterInflow
        {
            public String hectare_m;
            public String ltr;
        }
        public static class RDIIInflow
        {
            public String hectare_m;
            public String ltr;
        }
        public static class ExternalInflow
        {
            public String hectare_m;
            public String ltr;
        }
        public static class ExternalOutflow
        {
            public String hectare_m;
            public String ltr;
        }
        public static class InternalOutflow
        {
            public String hectare_m;
            public String ltr;
        }
        public static class EvaporationLoss2
        {
            public String hectare_m;
            public String ltr;
        }
        public static class ExfiltrationLoss
        {
            public String hectare_m;
            public String ltr;
        }
        public static class InitialStoredVolume
        {
            public String hectare_m;
            public String ltr;
        }
        public static class FinalStoredVolume
        {
            public String hectare_m;
            public String ltr;
        }
        @Data
        public static class FlowRoutingContinuity
        {
            public DryWeatherInflow pDryWeatherInflow;
            public WetWeatherInflow pWetWeatherInflow;
            public GroundWaterInflow pGroundWaterInflow;
            public RDIIInflow pRDIIInflow;
            public ExternalInflow pExternalInflow;
            public ExternalOutflow pExternalOutflow;
            public InternalOutflow pInternalOutflow;
            public EvaporationLoss2 pEvaporationLoss2;
            public ExfiltrationLoss pExfiltrationLoss;
            public InitialStoredVolume pInitialStoredVolume;
            public FinalStoredVolume pFinalStoredVolume;
            public String continuityerror;
        }
        private FlowRoutingContinuity flowRoutingContinuity;

        //
        public static class HighestFlowInstabilityIndexes
        {
            public String linkname;
            public int index;
        }
        private List<HighestFlowInstabilityIndexes> highestFlowInstabilityIndexes;

        //
        public static class MinimumTimeStep
        {
            public String value;
            public String unit;
        }
        public static class AverageTimeStep
        {
            public String value;
            public String unit;
        }
        public static class MaximumTimeStep
        {
            public String value;
            public String unit;
        }
        @Data
        public static class RoutingTimeStepSummary
        {
            private MinimumTimeStep pMinimumTimeStep;
            private AverageTimeStep pAverageTimeStep;
            private MaximumTimeStep pMaximumTimeStep;
            private String percent_in_steady_state;
            private String average_iterations_per_step;
            private String percent_not_converging;
        }
        private RoutingTimeStepSummary routingTimeStepSummary;

        //
        @Data
        public static class SubcatchmentRunoffSummary
        {
            private String subcatchment;
            private String totalPrecip;
            private String totalRunon;
            private String totalEvap;
            private String totalInfil;
            private String impervRunoff;
            private String pervRunoff;
            private String totalRunoffMm;
            private String totalRunoffLtr;
            private String peakRunoff;
            private String runoffCoeff;
        }
        private List<SubcatchmentRunoffSummary> subcatchmentRunoffSummaries;


        //
        @Data
        public static class NodeDepthSummary
        {
            private String node;
            private String type;
            private String averageDepth;
            private String maximumDepth;
            private String maximumHgl;
            private String timeOfMaxOccurrenceDay;
            private String timeOfMaxOccurrenceTime;
            private String reportedMaxDepth;
        }
        private List<NodeDepthSummary> nodeDepthSummaries;


        //
        @Data
        public static class NodeInflowSummary
        {
            private String node;
            private String type;
            private String maximumLateralInflow;
            private String maximumTotalInflow;
            private String timeOfMaxOccurrenceDay;
            private String timeOfMaxOccurrenceTime;
            private String lateralInflowVolume;
            private String totalInflowVolume;
            private String flowBalanceErrorPercent;
        }
        private List<NodeInflowSummary> nodeInflowSummaries;


        //
        @Data
        public static class NodeSurchargeSummary
        {
            private String node;
            private String type;
            private String hoursSurcharged;
            private String maxHeightAboveCrown;
            private String minDepthBelowRim;
        }
        private List<NodeSurchargeSummary> nodeSurchargeSummaries;


        //
        @Data
        public static class NodeFloodingSummary
        {
            private String node;
            private String hoursFlooded;
            private String maximumRate;
            private String timeOfMaxOccurrenceDay;
            private String timeOfMaxOccurrenceTime;
            private String totalFloodVolume;
            private String maximumPondedVolume;
        }
        public List<NodeFloodingSummary> nodeFloodingSummaries;


        //
        @Data
        public static class StorageVolumeSummary
        {
            private String storageUnit;
            private String averageVolume;
            private String avgPcntFull;
            private String evapPcntLoss;
            private String exfilPcntLoss;
            private String maximumVolume;
            private String maxPcntFull;
            private String timeOfMaxOccurrenceDay;
            private String timeOfMaxOccurrenceTime;
            private String maximumOutflow;
        }
        private List<StorageVolumeSummary> storageVolumeSummaries;


        //
        @Data
        public static class OutfallLoadingSummary
        {
            private String outfallNode;
            private String flowFregPcnt;
            private String avgFlow;
            private String maxFlow;
            private String totalVolume;
        }
        private List<OutfallLoadingSummary> outfallLoadingSummaries;


        //
        @Data
        public static class LinkFlowSummary
        {
            private String link;
            private String type;
            private String maximumFlow;
            private String timeOfMaxOccurrenceDay;
            private String timeOfMaxOccurrenceTime;
            private String maximumVeloc;
            private String maxFullFlow;
            private String maxFullDepth;
        }
        private List<LinkFlowSummary> linkFlowSummaries;


        //
        @Data
        public static class ConduitSurchargeSummary
        {
            private String conduit;
            private String bothEnds;
            private String upstream;
            private String dnstream;
            private String hoursAboveFullNormalFlow;
            private String hoursCapacityLimited;
        }
        private List<ConduitSurchargeSummary> conduitSurchargeSummaries;

        @Data
        public static class SubcatchmentResult{
            private String date;
            private String time;
            private String precip;
            private String losses;
            private String runoff;
        }
        @Data
        public static class SubcatchmentResults{
            private List<SubcatchmentResult> subcatchmentResults;
            private String name;
        }
        private List<SubcatchmentResults> subcatchmentResultsList;


        @Data
        public static class NodeResult{
            private String date;
            private String time;
            private String inflow;
            private String flooding;
            private String depth;
            private String head;
        }
        Map<String,List<NodeResult>> nodeResultsMap;

        @Data
        public static class LinkResult{
            private String date;
            private String time;
            private String flow;
            private String velocity;
            private String depth;
            private String capacity;
        }
        @Data
        public static class LinkResults{
            private List<LinkResult> linkResults;
            private String name;
        }
        private List<LinkResults> linkResultsList;

        //
        @Data
        public static class End
        {
            private String beginTime;
            private String endTime;
            private String totalElapsedTime;
        }
        private End end;

}

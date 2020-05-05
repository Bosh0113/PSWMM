<style scoped>
#summaryCard>>>.ivu-card-body{
    padding: 10px
}
.btnHoverGreen:hover {
    background-color: #19be6b;
    color: white;
}
</style>
<template>
    <div>
        <Card dis-hover style="height:100%" id="summaryCard">
            <div slot="title">
                <h3>
                    <Icon type="md-grid" />
                    Summary Results
                </h3>
            </div>
            <div slot="extra">
                <Button @click="loadReport" size="small" class="btnHoverGreen">Load Report</Button>
            </div>
            <div>
                <Select v-model="selectType" size="small" style="width:200px" @on-change="selectedTypeChange" :disabled="!originalData">
                    <Option v-for="item in typeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
                <span>Click a column header to sort the column.</span>
            </div>
            <div style="border: 1px solid #dcdee2;margin-top:5px;height:calc(100vh - 100px)">
                <div v-if="!originalData" style="text-align:center">
                    <div style="color:lightgray;font-size:4em; font-weight:bold">
                        Please load report firstly.
                    </div>
                </div>
                <div v-else>
                    <Table :columns="columnsShow" :data="dataShow" :height="tableHeight" border></Table>
                </div>
            </div>
        </Card>
    </div>
</template>
<script>
export default {
    mounted(){
        window.onresize = ()=>{
            console.log("resize");
            this.tableHeight = window.innerHeight-100;
        }
    },
    data(){
        return{
            tableHeight: window.innerHeight-100,
            originalData:false,
            selectType: "subcatchmentRunoffSummaries",
            typeList:[
                {   value: "subcatchmentRunoffSummaries",
                    label: "Subcatchment Runoff"
                }, 
                {   value: "nodeDepthSummaries",
                    label: "Node Depth"
                }, 
                {   value: "nodeInflowSummaries",
                    label: "Node Inflow"
                }, 
                {   value: "nodeSurchargeSummaries",
                    label: "Node Surcharge"
                }, 
                {   value: "nodeFloodingSummaries",
                    label: "Node Flooding"
                }, 
                {   value: "storageVolumeSummaries",
                    label: "Storage Volume"
                }, 
                {   value: "outfallLoadingSummaries",
                    label: "Outfall Loading"
                }, 
                {   value: "linkFlowSummaries",
                    label: "Link Flow"
                }, 
                {   value: "conduitSurchargeSummaries",
                    label: "Conduit Surcharge"
                }, 
            ],
            columnsShow:[],
            dataShow:[],
            columnsSR:[
                {
                    title:"Subcatchment",
                    key:"subcatchment",
                    sortable: true
                },
                {
                    title:"Total Precip(mm)",
                    key:"totalPrecip",
                    sortable: true
                },
                {
                    title:"Total Runon(mm)",
                    key:"totalRunon",
                    sortable: true
                },
                {
                    title:"Total Evap(mm)",
                    key:"totalEvap",
                    sortable: true
                },
                {
                    title:"Total Infil(mm)",
                    key:"totalInfil",
                    sortable: true
                },
                {
                    title:"Total Runoff(mm)",
                    key:"totalRunoffMm",
                    sortable: true
                },
                {
                    title:"Total Runoff(10^6 ltr)",
                    key:"totalRunoffLtr",
                    sortable: true
                },
                {
                    title:"Peak Runoff(LPS)",
                    key:"peakRunoff",
                    sortable: true
                },
                {
                    title:"Runoff Coeff",
                    key:"runoffCoeff",
                    sortable: true
                }],
            columnsND:[
                {
                    title:"Node",
                    key:"node",
                    sortable: true
                },
                {
                    title:"Type",
                    key:"type",
                    sortable: true
                },
                {
                    title:"Average Depth(Meters)",
                    key:"averageDepth",
                    sortable: true
                },
                {
                    title:"Maximum Depth(Meters)",
                    key:"maximumDepth",
                    sortable: true
                },
                {
                    title:"Maximum HGL(Meters)",
                    key:"maximumHgl",
                    sortable: true
                },
                {
                    title:"Day of Maximum Depth",
                    key:"timeOfMaxOccurrenceDay",
                    sortable: true
                },
                {
                    title:"Hour of Maximum Depth",
                    key:"timeOfMaxOccurrenceTime",
                    sortable: true
                }],
            columnsNI:[
                {
                    title:"Node",
                    key:"node",
                    sortable: true
                },
                {
                    title:"Type",
                    key:"type",
                    sortable: true
                },
                {
                    title:"Maximum Lateral Inflow(LPS)",
                    key:"maximumLateralInflow",
                    sortable: true
                },
                {
                    title:"Maximum Total Inflow(LPS)",
                    key:"maximumTotalInflow",
                    sortable: true
                },
                {
                    title:"Day of Maximum Inflow",
                    key:"timeOfMaxOccurrenceDay",
                    sortable: true
                },
                {
                    title:"Hour of Maximum Inflow",
                    key:"timeOfMaxOccurrenceTime",
                    sortable: true
                },
                {
                    title:"Lateral Inflow Volume(10^6 ltr)",
                    key:"lateralInflowVolume",
                    sortable: true
                },
                {
                    title:"Total Inflow Volume(10^6 ltr)",
                    key:"totalInflowVolume",
                    sortable: true
                },
                {
                    title:"Flow Balance Error Percent",
                    key:"flowBalanceErrorPercent",
                    sortable: true
                }],
            columnsNS:[
                {
                    title:"Node",
                    key:"node",
                    sortable: true
                },
                {
                    title:"Type",
                    key:"type",
                    sortable: true
                },
                {
                    title:"Hours Surcharged",
                    key:"hoursSurcharged",
                    sortable: true
                },
                {
                    title:"Max Height Above Crown(Meters)",
                    key:"maxHeightAboveCrown",
                    sortable: true
                },
                {
                    title:"Max Depth Below Rim(Meters)",
                    key:"minDepthBelowRim",
                    sortable: true
                }],
            columnsNF:[
                {
                    title:"Node",
                    key:"node",
                    sortable: true
                },
                {
                    title:"Hours Flooded",
                    key:"hoursFlooded",
                    sortable: true
                },
                {
                    title:"Maximum Rate(LPS)",
                    key:"maximumRate",
                    sortable: true
                },
                {
                    title:"Day of Maximum Flooding",
                    key:"timeOfMaxOccurrenceDay",
                    sortable: true
                },
                {
                    title:"Hour of Maximum Flooding",
                    key:"timeOfMaxOccurrenceTime",
                    sortable: true
                },
                {
                    title:"Total Flood Volume(10^6 ltr)",
                    key:"totalFloodVolume",
                    sortable: true
                },
                {
                    title:"Maximum Ponded Volume(1000 m3)",
                    key:"maximumPondedVolume",
                    sortable: true
                }],
            columnsSV:[
                {
                    title:"Storage Unit",
                    key:"storageUnit",
                    sortable: true
                },
                {
                    title:"Average Volume(1000 m3)",
                    key:"averageVolume",
                    sortable: true
                },
                {
                    title:"Average Percent Full",
                    key:"avgPcntFull",
                    sortable: true
                },
                {
                    title:"Evap Percent Loss",
                    key:"evapPcntLoss",
                    sortable: true
                },
                {
                    title:"Infil Percent Loss",
                    key:"exfilPcntLoss",
                    sortable: true
                },
                {
                    title:"Maximum Volume(1000 m3)",
                    key:"maximumVolume",
                    sortable: true
                },
                {
                    title:"Maximum Percent Full",
                    key:"maxPcntFull",
                    sortable: true
                },
                {
                    title:"Day of Maximum Volume",
                    key:"timeOfMaxOccurrenceDay",
                    sortable: true
                },
                {
                    title:"Hour of Maximum Volume",
                    key:"timeOfMaxOccurrenceTime",
                    sortable: true
                },
                {
                    title:"Maximum Outflow(LPS)",
                    key:"maximumOutflow",
                    sortable: true
                }],
            columnsOL:[
                {
                    title:"Outfall Node",
                    key:"outfallNode",
                    sortable: true
                },
                {
                    title:"Flow Freq.(Pcnt.)",
                    key:"flowFregPcnt",
                    sortable: true
                },
                {
                    title:"Avg. Flow(LPS)",
                    key:"avgFlow",
                    sortable: true
                },
                {
                    title:"Max. Flow(LPS)",
                    key:"maxFlow",
                    sortable: true
                },
                {
                    title:"Tortal Volume(10^6 ltr)",
                    key:"totalVolume",
                    sortable: true
                }],
            columnsLF:[
                {
                    title:"Link",
                    key:"link",
                    sortable: true
                },
                {
                    title:"Type",
                    key:"type",
                    sortable: true
                },
                {
                    title:"Maximum |Flow|(LPS)",
                    key:"maximumFlow",
                    sortable: true
                },
                {
                    title:"Day of Maximum Flow",
                    key:"timeOfMaxOccurrenceDay",
                    sortable: true
                },
                {
                    title:"Hour of Maximum Flow",
                    key:"timeOfMaxOccurrenceTime",
                    sortable: true
                },
                {
                    title:"Maximum |Velocity|(m/sec)",
                    key:"maximumVeloc",
                    sortable: true
                },
                {
                    title:"Max/Full Flow",
                    key:"maxFullFlow",
                    sortable: true
                },
                {
                    title:"Max/Full Depth",
                    key:"maxFullDepth",
                    sortable: true
                }],
            columnsCS:[
                {
                    title:"Conduit",
                    key:"conduit",
                    sortable: true
                },
                {
                    title:"Hours Both Ends Full",
                    key:"bothEnds",
                    sortable: true
                },
                {
                    title:"Hours Upstream Full",
                    key:"upstream",
                    sortable: true
                },
                {
                    title:"Hours Dnstream Full",
                    key:"dnstream",
                    sortable: true
                },
                {
                    title:"Hours Above Normal Flow",
                    key:"hoursAboveFullNormalFlow",
                    sortable: true
                },
                {
                    title:"Hours Capacity Limited",
                    key:"hoursCapacityLimited",
                    sortable: true
                }],
        }
    },
    methods:{
        loadReport(){
            this.axios
            .get(
                "/PSWMM/data/rpt-all" +
                "?name=" + "swmm"
            )
            .then(res => {
                if (res.data.code) {
                    this.originalData = res.data.data;
                    this.selectedTypeChange(this.selectType);
                }
                else{
                    confirm("error.");
                    console.log(res)
                }
            })
            .catch(err => {});
        },
        selectedTypeChange(type){
            switch(type){
                case "subcatchmentRunoffSummaries":{
                    this.columnsShow = this.columnsSR;
                    this.dataShow = this.originalData[type];
                    break;
                }
                case "nodeDepthSummaries":{
                    this.columnsShow = this.columnsND;
                    break;
                }
                case "nodeInflowSummaries":{
                    this.columnsShow = this.columnsNI;
                    break;
                }
                case "nodeSurchargeSummaries":{
                    this.columnsShow = this.columnsNS;
                    break;
                }
                case "nodeFloodingSummaries":{
                    this.columnsShow = this.columnsNF;
                    break;
                }
                case "storageVolumeSummaries":{
                    this.columnsShow = this.columnsSV;
                    break;
                }
                case "outfallLoadingSummaries":{
                    this.columnsShow = this.columnsOL;
                    break;
                }
                case "linkFlowSummaries":{
                    this.columnsShow = this.columnsLF;
                    break;
                }
                case "conduitSurchargeSummaries":{
                    this.columnsShow = this.columnsCS;
                }
            }
            this.dataShow = this.originalData[type];
        }
    }
}
</script>

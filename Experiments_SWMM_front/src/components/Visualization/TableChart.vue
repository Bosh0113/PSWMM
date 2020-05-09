<style scoped>
#summaryCard>>>.ivu-card-body{
    padding: 10px
}
.btnHoverGreen:hover {
    background-color: #19be6b;
    color: white;
}
.submitTitle{
    margin-top: 5px;
    margin-left: 5px;
    display: inline-block;
    word-break: break-word;
    width: 60px;
    font-size: 1px;
    position: absolute;
    line-height: 1.2;
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
                <div style="margin-top:-10px">
                    <div style="display:inline-block;margin: 5px 0 0 -60px;position: absolute;">
                        <h5 style="display:inline-block">You:</h5>
                        <avatar
                            :username="pageParams.userName"
                            :size="25"
                            :title="pageParams.userName"
                            style="display:inline-block;margin:0 2px"
                        ></avatar>
                    </div>
                    <h5 style="margin-top:8px;margin-left:5px;display: inline-block;position:absolute">
                        Participants:
                    </h5>
                    <div style="margin-left:80px;padding:1px 5px;margin-top:5px;display:inline-block;height:100%;width:200px;white-space: nowrap">
                        <vue-scroll :ops="ops">
                            <avatar
                                v-for="participant in participants"
                                :key="participant.index"
                                :username="participant.userName"
                                :size="25"
                                :title="participant.userName"
                                style="display:inline-block;margin:0 2px"
                            ></avatar>
                        </vue-scroll>
                    </div>
                    <p class="submitTitle">
                        Submit
                        operation
                    </p>
                    <h5 style="display: inline-block;margin:8px 0 0 60px;position:absolute">:</h5>
                    <Button icon="ios-paper-plane" 
                    shape="circle" class="btnHoverGreen" 
                    style="margin:-18px 0 0 70px" @click="submitOp"></Button>
                </div>
            </div>
            <div>
                <Select v-model="selectType" size="small" style="width:200px" @on-change="selectedTypeChange" :disabled="!originalData">
                    <Option v-for="item in typeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
                <span>Click a column header to sort the column.</span>
                <Button @click="loadReport" size="small" class="btnHoverGreen" style="float:right">Load Report</Button>
            </div>
            <div style="border: 1px solid #dcdee2;margin-top:5px;height:calc(100vh - 100px)">
                <div v-if="!originalData" style="text-align:center">
                    <div style="color:lightgray;font-size:4em; font-weight:bold">
                        Please load report firstly.
                    </div>
                </div>
                <div v-else>
                    <Table highlight-row :columns="columnsShow" :data="dataShow" :height="tableHeight" border @on-current-change="tableSelectd" :row-class-name="rowClassName"></Table>
                </div>
            </div>
        </Card>
    </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
    name: 'TableChart',
    components: {
        Avatar
    },
    created(){
        this.getPageInfo();
    },
    mounted(){
        this.connectSocket();
        window.onresize = ()=>{
            this.tableHeight = window.innerHeight-100;
        }
    },
    data(){
        return{
            pageParams:{},
            tableHeight: window.innerHeight-100,
            ops: {
                bar: {
                    background: "#808695"
                }
            },
            tcSocket:null,
            participants:[
                {
                    userName:"abc"
                },
                {
                    userName:"bbcc"
                },
                {
                    userName:"cbc"
                },
                {
                    userName:"vcb"
                },
                {
                    userName:"abc"
                },
                {
                    userName:"bbcc"
                }
            ],
            originalData:false,
            selectType: "subcatchmentRunoffSummaries",
            highlightRowIndex: 9999999999,
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
        getPageInfo(){
            var href = window.location.href;
            var url = href.split("&");

            for (var i = 0; i < url.length; i++) {
                if (/groupID/.test(url[i])) {
                    this.pageParams.pageId = url[i].match(/groupID=(\S*)/)[1];
                    continue;
                }

                if (/userID/.test(url[i])) {
                    this.pageParams.userId = url[i].match(/userID=(\S*)/)[1];
                    continue;
                }

                if (/userName/.test(url[i])) {
                    this.pageParams.userName = url[i].match(/userName=(\S*)/)[1];
                    continue;
                }
            }
        },
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
        },
        tableSelectd(currentRow, oldCurrentRow){
            var currentIndex = this.dataShow.findIndex((e)=>{
                return JSON.stringify(e)==JSON.stringify(currentRow);
            });
            this.highlightRowIndex = currentIndex;
        },
        rowClassName(row, index){
            if(index == this.highlightRowIndex){
                return 'ivu-table-row-highlight';
            }
            return "";
        },
        zoomToHighlight(newHighlight){
            setTimeout(() => {
                this.highlightRowIndex = newHighlight;
                setTimeout(() => {
        　　        document.getElementsByClassName('ivu-table-row ivu-table-row-highlight').length==0 ? '': document.getElementsByClassName('ivu-table-row ivu-table-row-highlight')[0].scrollIntoView({
                        behavior: "instant",
                        block: "start",
                        inline: "nearest"
                        })
                }, 1000);
            }, 500);
        },
        connectSocket(){
            this.tcSocket=null;
            var ip_port = window.location.host;
            if(ip_port = "localhost:8084"){
                ip_port = "localhost:8086";
            }
            var soSocketUrl = "ws://" + ip_port + "/PSWMM/SimulationOptionsSocket/" + this.pageParams.pageId + "-tc";
            this.tcSocket = new WebSocket(soSocketUrl);
            this.tcSocket.onopen = this.onOpen;
            this.tcSocket.onmessage = this.onMessage;
            this.tcSocket.onclose = this.onClose;
            this.tcSocket.onerror = this.onError;
        },
        onOpen() {
            console.log("Socket连接成功！");
            this.sendMessage("connect",{});
        },
        onMessage(e) {
            var messageObject = JSON.parse(e.data);
            switch(messageObject.type){
                case "operation":{
                    this.originalData = messageObject.operation.originalData;
                    this.selectType = messageObject.operation.selectType;
                    this.selectedTypeChange(this.selectType);
                    this.zoomToHighlight(messageObject.operation.highlightRowIndex);
                    break;
                }
                case "members":{
                    this.participants = messageObject.memberList;
                }
            }
        },
        onClose(e) {
            this.runParametersModal = false;
            console.log("Socket连接断开！");
        },
        onError(e) {
            this.runParametersModal = false;
            console.log("Socket连接错误！");
        },
        sendMessage(type, operation){
            var userId = this.pageParams.userId;
            var userName = this.pageParams.userName;
            var message ={
                type: type,
                userInfo:{
                    userId: userId,
                    userName: userName
                },
                operation: operation
            }
            this.tcSocket.send(JSON.stringify(message));
        },
        submitOp(){
            var operation = {
                originalData: this.originalData,
                selectType: this.selectType,
                highlightRowIndex:this.highlightRowIndex,
            };
            this.sendMessage("operation", operation);
        }
    }
}
</script>

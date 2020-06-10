<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#runModel>>>.ivu-modal-body{
    padding: 0
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
.btnHoverBlue:hover {
  background-color: #2db7f5;
  color: white;
}
.btnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.btnHoverOrange:hover {
  background-color: #ff9900;
  color: white;
}
.btnHoverRed:hover {
  background-color: #ed4014;
  color: white;
}
.cardDiv{
    margin:0 15px;
    height:100%;
    border: 1px solid #dcdee2
}
.cardTitle{
    margin-top: -10px;
    margin-left: 5px;
    background: white;
    display: inline-block;
    position: absolute;
}
.processCB{
    margin: 5px 5px;
}
.datesItem{
    margin:15px 0;
}
</style>
<template>
  <div>
    <Button @click="showRunParametersModal">Set run parameters</Button>
    <Modal
    id="runModel"
    v-model="runParametersModal"
    :mask-closable="false"
    :styles="{top: '20px'}"
    :closable="false"
    width="500">
        <div slot="header">
            <h4 style="display:inline-block">Simulation Options</h4>
            <div style="display:inline-block;float:right;margin-top: -5px;">
                <h5 style="display:inline-block">You:</h5>
                <avatar
                    :username="pageParams.userName"
                    :size="25"
                    :title="pageParams.userName"
                    style="display:inline-block;margin:0 2px"
                ></avatar>
            </div>
        </div>
        <div style="height:480px">
            <Tabs type="line" :animated="false" size="small" v-model="optionTab">
                <TabPane label="General" name="General">
                    <div style="height:210px;width:100%">
                        <Row style="height:100%">
                            <Col span="12" style="height:100%">
                                <div class="cardDiv">
                                    <div class="cardTitle">Process Models</div>
                                     <div style="margin-top:15px">
                                         <Checkbox v-model="simulationOptions.n_IGNORE_RAINFALL" class="processCB">
                                            <span>Rainfall/Runoff</span>
                                        </Checkbox><br/>
                                         <Checkbox v-model="simulationOptions.n_IGNORE_DEPENDENT" class="processCB">
                                            <span>Rainfall Dependent |/|</span>
                                        </Checkbox><br/>
                                         <Checkbox v-model="simulationOptions.n_IGNORE_SNOW_MELT" class="processCB">
                                            <span>Snow Melt</span>
                                        </Checkbox><br/>
                                         <Checkbox v-model="simulationOptions.n_IGNORE_GROUNDWATER" class="processCB">
                                            <span>Groundwater</span>
                                        </Checkbox><br/>
                                         <Checkbox v-model="simulationOptions.n_IGNORE_ROUTING" class="processCB">
                                            <span>Flow Routing</span>
                                        </Checkbox><br/>
                                         <Checkbox v-model="simulationOptions.n_IGNORE_WATER_QUALITY" class="processCB">
                                            <span>Water Quality</span>
                                        </Checkbox>
                                     </div>
                                </div>
                            </Col>
                            <Col span="12" style="height:100%">
                                <div class="cardDiv">
                                    <div class="cardTitle">Miscellaneous</div>
                                     <div style="margin-top:15px">
                                         <Checkbox v-model="simulationOptions.ALLOW_PONDING" class="processCB">
                                            <span>Allow Ponding</span>
                                        </Checkbox><br/>
                                         <Checkbox v-model="simulationOptions.CONTROLS" class="processCB">
                                            <span>Report Control Actions</span>
                                        </Checkbox><br/>
                                         <Checkbox v-model="simulationOptions.INPUT" class="processCB">
                                            <span>Report Input Summary</span>
                                        </Checkbox>
                                     </div>
                                     <p style="margin: 5px 5px 0">Minimum Conduit Slope</p>
                                     <Input v-model="simulationOptions.MIN_SLOPE" size="small" style="width:80px;margin-left:5px"/>
                                     <span style="margin-left:5px">%</span>
                                </div>
                            </Col>
                        </Row>
                    </div>
                    <div style="height:150px;margin-top:20px">
                        <Row style="height:100%">
                            <Col span="12" style="height:100%">
                                <div class="cardDiv">
                                    <div class="cardTitle">Infiltration Model</div>
                                    <RadioGroup v-model="simulationOptions.INFILTRATION" style="margin-top:15px">
                                        <Radio label="HORTON" class="processCB">Horton</Radio><br/>
                                        <Radio label="MODIFIED_HORTON" class="processCB">Modeified Horton</Radio><br/>
                                        <Radio label="GREEN_AMPT" class="processCB">Green Ampt</Radio><br/>
                                        <Radio label="CURVE_NUMBER" class="processCB">Curve Number</Radio>
                                    </RadioGroup>
                                </div>
                            </Col>
                            <Col span="12" style="height:100%">
                                <div class="cardDiv">
                                    <div class="cardTitle">Routing Model</div>
                                    <RadioGroup v-model="simulationOptions.FLOW_ROUTING" style="margin-top:15px">
                                        <Radio label="STEADY" class="processCB">Steady Flow</Radio><br/>
                                        <Radio label="KINWAVE" style="margin:18px 5px">Kinematic Wave</Radio><br/>
                                        <Radio label="DYNWAVE" class="processCB">Dynamic Wave</Radio>
                                    </RadioGroup>
                                </div>
                            </Col>
                        </Row>
                    </div>
                </TabPane>
                <TabPane label="Dates" name="Dates">
                    <div style="height:380px;width:100%">
                        <vue-scroll :ops="ops">
                        <div style="padding:10px 30px">
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p style="color: white;">1</p>
                                </Col>
                                <Col span="8" offset="1">
                                    <p>Date (M/D/Y)</p>
                                </Col>
                                <Col span="6" offset="1">
                                    <p>Time (H:M)</p>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p>Start Analysis on</p>
                                </Col>
                                <Col span="8" offset="1">
                                    <DatePicker size="small" 
                                    type="date" placeholder="Select date" 
                                    style="width:120px"
                                    format="MM/dd/yyyy"
                                    :clearable="false"
                                    v-model="simulationOptions.START_DATE"></DatePicker>
                                </Col>
                                <Col span="6" offset="1">
                                    <TimePicker format="HH:mm" 
                                    placeholder="Time" 
                                    style="width: 80px"
                                    size="small"
                                    v-model="simulationOptions.START_TIME"></TimePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p>Start Reporting on</p>
                                </Col>
                                <Col span="8" offset="1">
                                    <DatePicker size="small" 
                                    type="date" placeholder="Select date" 
                                    style="width:120px"
                                    format="MM/dd/yyyy"
                                    :clearable="false"
                                    v-model="simulationOptions.REPORT_START_DATE"></DatePicker>
                                </Col>
                                <Col span="6" offset="1">
                                    <TimePicker format="HH:mm" 
                                    placeholder="Time" 
                                    style="width: 80px"
                                    size="small"
                                    v-model="simulationOptions.REPORT_START_TIME"></TimePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p>End Analysis on</p>
                                </Col>
                                <Col span="8" offset="1">
                                    <DatePicker size="small" 
                                    type="date" placeholder="Select date" 
                                    style="width:120px"
                                    format="MM/dd/yyyy"
                                    :clearable="false"
                                    v-model="simulationOptions.END_DATE"></DatePicker>
                                </Col>
                                <Col span="6" offset="1">
                                    <TimePicker format="HH:mm" 
                                    placeholder="Time" 
                                    style="width: 80px"
                                    size="small"
                                    v-model="simulationOptions.END_TIME"></TimePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p>Start Sweeping on</p>
                                </Col>
                                <Col span="8" offset="1">
                                    <DatePicker size="small" 
                                    type="date" placeholder="Date" 
                                    style="width:80px"
                                    format="MM/dd"
                                    :clearable="false"
                                    v-model="simulationOptions.SWEEP_START"></DatePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p>End Sweeping on</p>
                                </Col>
                                <Col span="8" offset="1">
                                    <DatePicker size="small" 
                                    type="date" placeholder="Date" 
                                    style="width:80px"
                                    format="MM/dd"
                                    :clearable="false"
                                    v-model="simulationOptions.SWEEP_END"></DatePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p style="word-break: break-word;display: inline-block;width:80px;line-height: 1.2;">Antecedent
                                    Dry Days</p>
                                </Col>
                                <Col span="8" offset="1">
                                    <Input v-model="simulationOptions.DRY_DAYS" size="small" style="width:80px;"/>
                                </Col>
                            </Row>
                        </div>
                        </vue-scroll>
                    </div>
                </TabPane>
                <TabPane label="Time Steps" name="Time Steps">
                    <div style="height:380px;width:100%">
                        <div style="padding:10px 30px">
                            <Row style="margin:0 0 15px 0">
                                <Col span="7" offset="1">
                                    <p style="color: white;">1</p>
                                </Col>
                                <Col span="6" offset="1">
                                    <p>Days</p>
                                </Col>
                                <Col span="8" offset="1">
                                    <p>Hr:Min:Sec</p>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p>Reporting</p>
                                </Col>
                                <Col span="6" offset="1">
                                    <InputNumber v-model="simulationOptions.REPORT_STEP_d" size="small" :min="0" :max="59"></InputNumber>
                                </Col>
                                <Col span="8" offset="1">
                                    <TimePicker format="HH:mm:ss" 
                                    placeholder="Time" 
                                    style="width: 100px"
                                    size="small"
                                    v-model="simulationOptions.REPORT_STEP_h"></TimePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p style="word-break: break-word;display: inline-block;width:100px;line-height: 1.2;">Runoff:<br>
                                    Dry Weather</p>
                                </Col>
                                <Col span="6" offset="1">
                                    <InputNumber v-model="simulationOptions.DRY_STEP_d" size="small" :min="0" :max="59"></InputNumber>
                                </Col>
                                <Col span="8" offset="1">
                                    <TimePicker format="HH:mm:ss" 
                                    placeholder="Time" 
                                    style="width: 100px"
                                    size="small"
                                    v-model="simulationOptions.DRY_STEP_h"></TimePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p style="word-break: break-word;display: inline-block;width:100px;line-height: 1.2;">Runoff:<br>
                                    Wet Weather</p>
                                </Col>
                                <Col span="6" offset="1">
                                    <InputNumber v-model="simulationOptions.WET_STEP_d" size="small" :min="0" :max="59"></InputNumber>
                                </Col>
                                <Col span="8" offset="1">
                                    <TimePicker format="HH:mm:ss" 
                                    placeholder="Time" 
                                    style="width: 100px"
                                    size="small"
                                    v-model="simulationOptions.WET_STEP_h"></TimePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p>Routing</p>
                                </Col>
                                <Col span="6" offset="1">
                                    <InputNumber v-model="simulationOptions.ROUTING_STEP" size="small" :min="0" :max="59"></InputNumber>
                                </Col>
                                <Col span="8" offset="1">
                                    <p>Seconds</p>
                                </Col>
                            </Row>
                            <div style="padding:10px 15px">
                                <div class="cardDiv">
                                    <div class="cardTitle">Steady Flow Periods</div>
                                    <div style="margin:10px 0;padding:10px">
                                        <Checkbox v-model="simulationOptions.SKIP_STEADY_STATE">Skip Steady Flow Periods</Checkbox>
                                        <Row style="margin-top:5px">
                                            <Col span="16">
                                                <p style="display:inline-block">System Flow Tolerance(%)</p>
                                            </Col>
                                            <Col span="6" offset="1">
                                                <InputNumber v-model="simulationOptions.SYS_FLOW_TOL" size="small" :min="0" :max="100"></InputNumber>
                                            </Col>
                                        </Row>
                                        <Row style="margin-top:5px">
                                            <Col span="16">
                                                <p style="display:inline-block">Lateral Flow Tolerance(%)</p>
                                            </Col>
                                            <Col span="6" offset="1">
                                                <InputNumber v-model="simulationOptions.LAT_FLOW_TOL" size="small" :min="0" :max="100"></InputNumber>
                                            </Col>
                                        </Row>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </TabPane>
                <TabPane label="Dynamic Wave" name="Dynamic Wave">
                    <div style="height:380px;width:100%">
                        <div style="height:110px">
                            <Row style="height:100%">
                                <Col span="6">
                                    <div class="cardDiv">
                                        <div class="cardTitle">Inertial Terms</div>
                                        <RadioGroup v-model="simulationOptions.INERTIAL_DAMPING" style="margin-top:15px">
                                            <Radio label="NONE" class="processCB">Keep</Radio><br/>
                                            <Radio label="PARTIAL" class="processCB">Dampen</Radio><br/>
                                            <Radio label="FULL" class="processCB">Ignore</Radio>
                                        </RadioGroup>
                                    </div>
                                </Col>
                                <Col span="9">
                                    <div class="cardDiv">
                                        <div class="cardTitle">Normal Flow Criterion</div>
                                        <RadioGroup v-model="simulationOptions.NORMAL_FLOW_LIMITED" style="margin-top:15px">
                                            <Radio label="SLOPE" class="processCB">Slope</Radio><br/>
                                            <Radio label="FROUDE" class="processCB">Froude No.</Radio><br/>
                                            <Radio label="BOTH" class="processCB">Both</Radio>
                                        </RadioGroup>
                                    </div>
                                </Col>
                                <Col span="9" style="height:100%">
                                    <div class="cardDiv">
                                        <div class="cardTitle">Force Main Equation</div>
                                        <RadioGroup v-model="simulationOptions.FORCE_MAIN_EQUATION" style="margin-top:15px">
                                            <Radio label="H-W" style="margin:10px 5px;">Hazen-Willarms</Radio><br/>
                                            <Radio label="D-W" style="margin:10px 5px;">Darcy-Weisbach</Radio>
                                        </RadioGroup>
                                    </div>
                                </Col>
                            </Row>
                        </div>
                        <div style="padding:30px 50px">
                            <div>
                                <Checkbox v-model="simulationOptions.VARIABLE_STEP_c">Use Variable Time Steps</Checkbox>
                                <span style="margin-left:30px">Adjusted By</span>
                                <InputNumber v-model="simulationOptions.VARIABLE_STEP" size="small" :min="0" :max="1" :step="0.01" style="width:80px"></InputNumber>
                            </div>
                            <div class="datesItem">
                                <div style="width:293px;display:inline-block">Time Step For Condit Lengthening (sec)</div>
                                <Input v-model="simulationOptions.LENGTHENING_STEP" size="small" style="width:80px;"/>
                            </div>
                            <div class="datesItem">
                                <div style="width:293px;display:inline-block">Minimum Nodal Surface Area (sq.feet)</div>
                                <Input v-model="simulationOptions.MIN_SURFAREA" size="small" style="width:80px;"/>
                            </div>
                            <div class="datesItem">
                                <div style="width:293px;display:inline-block">Maximum Trial per Time Step</div>
                                <InputNumber v-model="simulationOptions.MAX_TRIALS" size="small" :min="3" :max="20"></InputNumber>
                            </div>
                            <div class="datesItem">
                                <div style="width:293px;display:inline-block">Head Convergence Tolerance (feet)</div>
                                <Input v-model="simulationOptions.HEAD_TOLERANCE" size="small" style="width:80px;"/>
                            </div>
                            <div class="datesItem">
                                <Button class="btnHoverOrange" @click="applyDefaults">Apply Defaults</Button>
                            </div>
                        </div>
                    </div>
                </TabPane>
                <TabPane label="Files" name="Files">
                    <div style="height:380px;width:100%">
                        <div style="padding:15px">
                            <p>Specify interface files to use or save:</p>
                            <div style="margin-top:10px;width:100%;height:180px;border:1px solid #808695"></div>
                            <div style="margin-top:15px;text-align: center;">
                                <Button class="btnHoverGreen" size="small" style="width:80px">Add</Button>
                                <Button class="btnHoverBlue" size="small" style="width:80px">Edit</Button>
                                <Button class="btnHoverRed" size="small" style="width:80px">Delete</Button>
                            </div>
                        </div>
                    </div>
                </TabPane>
            </Tabs>
            <div style="border: 1px solid #dcdee2;margin-top:10px">
                <h5 style="margin-top:8px;margin-left:5px;display: inline-block;position:absolute">
                    Participants:
                </h5>
                <div style="margin-left:80px;padding:1px 5px;margin-top:5px;display:inline-block;height:100%;width:300px;white-space: nowrap">
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
        <div slot="footer" style="text-align: center;">
            <Button @click="saveRunParameters()" type="primary" style="margin: 0 15px;width: 120px;" size="small">Save Config</Button>
            <Button @click="closeRunParametersModal()" style="margin: 0 15px;width: 120px;" size="small">Cancel</Button>
        </div>
    </Modal>
  </div>
</template>

<script>
import Avatar from "vue-avatar";
export default {
  name: 'RunParameters',
  components: {
    Avatar
  },
  data () {
    return {
        pageParams:{},
        runParametersModal:false,
        soSocket:null,
        ops: {
            bar: {
                background: "#808695"
            }
        },
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
        optionTab:"General",
        /*
        前端JSON内容与INP文件属性对应说明：
        1.变量命名与属性命名对应注意：
            a:变量n_XX表示属性XX的相反值;
            b:Checkbox的变量值对应ture为YES,false为NO;
        2.变量值与属性值对应注意：
            a:XX_d与XX_h对应的属性XX其值为XX_d+XX_h;
            b:ROUTING_STEP变量值**的属性值为00:00:**格式;
            c:VARIABLE_STEP_c变量对应复选框选中，则VARIABLE_STEP属性值为0.00，否则，VARIABLE_STEP的属性值为VARIABLE_STEP变量的值;
        3.其余变量名即为属性名。
        ps:具体转换可见函数variableToAttribute。
        */
       simulationOptions:{
            //General
            n_IGNORE_RAINFALL:false,
            n_IGNORE_DEPENDENT:false,
            n_IGNORE_SNOW_MELT:false,
            n_IGNORE_GROUNDWATER:false,
            n_IGNORE_ROUTING:false,
            n_IGNORE_WATER_QUALITY:false,
            ALLOW_PONDING:false,
            CONTROLS:false,
            INPUT:false,
            MIN_SLOPE:"0",
            INFILTRATION:"HORTON",
            FLOW_ROUTING:"KINWAVE",
            //Dates
            START_DATE:new Date(),
            START_TIME:"",
            REPORT_START_DATE:new Date(),
            REPORT_START_TIME:"",
            END_DATE:new Date(),
            END_TIME:"",
            SWEEP_START:new Date("2020/01/01"),
            SWEEP_END: new Date("2020/12/31"),
            DRY_DAYS:"0",
            //Time Steps
            REPORT_STEP_d:0,
            REPORT_STEP_h:"00:15:00",
            DRY_STEP_d: 0,
            DRY_STEP_h:"01:00:00",
            WET_STEP_d: 0,
            WET_STEP_h:"00:05:00",
            ROUTING_STEP:30,
            SKIP_STEADY_STATE:false,
            SYS_FLOW_TOL:5,
            LAT_FLOW_TOL:5,
            //Dynamic Wave
            INERTIAL_DAMPING:"PARTIAL",
            NORMAL_FLOW_LIMITED:"BOTH",
            FORCE_MAIN_EQUATION:"H-W",
            VARIABLE_STEP_c: true,
            VARIABLE_STEP:0.80,
            LENGTHENING_STEP:0,
            MIN_SURFAREA:12.566,
            MAX_TRIALS:8,
            HEAD_TOLERANCE:0.005,
            //Files
       }
    }
  },
  created(){
      this.getPageInfo();
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
    showRunParametersModal(){
        this.runParametersModal = true;
        this.connectSocket();
    },
    connectSocket(){
        this.soSocket=null;
        var ip_port = window.location.host;
        if(ip_port == "localhost:8084"){
            ip_port = "localhost:8086";
        }
        var soSocketUrl = "ws://" + ip_port + "/PSWMM/SimulationOptionsSocket/" + this.pageParams.pageId;
        this.soSocket = new WebSocket(soSocketUrl);
        this.soSocket.onopen = this.onOpen;
        this.soSocket.onmessage = this.onMessage;
        this.soSocket.onclose = this.onClose;
        this.soSocket.onerror = this.onError;
    },
    onOpen() {
        console.log("Socket连接成功！");
        this.sendMessage("connect",{});
        var that = this;
        window.setInterval(()=>{
            if(that.soSocket!=null){
                that.sendMessage("ping", {});
            }
        },20000);
    },
    onMessage(e) {
        var messageObject = JSON.parse(e.data);
        switch(messageObject.type){
            case "operation":{
                this.optionTab = messageObject.operation.tab;
                var optionsMessage = messageObject.operation.options;
                optionsMessage.START_DATE = new Date(optionsMessage.START_DATE);
                optionsMessage.REPORT_START_DATE = new Date(optionsMessage.REPORT_START_DATE);
                optionsMessage.END_DATE = new Date(optionsMessage.END_DATE);
                optionsMessage.SWEEP_START = new Date(optionsMessage.SWEEP_START);
                optionsMessage.SWEEP_END = new Date(optionsMessage.SWEEP_END);
                this.simulationOptions = optionsMessage;
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
      this.soSocket.send(JSON.stringify(message));
    },
    applyDefaults(){
            this.simulationOptions.INERTIAL_DAMPING="PARTIAL";
            this.simulationOptions.NORMAL_FLOW_LIMITED="BOTH";
            this.simulationOptions.FORCE_MAIN_EQUATION="H-W";
            this.simulationOptions.VARIABLE_STEP_c= true;
            this.simulationOptions.VARIABLE_STEP=0.80;
            this.simulationOptions.LENGTHENING_STEP=0;
            this.simulationOptions.MIN_SURFAREA=12.566;
            this.simulationOptions.MAX_TRIALS=8;
            this.simulationOptions.HEAD_TOLERANCE=0.005;
    },
    closeRunParametersModal(){
        this.runParametersModal = false;
        this.soSocket.close();
    },
    variableToAttribute(variableConfig){
        var attributeConfig = variableConfig;
        //General
        attributeConfig.n_IGNORE_RAINFALL?attributeConfig.IGNORE_RAINFALL="NO":attributeConfig.IGNORE_RAINFALL="YES";
        delete attributeConfig.n_IGNORE_RAINFALL;
        attributeConfig.n_IGNORE_DEPENDENT?attributeConfig.IGNORE_DEPENDENT="NO":attributeConfig.IGNORE_DEPENDENT="YES";
        delete attributeConfig.n_IGNORE_DEPENDENT;
        attributeConfig.n_IGNORE_SNOW_MELT?attributeConfig.IGNORE_SNOW_MELT="NO":attributeConfig.IGNORE_SNOW_MELT="YES";
        delete attributeConfig.n_IGNORE_SNOW_MELT;
        attributeConfig.n_IGNORE_GROUNDWATER?attributeConfig.IGNORE_GROUNDWATER="NO":attributeConfig.IGNORE_GROUNDWATER="YES";
        delete attributeConfig.n_IGNORE_GROUNDWATER;
        attributeConfig.n_IGNORE_ROUTING?attributeConfig.IGNORE_ROUTING="NO":attributeConfig.IGNORE_ROUTING="YES";
        delete attributeConfig.n_IGNORE_ROUTING;
        attributeConfig.n_IGNORE_WATER_QUALITY?attributeConfig.IGNORE_WATER_QUALITY="NO":attributeConfig.IGNORE_WATER_QUALITY="YES";
        delete attributeConfig.n_IGNORE_WATER_QUALITY;

        attributeConfig.ALLOW_PONDING?attributeConfig.ALLOW_PONDING="YES":attributeConfig.ALLOW_PONDING="NO";
        attributeConfig.CONTROLS?attributeConfig.CONTROLS="YES":attributeConfig.CONTROLS="NO";
        attributeConfig.INPUT?attributeConfig.INPUT="YES":attributeConfig.INPUT="NO";
        //Dates
        var startDate = attributeConfig.START_DATE;
        attributeConfig.START_DATE = startDate.getMonth()+"/"+startDate.getDay()+"/"+startDate.getFullYear();
        var rptStartDate = attributeConfig.REPORT_START_DATE;
        attributeConfig.REPORT_START_DATE = rptStartDate.getMonth()+"/"+rptStartDate.getDay()+"/"+rptStartDate.getFullYear();
        var endDate = attributeConfig.END_DATE;
        attributeConfig.END_DATE = endDate.getMonth()+"/"+endDate.getDay()+"/"+endDate.getFullYear();
        var sweepStart = attributeConfig.SWEEP_START;
        attributeConfig.SWEEP_START = sweepStart.getMonth()+"/"+sweepStart.getDay();
        var sweepEnd = attributeConfig.SWEEP_END;
        attributeConfig.SWEEP_END = sweepEnd.getMonth()+"/"+sweepEnd.getDay();
        //Time Steps
        var REPORT_STEP_h_array = attributeConfig.REPORT_STEP_h.split(":");
        attributeConfig.REPORT_STEP = (attributeConfig.REPORT_STEP_d*24 + parseInt(REPORT_STEP_h_array[0])) +":" + REPORT_STEP_h_array[1] +":"+REPORT_STEP_h_array[2];
        delete attributeConfig.REPORT_STEP_d;
        delete attributeConfig.REPORT_STEP_h;
        var DRY_STEP_h_array = attributeConfig.DRY_STEP_h.split(":");
        attributeConfig.DRY_STEP = (attributeConfig.DRY_STEP_d*24 + parseInt(DRY_STEP_h_array[0])) +":" + DRY_STEP_h_array[1] +":"+DRY_STEP_h_array[2];
        delete attributeConfig.DRY_STEP_d;
        delete attributeConfig.DRY_STEP_h;
        var WET_STEP_h_array = attributeConfig.WET_STEP_h.split(":");
        attributeConfig.WET_STEP = (attributeConfig.WET_STEP_d*24 + parseInt(WET_STEP_h_array[0])) +":" + WET_STEP_h_array[1] +":"+WET_STEP_h_array[2];
        delete attributeConfig.WET_STEP_d;
        delete attributeConfig.WET_STEP_h;
        attributeConfig.ROUTING_STEP = "00:00:"+attributeConfig.ROUTING_STEP.toString();
        attributeConfig.SKIP_STEADY_STATE?attributeConfig.SKIP_STEADY_STATE="YES":attributeConfig.SKIP_STEADY_STATE="NO";
        //Dynamic Wave
        if(attributeConfig.VARIABLE_STEP_c){
            attributeConfig.VARIABLE_STEP = "0.00";
        }
        delete attributeConfig.VARIABLE_STEP_c;
        //Files
        return attributeConfig;
    },
    saveRunParameters(){
        console.log("-----------Variable-----------");
        console.log(this.simulationOptions);
        var reader = new FileReader();
        var originalConfig = Object.assign({},this.simulationOptions);
        var downloadConfig = this.variableToAttribute(originalConfig);
        console.log("-----------Attribute-----------");
        console.log(downloadConfig);
        var jsonBlob = new Blob([JSON.stringify(downloadConfig, null, 2)],{ type: "application/json" });
        reader.readAsDataURL(jsonBlob);
        reader.onload = function(e) {
            var a = document.createElement("a");
            a.download = "run_parameter.json";
            a.href = e.target.result;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
        }
    },
    submitOp(){
        var optionTab = this.optionTab;
        var options = this.simulationOptions;
        options.START_DATE = new Date(options.START_DATE);
        options.REPORT_START_DATE = new Date(options.REPORT_START_DATE);
        options.END_DATE = new Date(options.END_DATE);
        options.SWEEP_START = new Date(options.SWEEP_START);
        options.SWEEP_END = new Date(options.SWEEP_END);
        var operation = {
            tab: optionTab,
            options: options
        }
        this.sendMessage("operation", operation);
    }
  }
}
</script>


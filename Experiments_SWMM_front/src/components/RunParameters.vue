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
    width="500">
        <h4 slot="header">Simulation Options</h4>
        <div style="height:480px">
            <Tabs type="line" :animated="false" size="small" :value="optionTab">
                <TabPane label="General" name="General">
                    <div style="height:210px;width:100%">
                        <Row style="height:100%">
                            <Col span="12" style="height:100%">
                                <div class="cardDiv">
                                    <div class="cardTitle">Process Models</div>
                                     <CheckboxGroup v-model="processCheck" style="margin-top:15px">
                                         <Checkbox label="Rainfall/Runoff" class="processCB">
                                            <span>Rainfall/Runoff</span>
                                        </Checkbox><br/>
                                         <Checkbox label="Rainfall Dependent |/|" class="processCB">
                                            <span>Rainfall Dependent |/|</span>
                                        </Checkbox><br/>
                                         <Checkbox label="Snow Melt" class="processCB">
                                            <span>Snow Melt</span>
                                        </Checkbox><br/>
                                         <Checkbox label="Groundwater" class="processCB">
                                            <span>Groundwater</span>
                                        </Checkbox><br/>
                                         <Checkbox label="Flow Routing" class="processCB">
                                            <span>Flow Routing</span>
                                        </Checkbox><br/>
                                         <Checkbox label="Water Quality" class="processCB">
                                            <span>Water Quality</span>
                                        </Checkbox>
                                     </CheckboxGroup>
                                </div>
                            </Col>
                            <Col span="12" style="height:100%">
                                <div class="cardDiv">
                                    <div class="cardTitle">Miscellaneous</div>
                                     <CheckboxGroup v-model="miscellaneousCheck" style="margin-top:15px">
                                         <Checkbox label="Allow Ponding" class="processCB">
                                            <span>Allow Ponding</span>
                                        </Checkbox><br/>
                                         <Checkbox label="Report Control Actions" class="processCB">
                                            <span>Report Control Actions</span>
                                        </Checkbox><br/>
                                         <Checkbox label="Report Input Summary" class="processCB">
                                            <span>Report Input Summary</span>
                                        </Checkbox>
                                     </CheckboxGroup>
                                     <p style="margin: 5px 5px 0">Minimum Conduit Slope</p>
                                     <Input v-model="mininSlope" size="small" style="width:80px;margin-left:5px"/>
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
                                    <RadioGroup v-model="infiltrationRadio" style="margin-top:15px">
                                        <Radio label="Horton" class="processCB"></Radio><br/>
                                        <Radio label="Modeified Horton" class="processCB"></Radio><br/>
                                        <Radio label="Green Ampt" class="processCB"></Radio><br/>
                                        <Radio label="Curve Number" class="processCB"></Radio>
                                    </RadioGroup>
                                </div>
                            </Col>
                            <Col span="12" style="height:100%">
                                <div class="cardDiv">
                                    <div class="cardTitle">Routing Model</div>
                                    <RadioGroup v-model="routingRadio" style="margin-top:15px">
                                        <Radio label="Steady Flow" class="processCB"></Radio><br/>
                                        <Radio label="Kinematic Wave" style="margin:18px 5px"></Radio><br/>
                                        <Radio label="Dynamic Wave" class="processCB"></Radio>
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
                                    :value="SAOD"></DatePicker>
                                </Col>
                                <Col span="6" offset="1">
                                    <TimePicker format="HH:mm" 
                                    placeholder="Time" 
                                    style="width: 80px"
                                    size="small"
                                    :value="SAOT"></TimePicker>
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
                                    :value="SROD"></DatePicker>
                                </Col>
                                <Col span="6" offset="1">
                                    <TimePicker format="HH:mm" 
                                    placeholder="Time" 
                                    style="width: 80px"
                                    size="small"
                                    :value="SROT"></TimePicker>
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
                                    :value="EAOD"></DatePicker>
                                </Col>
                                <Col span="6" offset="1">
                                    <TimePicker format="HH:mm" 
                                    placeholder="Time" 
                                    style="width: 80px"
                                    size="small"
                                    :value="EAOT"></TimePicker>
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
                                    :value="SSOD"></DatePicker>
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
                                    :value="ESOD"></DatePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p style="word-break: break-word;display: inline-block;width:80px;line-height: 1.2;">Antecedent
                                    Dry Days</p>
                                </Col>
                                <Col span="8" offset="1">
                                    <Input v-model="ADD" size="small" style="width:80px;"/>
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
                                    <InputNumber v-model="RD" size="small" min="0" max="59"></InputNumber>
                                </Col>
                                <Col span="8" offset="1">
                                    <TimePicker format="HH:mm:ss" 
                                    placeholder="Time" 
                                    style="width: 100px"
                                    size="small"
                                    :value="RH"></TimePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p style="word-break: break-word;display: inline-block;width:100px;line-height: 1.2;">Runoff:<br>
                                    Dry Weather</p>
                                </Col>
                                <Col span="6" offset="1">
                                    <InputNumber v-model="RDWD" size="small" min="0" max="59"></InputNumber>
                                </Col>
                                <Col span="8" offset="1">
                                    <TimePicker format="HH:mm:ss" 
                                    placeholder="Time" 
                                    style="width: 100px"
                                    size="small"
                                    :value="RDWH"></TimePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p style="word-break: break-word;display: inline-block;width:100px;line-height: 1.2;">Runoff:<br>
                                    Wet Weather</p>
                                </Col>
                                <Col span="6" offset="1">
                                    <InputNumber v-model="RWWD" size="small" min="0" max="59"></InputNumber>
                                </Col>
                                <Col span="8" offset="1">
                                    <TimePicker format="HH:mm:ss" 
                                    placeholder="Time" 
                                    style="width: 100px"
                                    size="small"
                                    :value="RWWH"></TimePicker>
                                </Col>
                            </Row>
                            <Row class="datesItem">
                                <Col span="7" offset="1">
                                    <p>Routing</p>
                                </Col>
                                <Col span="6" offset="1">
                                    <InputNumber v-model="RDI" size="small" min="0" max="59"></InputNumber>
                                </Col>
                                <Col span="8" offset="1">
                                    <p>Seconds</p>
                                </Col>
                            </Row>
                            <div style="padding:10px 15px">
                                <div class="cardDiv">
                                    <div class="cardTitle">Steady Flow Periods</div>
                                    <div style="margin:10px 0;padding:10px">
                                        <Checkbox v-model="KSFP">Skip Steady Flow Periods</Checkbox>
                                        <Row style="margin-top:5px">
                                            <Col span="16">
                                                <p style="display:inline-block">System Flow Tolerance(%)</p>
                                            </Col>
                                            <Col span="6" offset="1">
                                                <InputNumber v-model="SFT" size="small" min="0" max="100"></InputNumber>
                                            </Col>
                                        </Row>
                                        <Row style="margin-top:5px">
                                            <Col span="16">
                                                <p style="display:inline-block">Lateral Flow Tolerance(%)</p>
                                            </Col>
                                            <Col span="6" offset="1">
                                                <InputNumber v-model="LFT" size="small" min="0" max="100"></InputNumber>
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
                                        <RadioGroup v-model="inertialTerms" style="margin-top:15px">
                                            <Radio label="Keep" class="processCB"></Radio><br/>
                                            <Radio label="Dampen" class="processCB"></Radio><br/>
                                            <Radio label="Ignore" class="processCB"></Radio>
                                        </RadioGroup>
                                    </div>
                                </Col>
                                <Col span="9">
                                    <div class="cardDiv">
                                        <div class="cardTitle">Normal Flow Criterion</div>
                                        <RadioGroup v-model="normalFC" style="margin-top:15px">
                                            <Radio label="Slope" class="processCB"></Radio><br/>
                                            <Radio label="Froude No." class="processCB"></Radio><br/>
                                            <Radio label="Both" class="processCB"></Radio>
                                        </RadioGroup>
                                    </div>
                                </Col>
                                <Col span="9" style="height:100%">
                                    <div class="cardDiv">
                                        <div class="cardTitle">Force Main Equation</div>
                                        <RadioGroup v-model="forceME" style="margin-top:15px">
                                            <Radio label="Hazen-Willarms" style="margin:10px 5px;"></Radio><br/>
                                            <Radio label="Darcy-Weisbach" style="margin:10px 5px;"></Radio>
                                        </RadioGroup>
                                    </div>
                                </Col>
                            </Row>
                        </div>
                        <div style="padding:30px 50px">
                            <div>
                                <Checkbox v-model="UVTS">Use Variable Time Steps</Checkbox>
                                <span style="margin-left:30px">Adjusted By</span>
                                <InputNumber v-model="AB" size="small" min="0" max="100" style="width:50px"></InputNumber>
                                <span style="margin-left:5px">%</span>
                            </div>
                            <div class="datesItem">
                                <div style="width:293px;display:inline-block">Time Step For Condit Lengthening (sec)</div>
                                <Input v-model="TSFCL" size="small" style="width:80px;"/>
                            </div>
                            <div class="datesItem">
                                <div style="width:293px;display:inline-block">Minimum Nodal Surface Area (sq.feet)</div>
                                <Input v-model="MNSA" size="small" style="width:80px;"/>
                            </div>
                            <div class="datesItem">
                                <div style="width:293px;display:inline-block">Maximum Trial per Time Step</div>
                                <InputNumber v-model="MTPTS" size="small" min="3" max="20"></InputNumber>
                            </div>
                            <div class="datesItem">
                                <div style="width:293px;display:inline-block">Head Convergence Tolerance (feet)</div>
                                <Input v-model="HCT" size="small" style="width:80px;"/>
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
        runParametersModal:false,
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
        //General
        processCheck:[],
        miscellaneousCheck:[],
        mininSlope:"0",
        infiltrationRadio:"Horton",
        routingRadio:"Kinematic Wave",
        //Dates
        SAOD:"",
        SAOT:"",
        SROD:"",
        SROT:"",
        EAOD:"",
        EAOT:"",
        SSOD:"01/01",
        ESOD:"12/31",
        ADD:"0",
        //Time Steps
        RD:0,
        RH:"00:15:00",
        RDWD: 0,
        RDWH:"01:00:00",
        RWWD: 0,
        RWWH:"00:05:00",
        RDI:"30",
        KSFP:false,
        SFT:5,
        LFT:5,
        //Dynamic Wave
        inertialTerms:"Dampen",
        normalFC:"Both",
        forceME:"Hazen-Willarms",
        UVTS:true,
        AB:75,
        TSFCL:0,
        MNSA:12.557,
        MTPTS:8,
        HCT:0.005
    }
  },
  methods:{
    showRunParametersModal(){
        this.runParametersModal = true;
    },
    applyDefaults(){
        confirm("apply defaults.");
    },
    closeRunParametersModal(){
        this.runParametersModal = false;
    },
    saveRunParameters(){
        confirm('save run parameters.');
    },
    submitOp(){
        confirm('Submit operation.');
    }
  }
}
</script>


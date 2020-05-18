<style scoped>
#mapCard>>>.ivu-card-body{
  padding: 2px;
}
.btnHoverBlue:hover {
  background-color: #2db7f5;
  color: white;
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
.map-container {
  position: fixed;
  top: 80px;
  left: 0;
  width: 100%;
  /* height: 100%; */
  z-index: 0;
}
.div-form-group {
  position: absolute;
  display: inline;
  top: 30px;
  right: 20px;
  z-index: 1;
}
.div-right {
  display: inline-block;
  padding: 2px;
  margin: 2px;
  border-radius: 4px;
  background-color: rgba(255, 255, 255, 0.6);
  font-size: large;
}
.btn-right {
  height: 1.375em;
  width: 1.375em;
  border-radius: 2px;
  opacity: 0.8;
  border: none;
  background-color: rgba(0, 60, 136, 0.7);
  margin: 1px;
  color: #fff;
  text-align: center;
  font-size: inherit;
}

input[type="range"] {
  -webkit-appearance: none;
  width: 300px;
  border-radius: 10px; /*这个属性设置使填充进度条时的图形为圆角*/
}
input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
}
input[type="range"]::-webkit-slider-runnable-track {
  height: 15px;
  border-radius: 10px; /*将轨道设为圆角的*/
  box-shadow: 0 1px 1px #def3f8, inset 0 0.125em 0.125em #0d1112; /*轨道内置阴影效果*/
}
input[type="range"]:focus {
  outline: none;
}
input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  height: 25px;
  width: 25px;
  margin-top: -5px; /*使滑块超出轨道部分的偏移量相等*/
  background: #ffffff;
  border-radius: 50%; /*外观设置为圆形*/
  border: solid 0.125em rgba(205, 224, 230, 0.5); /*设置边框*/
  box-shadow: 0 0.125em 0.125em #3b4547; /*添加底部阴影*/
}
.demo-spin-icon-load{
  animation: ani-demo-spin 1s linear infinite;
}
</style>
<template>
  <div style="min-width:1200px">
      <Card dis-hover style="height:100%" id="mapCard">
          <div slot="title">
              <h3>
                  <Icon type="md-pulse" />
                  Map - Flooding nodes
              </h3>
          </div>
          <!-- <div slot="extra">
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
          </div> -->
        <div style="padding:0 5px">
          <Button @click="setProjModalShow" size="small" class="btnHoverBlue">Set Projection</Button>
          <div style="float:right">
            <div style="margin:0 10px;display:inline-block">
                <span>Mix flooded hours:</span>
                <InputNumber size="small" :min="0" :step="0.01" v-model="mixFloodedHr"></InputNumber>
            </div>
            <div style="margin:0 10px;display:inline-block">
                <span>Mix flood volume (10^6 ltr):</span>
                <InputNumber size="small" :min="0" :step="0.001" v-model="mixFloodVolume"></InputNumber>
            </div>
            <Button @click="setVisualizaModalShow" size="small" class="btnHoverBlue">Show Flooding nodes</Button>
          </div>
        </div>
        <!-- 地图可视化 -->
        <div style="height:25px;padding:0 5px" v-show="sliderShow">
          <strong style="display:inline-block;">Precipitation duration:</strong>
          <Slider v-model="sliderValue" range
          style="width:800px;display: inline-block;height:20px;margin:0 15px" 
          :min="rptStart" 
          :max="rptEnd" 
          show-tip="never"
          @on-input="changeTimeScale"></Slider>
          <span>{{sliderValue[0]+15}}th Min - {{sliderValue[1]+15}}th Min</span>
        </div>
        <div class="map-container" id="map-container">
        </div>
      </Card>
      <Modal
      id="selectProj"
      v-model="selectProjectionModal"
      :mask-closable="false"
      :styles="{top: '20px'}"
      width="800">
          <div slot="header">
              <h4 style="display:inline-block">Select projection</h4>
          </div>
          <div style="height:300px">
            <!-- 投影搜索 -->
            <div>
              <span>Search projection:</span>
              <Input v-model="queryInputValue" placeholder="4326, 27700, US National Atlas, France, ..." style="width: 300px;margin:0 10px" />
              <Button type="primary" shape="circle" icon="ios-search" @click="searchProjection(queryInputValue)"></Button>
            </div>
            <!-- 投影结果 -->
            <div style="margin-top:25px">
              <Table id="epsg-result" highlight-row height="260" ref="currentRowTable" :columns="projResultCols" :data="projResultData" @on-row-click="handleTableOnRowClick">
              </Table>
            </div>
          </div>
          <div slot="footer">
              <Button @click="setNewProjection" style="margin: 0 15px;width: 200px;" size="small" type="primary" :disabled="disabledSetNewProj">Set new projection</Button>
          </div>
      </Modal>
      <Modal
      id="showNodes"
      v-model="showNodesModal"
      :mask-closable="false"
      :styles="{top: '20px'}"
      width="600">
          <div slot="header">
              <h4 style="display:inline-block">Upload data</h4>
          </div>
          <div style="height:100px;text-align: center;">
            <div>
              <span>Inp file:</span>
              <Input v-model="inpFile" placeholder="inp file" style="width: 300px;margin:0 10px" disabled />
              <Button class="btnHoverBlue" @click="uploadInp(inpFile)">upload inp</Button>
            </div>
            <div style="margin-top:25px">
              <span>Rpt file:</span>
              <Input v-model="rptFile" placeholder="rpt file" style="width: 300px;margin:0 10px" disabled />
              <Button class="btnHoverBlue" @click="uploadRpt(rptFile)">upload rpt</Button>
            </div>
          </div>
          <div slot="footer">
              <Button @click="showFloodingNodes" style="margin: 0 15px;width: 200px;" size="small" type="primary" :disabled="visualizaBtnDisabled">Show flooding nodes</Button>
          </div>
      </Modal>
      <Spin fix v-if="spinShow">
          <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
          <div>Loading</div>
      </Spin>
  </div>
</template>

<script>
import Map from 'ol/Map'
import View from 'ol/View'
import TileLayer from 'ol/layer/Tile'
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import XYZ from 'ol/source/XYZ'
import GeoJSON from 'ol/format/GeoJSON'
import 'font-awesome/css/font-awesome.min.css'
import { register } from 'ol/proj/proj4'
import Feature from 'ol/Feature'
import Point from 'ol/geom/Point'
import proj4 from 'proj4'
import { Draw, Modify, Select, Snap } from 'ol/interaction'
import { get as getProjection, getTransform } from 'ol/proj'
import { applyTransform } from 'ol/extent'
import { click } from 'ol/events/condition'
import { Circle as CircleStyle, Fill, Stroke, Style, Text } from 'ol/style'
import 'ol/ol.css'
import WebGLPointsLayer from 'ol/layer/WebGLPoints'
import ImageWMS from 'ol/source/ImageWMS'

export default {
  name: 'Map',
  components: {},
  mounted () {
    this.getHeight()
    this.initMap()

  },
  data () {
    return {
      spinShow:false,
      selectProjectionModal:false,
      disabledSetNewProj:true,
      queryInputValue: '',
      selectProjectInfo:{},
      // 投影坐标系表格的列
      projResultCols: [
        {
          type: 'index',
          width: 50,
          align: 'center'
        },
        {
          title: 'Code',
          key: 'code',
          width: 80
        },
        {
          title: 'Name',
          key: 'name'
        },
        {
          title: 'Extend',
          key: 'extend'
        },
        {
          title: 'Unit',
          key: 'unit',
          width: 80
        }
      ],
      projResultData:[],
      showNodesModal:false,
      inpFile:"",
      rptFile:"",
      inpfileUploaded:false,
      rptfileUploaded:false,
      mixFloodedHr:0.5,
      mixFloodVolume:1,
      visualizaBtnDisabled:true,
      map: Object,
      highlight: '',
      // circles related to mass
      style: {
        'variables': {
          startTime: 0,
          endTime: 165
        },
        'filter': ['between', ['get', 'currentTime'], ['var', 'startTime'], ['var', 'endTime']],//用时间过滤
        'symbol': {
          'symbolType': 'circle',
          'size': ['interpolate',
              ['linear'],
              ['get', 'mass'],//输入值
              0,
              4,
              1, 
              16,
          ],
          'color':
            ['interpolate',
              ['linear'],
              ['get', 'mass'],//输入值
              0, 
              'rgba(8,48,107,0.1)',// 极值1，颜色值 
              1,
              'rgba(8,48,107,0.8)',
            ],
          //   'color': '#006688',水墨蓝色
          'rotateWithView': false,
          'offset': [
            0,
            0
          ],
          'opacity': 0.95
        }
      },
      sliderShow:false,
      sliderValue:[0,165],
      //时间起止
      rptStart:0,
      rptEnd:165
    }

  },
  props: {
    features: {},
    drawType: String,
    operationType: String
  },
  methods: {
    getHeight: () => {
      let screenheight = window.screen.height
      let mapDiv = document.getElementById('map-container')
      mapDiv.style.height = screenheight - 60 + 'px'
    },
    initMap () {
      let that = this;
      var TiandiMap_img = new TileLayer({
        title: '天地图卫星影像',
        source: new XYZ({
          url: 'http://t3.tianditu.com/DataServer?T=img_w&x={x}&y={y}&l={z}&tk=17f29115865a40c78e148d485673d4db',
          wrapX: false,
          //   url: 'http://t0.tianditu.gov.cn/img_w/wmts?tk=17f29115865a40c78e148d485673d4db'
        })
      })
      var TiandiMap_cia = new TileLayer({
        name: '天地图影像注记图层',
        source: new XYZ({
          url: 'http://t0.tianditu.com/DataServer?T=eia_w&x={x}&y={y}&l={z}&tk=17f29115865a40c78e148d485673d4db',//英文注记
          // url: 'http://t0.tianditu.com/DataServer?T=cia_w&x={x}&y={y}&l={z}&tk=17f29115865a40c78e148d485673d4db',//parent.TiandituKey()为天地图密钥
          wrapX: false,

        })
      })

      this.map = new Map({
        // 设置地图图层
        layers: [
          // 创建一个使用Open Street Map地图源的瓦片图层
          TiandiMap_img,
          TiandiMap_cia
        ],
        // 设置显示地图的视图
        view: new View({
          center: [0, 0], // 定义地图显示中心于经度0度，纬度0度处
          zoom: 2 // 并且定义地图显示层级为2
        }),
        // 让id为map的div作为地图的容器
        target: 'map-container'
      })

      // select
      var select = new Select({
        condition: click
      })
      this.map.addInteraction(select)
      select.on('select', event => {
        this.sendSelectedFeature(event)
      })
    },
    setProjModalShow(){
      this.selectProjectInfo={};
      this.disabledSetNewProj=true;
      this.selectProjectionModal=true;
    },
    // 搜索投影坐标系
    searchProjection (query) {
      var epsg_result = document.getElementById('epsg-result')
      epsg_result.style.display = 'block'
      this.projResultData = []
      this.$Message.info('searching...');
      fetch('https://epsg.io/?format=json&q=' + query)
        .then(response => {
          return response.json()
        })
        .then(json => {
          var results = json['results']
          if (results && results.length > 0) {
            for (var i = 0, ii = results.length; i < ii; i++) {
              var result = results[i]
              if (result) {
                var item = {}
                if (
                  result['code'] &&
                  result['name'].length > 0 &&
                  result['proj4'] &&
                  result['proj4'].length > 0 &&
                  result['bbox'] &&
                  result['bbox'].length == 4 &&
                  result['unit']
                ) {
                  item.code = result['code']
                  item.name = result['name']
                  item.proj4def = result['proj4']
                  item.extend = result['bbox']
                  item.unit = result['unit']
                  this.projResultData.push(item)
                }
              }
            }
          }
        })
    },
    // 点击投影表格某行时，进行投影变换并将inp的投影设置为当前投影
    handleTableOnRowClick (data) {
      this.selectProjectInfo = data;
      this.disabledSetNewProj=false;
    },
    setNewProjection(){
      var data = this.selectProjectInfo;
      this.setProjection(
        data.code,
        data.name,
        data.proj4def,
        data.extend
      );
      this.selectProjectionModal = false;
    },
    // 设定投影坐标系
    setProjection (code, name, proj4def, bbox) {
      if (
        code === null ||
        name === null ||
        proj4def === null ||
        bbox === null
      ) {
        this.map.setView(
          new View({
            projection: 'EPSG:4326',
            center: [0, 0],
            zoom: 1
          })
        )
        return
      }
      var newProjCode = 'EPSG:' + code
      proj4.defs(newProjCode, proj4def)
      register(proj4)
      var newProj = getProjection(newProjCode)
      var fromLonLat = getTransform('EPSG:4326', newProj)
      // very approximate calculation of projection extent
      var extent = applyTransform(
        [bbox[1], bbox[2], bbox[3], bbox[0]],
        fromLonLat
      )
      newProj.setExtent(extent)
      var newView = new View({
        projection: newProj
      })
      this.map.setView(newView)
      newView.fit(extent)
    },
    setVisualizaModalShow(){
      this.inpfileUploaded=false;
      this.rptfileUploaded=false;
      this.visualizaBtnDisabled=true;
      this.showNodesModal=true;
    },
    uploadInp(inpFile){
      confirm("inp file uploaded successfully.");
      this.inpfileUploaded=true;
      this.setVisualizaBtnDisabled();
    },
    uploadRpt(rptFile){
      confirm("rpt file uploaded successfully.");
      this.rptfileUploaded=true;
      this.setVisualizaBtnDisabled();
    },
    setVisualizaBtnDisabled(){
      this.visualizaBtnDisabled=this.inpfileUploaded&&this.rptfileUploaded?false:true;
    },
    showFloodingNodes(){
      this.spinShow = true;
      this.showNodesModal=false;
      this.axios
      .get(
          "/PSWMM/vision/floodingNodes" +
          "?inpName=" + "LishuiEx"+
          "&rptName=" + "LishuiEx"+
          "&mixFloodedHr=" + this.mixFloodedHr +
          "&mixFloodVolume="+ this.mixFloodVolume
      )
      .then(res => {
        this.spinShow = false;
        if (res.data.code) {
          // this.initStyle(res.data.data);//更新渲染配置
          this.initThematic(res.data.data);
          this.sliderShow = true;
          document.getElementById("map-container").style.top="100px";
        }
        else{
            confirm("error.");
            console.log(res);
        }
      })
      .catch(err => {
        confirm("error.");
        this.spinShow = false;}
      );
    },
    initStyle(data){
      var minValue = 0;
      var maxValue = 0;
      data.forEach((item)=>{
        var value = parseFloat(item.nodeFlooding.totalFloodVolume);
        if(minValue>value){
          minValue = value;
        }
        if(maxValue<value){
          maxValue = value;
        }
      });
      this.style.symbol = {
          'symbolType': 'circle',
          'size': ['interpolate',
              ['linear'],
              ['get', 'mass'],//输入值
              minValue,
              8,
              maxValue, 
              16,
          ],
          'color':
            ['interpolate',
              ['linear'],
              ['get', 'mass'],//输入值
              minValue, 
              'rgba(8,48,107,0.1)',// 极值1，颜色值 
              maxValue,
              'rgba(8,48,107,0.8)',
            ],
          'rotateWithView': false,
          'offset': [
            0,
            0
          ],
          'opacity': 0.95
        };
    },
    changeTimeScale(val){
      this.style.variables.startTime = val[0];
      this.style.variables.endTime = val[1];
    },
    sendSelectedFeature (event) {
      console.log("选择要素");
      console.log(event.selected[0].values_.geoType);
      console.log(event.selected[0].values_.name);
      // this.$emit('sendSelectedFeature', event.selected[0].values_.geoType, event.selected[0].values_.name)
    },
    animate () {
      this.map.render();
      window.requestAnimationFrame(this.animate);
    },
    initThematic (res) {
      var features = []
      for (let i = 0; i < res.length; i++) {
        // var line = quake[i]
        var coords = res[i].coord
        // guard against bad data
        if (isNaN(coords[0]) || isNaN(coords[1])) {
          continue
        }
        for (let j = 0; j < res[i].data.length; j++) {


          // console.log('name:' + res[i].name + '  time:' + j + '  value:' + res[i].data[j].value)
          features.push(new Feature({
            mass: parseFloat(res[i].data[j].flooding || 0),
            currentTime: parseInt(j || 0),
            geometry: new Point(coords)

          }))


        }

      }
      var vectorSource = new VectorSource({
        attributions: 'NASA'
      })

      vectorSource.addFeatures(features)
      var webGLPointsLayer = new WebGLPointsLayer({
        style: this.style,
        source: vectorSource,
        disableHitDetection: true
      })
      this.map.addLayer(webGLPointsLayer)
      this.animate()
    },

  },
}
</script>
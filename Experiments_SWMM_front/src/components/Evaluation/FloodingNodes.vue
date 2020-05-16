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
  <div>
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
        <div>
          <Button @click="setProjModalShow" size="small" class="btnHoverBlue">Set Projection</Button>
          <Button @click="setVisualizaModalShow" size="small" class="btnHoverBlue">Show Flooding nodes</Button>
        </div>
        <div class="map-container" id="map-container">
          <!-- 地图可视化 -->
          <div id="map-visualization-div" style="height: 50px; width: 300px;z-index:500;display:none;top: 30px; position: absolute;  left: 45%;">
            <form>
              <div id="status">Time: <span class="min-year"></span> <span class="max-year" style="font-size:large">{{maxYearInput}}</span>minutes</div>
              <input id="max-year" type="range" min="0" max="180" step="1" value="180" style="width:300px;background-color:dodgerblue" />
            </form>
          </div>
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
      visualizaBtnDisabled:true,
      modalFile: false,
      map: Object,
      isCenterGot: false,
      centerCoords: [],
      highlightStyle: Object,
      featureOverlay: Object,
      highlight: '',
      selectedFeatures: this.features,
      drawing: '',
      modifying: '',
      oldColor: 'rgba(242,56,22,0.61)',
      newColor: '#ffe52c',
      period: 12,
      animRatio: ['^',
        ['/',
          ['%',
            ['+',
              ['time'],
              [
                'interpolate',
                ['linear'],
                ['get', 'year'],
                1850, 0,
                2015, 12
              ]
            ],
            12
          ],
          12
        ],
        0.5
      ],
      // circles related to mass
      style: {
        'variables': {
          minYear: 0,
          maxYear: 180
        },
        'filter': ['between', ['get', 'year'], ['var', 'minYear'], ['var', 'maxYear']],//用时间过滤
        'symbol': {
          'symbolType': 'circle',
          'size': [
            16,
            16
          ],
          'color':
            ['interpolate',
              ['linear'],
              ['get', 'mass'],//输入值
              1,
              'rgba(8,48,107,0.9)',
              0.4,
              'rgba(8,81,156,0.9)',
              0.2,
              'rgba(33,113,181,0.9)',
              0.1,
              'rgba(66,146,198,0.9)',
              0.06,
              'rgba(107,174,214,0.9)',
              0.05,
              'rgba(158,202,225,0.9)',
              0.04,
              'rgba(198,219,239,0.9)',
              0.03,
              'rgba(222,235,247,0.9)',
              0.02,
              'rgba(247,251,255,0.9)'
              //   黄色和红色
              //   0, 'rgba(242,56,22,0.61)',// 极值1，颜色值 
              //   0.5, '#ffe52c'//极值2，颜色值
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
      minYearInput: '0',
      maxYearInput: '180',
      quakeData: [
        ['Aachen', 21, 1880, 50.775, 6.08333],
        ['Aachen', 21, 1880, 50.775, 6.08333],
        ['Aarhus', 720, 1951, 56.18333, 10.23333],
        ['Abee', 107000, 1952, 54.21667, -113.0],
        ['Acapulco', 1914, 1976, 16.88333, -99.9],
        ['Achiras', 780, 1902, -33.16667, -64.95]
      ],
      //地图可视化起止时间
      startTime: '0',
      endTime: '180'
    }

  },
  props: {
    features: {},
    drawType: String,
    operationType: String
  },
  methods: {
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
          "&rptName=" + "LishuiEx"
      )
      .then(res => {
        this.spinShow = false;
        if (res.data.code) {
          this.initThematic(res.data.data);
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
    initMap () {
      let that = this

      //天地图文字标注
      //   var tian_di_tu_annotation = new TileLayer({
      //     title: '天地图文字标注',
      //     source: new XYZ({
      //       url: 'http://t3.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}'
      //     })
      //   })
      // map.addLayer(tian_di_tu_annotation);


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

      // map.addLayer(tian_di_tu_satellite_layer);

      //OSM
      //   var osm_layer = new TileLayer({
      //     preload: 4,
      //     source: new OSM()
      //   })

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




      // hover
      this.highlightStyle = new Style({
        stroke: new Stroke({
          color: '#f00',
          width: 1
        }),
        fill: new Fill({
          color: 'rgba(255,0,0,0.1)'
        }),
        text: new Text({
          font: '12px Calibri,sans-serif',
          fill: new Fill({
            color: '#000'
          }),
          stroke: new Stroke({
            color: '#f00',
            width: 3
          })
        })
      })
      this.featureOverlay = new VectorLayer({
        source: new VectorSource(),
        map: this.map,
        style: function (feature) {
          that.highlightStyle.getText().setText(feature.get('name'))
          return that.highlightStyle
        }
      })

      var pointermove = evt => {
        if (evt.dragging) {
          return
        }
        var pixel = this.map.getEventPixel(evt.originalEvent)
        that.displayFeatureName(pixel)
      }
      this.map.on('pointermove', pointermove)

      // select
      var select = new Select({
        condition: click
      })
      this.map.addInteraction(select)
      select.on('select', event => {
        this.sendSelectedFeature(event)
      })

      // draw
      var vector = new VectorLayer({
        source: new VectorSource(),
        style: new Style({
          fill: new Fill({
            color: 'rgba(255, 255, 255, 0.2)'
          }),
          stroke: new Stroke({
            color: '#ffcc33',
            width: 2
          }),
          image: new CircleStyle({
            radius: 7,
            fill: new Fill({
              color: '#ffcc33'
            })
          })
        })
      })
      this.map.addLayer(vector);
      this.modifying = {
        init: function () {
          this.select = new Select()
          that.map.addInteraction(this.select)

          this.modify = new Modify({
            features: this.select.getFeatures()
          })
          that.map.addInteraction(this.modify)
          this.setEvents()
        },
        setEvents: function () {
          var selectedFeatures = this.select.getFeatures()

          this.select.on('change:active', function () {
            selectedFeatures.forEach(function (each) {
              selectedFeatures.remove(each)
            })
          })
        },
        setActive: function (active) {
          this.select.setActive(active)
          this.modify.setActive(active)
        }
      }
      this.modifying.init()
      this.drawing = {
        init: function () {
          that.map.addInteraction(this.Point)
          this.Point.setActive(false)
          that.map.addInteraction(this.LineString)
          this.LineString.setActive(false)
          that.map.addInteraction(this.Polygon)
          this.Polygon.setActive(false)
          that.map.addInteraction(this.Circle)
          this.Circle.setActive(false)
        },
        Point: new Draw({
          source: vector.getSource(),
          type: 'Point'
        }),
        LineString: new Draw({
          source: vector.getSource(),
          type: 'LineString'
        }),
        Polygon: new Draw({
          source: vector.getSource(),
          type: 'Polygon'
        }),
        Circle: new Draw({
          source: vector.getSource(),
          type: 'Circle'
        }),
        getActive: function () {
          return this.activeType ? this[this.activeType].getActive() : false
        },
        setActive: function (active, drawType) {
          if (active) {
            this.activeType && this[this.activeType].setActive(false)
            this[drawType].setActive(true)
            this.activeType = drawType
          } else {
            this.activeType && this[this.activeType].setActive(false)
            this.activeType = null
          }
        }
      }
      this.drawing.init()
      var snap = new Snap({
        source: vector.getSource()
      })
      that.map.addInteraction(snap);
    },
    setOperation (operationType, drawType) {
      this.operationType = operationType
      this.drawType = drawType
      if (operationType == 'modify') {
        this.drawing.setActive(false, null)
        this.modifying.setActive(true)
      } else if (operationType == 'draw') {
        this.drawing.setActive(true, drawType)
        this.modifying.setActive(false)
      }
    },
    getHeight: () => {
      let screenheight = window.screen.height
      let mapDiv = document.getElementById('map-container')
      mapDiv.style.height = screenheight - 60 + 'px'
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
    // 添加GeoJSON数据
    setGeoJSON (data, geoCenter) {
      // 添加图层
      var vectorSource = new VectorSource({
        features: new GeoJSON().readFeatures(data)
      })
      var vectorLayer = new VectorLayer({
        source: vectorSource
      })
      this.map.addLayer(vectorLayer)

      //   var center = fromLonLat(geoCenter)
      if (null != geoCenter) {
        this.flyTo(geoCenter, function () { })
      }

    },
    // 发送选择的要素信息给父组件
    sendSelectedFeature (event) {
      this.$emit('sendSelectedFeature', event.selected[0].values_.geoType, event.selected[0].values_.name)
    },

    // deperated
    setCenterCoords (data) {
      if (!this.isCenterGot) {
        for (let key in data) {
          if (key == 'geometry') {
            if (data[key].type != 'Point') {
              this.isCenterGot = true
              this.centerCoords = data[key].coordinates[0]
              return
            }
          }
          if (data[key] && typeof data[key] == 'object') {
            this.setCenterCoords(data[key])
          }
        }
      }
    },
    // zoom
    flyTo (location, done) {
      var duration = 4000
      //   var zoom = this.map.getView().getZoom()
      var zoom = 16
      var parts = 2
      var called = false
      function callback (complete) {
        --parts
        if (called) {
          return
        }
        if (parts === 0 || !complete) {
          called = true
          done(complete)
        }
      }
      this.map.getView().animate(
        {
          center: location,
          duration: duration
        },
        callback
      )
      this.map.getView().animate(
        {
          zoom: zoom - 1,
          duration: duration / 2
        },
        {
          zoom: zoom,
          duration: duration / 2
        },
        callback
      )
    },
    // hover显示要素名称
    displayFeatureName (pixel) {
      var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
        return feature
      })
      if (feature !== this.highlight) {
        if (this.highlight) {
          this.featureOverlay.getSource().removeFeature(this.highlight)
        }
        if (feature) {
          this.featureOverlay.getSource().addFeature(feature)
        }
        this.highlight = feature
      }
    },
    updateMinYear () {
      this.style.variables.minYear = parseInt(this.minYearInput.value)
      this.updateStatusText()
    },
    updateMaxYear () {
      this.style.variables.maxYear = parseInt(this.maxYearInput.value)
      this.updateStatusText()
    },
    updateStatusText () {
      var div = document.getElementById('status')
      //   div.querySelector('span.min-year').textContent = this.minYearInput.value
      div.querySelector('span.max-year').textContent = this.maxYearInput.value
    },
    animate () {
      this.map.render()
      window.requestAnimationFrame(this.animate)
    },
    initThematic (res) {
      var div = document.getElementById('map-visualization-div')
      div.style.display = 'block'
      // color
      //   this.minYearInput = document.getElementById('min-year')
      this.maxYearInput = document.getElementById('max-year')

      //   this.minYearInput.addEventListener('input', this.updateMinYear)
      //   this.minYearInput.addEventListener('change', this.updateMinYear)
      this.maxYearInput.addEventListener('input', this.updateMaxYear)
      this.maxYearInput.addEventListener('change', this.updateMaxYear)
      this.updateStatusText()
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
            year: parseInt(j || 0),
            // year: parseInt(res[i].data[j].time || 0),
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
    initLegend () {
      //legend
      var wmsSource = new ImageWMS({
        url: 'https://ahocevar.com/geoserver/wms',
        params: { 'LAYERS': 'topp:states' },
        ratio: 1,
        serverType: 'geoserver'
      })
      var updateLegend = function (resolution) {
        var graphicUrl = wmsSource.getLegendUrl(resolution)
        var img = document.getElementById('legend')
        img.src = graphicUrl
      }

      var resolution = this.map.getView().getResolution()
      updateLegend(resolution)

      // Update the legend when the resolution changes
      this.map.getView().on('change:resolution', function (event) {
        var resolution = event.target.getResolution()
        updateLegend(resolution)
      })
    }

  },
  mounted () {
    this.getHeight()
    this.initMap()
    // this.initThematic()

  }
}
</script>
<style scoped>

</style>
>
</style>
<template>
    <div>
        <div id="map" style="height:600px;width:1000px"></div>
    </div>
</template>
<script>
import L from "leaflet"
import "leaflet/dist/leaflet.css"
import 'leaflet.pm'
import 'leaflet.pm/dist/leaflet.pm.css'
delete L.Icon.Default.prototype._getIconUrl;
//请求带上cookie以防session丢失
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png")
});
export default {
    name:"CollaborativeEditor",
    mounted(){
        this.initMap();
        this.initControl();
        this.initSidebar();
        this.initLayers();
        this.listenOperation();
    },
    data(){
        return{
            map: null,
            operateLayers: null,
            lastLayers: null,
        }
    },
    methods:{
        initMap(){
            this.map = L.map("map", {
            crs: L.CRS.EPSG3857,
            center: L.latLng(28.46, 119.92),
            zoom: 14
            });
        },
        initControl(){
            // 图层控件
            var tdtVectorMap =
                "http://t0.tianditu.gov.cn/vec_w/wmts?tk=8b02739f47b03c98556cc1a9b87991dc" +
                "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
            var tdtVectorAno =
                "http://t0.tianditu.gov.cn/eva_w/wmts?tk=8b02739f47b03c98556cc1a9b87991dc" +
                "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=eva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
            var tdtImgMap =
                "http://t0.tianditu.gov.cn/img_w/wmts?tk=8b02739f47b03c98556cc1a9b87991dc" +
                "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
            var tdtImgAno =
                "http://t0.tianditu.gov.cn/eia_w/wmts?tk=8b02739f47b03c98556cc1a9b87991dc" +
                "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=eia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";

            var vectorMap = L.tileLayer(tdtVectorMap, {
                maxZoom: 20,
                attribution:
                '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
            });
            var vectorAno = L.tileLayer(tdtVectorAno, { maxZoom: 18 });
            var vector = L.layerGroup([vectorMap, vectorAno]);
            var vectorNoAno = L.layerGroup([vectorMap]);

            var satelliteMap = L.tileLayer(tdtImgMap, {
                maxZoom: 18,
                attribution:
                '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
            });
            var satelliteAno = L.tileLayer(tdtImgAno, { maxZoom: 18 });
            var satellite = L.layerGroup([satelliteMap, satelliteAno]);
            var satelliteNoAno = L.layerGroup([satelliteMap]);

            var baseLayers = {
                "Vector map": vectorNoAno,
                "Satellite map": satelliteNoAno,
                "Vector with Annotation": vector,
                "Satellite with Annotation": satellite
                // "Google satellite map": googleSatellite
            };
            var overlayLayers = {};
            L.control.layers(baseLayers, overlayLayers).addTo(this.map);
            baseLayers["Vector with Annotation"].addTo(this.map);
            // 比例尺
            L.control
                .scale({
                position: "bottomleft"
                })
                .addTo(this.map);
        },
        initSidebar(){
            // 侧边栏
            var options = {
                position: "topleft", // toolbar position, options are 'topleft', 'topright', 'bottomleft', 'bottomright'
                drawMarker: false, // adds button to draw markers
                drawPolyline: false, // adds button to draw a polyline
                drawRectangle: false, // adds button to draw a rectangle
                drawPolygon: true, // adds button to draw a polygon
                drawCircle: false, // adds button to draw a cricle
                drawCircleMarker:false,
                cutPolygon: false, // adds button to cut a hole in a polygon
                editMode: true, // adds button to toggle edit mode for all layers
                dragMode: false,
                removalMode: true // adds a button to remove layers
            };
            this.map.pm.addControls(options);
        },
        initLayers(){
            this.lastLayers = L.layerGroup([]);
            this.lastLayers.addTo(this.map);
            this.operateLayers = L.layerGroup([]);
            this.operateLayers.addTo(this.map);
        },
        listenOperation(){
            //添加新元素
            this.map.on("pm:create", e => {
                this.operateLayers.addLayer(e.layer);
                this.lastLayers.addLayer(e.layer);
            });
            // 编辑元素
            this.map.on("pm:edit", e => {
            });
            // 删除元素
            this.map.on("pm:remove", e => {
                this.operateLayers.removeLayer(e.layer);
                console.log(this.operateLayers.toGeoJSON());
                console.log(this.lastLayers.toGeoJSON());
            });
        }
    }
}
</script>

<template>
  <div class="monitor" ref="monitor">
    <div class="title">集群监控</div>
    <div class="description">集群监控功能提供了对平台中的集群状态、集群物理资源、资源利用率、节点状态等信息的监控展示。</div>
    <el-card v-loading="loading" style="min-height: 300px;">
      <el-container>
        <el-header>
          集群：
          <el-select v-model="monitorValue" size="small" placeholder="请选择" @change="clusterChange()">
            <el-option
              v-for="item in monitorList"
              :key="item.clusterId"
              :label="item.clusterName"
              :value="item.clusterId">
            </el-option>
          </el-select>
          命名空间：
          <el-select v-model="namespaceValue" size="small" placeholder="请选择" @change="namespaceChange()">
            <el-option
              v-for="item in namespaceList"
              :key="item.namespaceIdentify"
              :label="item.namespaceName"
              :value="item.namespaceIdentify">
            </el-option>
          </el-select>
          设置刷新频率：
          <el-select v-model="refreshRateValue" size="small" placeholder="请选择" @change="setRefreshRate()">
            <el-option
              v-for="item in refreshRateList"
              :key="item.rateValue"
              :label="item.rateName"
              :value="item.rateValue">
            </el-option>
          </el-select>
          <el-button size="small" icon="el-icon-refresh" @click="clusterChange()"></el-button>
          <!--节点：-->
          <!--<el-select v-model="nodeValue" size="small" placeholder="请选择">-->
          <!--<el-option-->
          <!--v-for="item in nodeList"-->
          <!--:key="item.value"-->
          <!--:label="item.label"-->
          <!--:value="item.value">-->
          <!--</el-option>-->
          <!--</el-select>-->
        </el-header>
        <el-main>
          <el-collapse v-model="activeNames" @change="">
            <el-collapse-item title="应用" name="1">
              <el-row :gutter="20">
                <el-col :span="9" :offset="0">
                  <div ref="appCpuBrokenLine" :style="{width: '650px', height: '400px'}"></div>
                </el-col>
                <el-col :span="8" :offset="2">
                  <div ref="appMemBrokenLine" :style="{width: '650px', height: '400px'}"></div>
                </el-col>
              </el-row>
            </el-collapse-item>
          </el-collapse>
        </el-main>
      </el-container>
    </el-card>

  </div>
</template>
<script>
  import echarts from 'echarts';
  import axios from 'axios';
  import {getAllClusterMonitorInfo, getMaster, monitorProxy} from '../../axios/api'



  export default {
    name: "monitor",
    data() {
      return {
        //集群下拉框变量定义
        monitorList: [],
        monitorValue: '',

        //命名空间下拉框变量定义
        namespaceList: [],
        namespaceValue: '',
        monitorIP: '',
        //刷新频率变量定义
        refreshRateList: [
          {rateName: '30秒', rateValue: '30000',},
          {rateName: '一分钟', rateValue: '60000'},
          {rateName: '五分钟', rateValue: '300000'},
        ],
        refreshRateValue: '',

        nodeList: [],
        nodeValue: '',
        showItem: 0,
        boxWidth: '',
        boxHeight: '240px',
        loading: false,
        activeNames: ['1'],
        queryData: [],
        lineOptions: {
          title: {
            text: ''
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            type: 'scroll',
            orient: 'vertical',
            right: 50,
            top: 20,
            bottom: 20,
            data: []
          },
          xAxis: {

            data: []
          },
          yAxis: {
            splitLine: {
              show: false,
            },
            axisLabel: {
              formatter: ''
            },
            min: 'dataMin',
          },
          series: []
        }
      };
    },
    mounted() {

      if (this.$route.meta.keepAlive === false && this.$route.name == 'monitorApp') {
        this.getInfo();
        this.boxWidth = this.$refs.monitor.offsetWidth - 500 + 'px';
        window.onresize = () => {
          if (this.$route.meta.keepAlive === false && this.$route.name == 'monitorApp') {
            return (() => {
              this.boxWidth = this.$refs.monitor.offsetWidth - 500 + 'px';
            })()
          }
        };
      }
    },
    methods: {

      getInfo() {
        this.loading = true;
        getAllClusterMonitorInfo().then((res) => {
          if (res.success) {
            this.monitorList = res.data;
            this.monitorValue = this.monitorList[0].clusterId;
            this.clusterChange()
          } else {

          }
          this.loading = false;
        })
      },
      getData(type, data) {
        let eNode = this.echarts.init()
        let result = [];
        data.forEach((item) => {
          result.push(item[type]);
        })
        return result;
      },
      setRefreshRate() {
        let int = 0;
        clearInterval(int)
        int = setInterval(() => {
          this.clusterChange()
        }, this.refreshRateValue);
      },
      clusterChange() {
        //查询所选集群的命名空间

        //获取到选中的集群的某个管理节点
        getMaster({clusterId: this.monitorValue}).then((res) => {
          if (res.success) {
            //刷新echarts
            this.monitorIP = res.data.masterNode.nodeIP
            this.namespaceList = res.data.namespace
            this.queryEChartsData(this.namespaceValue)
          } else {
            this.$message({
              message: '无可用节点',
              center: true
            });
          }
        })
      },
      namespaceChange() {
        this.queryEChartsData(this.namespaceValue)
      },
      queryEChartsData(namespace) {
        if (namespace == undefined || namespace === "") {
          namespace = ".."
        }
        this.httpProxy(echarts.init(this.$refs.appCpuBrokenLine), {
          queryProper: {
            queryUnitExp: 1,
            queryUnit: '%',
            title: '应用CPU使用率',
            needLegend: true,

          },
          queryParams: {
            query: 'sum(rate(container_cpu_usage_seconds_total{namespace=~"' + namespace + '"}[1m])) by (pod_name) * 100',
            start: new Date().getTime() / 1000 - 3600,
            end: new Date().getTime() / 1000,
            step: '10'
          }
        })
        this.httpProxy(echarts.init(this.$refs.appMemBrokenLine), {
          queryProper: {
            queryUnitExp: 1024 * 1024,
            queryUnit: 'M',
            title: '应用内存使用',
            needLegend: true,
          },
          queryParams: {
            query: 'sum(container_memory_usage_bytes{namespace=~"' + namespace + '"})by(pod_name)',
            start: new Date().getTime() / 1000 - 3600,
            end: new Date().getTime() / 1000,
            step: '10'
          }
        })
      },
      httpProxy(brokenLine, params) {
        params.queryParams.query = encodeURIComponent(params.queryParams.query);
        monitorProxy({
          monitorIP: this.monitorIP,
          monitorParamsVo: params.queryParams
        }).then(response => {
          this.lineOptions.xAxis.data = JSON.parse(response.data).data.result[0].values.map((item) => {
            //横坐标，时间戳转换为时间格式
            return new Date(parseInt(item[0]) * 1000).toLocaleString().replace(/:\d{1,2}$/, ' ')
          })
          JSON.parse(response.data).data.result.forEach((item) => {
            this.lineOptions.legend.data.push(item.metric.pod_name)
            this.lineOptions.series.push({
                name: item.metric.pod_name,
                type: 'line',
                stack: item.metric.pod_name,
                data: item.values.map((vl) => {
                  return vl[1] / params.queryProper.queryUnitExp
                })
              }
            )
          })
          //设置纵坐标单位
          this.lineOptions.yAxis.axisLabel.formatter = '{value}' + params.queryProper.queryUnit
          //设置标题
          this.lineOptions.title.text = params.queryProper.title
          brokenLine.setOption(this.lineOptions, true)
          //每次绘图结束清空数据，因为push方式不会被覆盖
          this.lineOptions.series = []
        });
      }
      ,

    }
  }
  ;
</script>
<style lang="scss" type="text/scss">
  .monitor {
    .el-card__body {
      padding: 0px;
    }
  }
</style>
<style lang="scss" scoped type="text/scss">
  $title-color: #ebeef5;
  .monitor {
    height: 100%;
    padding: 20px 25px;
    box-sizing: border-box;
    @include beautifyScrollbar();
    overflow-y: auto;
  }

  .title {
    height: 32px;
    line-height: 32px;
    color: $main-ft-color;
    font-size: 18px;
  }

  .description {
    margin: 10px 0 20px;
    padding: 20px;
    color: $main-ft-color;
    background-color: $description-color;
  }

  .header-title {
    padding: 18px 20px;
    border-bottom: 1px solid $title-color;
    @include userSelect();
    cursor: pointer;

    > i {
      margin-right: 10px;
    }
  }

  .bd-t {
    border-top: 1px solid $title-color;
  }

  .content {
    @include flexbox-display-flex(row);
  }

  .info-box {
    width: 250px;
    min-width: 180px;
  }

  .info {
    color: #999;
    font-size: 16px;
    line-height: 32px;
    @include text-overflow();
    font-weight: bold;
  }

  $color1: rgba(142, 113, 199, 0.7);
  $color2: #419ff4;
  $circle-height: 140px;
  .circle-box {
    width: $circle-height;
    padding-right: 40px;
    min-width: $circle-height + 20;
  }

  .circle {
    width: $circle-height;
    height: $circle-height;
    line-height: $circle-height/4;
    box-sizing: border-box;
    border-radius: 100%;
    margin: 10px auto 0;
    text-align: center;
    font-size: 14px;
    @include transition();
  }

  .color1 {
    color: $color1;
    border: solid 4px $color1;
  }

  .color2 {
    color: $color2;
    border: solid 4px $color2;
  }

  .title1 {
    color: $color1;
    text-align: center;
    padding-top: 20px;
    font-size: 16px;
  }

  .title2 {
    color: $color2;
    text-align: center;
    padding-top: 20px;
    font-size: 16px;
  }

  .line-box {
    @include flexbox-flex();
    height: 240px;
  }

  .no-data {
    background-color: #fafafa;
    color: $text-l2-color;
    height: 220px;
    margin-top: 10px;
    text-align: center;
    line-height: 220px;
  }

  .pd-t3 {
    padding-top: 30px;
  }

  .pd-20 {
    padding: 20px;
  }
</style>

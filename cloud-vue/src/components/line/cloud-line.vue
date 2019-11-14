<template>
  <div :id="'line_'+id" :style="style"></div>
</template>
<script>
  import echarts from 'echarts';
  export default {
    name: 'CloudLine',
    props: {
      //父组件需要传递的参数：id，width，height，option
      id: {
        type: String,
      },
      width: {
        type: String,
        default: "100%"
      },
      height: {
        type: String,
        default: "300px"
      },
      dataX: {
        type: Array,
      },
      dataY: {
        type: Array,
      },
      lineColor: {
        type: String,
        default: "#a1c4fd"
      },
      option: {
        type: Object,
        default(){
          return {
            tooltip : {
              trigger: 'axis'
            },
            color: this.lineColor,
            grid: {
              top: '10',
              left: '10',
              right: '30',
              bottom: '10',
              containLabel: true
            },
            xAxis : [
              {
                type: 'category',
                boundaryGap: false,
                data: this.dataX,
                axisLine: {
                  lineStyle: {
                    color: '#606266',
                  }
                },
                splitLine:{
                  show:false
                },
              }
            ],
            yAxis : [
              {
                type : 'value',
                axisLine: {
                  lineStyle: {
                    color: '#606266',
                  }
                },
                splitLine:{
                  show:false
                },
              }
            ],
            series : [
              {
                name:'利用率',
                type:'line',
                data: this.dataY,
                smooth: true,
                lineStyle:{
                  normal:{
                    color: this.lineColor,
                  }
                }
              }
            ]
          }
        }
      }
    },
    watch: {
      //观察option的变化
      dataY: {
        handler() {
          if (this.chart) {
            this.chart.setOption(this.option);
          } else {
            this.init();
          }
          this.chart.hideLoading();
        },
        deep: true //对象内部属性的监听，关键。
      }
    },
    computed: {
      style() {
        return {
          height: this.height,
          width: this.width,
        };
      }
    },
    methods:{
      init(){
        this.chart = echarts.init(document.getElementById('line_' + this.id));
        this.chart.setOption(this.option);
        window.addEventListener("resize", this.chart.resize);
      }
    },
    mounted(){
      this.init();
    }
  }
</script>

<template>
  <div :id="'pie_'+id" style="width: 300px; height: 250px; margin: 0 auto;"></div>
</template>
<script>
  import echarts from 'echarts';
  export default {
    name: 'DrawPie',
    props: {
      //父组件需要传递的参数
      id: {
        type: String,
      },
      topTitle: {
        type: String,
      },
      dataPie: {
        type: Array
      },
      describe: {
        type: Array,
      },
      option: {
        type: Object,
        default() {
          return {
            title : {
              text: this.topTitle,
              x:'center',
              textStyle:{
                color: '#666',
                fontWeight: 'normal',
                fontSize: '16',
              }
            },
            tooltip : {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
              x : 'center',
              y : 'bottom',
              icon: 'circle',
              data: this.describe,
              formatter: (name)=>{
                for (var i = 0;i < this.dataPie.length;i++){
                  if(name == this.dataPie[i].name){
                    return name + '：'+this.dataPie[i].value;
                  }
                }
              }
            },
            color:['#3ED6E5','#3EE54D','#CEE53E','#E5CB3E','#B27E5F'],
            series : [
              {
                name: '信息',
                type: 'pie',
                radius : '55%',
                avoidLabelOverlap: false,
                label: {
                  normal: {
                    show: false,
                    position: 'center'
                  },
                  emphasis: {
                    show: true,
                    textStyle: {
                      fontSize: '16'
                    }
                  }
                },
                labelLine: {
                  normal: {
                    show: false
                  }
                },
                data: this.dataPie
              }
            ]
          }
        }
      }
    },
    watch: {
      //观察option的变化
      dataPie: {
        handler() {
          this.option.series[0].data = this.dataPie;
          if (this.chart) {
            this.chart.setOption(this.option);
          } else {
            this.init();
          }
        },
        deep: true //对象内部属性的监听，关键。
      }
    },
    methods:{
      init(){
        this.chart = echarts.init(document.getElementById('pie_' + this.id));
        this.chart.setOption(this.option);
        window.addEventListener("resize", this.chart.resize);
      }
    }
  }
</script>

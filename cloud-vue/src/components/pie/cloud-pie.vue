<template>
  <div :id="'pie_'+id" style="width: 300px; height: 220px; margin: 0 auto;"></div>
</template>
<script>
  import echarts from 'echarts';
  export default {
    name: 'CloudPie',
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
        default(){
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
            tooltip: {
              trigger: 'item',
              formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            legend: {
              x : 'center',
              y : 'bottom',
              data: this.describe,
              icon: 'circle',
              formatter: (name)=>{
                for (var i = 0;i < this.dataPie.length;i++){
                  if(name == this.dataPie[i].name){
                    return name + '：'+this.dataPie[i].value;
                  }
                  // else if(name == this.dataPie[1].name){
                  //   return name + '：'+this.dataPie[1].value;
                  // }
                }
              }
            },
            color:['#67C23A', '#E6A23C'],
            series: [
              {
                name:'信息',
                type:'pie',
                radius: ['45%', '60%'],
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

<template>
  <div class="monitor" ref="monitor">
    <div class="title">节点信息</div>
    <div class="description">节点信息描述</div>
    <el-card>
      <div v-for="(item, index) in monitorList" :key="index">
        <div class="header-title" :class="{'bd-t': index != 0}" @click="showItem = index"><i :class="showItem == index? 'el-icon-arrow-down' : 'el-icon-arrow-right'"></i>{{item.nodeHostName}}</div>
        <el-collapse-transition>
          <div class="pd-20" v-if="showItem == index">
            <div class="content">
              <div class="info-box">
                <div class="info">主机名：{{item.nodeHostName}}</div>
                <div class="info">IP：{{item.nodeIp}}</div>
                <div class="info">操作系统：{{item.nodeOs}}</div>
                <div class="info">运行情况：{{item.nodeStatus}}</div>
                <div class="info">POD：已分配{{item.nodeUsedPod}} | 总量{{item.nodeMaxPod}}</div>
                <div class="info">查看明细：<span style="color:#0091EA;">查看</span></div>
              </div>
              <div class="line-box">
                <div>
                  <div class="line-title">CPU利用率</div>
                  <cloud-line :id="'cpu_'+item.nodeId" :width="boxWidth" :height="boxHeight" :data-x="getData('biztime',item.cpuAvails)" :data-y="getData('cpuAvail',item.cpuAvails)" line-color="rgba(142, 113, 199, 0.7)"></cloud-line>
                </div>
                <div class="line-box"></div>
                <div>
                  <div class="line-title">内存利用率</div>
                  <cloud-line :id="'memory_'+item.nodeId" :width="boxWidth" :height="boxHeight" :data-x="getData('biztime',item.memAvails)" :data-y="getData('memAvail',item.memAvails)" line-color="#409EFF"></cloud-line>
                </div>
              </div>

            </div>
          </div>
        </el-collapse-transition>
      </div>
    </el-card>
  </div>
</template>
<script>
  import echarts from 'echarts';
  import { getNodeMonitor } from '../../axios/api'
  export default {
    name: "monitor",
    data() {
      return {
        monitorList:[],
        showItem: 0,
        boxWidth:'',
        boxHeight: '240px',
      };
    },
    mounted() {
      if(this.$route.meta.keepAlive === false && this.$route.name == 'monitorNode') {
        this.getInfo();
        this.boxWidth = (this.$refs.monitor.offsetWidth - 400)/2 + 'px';
        window.onresize = () => {
          if(this.$route.meta.keepAlive === false && this.$route.name == 'monitorNode') {
            return (() => {
              this.boxWidth = (this.$refs.monitor.offsetWidth - 400)/2 + 'px';
            })()
          }
        };
      }
    },
    methods: {
      getInfo(){
        getNodeMonitor({clusterId:this.$route.query.clusterId}).then((res)=>{
          if(res.success){
            this.monitorList = res.data;
          }
        })
      },
      getData(type,data){
        let result = [];
        data.forEach((item)=>{
          result.push(item[type]);
        })
        return result;
      }
    }
  };
</script>
<style lang="scss" type="text/scss">
  .monitor {
    .el-card__body { padding: 0px; }
  }
</style>
<style lang="scss" scoped type="text/scss">
  $title-color: #ebeef5;
  .monitor { height: 100%; padding: 20px 25px; box-sizing: border-box; @include beautifyScrollbar(); overflow-y: auto; }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .header-title { padding: 18px 20px; border-bottom: 1px solid $title-color; @include userSelect(); cursor: pointer;
    > i { margin-right: 10px; }
  }
  .bd-t {  border-top: 1px solid $title-color; }
  .content { @include flexbox-display-flex(row); }
  .info-box { width: 250px; min-width: 180px; }
  .info { color: $main-ft-color; font-size: 16px; line-height: 32px; @include text-overflow(); font-weight: bold; }
  .line-title { text-align: left; font-size: 16px; padding-bottom: 10px;  }
  .line-box { @include flexbox-display-flex(row); @include flexbox-flex(); }
  .pd-t3 { padding-top: 30px; }
  .pd-20 { padding: 20px; }
</style>

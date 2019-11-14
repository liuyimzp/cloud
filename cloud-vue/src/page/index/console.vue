<template>
  <div class="console">
    <el-card shadow="hover" class="card-box">
      <div class="card-title">资源总览</div>
      <el-row :span="24" class="pd-t2">
        <el-col :span="8">
          <cloud-pie id="1" :data-pie="dataPie1" top-title="集群" :describe="['正常','异常']"></cloud-pie>
        </el-col>
        <el-col :span="8">
          <cloud-pie id="2" :data-pie="dataPie2" top-title="节点" :describe="['正常','异常']"></cloud-pie>
        </el-col>
        <el-col :span="8">
          <cloud-pie id="3" :data-pie="dataPie3" top-title="存储池" :describe="['总存储','已使用存储']"></cloud-pie>
        </el-col>
      </el-row>
    </el-card>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" class="card-box mg-t2" style="height: 204px">
          <div class="card-title">工作负载</div>
          <div class="work-info pd-t2">
            <p><span class="title-right">已部署应用：</span><span class="ft-num">{{dataInfo.totalApps}}</span>个</p>
            <p class="color-success"><span class="title-right">运行中：</span><span class="ft-num">{{dataInfo.runningApps}}</span>个</p>
            <p class="color-warning"><span class="title-right">未发布：</span><span class="ft-num">{{dataInfo.noReleases}}</span>个</p>
            <p class="color-warning"><span class="title-right">未启动：</span><span class="ft-num">{{dataInfo.notRunnings}}</span>个</p>
            <p class="color-warning"><span class="title-right">已停止：</span><span class="ft-num">{{dataInfo.stopApps}}</span>个</p>
            <!--<p class="color-warning"><span class="title-right">运行异常：</span><span class="ft-num">{{dataInfo.noDeployApps}}</span>个</p>-->
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" v-loading="podsSumLoadStatus" class="card-box mg-t2" style="height: 204px">
          <div class="card-title">容器组（POD）总数</div>
          <div class="pod-info">
            <div>容器组（POD）总数：<span class="color-info ft-num">{{podsSum}}</span></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="card-box mg-t2" style="height: 263px">
          <div class="card-title">镜像仓库</div>
          <div class="pd-t2">
            <p :class="dataInfo.registryStatus == 1 ? 'color-success':'color-warning'"><span class="title-right">运行状态：</span>{{dataInfo.registryStatus == 1 ? '正常':'异常'}}</p>
            <p><span class="title-right">中间件镜像：</span><span class="ft-num">{{dataInfo.middlewareImages}}</span>个</p>
            <p><span class="title-right">用户镜像：</span><span class="ft-num">{{dataInfo.userImages}}</span>个</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover" class="card-box mg-t2" style="height: 263px">
          <div class="card-title">平台动态</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="line-info" ref="line">
                <p>新增应用数</p>
                <cloud-line id="1" :width="boxWidth" height="150px" :data-x="dataX" :data-y="dataY1" line-color="#409EFF"></cloud-line>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="line-info">
                <p>新增镜像数</p>
                <cloud-line id="2" :width="boxWidth" height="150px" :data-x="imageX" :data-y="imageY" line-color="#409EFF"></cloud-line>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>

  import { showPlatformInfo,getPodsSum } from '../../axios/api'
  export default {
    name: 'console',
    data(){
      return{
        boxWidth: '',
        dataInfo:{},
        dataPie1:[],
        dataPie2:[],
        dataPie3:[],

        imageX:[],
        imageY:[],
        dataX:[],
        dataY1:[],
        podsSum:0,
        podsSumLoadStatus:true
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'console') {
        this.boxWidth = this.$refs.line.offsetWidth + 'px';
        this.getInfo();
        this.fnGetPodsSum();
        window.onresize = () => {
          if(this.$route.meta.keepAlive === false && this.$route.name == 'console') {
            return (() => {
              this.boxWidth = this.$refs.line.offsetWidth + 'px';
            })()
          }
        };
      }
    },
    methods:{
      getInfo(){
        showPlatformInfo().then((res)=>{
          if(res.success){
            this.dataInfo = res.data;
            this.dataPie1.push({value:res.data.runningClusters, name:'正常'});
            this.dataPie1.push({value:res.data.abnormalClusters, name:'异常'});
            this.dataPie2.push({value:res.data.runningNodes, name:'正常'});
            this.dataPie2.push({value:res.data.abnormalNodes, name:'异常'});
            this.dataPie3.push({value:res.data.totalStorage, name:'总存储'});
            this.dataPie3.push({value:res.data.totalStorage - res.data.freeStorage, name:'已使用存储'});
            for(let i = 0;i<res.data.imageTime.length;i++){
              this.imageX.push(res.data.imageTime[i]);
            }
            for(let i = 0;i<res.data.imageNum.length;i++){
              this.imageY.push(res.data.imageNum[i]);
            }
            for(let i = 0;i<res.data.appTime.length;i++){
              this.dataX.push(res.data.appTime[i]);
            }
            for(let i = 0;i<res.data.appNum.length;i++){
              this.dataY1.push(res.data.appNum[i]);
            }
            //stopApps,totalClusters,totalNodes
          }
        })
      },
      fnGetPodsSum(){
        getPodsSum().then((res)=>{
          if(res.success){
            this.podsSumLoadStatus=false,
            this.podsSum=res.data
          }
        })
      },
    }
  }
</script>
<style lang="scss" scoped type="text/scss">
  .console { padding: 20px; @include beautifyScrollbar(); overflow-y: auto; height: 100%; box-sizing: border-box; }
  .card-box { box-shadow: 0 2px 4px 0 rgba(0,0,0,.1);  color: $text-l1-color; }
  .mg-t2 { margin-top: 20px; }
  .pd-t2 { padding-top: 20px; }
  .mg-l1 { margin-left: 10px; }
  .card-title { border-left: solid 3px $sub-color; font-size: 16px; line-height: 16px; color: $sub-color; padding-left: 6px; }
  $pot-size: 12px;
  .circle-status { margin-top: 12px; }
  .status-pot { width: $pot-size; height: $pot-size; background-color: #e5e9f2; border-radius: 100%; display: inline-block; margin-right: 2px; }
  .line-info { width: 100%; padding-top: 10px }
  .pod-info { height: 140px; line-height: 140px; width: 240px; margin: 0 auto; padding-left: 130px; @include bgimgCover($src:"img/docker.png",$size: 113px 90px, $position: left center); }
  .title-right { display: inline-block; width: 100px; text-align: right; }
  .ft-num { font-size: 18px; margin: 0 6px }
  .color-info { color: $color-info }
  .color-success { color: $color-success;width: 50%;float: left}
  .bg-success { background-color: $color-success }
  .color-warning { color: $color-warning ;width: 50%;float: left}
</style>

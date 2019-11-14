<template>
  <div class="resourceConfig">
    <el-card>
      <!--应用设置-->
      <div class="header-title" @click="show1 = !show1"><i :class="show1? 'el-icon-arrow-down' : 'el-icon-arrow-right'"></i>应用设置</div>
      <el-collapse-transition>
        <div v-if="show1">
          <el-form label-position="left" label-width="80px" class="pd-2 bd-b" :model="formInfo" ref="formInfo">
            <el-row>
              <el-col :span="10">
                <el-form-item label="镜像名称">
                  <el-input v-model="formInfo.imagePath" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="2" style="text-align: center;">
                <el-button type="text" @click="toChangeImage">{{formInfo.imagePath?'更换':'新增'}}镜像</el-button>
              </el-col>
              <el-col :span="6" :offset="1"><div class="version"><span>版本：</span><span>{{formInfo.version? formInfo.version : '暂无'}}</span></div></el-col>
            </el-row>
            <el-form-item label="CPU配额">
              <el-slider v-model="cpuQuota" style="width: 90%" range :step="0.25" :max="totalCpu" @change="cpuChange" v-if="cpuShow"></el-slider>
            </el-form-item>
            <div class="slider-detail">
              <span>申请</span>
              <span class="slider-unit">{{cpuQuota[0]}} Core</span>
              <div class="describe">CPU申请值，描述需要预备给容器的CPU值，依据此值选择有足够资源的节</div>
            </div>
            <div class="slider-detail">
              <span>限制</span>
              <span class="slider-unit">{{cpuQuota[1]}} Core</span>
              <div class="describe">CPU限制值，描述需要预备给容器的CPU值，依据此值选择有足够资源的节</div>
            </div>
            <el-form-item label="内存配额">
              <el-slider v-model="memoryQuota" style="width: 90%" range :step="0.5" :max="totalMemory" @change="memoryChange" v-if="memoryShow"></el-slider>
            </el-form-item>
            <div class="slider-detail">
              <span>申请</span>
              <span class="slider-unit">{{memoryQuota[0]}} Gib</span>
              <div class="describe">Gib申请值，描述需要预备给容器的CPU值，依据此值选择有足够资源的节</div>
            </div>
            <div class="slider-detail">
              <span>限制</span>
              <span class="slider-unit">{{memoryQuota[1]}} Gib</span>
              <div class="describe">Gib申请值，描述需要预备给容器的CPU值，依据此值选择有足够资源的节</div>
            </div>
          </el-form>
        </div>
      </el-collapse-transition>
      <!--生命周期-->
      <div class="header-title" @click="show2 = !show2"><i :class="show2? 'el-icon-arrow-down' : 'el-icon-arrow-right'"></i>生命周期</div>
      <el-collapse-transition>
        <div class="pd-2 bd-b" v-if="show2">
          <div class="describe">生命周期脚本定义，主要针对容器类应用的生命周期事件应该采取的动作。</div>
          <el-radio-group v-model="lifeCycle" style="margin: 15px auto;">
            <el-radio-button label="启动"></el-radio-button>
            <el-radio-button label="启动后处理"></el-radio-button>
            <el-radio-button label="停止前处理"></el-radio-button>
          </el-radio-group>
          <div class="describe">{{lifeCycle}}命令如何使用。</div>
          <p>{{lifeCycle}}命令</p>
          <el-button type="text" icon="el-icon-circle-plus-outline" style="padding-top: 0px" @click="showInput = true">添加</el-button>
          <el-input v-if="showInput" v-model="addCommand"></el-input>
        </div>
      </el-collapse-transition>
      <!--健康检查-->
      <div class="header-title" @click="show3 = !show3"><i :class="show3? 'el-icon-arrow-down' : 'el-icon-arrow-right'"></i>健康检查</div>
      <el-collapse-transition>
        <div class="pd-2" v-if="show3">
          <div class="describe">健康检查是指容器运行过程中根据用户需要定时检查容器健康状况或是容器中应用的健康状况。</div>
          <el-collapse accordion style="margin-top: 15px;">
            <el-collapse-item>
              <template slot="title">应用存活探针，探测应用是否已经启动<el-button type="text" icon="el-icon-question"></el-button></template>
              <el-form ref="healthFormInfo" :model="healthFormInfo" class="health-form">
                <el-form-item label="延迟探测时间，标识从应用启动后多久开始探测">
                  <el-input type="input"></el-input>
                </el-form-item>
                <el-form-item label="超时时间，表示探测超时时间">
                  <el-input type="input"></el-input>
                </el-form-item>
                <el-radio-group v-model="radio">
                  <el-radio :label="1">命令行方式</el-radio>
                  <el-radio :label="2">TCP建链方式</el-radio>
                  <el-radio :label="3">HttpGet请求方式</el-radio>
                </el-radio-group>
                <el-form-item label="命令行方式">
                  <el-input type="textarea" :autosize="{ minRows: 3, maxRows: 5}"></el-input>
                </el-form-item>
              </el-form>
            </el-collapse-item>
            <el-collapse-item>
              <template slot="title">应用业务探针，探测应用业务是否已经就绪<el-button type="text" icon="el-icon-question"></el-button></template>
              <el-form ref="healthFormInfo" :model="healthFormInfo" class="health-form">
                <el-form-item label="延迟探测时间，标识从应用启动后多久开始探测">
                  <el-input type="input"></el-input>
                </el-form-item>
                <el-form-item label="超时时间，表示探测超时时间">
                  <el-input type="input"></el-input>
                </el-form-item>
                <el-radio-group v-model="radio">
                  <el-radio :label="1">命令行方式</el-radio>
                  <el-radio :label="2">TCP建链方式</el-radio>
                  <el-radio :label="3">HttpGet请求方式</el-radio>
                </el-radio-group>
                <el-form-item label="命令行方式">
                  <el-input type="textarea" :autosize="{ minRows: 3, maxRows: 5}"></el-input>
                </el-form-item>
              </el-form>
            </el-collapse-item>
          </el-collapse>
        </div>
      </el-collapse-transition>
      <div style="text-align: center; margin: 20px 0;">
        <el-button size="small" @click="$router.push({name: 'appManage'})" icon="el-icon-arrow-left">返回</el-button>
        <el-button type="primary" size="small" @click="fnNext()" :disabled="submitLoading">下一步<i class="el-icon-arrow-right"></i></el-button>
      </div>
    </el-card>
  </div>
</template>
<script>
  import {getAppConfig,saveAppConfig,getClusterInfo} from '../../axios/api'
  export default {
    name: 'resourceConfig',
    data() {
      return {
        appStatus:'',
        appId:'',
        recordLog:'1',
        totalCpu:4,
        totalMemory:10,
        cpuQuota:['',''],
        memoryQuota:['',''],
        cpuShow:false,
        memoryShow:false,
        formInfo:{
          appId:'',
          appImageId:'',
          imagePath:'',
          version:'',
          minCPU: '0.5',
          maxCPU: '2',
          minMemory: '0.5',
          maxMemory: '8',
        },
        show1: true,
        show2: true,
        show3: true,
        submitLoading: false,


        lifeCycle:'启动',
        addCommand:'',
        showInput:false,
        healthFormInfo:{},
        radio: 1,
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == "resourceConfig") {
        if(this.$route.query.id != undefined){
          this.appId = this.$route.query.id;
        }
        if(this.$route.query.recordLog != undefined){
            this.recordLog = this.$route.query.recordLog;
        }
        if(this.$route.query.appStatus != undefined){
          this.appStatus = this.$route.query.appStatus;
        }
        /*选择镜像后*/
        if(this.$route.params.id != undefined){
          this.formInfo = Object.assign({}, this.formInfo, {
            'appImageId':this.$route.params.id,
            'version':this.$route.params.version,
            'imagePath':this.$route.params.imagePath,
          })
        }
        /*获取总CPU、Memory*/
        /*getClusterInfo({'appId':this.appId}).then((res)=>{
          if(res.success){
            if(res.data.totalMemory){
              this.totalMemory = res.data.totalMemory;
            }
            if(res.data.totalCpu){
              this.totalCpu = res.data.totalCpu;
            }
            this.fnGetAll();
          }
        })*/
        /*暂时固定传值*/
        this.fnGetAll();
      }
    },
    methods: {
      fnGetAll(){
        getAppConfig({'appId':this.appId}).then((res)=>{
          if(res.success){
            if(this.$route.params.id){
              this.formInfo =  Object.assign({},this.formInfo,{
                minCPU : res.data.minCPU,
                maxCPU : res.data.maxCPU,
                minMemory : res.data.minMemory,
                maxMemory : res.data.maxMemory,
                appId : res.data.appId,
                class : res.data.class,
              });
            }else {
              this.formInfo =  Object.assign({},this.formInfo,res.data);
            }
          }
          this.$set(this.cpuQuota, 0, this.formInfo.minCPU);
          this.$set(this.cpuQuota, 1, this.formInfo.maxCPU);
          this.$set(this.memoryQuota, 0, this.formInfo.minMemory);
          this.$set(this.memoryQuota, 1, this.formInfo.maxMemory);
          this.cpuShow = true;
          this.memoryShow = true;
        })
      },
      /*cpu滑块*/
      cpuChange(data){
        this.formInfo.minCPU = data[0];
        this.formInfo.maxCPU = data[1];
      },
      /*memory滑块*/
      memoryChange(data){
        this.formInfo.minMemory = data[0];
        this.formInfo.maxMemory = data[1];
      },
      toChangeImage(){
        let checkImage = {};
        if(this.$route.params.id){
          checkImage = this.$route.params;
        }else {
          checkImage.id= this.formInfo.appImageId;
        }
        this.$router.push({name: 'changeImage', query: this.$route.query, params:{'checkImage': checkImage}});
      },
      fnNext() {
        if(this.formInfo.appImageId){
          this.formInfo.appId = this.appId;
          this.submitLoading = true;
          saveAppConfig(this.formInfo).then((res)=>{
            if(res.success){
              if(this.recordLog == '1'){
                this.$router.push({name: 'envParamConfig', query:{'id':this.appId,'recordLog':this.recordLog,'appStatus':this.appStatus}});
              }else{
                this.$router.push({name: 'configMap', query:{'id':this.appId}});
              }
            }else {
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
            this.submitLoading = false;
          })
        }else {
          this.$message({ showClose: true, message: '请选择应用镜像', type: 'error' });
        }
      },
    }
  }
</script>

<style lang="scss" type="text/scss">
  .el-collapse-item__wrap { .el-collapse-item__content { padding-bottom: 0px; } }
  .el-textarea__inner { @include beautifyScrollbar($scrollbar-bgcolor: $textArea-scrollbar-bgcolor)}
  .resourceConfig {
    .el-card__body { padding: 0px; }
  }
</style>
<style lang="scss" scoped type="text/scss">
  $title-color: #ebeef5;
  .resourceConfig { padding-bottom: 20px; }
  .header-title { padding: 18px 20px; border-bottom: 1px solid $title-color; @include userSelect(); cursor: pointer;
    > i { margin-right: 10px; }
  }
  .version { color: $main-ft-color; height: 40px; line-height: 40px;}
  .el-switch { vertical-align: unset; }
  .slider-detail { margin-bottom: 10px; padding-left: 80px; }
  .slider-unit { margin: 0 15px; color: $color-info; font-weight: bold; }
  .describe { color: $text-l2-color; margin-top: 10px }
  .health-form { background-color: #f5f7fa; padding: 10px 20px;
    .el-form-item { margin-bottom: 10px; }
  }
  .pd-2 { padding: 20px }
  .bd-b { border-bottom: 1px solid $title-color; }
</style>

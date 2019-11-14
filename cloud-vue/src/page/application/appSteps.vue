<template>
  <div class="appSteps">
    <el-col>
      <el-button style="float: right; padding: 10px 10px;" type="text" icon="el-icon-close"  @click="$router.push({name: 'appManage'})">关闭</el-button>
    </el-col>
      <el-steps :active="active" align-center finish-status="success" style="padding: 30px 0 10px;">
        <el-step title="资源配置" icon="el-icon-edit"></el-step>
        <el-step title="存储配置" icon="el-icon-edit"></el-step>
        <el-step title="参数配置" icon="el-icon-edit"></el-step>
        <el-step title="服务端口配置" icon="el-icon-edit"></el-step>
        <el-step title="完成" icon="el-icon-tickets"></el-step>
      </el-steps>
      <div class="flex-content">
        <keep-alive>
          <router-view v-if="!(this.$route.meta.keepAlive === false)"></router-view>
        </keep-alive>
        <router-view v-if="this.$route.meta.keepAlive === false"></router-view>
      </div>
  </div>
</template>
<script>
  export default {
    name: 'appSteps',
    data() {
      return {
        active: 0,
        nextBtn: false
      }
    },
    watch: {
      $route(e) {
        this.getActive(e.name)
      }
    },
    mounted() {
      this.getActive(this.$route.name)
    },
    methods: {
      getActive(name) {
        switch (name) {
          case undefined:
            this.active = 0;
            break;
          case 'resourceConfig':
            this.active = 0;
            break;
          case 'envParamConfig':
            this.active = 1;
            break;
          case 'configMap':
            this.active = 2;
            break;
          case 'serviceConfig':
            this.active = 3;
            break;
          case 'success':
            this.active = 4;
            break;
          default:
            break;
        }
      }
    }
  }
</script>
<style lang="scss" type="text/scss">
  .appSteps {
    .el-step__icon {
      background: #f9f9f9 !important;
    }
  }
</style>
<style lang="scss" scoped type="text/scss">
  .appSteps {
    height: 100%;
    @include flexbox-display-flex();
  }

  .flex-content {
    padding: 0 25px;
    box-sizing: border-box;
    overflow: auto;
    @include flexbox-display-flex();
    @include flexbox-flex();
    @include beautifyScrollbar();
  }
</style>

<template>
  <div class="index">
    <div class="index-header">
      <div class="logo" :class="{'collapse': collapse}"><span>银海容器云平台</span></div>
      <div class="pos-collapse" @click="collapse = !collapse" >
        <i class="cloud-icon-size cloud-icon-close" :class="{'cloud-icon-open': collapse}"></i>
      </div>
      <user-info></user-info>
    </div>
    <div class="index-content">
      <div class="index-menu" :class="{'collapse': collapse}">
        <el-menu :default-active="childRoute" :collapse-transition="false"  background-color="#2f343e" text-color="#bbb" active-text-color="#0091ea" :router="true" :collapse="collapse" :unique-opened="true">
          <div v-for="(item, index) in menuList" :id="item.id" :key="index">
            <el-submenu v-if="item.children" :index="item.path">
              <template slot="title">
                <i class="menu-icon-style" :class="item.icon"></i>
                <span>{{item.name}}</span>
              </template>
              <el-menu-item v-for="(cItem, cIndex) in item.children" :key="cIndex" :index="cItem.path" :route="{path: cItem.path}" :disabled="cItem.disabled">{{cItem.name}}</el-menu-item>
            </el-submenu>
            <el-menu-item v-else :index="item.path" :route="{path: item.path}" :disabled="item.disabled">
              <i class="menu-icon-style" :class="item.icon"></i>
              <span slot="title">{{item.name}}</span>
            </el-menu-item>
          </div>
        </el-menu>
      </div>
      <div class="index-center" :class="{'collapse': collapse}">
        <keep-alive>
          <router-view v-if="!(this.$route.meta.keepAlive === false)"></router-view>
        </keep-alive>
        <router-view v-if="this.$route.meta.keepAlive === false"></router-view>
      </div>
    </div>
  </div>
</template>
<script>
  import userInfo from './userInfo.vue';
  export default {
    name: 'index',
    data(){
      return{
        collapse: false,
        menuList:[
          { id:1, name:'概览', path:'/index',icon:'cloud-icon-index' },
          {
            id:2, name:'容器集群管理', path:'container',icon:'el-icon-news',
            children: [
              {id:21, name:'集群管理', path:'/addCluster'},
              {id:22, name:'节点管理', path:'/nodesManage'},
              {id:23, name:'命名空间管理', path:'/nameSpace'},
            ]
          },
          {
            id:7, name:'存储管理', path:'storage',icon:'el-icon-document',
            children: [
              {id:71, name:'nfs存储池管理', path:'/saveManage'},
              {id:72, name:'云目录管理', path:'/cloudSaveManage'},
              {id:73, name:'ceph存储池管理', path:'/cephPool'},
              {id:74, name:'ceph集群管理', path:'/addCephCluster'},
              {id:75, name:'ceph节点管理', path:'/cephCluster'},
            ]
          },
          {
            id:3, name:'应用管理', path:'application',icon:'el-icon-goods',
            children: [
              {id:31, name:'应用管理', path:'/appManage'},
              /*{id:32, name:'服务管理', path:'/serviceManage'},*/
            ]
          },
          { id:4, name:'镜像管理', path:'repoManage',icon:'el-icon-menu' ,
            children: [
              {id:51, name:'镜像仓库管理', path:'/repoManage'},
              {id:52, name:'jenkins管理', path:'/jenkins'},
              {id:52, name:'jenkins集成', path:'/jenkinsIntegration'},
            ]
          },
          { id:5, name:'系统设置', path:'setting',icon:'el-icon-setting',
            children: [
              {id:51, name:'系统脚本管理', path:'/scriptManage'},
              {id:52, name:'人员及权限管理', path:'/author'},
              {id:53, name:'全局变量管理', path:'/systemproperty'},
            ]
          },
          { id:6, name:'监控', path:'monitor',icon:'cloud-icon-monitor',
            children: [
              {id:61, name:'集群监控', path:'/monitorCluster'},
              {id: 62, name: '应用监控', path: '/monitorApp'},
              {id: 65, name: '定时任务配置', path: '/monitorConfig'},
            ]
          }
        ]
      }
    },
    components:{userInfo},
    computed:{
      childRoute(){
        return this.$route.meta.parent || this.$route.path
      },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .index-menu {
    $item-bg-color: #262a32;
    @mixin index-menu-item($color: $white-color , $bg: $item-bg-color){
      color: $color !important;
      background-color: $bg !important;
    }
    .el-menu>div>.el-menu-item {
      &.is-active {
        @include index-menu-item($bg: $sub-color);
        span { color: $white-color; }
      }
    }
    ul.el-menu>li.el-menu-item {
      background-color: #262a32!important;
      &.is-active {
        @include index-menu-item($bg: $sub-color);
        span { color: $white-color; }
      }
    }
    .el-submenu.is-opened .el-submenu__title{
      i {
        color: $white-color!important;
      }
      @include index-menu-item();
    }

    .el-menu {
      border-right: solid 1px $main-color;
    }

    .el-menu--collapse .el-menu-item span, .el-menu--collapse .el-submenu > .el-submenu__title span {
      height: 0;
      width: 0;
      overflow: hidden;
      visibility: hidden;
      display: inline-block;
    }

    .el-menu--collapse .el-menu-item .el-submenu__icon-arrow, .el-menu--collapse .el-submenu > .el-submenu__title .el-submenu__icon-arrow {
      display: none
    }
  }
</style>
<style lang="scss" scoped type="text/scss">
  @import "../../assets/icon/iconfont.css";

  .index {
    width: 100%;
    height: 100%;
    min-width: $main-content;
  }

  .index-header {
    width: 100%;
    height: $header-height;
    background-color: $white-color;
    box-shadow: 2px 0px 4px rgba(0, 0, 0, 0.2);
    position: absolute;
    left: 0;
    top: 0;
    z-index: 1;
  }

  .logo {
    float: left;
    width: $menu-width;
    background-color: $main-color;
    height: $header-height;
    line-height: $header-height;
    color: $white-color;
    font-size: 18px;
    @include bgimgCover($src: "./img/logo.png", $size: 35px 25px, $position: 15px center);
    padding-left: 55px;
    box-sizing: border-box;
    @include transition();
    white-space: nowrap;

    &.collapse {
      width: 65px;

      span {
        display: none;
      }
    }
  }

  .index-content {
    position: absolute;
    top: $header-height;
    bottom: 0;
    left: 0;
    right: 0;
    background: #f9f9f9
  }

  .index-menu {
    width: $menu-width;
    height: 100%;
    overflow-y: auto;
    background-color: $main-color;
    overflow-x: hidden;
    @include beautifyScrollbar($scrollbar-barcolor: $main-color, $scrollbar-bgcolor: $main-color, $scrollbar-barcolor-active: $main-color);
    @include transition();

    &.collapse {
      width: 65px !important;

      .part-title {
        padding-left: 0;
        text-align: center;
      }
    }
  }

  .part-title {
    color: #909399;
    font-size: 12px;
    padding: 8px 0 8px 20px;
  }

  .cloud-icon-size {
    font-size: 20px;
  }

  .pos-collapse {
    width: 45px;
    height: 45px;
    line-height: 45px;
    float: left;
    text-align: center;
    color: $text-l2-color;
    cursor: pointer;

    &:hover {
      background-color: rgba(0, 0, 0, .025)
    }
  }

  .index-center {
    box-sizing: border-box;
    position: absolute;
    top: 0;
    bottom: 0;
    left: $menu-width;
    right: 0;
    @include transition();
    min-width: $main-content - $menu-width;

    &.collapse {
      left: 65px;
    }
  }

  .menu-icon-style {
    vertical-align: middle;
    margin-right: 5px;
    width: 24px;
    text-align: center;
    font-size: 18px;
  }
</style>

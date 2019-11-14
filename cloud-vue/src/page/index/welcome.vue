<template>
  <div class="index-content" @scroll="paperScroll">
    <div class="top-title" :class="{'top-normal': currentPage != 1 }">
      <div class="logo"></div>
      <span>银海容器云平台</span>
      <div class="login" v-show="!showLogin" @click="currentPage = 1; showLogin = true;">登录</div>
    </div>
    <div class="part part1" :style="{'top': firstTop +'px', 'height':fullHeight+'px'}" @click="showLogin = false">
      <div class="index-bg"></div>
      <canvas id="index_bg_canvas" class="canvas">您的浏览器不支持canvas标签</canvas>
      <div class="title"  :class="{'login-blur': showLogin}" :style="{'padding-top': (fullHeight - 130)/2+'px'}"><span class="point">驾驭</span>创新技术 · <span class="point">驱动</span>行业发展</div>
    </div>
    <login :top="showLogin ? (fullHeight - 300)/2 : -300" @closeEvent="showLogin = false" :showLogin="showLogin"></login>
    <div class="part part2" :style="{'top': secondTop +'px', 'height':fullHeight+'px'}">
      <div class="part2-center" :style="{'padding-top': (fullHeight - 360)/2+'px'}">
        <div class="part2-left" :class="{'part2-left-center': currentPage == 2}">
          <div class="title">容器云平台简介</div>
          <div class="subtitle">银海容器云以开源容器技术为核心，用于为行业客户搭建基于容器技术的云管理平台。</div>
          <div class="subtitle">平台提供了大规模容器集群的运维管理、容器镜像的生命周期管理、业务应用的快捷发布运行等功能，为行业客户提供高资源利用率、弹性、高可用、高效便捷的平台服务，助力客户提高应用交付效率和未来微服务的环境支撑。</div>
        </div>
        <div class="part2-right" :class="{'part2-right-center': currentPage == 2}"></div>
        <div class="part2-img1" :class="{'part2-image-current': currentPage == 2}"></div>
        <div class="part2-img2" :class="{'part2-image-current': currentPage == 2}"></div>
        <div class="part2-img3" :class="{'part2-image-current': currentPage == 2}"></div>
      </div>
    </div>
    <div class="part part3" :style="{'top': thirdTop +'px', 'height':fullHeight+'px'}">
      <div class="part3-center" :style="{'height':fullHeight-80 +'px','padding-top': (fullHeight - 400)/2+'px'}">
        <div class="title">产品特性</div>
        <el-carousel :interval="4000" type="card" height="260px" class="slider-box">
          <el-carousel-item v-for="(item,index) in sliderList" :key="index">
            <div class="slider-title">{{item.title}}</div>
            <div class="slider-info">{{item.info}}</div>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="footer">
        <p>Copyright © 四川久远银海软件股份有限公司保留所有权利</p>
        <p>蜀ICP备05001851号-1</p>
      </div>
    </div>
    <ul class="pos-nav" :style="{'top': (fullHeight - 120)/2 +'px'}" >
      <li class="nav-box" v-for="item in pageSize" :key="item" :class="{'active': currentPage == item}" @click="currentPage = item"><span class="nav-circle"></span></li>
    </ul>
  </div>
</template>
<script>
  import login from './login.vue';
  import { startCanvas,clearCanvas } from './js/indexBg';
  let timer = null;
  export default {
    name: 'welcome',
    data(){
      return {
        fullHeight: document.documentElement.clientHeight,
        scrollTop: 0,
        currentPage: 1,
        pageSize: 3,
        showLogin: false,
        sliderList:[{title:'简单易用',info:'自动化创建容器集群，一站式部署和运维容器应用，一键式滚动升级。'},
                    {title:'资源池化',info:'计算主机资源虚拟为应用资源池，应用容器按资源配置自动分配；自动分配容器虚拟网络环境，网络及应用可按命名空间隔离，提高系统资源利用率。'},
                    {title:'弹性伸缩',info:'提供集群资源和容器应用两层级的弹性伸缩能力，可根据实际需求进行动态伸缩。'},
                    {title:'应用高可用',info:'容器集群对运行失败的容器应用进行自动迁移和快速重启，以修复错误状态，保证业务应用高可用。'}]
      }
    },
    components:{ login },
    computed:{
      firstTop(){
        return (1-this.currentPage) * this.fullHeight;
      },
      secondTop(){
        return this.firstTop + this.fullHeight
      },
      thirdTop(){
        return this.firstTop + this.fullHeight * 2
      },
    },
    mounted(){
      startCanvas();
      let _this = this;
      if (document.addEventListener) {//firefox
        document.addEventListener('DOMMouseScroll', function (event) {
          let direction = event.detail > 0 ? 'down':'up';
          _this.scrollHandler(direction);
        }, false);
      }
      //滚动滑轮触发scrollFunc方法  //ie 谷歌
      window.onmousewheel = document.onmousewheel = function (event) {   // IE/Opera/Chrome
        //var e = e || window.event;
        let direction = event.wheelDelta > 0 ? 'up':'down';
        _this.scrollHandler(direction);
      }
      window.onresize = () => {
        return (() => {
          window.fullHeight = document.documentElement.clientHeight;
          _this.fullHeight = window.fullHeight;
        })()
      };
    },
    destroyed(){
      clearCanvas();
    },
    methods: {
      scrollHandler (direction) {
        // 防止重复触发滚动事件
        if (timer != null) {
          return;
        }
        if (direction === 'down') {
          if(this.currentPage < this.pageSize){
            this.currentPage ++;
            this.showLogin = false;
          }
        } else {
          if(this.currentPage > 1){
            this.currentPage --;
          }
        }
        timer = setTimeout(function() {
          clearTimeout(timer);
          timer = null;
        }, 300);
      },
      paperScroll(e) {
        e.target.scrollTop = 0
      }
    }
  }
</script>
<style lang="scss" type="text/scss">
  .slider-box {
    color: $text-l0-color;
    .el-carousel__item { background-color: $white-color; border: 1px solid #ebeef5; box-sizing: border-box; box-shadow: 0 2px 4px 0 rgba(0,0,0,.1); border-radius: 10px; }
    .slider-title { width: 100%; height: 60px; border-bottom: 1px solid #ebeef5; font-size: 22px; line-height: 60px; padding-left: 60px; @include bgimgCover($src:"img/slider.png",$size: 30px, $position: 20px center); }
    .slider-info { padding: 20px; font-size: 18px; }
  }
</style>
<style lang="scss" scoped type="text/scss">
  .index-content { position: absolute; top: 0; right: 0; bottom: 0; left: 0; @include beautifyScrollbar(); @include transition(); overflow-y: scroll; min-width: 1200px; overflow-x: hidden; @include userSelect(); }
  .top-title { z-index: 2; color: $white-color; font-size: 26px; position: fixed; width: 100%; box-sizing: border-box; padding: 14px 100px; height: 60px; top: 0; left: 0; background-color: rgba(28, 31, 40, 0.5); @include transition();}
  .top-normal { background-color: $main-color; }
  .logo { width: 60px; height: 32px; @include bgimgCover($src:"img/logo.png",$size: contain,$position:center); float: left; }
  .login { float: right; color: $white-color; font-size: 14px; height: 30px; width: 60px; line-height: 30px; text-align: center; border: solid 1px $white-color; border-radius: 5px; cursor: pointer; }
  .part { position: absolute; z-index: 1; top: 0; right: 0; left: 0; @include transition(); }
  .part1 { width: 100%; /*@include gradient-vertical($start-color:#1d1e27,$end-color: #2b3039);*/
    .index-bg { width: 100%; height: 100%; @include bgimgCover($src:"img/index_bg.png",$size: cover ,$position:center); }
    .canvas { display: block; margin: 0; width:100%; height:100%; position: absolute; left: 0; right: 0; top:0; bottom:0; z-index: 1; }
    .title { font-size: 50px; color: $white-color; font-weight: bold; letter-spacing: 4px; text-shadow: 0 0 2px $white-color; padding-top: 40px; text-align: center; left: 0; right: 0; top:0; position: absolute; z-index: 1;
      &.login-blur { @include filter-blur(5px) }
    }
  }
  .part2 { background-color: #f1f3fe; overflow: hidden;
    .part2-center { width: 1200px; height: 360px; position: relative; margin: 0 auto;}
    .part2-left { width: 500px; margin-right: 100px; padding-top: 40px; @include transition($time: 0.8s,$style:ease-in-out); position: absolute; left: -1000px;
      &.part2-left-center { left: 0; }
    }
    .part2-right { @include bgimgCover($src:"img/part2.png",$size: contain,$position:center); width: 526px; height: 360px; @include transition($time: 0.8s,$style:ease-in-out); position: absolute; right: -1000px;
      &.part2-right-center { right: 0; }
    }
    .part2-img1 { @include bgimgCover($src:"img/part2_01.png",$size: contain,$position:center); width: 59px; height: 64px; @include transition($time: 0.8s,$style:ease-in-out); position: absolute; bottom: -600px; right: -400px; }
    .part2-img2 { @include bgimgCover($src:"img/part2_02.png",$size: contain,$position:center); width: 86px; height: 86px; @include transition($time: 0.8s,$style:ease-in-out); position: absolute; bottom: -580px; left: -600px; }
    .part2-img3 { @include bgimgCover($src:"img/part2_03.png",$size: contain,$position:center); width: 120px; height: 126px; @include transition($time: 0.8s,$style:ease-in-out); position: absolute; top: -400px; right: -110px; }
    .part2-image-current {
      &.part2-img1 { bottom: -100px; right: 100px; }
      &.part2-img2 { bottom: 80px; left: -100px; }
      &.part2-img3 { top: 100px; right: -110px; }
    }
    .title { font-size: 34px; color: $text-l0-color; padding-bottom: 20px}
    .subtitle { font-size: 16px; color: $text-l1-color; padding-top: 10px; text-align: justify; }
  }
  .part3 { background-color: $white-color;
    .part3-center { margin: 0 auto; width: 1000px; box-sizing: border-box; }
    .title { font-size: 34px; color: $text-l0-color; text-align: center; padding-bottom: 30px }
    .footer { width: 100%; height: 80px; padding-top: 10px; box-sizing: border-box; background-color: $main-color; color: $white-color; text-align: center; p { line-height: 30px; margin: 0; } }
  }
  $nav-size: 4px;
  $nav-hover-size: 8px;
  $nav-active-size: 12px;
  .pos-nav { width: $nav-active-size; position: fixed; right: 30px; z-index: 2; list-style: none; margin: 0; padding: 0;
    .nav-box { width: $nav-active-size; height: $nav-active-size; cursor: pointer; position: relative; margin-bottom: 24px;
      &:last-child { margin-bottom: 0; }
      .nav-circle { display: block; width: $nav-size; height: $nav-size; background-color: $sub-color; border-radius: 100%; @include transition($time:0.1s); position: absolute; left: 50%; top: 50%; margin: (-$nav-size/2) 0 0 (-$nav-size/2); }
      &:hover .nav-circle { width: $nav-hover-size; height: $nav-hover-size; margin: (-$nav-hover-size/2) 0 0 (-$nav-hover-size/2);}
      &.active .nav-circle { width: $nav-active-size; height: $nav-active-size; margin: (-$nav-active-size/2) 0 0 (-$nav-active-size/2);}
    }
  }
</style>

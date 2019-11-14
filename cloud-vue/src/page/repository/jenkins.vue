<template>
  <div>
    <iframe :src=jenkinsUrl id="mobsf" scrolling="auto"
            frameborder="0"></iframe>
  </div>
</template>

<script>
  import {
    getJenkinsUrl
  } from '../../axios/api'

  export default {
    data() {
      return {
        jenkinsUrl: ""
      }
    },
    mounted() {
      this.getJenkinsUrl()

      this.changeMobsfIframe()
      window.onresize = function () {
        this.changeMobsfIframe()
      }
    },
    methods: {
      getJenkinsUrl() {
        getJenkinsUrl({test: ""}).then((res) => {
          if (res.success) {
            this.jenkinsUrl = res.data
          }
        })
      },
      /**
       * iframe-宽高自适应显示
       */
      changeMobsfIframe() {
        const mobsf = document.getElementById('mobsf');
        const deviceWidth = document.body.clientWidth;
        const deviceHeight = document.body.clientHeight;
        mobsf.style.width = (Number(deviceWidth) - 200) + 'px'; //数字是页面布局宽度差值
        mobsf.style.height = (Number(deviceHeight) - 80) + 'px'; //数字是页面布局高度差
      }
    }
  }

</script>

<style scoped>

</style>

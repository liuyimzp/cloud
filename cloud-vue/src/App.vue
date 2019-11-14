<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>
<script>
  export default {
    name: 'App',
  }
</script>
<style lang="scss" type="text/scss">
  .el-table .cell { white-space: nowrap!important; }
  .el-table__body-wrapper { @include beautifyScrollbar()}
  .el-message { top: 55px!important; }
  #app { width: 100%; height: 100%; position: absolute; top: 0; bottom: 0; left: 0; right: 0; }
</style>

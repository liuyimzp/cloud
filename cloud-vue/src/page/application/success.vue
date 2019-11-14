<template>
  <div class="success">
    <ace-editor id="editor" lang="batchfile" theme="terminal" height="100%"></ace-editor>
    <div style="text-align: center; margin: 20px auto;">
      <el-button size="small" @click="$router.go(-1)" icon="el-icon-arrow-left">上一步</el-button>
      <el-button type="primary" size="small" @click="fnStart()" :disabled="startLoading">启动<i v-if="startLoading" class="el-icon-loading el-icon--right"></i></el-button>
    </div>
  </div>
</template>
<script>
  import {getYaml,appStart} from '../../axios/api'
  import AceEditor from 'ace-vue2'
  import 'brace/mode/batchfile';
  import 'brace/theme/terminal';
  export default {
    name: 'success',
    data() {
      return {
        appId:'',
        edit:'',
        startLoading:false
      }
    },
    components:{
      AceEditor
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == "success") {
        if (this.$route.query.id != undefined) {
          this.appId = this.$route.query.id;
          this.fnGetYaml();
        }
      }
    },
    methods: {
      fnGetYaml(){
        getYaml({'id':this.appId}).then((res)=>{
          if(res.success){
            let editor = ace.edit("editor");
            editor.setValue(res.data);
            editor.moveCursorTo(0, 0);
            editor.setReadOnly(true);
          } else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
        });
      },
      /*启动*/
      fnStart(){
        /*更新的启动*/
        this.startLoading = true;
        appStart({'id':this.appId}).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '启动成功', type: 'success' });
            this.$router.push({name: 'appManage'});
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.startLoading = false;
        });
      }
    }
  }
</script>
<style lang="scss" type="text/scss">
  .success { height: 100%; padding: 20px 100px; }
  .ace_scrollbar { @include beautifyScrollbar() }
</style>

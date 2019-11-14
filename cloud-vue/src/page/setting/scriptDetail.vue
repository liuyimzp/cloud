<template>
  <div class="scriptDetail">
    <el-breadcrumb separator-class="el-icon-arrow-right" style="line-height: 32px;">
      <el-breadcrumb-item @click="" :to="{ path: '/scriptManage'}" :replace="true">脚本管理</el-breadcrumb-item>
      <el-breadcrumb-item>{{type =='add' ? '新增': type == 'edit' ? '修改' : '查看'}}脚本</el-breadcrumb-item>
    </el-breadcrumb>
    <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="80px" class="form-content fit" >
      <el-form-item label="脚本名称" prop="shellName" >
        <el-input v-model="formInfo.shellName" class="full-width" :disabled="!formEdit"></el-input>
      </el-form-item>
      <el-form-item label="脚本标识" prop="shellUid">
        <el-select v-model="formInfo.shellUid" class="full-width" filterable :disabled="!formEdit">
          <el-option v-for="item in shellUidOptions" :key="item.value" :label="item.key" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="脚本类型" prop="shellType">
        <el-select v-model="formInfo.shellType" class="full-width" filterable :disabled="!formEdit">
          <el-option v-for="item in shellTypeOptions" :key="item.codeValue" :label="item.codeDESC" :value="item.codeValue"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="shellContent" style="display: none"></el-form-item>
      <ace-editor id="editor" lang="batchfile" theme="terminal" height="100%"></ace-editor>
      <div style="text-align: center; margin-top: 20px;" v-show="formEdit" >
        <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-form>
  </div>
</template>
<script>
  import {shellType, shellUid,shellSave,shellEdit} from '../../axios/api'
  import AceEditor from 'ace-vue2'
  import 'brace/mode/batchfile';
  import 'brace/theme/terminal';
  export default {
    name: 'scriptManage',
    data() {
      return {
        id: "",
        formEdit: true,
        type: "",
        submitLoading: false,
        formInfo: {
          shellName: "",
          shellUid: "",
          shellType: "",
          shellContent:'',
          shellId:'',
        },
        formInfoRules: {
          shellName: [
            { required: true, message: '请输入脚本名称', trigger: 'blur' },
            { min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur' }
          ],
          shellUid: [ { required: true, message: '请选择脚本标识', trigger: 'change' } ],
          shellType: [ { required: true, message: '请选择脚本类型', trigger: 'change' } ],
        },
        shellUidOptions: [],
        shellTypeOptions: [],
      }
    },
    components:{
      AceEditor
    },
    beforeCreate(){
      shellUid().then((res) => {
        if (res.success) {
          this.shellUidOptions = res.data
        }
      })
      shellType().then((res) => {
        if (res.success) {
          this.shellTypeOptions = res.data
        }
      })
    },
    mounted() {
      if(this.$route.name == 'scriptDetail') {
        this.init()
      }
    },
    methods: {
      init(){
        const {type, row, edit} = this.$route.params
        this.type = type
        if(!type){
          this.$router.push({name: 'scriptManage'})
          return false
        }
        if(row && row.shellId){
          this.id = row.shellId
          this.formEdit = edit
          const {shellId, shellName, shellUid, shellType, shellContent} = row
          this.formInfo = Object.assign({},this.formInfo,{
            shellId: shellId,
            shellName: shellName,
            shellUid: shellUid,
            shellType: shellType,
            shellContent: shellContent,
          });

          let editor = ace.edit("editor");
          editor.setValue(this.formInfo.shellContent);
          editor.moveCursorTo(0, 0);
          editor.setReadOnly(!this.formEdit);
        }
      },
      fnSave(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitLoading = true;
            let code = ace.edit("editor").getValue();
            this.formInfo.shellContent = code;
            if (this.type == 'add') {
              shellSave(this.formInfo).then((res)=>{
                this.successEvent(res)
              })
            } else {
              shellEdit(this.formInfo).then((res)=>{
                this.successEvent(res)
              })
            }
          }
        });
      },
      successEvent(res){
        if (res.success) {
          this.$message({ showClose: true, message: this.type == 'add'? '新增成功': '修改成功', type: 'success' });
          this.$router.push({name: 'scriptManage', params:{ reload: true}})
        }else {
          this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
        }
        this.submitLoading = false;
      },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .ace_scrollbar { @include beautifyScrollbar() }
</style>
<style lang="scss" scoped type="text/scss">
  .scriptDetail { padding: 20px 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .fit { @include flexbox-display-flex();@include flexbox-flex();@include beautifyScrollbar();}
  .form-content { width: 100%; background-color: $white-color; padding: 20px; box-sizing: border-box; margin-top: 10px;  }
  .el-form-item { margin-bottom: 16px; }
  .full-width{width:100%}
</style>

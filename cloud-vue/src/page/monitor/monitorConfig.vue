<template>
  <div class="monitorConfig">
    <div class="left-part">
      <div class="title">
        <span>定时任务配置</span>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="rightMenu = true; formEdit = false;" style="float: right">新增</el-button>
      </div>
      <div class="description">定时任务配置主要实现对平台监控任务频率的控制。</div>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
            <el-table-column prop="jobName" label="任务名称"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="triggerName" label="调度器名"></el-table-column>
            <el-table-column prop="triggerGroup" label="调度器组名"></el-table-column>
            <el-table-column prop="triggerState" label="状态"></el-table-column>
            <el-table-column prop="st" label="开始时间"></el-table-column>
            <el-table-column prop="et" label="结束时间"></el-table-column>
            <el-table-column prop="pt" label="上一次执行时间"></el-table-column>
            <el-table-column prop="nt" label="下一次执行时间"></el-table-column>
            <el-table-column label="操作" width="130px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="fnResumeJob(scope.row)" size="small">启动</el-button>
                <el-button type="text" @click="fnPauseJob(scope.row)" size="small">暂停</el-button>
                <el-button type="text" @click="deleteRow(scope.row)" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}定时任务</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; formEdit = false; $refs['formInfo'].resetFields()">关闭</el-button>
      </div>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="110px">
        <el-form-item label="任务名称" prop="jobName">
          <el-input v-model="formInfo.jobName" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="任务组" prop="jobGroup">
          <el-input v-model="formInfo.jobGroup" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="任务入口类名" prop="className" style="display: none;">
          <el-input type="textarea" v-model="formInfo.className" :autosize="{ minRows: 2, maxRows: 3}" disabled resize="none"></el-input>
        </el-form-item>
        <el-form-item label="任务描述" prop="jobDesc">
          <el-input v-model="formInfo.jobDesc" type="textarea" :autosize="{ minRows: 3, maxRows: 5}"></el-input>
        </el-form-item>
        <el-form-item label="调度器类型" prop="triggerType">
          <el-select v-model="formInfo.triggerType" class="full-width" filterable>
            <el-option v-for="item in triggerOption" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="任务开始时间" prop="startTime">
          <el-date-picker v-model="formInfo.startTime" type="datetime" align="right" class="full-width" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
        </el-form-item>
        <el-form-item label="任务结束时间" prop="endTime">
          <el-date-picker v-model="formInfo.endTime" type="datetime" align="right" class="full-width" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
        </el-form-item>
        <el-form-item label="重复执行次数" prop="repeatCount" v-if="formInfo.triggerType == '1'">
          <el-input v-model="formInfo.repeatCount" class="full-width" placeholder="-1为不限次数"></el-input>
        </el-form-item>
        <el-form-item label="间隔时间" prop="repeatInterval" v-if="formInfo.triggerType == '1'">
          <el-input v-model="formInfo.repeatInterval" class="full-width" placeholder="单位为秒">
            <template slot="append">s</template>
          </el-input>
        </el-form-item>
        <el-form-item label="Cron表达式" prop="cronExpression" v-if="formInfo.triggerType == '2'">
          <el-input v-model="formInfo.cronExpression" class="full-width" placeholder="如每分钟一次为：* */1 * * * ?"></el-input>
        </el-form-item>
        <el-form-item label="异常是否暂停" prop="isPause" >
          <el-select v-model="formInfo.isPause" class="full-width" filterable>
            <el-option v-for="item in pauseOption" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <div style="margin: 30px auto; text-align: center">
          <el-button-group>
            <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading"></i></el-button>
            <el-button type="primary" size="small" @click="$refs['formInfo'].resetFields()">重置</el-button>
          </el-button-group>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import { getAllJob, addMonitorJob, pauseJob, resumeJob, removeJob } from '../../axios/api'
  export default {
    name: 'monitorConfig',
    data() {
      return {
        rightMenu: false,
        tableData: [],
        formInfo:{
          jobName:"",
          jobGroup:"",
          jobDesc:"",
          startTime:"",
          endTime:"",
          repeatCount:"",
          repeatInterval:"",
          triggerType:"",
          cronExpression:"",
          isPause:"",
        },
        formInfoRules:{
          jobName: [
            { required: true, message: '请输入任务名称', trigger: 'blur' },
            { min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur' }
          ],
          jobGroup: [
            { required: true, message: '请输入任务组名称', trigger: 'blur' },
            { min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur' }
          ],
          triggerType: [
            { required: true, message: '请选择调度器类型', trigger: 'change' }
          ],
          startTime: [
            { required: true, message: '请选择开始时间', trigger: 'blur' }
          ],
          cronExpression: [
            { required: true, message: '请输入Cron表达式', trigger: 'blur' }
          ],
          isPause: [
            { required: true, message: '请选择出现异常处理方式', trigger: 'change' }
          ]
        },
        triggerOption: [{ value: '1', label: 'Simple Trigger' }, { value: '2', label: 'Cron Trigger' }],
        pauseOption: [{ value: '0', label: '不暂停' }, { value: '1', label: '要暂停' }, { value: '2', label: '仅当通讯异常时暂停' }, { value: '3', label: '仅当业务异常时暂停' }],
        formEdit:false,
        loading:false,
        submitLoading:false,
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'monitorConfig') {
        this.loading = true;
        this.fnGetAll();
      }
    },
    methods: {
      fnGetAll(){
        this.loading = true;
        getAllJob().then((res)=>{
          if(res.success){
            this.tableData = res.data;
          }else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
          this.loading = false;
        });
      },
      fnSave(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitLoading = true;
            if(this.formEdit){
             // this.fnEdit();
            }else {
              this.fnAdd();
            }
          }
        });
      },
      fnAdd(){
        console.log(this.formInfo.endTime)
        addMonitorJob(this.formInfo).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '新增定时任务成功', type: 'success' });
            this.rightMenu = false;
            this.$refs['formInfo'].resetFields();
            this.fnGetAll();
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            this.loading = false;
          }
        });
      },
      editRow(row){
        this.formInfo =  Object.assign({}, this.formInfo, {
          name:row.name,
        });
        this.rightMenu = true;
        this.formEdit = true;
      },
      deleteRow(row) {
        console.log(row)
        this.$confirm('此操作将永久删除该任务, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          removeJob(row).then((res)=>{
            if(res.success){
              this.fnGetAll();
              this.$message({ showClose: true, message: '删除任务成功', type: 'success' });
            }else {
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          });
        }).catch(()=>{});
      },
      fnPauseJob(row){
        pauseJob(row).then((res)=>{
          if(res.success){
            this.fnGetAll();
            this.$message({ showClose: true, message: '暂停任务成功', type: 'success' });
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
        });
      },
      fnResumeJob(row){
        resumeJob(row).then((res)=>{
          if(res.success){
            this.fnGetAll();
            this.$message({ showClose: true, message: '启动任务成功', type: 'success' });
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
        });
      }
    }
  }
</script>
<style lang="scss" type="text/scss">
  .full-width { width: 100%!important; }
</style>
  <style lang="scss" scoped type="text/scss">
  .monitorConfig,.fit { height: 100%; }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .left-part { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .box-card { @include rightMenu(); }
  .el-form-item { margin-bottom: 16px; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
</style>

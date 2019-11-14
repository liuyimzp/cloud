<template>
  <div class="changeImage">
    <div class="title">镜像选择</div>
    <el-form label-position="right" :model="formInfo" ref="formInfo" label-width="104px" class="search-box">
      <el-form-item label="所属业务领域" prop="businessArea">
        <el-select v-model="formInfo.businessArea" class="full-width" filterable @change="fnCheckBusinessArea" clearable>
          <el-option v-for="item in businessOptions" :key="item.codeValue" :label="item.codeDESC" :value="item.codeValue"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="应用组" prop="groupId">
        <el-select v-model="formInfo.groupId" class="full-width" filterable clearable>
          <el-option v-for="item in appGroupOptions" :key="item.id" :label="item.groupName" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="英文标识">
        <el-input v-model="formInfo.identify" class="full-width"></el-input>
      </el-form-item>
      <el-button type="primary" size="small" style="margin: 4px 0 0 30px;" @click="fnSearch('formInfo')" icon="el-icon-search">查询</el-button>
    </el-form>
    <div class="flex-content">
      <el-table v-loading="loading" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
        <el-table-column align="center" prop="checked" label="选择" width="55">
          <template slot-scope="scope">
            <el-radio v-model="checked" :label="scope.row.id" @change="fnGetRow(scope.row)">&nbsp;</el-radio>
          </template>
        </el-table-column>
        <el-table-column prop="appName" label="应用名称" align="center"></el-table-column>
        <el-table-column prop="identify" label="英文标识" align="center"></el-table-column>
        <el-table-column prop="imagePath" label="镜像路径" :show-overflow-tooltip="true" min-width="150px"  align="center"></el-table-column>
        <el-table-column prop="version" label="版本" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" :show-overflow-tooltip="true"></el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination @current-change="fnCurrentChange" background :current-page.sync="currentPage" :page-size="pageSize" layout="prev, pager, next, jumper, ->, total" :total="total"></el-pagination>
      </div>
    </div>
    <div class="pos-button">
      <el-button size="small" @click="$router.go(-1)">取消</el-button>
      <el-button size="small" type="primary" @click="fnCheckImage()">确定</el-button>
    </div>
  </div>
</template>
<script>
  import {getAllAppImages,appBusinessCode,appGroupGetAll} from '../../axios/api'
  export default {
    name: 'changeImage',
    data() {
      return {
        loading: false,
        tableData: [],
        formInfo: {
          businessArea:'',
          groupId:'',
          identify:'',
        },
        formInfoRules:{
        },
        appId:'',
        checked: null,
        tempCheck:{},
        businessOptions:[],
        appGroupOptions:[],
        currentPage:1,
        pageSize:5,
        total:0,
        job:false
      }
    },

    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'changeImage') {
        if (this.$route.query.id != undefined) {
          this.appId = this.$route.query.id;
        }
        if (this.$route.query.job != undefined) {
          this.job = true;
        }
        this.fnGetAll();
        this.fnGetBusinessCode();
        if (this.$route.params.checkImage != undefined) {
          this.checked = this.$route.params.checkImage.id;
        }
      }
    },
    methods: {
      /*查询所有镜像*/
      fnGetAll(){
        this.loading = true;
        getAllAppImages({"appId":this.appId,"businessArea":this.formInfo.businessArea,"groupId":this.formInfo.groupId,"identify":this.formInfo.identify,"currentPage":this.currentPage,"pageSize":this.pageSize,"isFine":"true"}).then((res)=>{
          if(res.success){
            this.tableData = res.data.list;
            this.total = res.data.total;
          }
          this.loading = false;
        });
      },
      fnGetBusinessCode(){
        appBusinessCode({codeType:"BUSINESSAREA",yab003:"9999"}).then((res)=>{
          if(res.success){
            this.businessOptions = res.data;
          }
        });
      },
      fnCheckBusinessArea(businessArea){
        this.$set(this.formInfo,'groupId','');
        if(businessArea.length){
          this.fnAppGroupGetAll(businessArea);
        }else {
          this.appGroupOptions = [];
        }
      },
      fnAppGroupGetAll(businessArea){
        appGroupGetAll({"businessArea":businessArea}).then((res)=>{
          if(res.success){
            this.appGroupOptions = res.data;
          }
        });
      },
      /*确定镜像*/
      fnCheckImage(){
        if(this.tempCheck.id != undefined){
          this.$router.push({name: this.job?'appManage':'resourceConfig', query: this.$route.query, params: this.tempCheck})
        }else {
          if(this.checked){
            this.$router.push({name: this.job?'appManage':'resourceConfig', query: this.$route.query, params: this.$route.params.checkImage})
          }else {
            this.$message({ showClose: true, message: '请先选择镜像', type: 'error' });
          }
        }
      },
      /*选择的镜像*/
      fnGetRow(row){ this.tempCheck = Object.assign({},this.tempCheck, row); },
      /*查询*/
      fnSearch(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.currentPage = 1;
            this.fnGetAll(this.formInfo.businessArea, this.formInfo.groupId, this.formInfo.identify);
          }
        });
      },
      fnCurrentChange(val) {
        this.currentPage = val;
        this.fnGetAll();
      },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .changeImage { .el-radio__label { padding-left: 0px;} }
</style>
<style lang="scss" scoped type="text/scss">
  .fit { height: 100%; }
  .changeImage { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px; margin-bottom: 20px; }
  .full-width { width: 100%; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
  .pagination { padding: 20px 150px 20px 0; text-align: center; }
  .search-box { .el-form-item { margin-right: 0; margin-bottom: 20px!important; width: 30%; float: left; } }
  .pos-button { position: absolute; right: 25px; bottom: 20px;}
</style>

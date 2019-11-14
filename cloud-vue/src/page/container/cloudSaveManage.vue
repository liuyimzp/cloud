<template>
  <div class="cloudSaveManage">
    <div class="left-part">
      <div class="title">
        <span>云目录管理</span>
        <div style="float: right">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="fnOpenRightMenu" style="float: right">
            添加目录
          </el-button>
        </div>
      </div>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true"
                    :highlight-current-row="true" style="width: 100%;" height="100%">
            <el-table-column prop="storageName" label="目录名称" align="center"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="hostIp" label="IP" align="center" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="hostRootPath" label="目录路径" align="center"></el-table-column>
            <!--<el-table-column prop="storageSize" label="可用/总大小" align="center" min-width="110px">-->
              <!--<template slot-scope="scope">-->
                <!--{{scope.row.freeSize}}Gb/{{scope.row.totleSize}}Gb-->
              <!--</template>-->
            <!--</el-table-column>-->
            <el-table-column prop="storageStatus" label="状态" align="center"></el-table-column>
            <el-table-column label="操作" width="150px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="checkRow(scope.row,scope.$index)" :disabled="scope.$index===indexInfo" size="small">刷新</el-button>
                <el-button type="text" @click="deleteRow(scope.row)" :disabled="scope.$index===indexInfo" size="small">删除</el-button>
                <el-button type="text" @click="startNfs(scope.row,scope.$index)"  v-if="scope.row.active" :disabled="scope.$index===indexInfo" size="small">启动</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}云目录</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close"
                   @click="rightMenu = false; formEdit = false; $refs['formCloud'].resetFields(); submitLoading = false">
          关闭
        </el-button>
      </div>
      <el-form label-position="right" label-width="100px" :model="formCloud" ref="formCloud" :rules="formCloudRules">
        <el-form-item label="名称" prop="storageName">
          <el-input v-model="formCloud.storageName" class="full-width" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="IP" prop="hostIp">
          <el-input v-model="formCloud.hostIp" class="full-width" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="hostUserName">
          <el-input v-model="formCloud.hostUserName" class="full-width" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="hostPassword">
          <el-input type="password" v-model="formCloud.hostPassword" class="full-width" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="端口" prop="hostPort">
          <el-input v-model="formCloud.hostPort" class="full-width" auto-complete="off"></el-input>
        </el-form-item>
        <div style="margin-bottom: 16px; text-align: center">
          <el-button size="small" type="primary" @click="fnShowTable('formCloud')">获取磁盘空间</el-button>
        </div>
        <div class="flex-content" style="padding-bottom: 16px;height:260px;">
          <el-table v-loading="loadingCloud" element-loading-text="拼命加载中" :data="cloudTableData" :stripe="true"
                    :highlight-current-row="true" style="width: 100%;" height="100%">
            <el-table-column prop="mountPath" label="目录" align="center" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="totleSize" label="总空间" align="center"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="freeSize" label="空闲空间" align="center"
                             :show-overflow-tooltip="true"></el-table-column>
          </el-table>
        </div>
        <el-form-item label="云盘根路径" prop="hostRootPath" class="is-required">
          <el-input v-model="formCloud.hostRootPath" class="full-width" auto-complete="off"
                    placeholder='云盘根路径需以"/"开头'></el-input>
        </el-form-item>
        <div style="margin: 20px auto 0; text-align: center">
          <el-button size="small" type="primary" @click="fnSaveCloud('formCloud')" :disabled="dialogLoading">保存<i
            v-if="dialogLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import {
    addStorage,
    queryAllStorage,
    getDiskMounts,
    checkStorage,
    stopNfs,
    startNfs,
    deleteStorage,
    deleteStorageForce
  } from '../../axios/api'
  import {isMinus, isIP, isNumber, isSlash} from '../../assets/js/utils'

  var validateCloudIP = (rule, value, callback) => {
    if (!value) {
      callback(new Error('请输入IP'))
    } else if (!isIP(value)) {
      callback(new Error('您输入的IP地址格式不正确'))
    } else {
      callback()
    }
  };
  var validateHostPort = (rule, value, callback) => {
    if (!value) {
      callback(new Error('请输入端口'))
    } else if (!isNumber(value)) {
      callback(new Error('只允许输入数字'))
    } else {
      callback()
    }
  };
  export default {
    name: 'cloudSaveManage',
    data() {
      return {
        rightMenu: false,
        tableData: [],
        loading: false,
        indexInfo:'',
        formEdit: false,
        volumeCloudOptions: [],
        formCloud: {
          storageName: '',
          hostIp: '',
          hostUserName: '',
          hostPassword: '',
          hostPort: '22',
          hostRootPath: '',
        },
        formCloudRules: {
          hostIp: [{required: true, validator: validateCloudIP, trigger: 'blur'}],
          hostUserName: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur'}
          ],
          hostPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur'}
          ],
          hostPort: [
            {required: true, validator: validateHostPort, trigger: 'blur'},
          ]
        },
        cloudTableData: [],
        loadingCloud: false,
        dialogLoading: false,
        submitLoading: false,
      }
    },
    mounted() {
      if (this.$route.meta.keepAlive === false && this.$route.name == 'cloudSaveManage') {
        this.fnGetCloud();
      }
    },
    methods: {
      /*打开右侧菜单*/
      fnOpenRightMenu() {
        this.rightMenu = true;
        this.formEdit = false;
      },
      /*获取云目录*/
      fnGetCloud() {
        this.loading = true;
        queryAllStorage().then((res) => {
          if (res.success) {
            this.tableData = res.data;
          } else {
            if (res.errorMsg) {
              this.$message({showClose: true, message: res.errorMsg, type: 'error'});
            }
          }
          this.loading = false;
        })
      },
      /*刷新节点信息*/
      checkRow(row, index) {
        this.indexInfo = index;
        checkStorage(row).then((res) => {
          if (res.success) {
            row.freeSize = res.data[0].freeSize;
            row.totleSize = res.data[0].totleSize;
            row.storageStatus = "可用"
            row.active = false;
          } else {
            row.storageStatus = "不可用";
            row.active = true;
          }
          this.indexInfo = '';
          this.$set(this.tableData, index, row);
        });

      },
      /*停止节点nfs服务*/
      stopNfs(row, index) {
        this.indexInfo = index;
        stopNfs(row).then((res) => {
          if (res.success) {
            this.fnGetCloud();
          } else {
            this.$message({showClose: true, message: res.errorMsg, type: 'error'});
          }
          this.indexInfo = '';
          this.$set(this.tableData, index, row);
        });
      },
      /*启动节点nfs服务*/
      startNfs(row, index) {
        this.indexInfo = index;
        startNfs(row).then((res) => {
          if (res.success) {
            this.fnGetCloud();
          } else {
            this.$message({showClose: true, message: res.errorMsg, type: 'error'});
          }
          this.indexInfo = '';
          this.$set(this.tableData, index, row);
        });
      },
      /*删除节点信息*/
      deleteRow(rows) {
        this.$confirm('此操作将永久删除该节点, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true;
          deleteStorage(rows).then((res) => {
            if (res.success) {
              this.$message({showClose: true, message: '删除目录成功', type: 'success'});
              this.fnGetCloud();
            } else {
              if (res.data === 'SSH_CONNECT_DEFEATED') {
                console.log("打印：" + rows);
                this.$confirm('无法连接云储存是否强制删除?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(()=>{
                  deleteStorageForce(rows).then((res) => {
                    if (res.success) {
                      this.$message({showClose: true, message: '删除目录成功', type: 'success'});
                      this.fnGetCloud();
                    } else {
                      this.$message({showClose: true, message: res.errorMsg, type: 'error'});
                    }
                    this.loadingCloud = false;
                  })
                })
              } else {
                this.$message({showClose: true, message: res.errorMsg || '删除目录失败', type: 'error'});
                this.loading = false;
              }
            }
          });
        }).catch(() => {
        });
      },
      /*获取磁盘空间*/
      fnShowTable(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.loadingCloud = true;
            getDiskMounts(this.formCloud).then((res) => {
              if (res.success) {
                this.cloudTableData = res.data;
              } else {
                this.$message({showClose: true, message: res.errorMsg, type: 'error'});
              }
              this.loadingCloud = false;
            })
          }
        })
      },
      /*保存网盘*/
      fnSaveCloud(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.formCloud.hostRootPath) {
              if (!isSlash(this.formCloud.hostRootPath)) {
                this.$message({showClose: true, message: '云盘根路径请以"/"开头', type: 'error'});
                return false;
              }
            } else {
              this.$message({showClose: true, message: '请输入云盘根路径', type: 'error'});
              return false;
            }
            this.dialogLoading = true;
            addStorage(this.formCloud).then((res) => {
              if (res.success) {
                this.$message({showClose: true, message: '新增成功', type: 'success'});
                queryAllStorage().then((res) => {
                  if (res.success) {
                    this.tableData = res.data;
                  }
                })
                this.rightMenu = false;
                this.$refs['formCloud'].resetFields();
              } else {
                this.$message({showClose: true, message: res.errorMsg, type: 'error'});
              }
              this.dialogLoading = false;
            })
          }
        })
      },
      /*存储名称输入建议*/
      querySearch(queryString, cb) {
        var saveNameList = this.saveNameList;
        var results = queryString ? saveNameList.filter(this.createFilter(queryString)) : saveNameList;
        cb(results);
      },
      createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .el-table-filter__bottom {
    text-align: center !important;
  }

  .saveManage {
    .el-table__expanded-cell {
      padding: 12px 20px 12px 60px !important;
    }

    .el-table th {
      padding: 7px 0
    }
  }

  .open-cloud {
    width: 50%;
    margin: 0 auto !important;;

    .is-fullscreen, .el-dialog__body {
      overflow: auto;
      @include flexbox-display-flex();
      @include flexbox-flex();
      @include beautifyScrollbar();
      box-sizing: border-box;
    }

    .el-dialog__body {
      padding: 10px 20px 0;
    }
  }
</style>
<style lang="scss" scoped type="text/scss">
  .cloudSaveManage, .fit {
    height: 100%;
  }

  .title {
    height: 32px;
    line-height: 32px;
    color: $main-ft-color;
    font-size: 18px;
  }

  .description {
    margin: 10px 0 20px;
    padding: 20px;
    color: $main-ft-color;
    background-color: $description-color;
  }

  .left-part {
    padding: 20px 25px 0 25px;
    box-sizing: border-box;
    height: 100%;
    @include flexbox-display-flex();
  }

  .cluster-name {
    color: $main-ft-color;
    font-size: 14px;
    text-align: center;
  }

  .box-card {
    @include rightMenu();
  }

  .pos-add {
    position: absolute;
    top: 0;
    right: -18px;
  }

  .flex {
    @include flexbox-display-flex();
  }

  .el-form-item {
    margin-bottom: 16px;
  }

  .full-width {
    width: 100%;
  }

  .flex-content {
    overflow: auto;
    @include flexbox-display-flex();
    @include flexbox-flex();
    @include beautifyScrollbar();
  }

  .hidden {
    display: none;
  }
</style>

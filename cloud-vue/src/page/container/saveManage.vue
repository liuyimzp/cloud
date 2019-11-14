<template>
  <div class="saveManage">
    <div class="left-part">
      <div class="title">
        <span>nfs存储池管理</span>
        <div style="float: right">
          <div style="float:left; margin: -4px 10px 0 0">
            <span class="cluster-name">集群名称</span>
            <el-select v-model="selectClusterName" style="margin: 0 10px; width: 300px" filterable
                       @change="fnSelectChange">
              <el-option v-for="item in clusterNameInfo" :key="item.id" :value="{id:item.id,value:item.name}"
                         :label="item.name"></el-option>
            </el-select>
          </div>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="fnOpenRightMenu" style="float: right">
            添加存储
          </el-button>
        </div>
      </div>
      <div class="description">存储管理提供容器的持久存储管理能力，提供统一容器存储管理和使用方式，支持对接到多种存储后端，包括NFS、分布式文件系统Ceph，支持存储按需申请。</div>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true"
                    :highlight-current-row="true" style="width: 100%;" height="100%">
            <el-table-column prop="volumeDisplayName" label="存储名称" align="center"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="clusterName" label="集群名称" align="center"></el-table-column>
            <el-table-column prop="volumeUuid" label="英文标识" align="center"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="volumeEnableStateDesc" label="状态" align="center"></el-table-column>
            <el-table-column prop="volumeType" label="类型" align="center" width="100px"
                             :filters="[{text: '本地存储', value: '0'}, {text: '远程存储', value: '1'},{text:'ceph存储', value:'2'}]"
                             :filter-method="filterHandler">
              <template slot-scope="scope">{{scope.row.volumeTypeDesc}}</template>
            </el-table-column>
            <el-table-column prop="volumeMaxSpace" label="可用/总存储" align="center" min-width="110px">
              <template slot-scope="scope">
                {{scope.row.volumeAvailableSpace}}Gb/{{scope.row.volumeMaxSpace}}Gb
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="editRow(scope.row)" size="small">修改</el-button>
                <el-button type="text" @click="deleteRow(scope.row)" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}存储</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close"
                   @click="rightMenu = false; formEdit = false; $refs['formInfo'].resetFields(); preStoragePath='/';formInfo.volumeCloudStorageId=''; formInfo.volumeId = ''; submitLoading = false ; testBtnEnable=false
          cloudStorageInfo={}">
          关闭
        </el-button>
      </div>
      <el-form label-position="right" label-width="100px" :model="formInfo" ref="formInfo" :rules="rules">
        <el-form-item prop="clusterId" class="hidden"></el-form-item>
        <el-form-item label="集群名称" prop="clusterName">
          <el-input v-model="formInfo.clusterName" disabled class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="存储名称" prop="volumeDisplayName">
          <el-autocomplete v-model="formInfo.volumeDisplayName" class="full-width"
                           :fetch-suggestions="querySearch"></el-autocomplete>
        </el-form-item>
        <el-form-item label="英文标识" prop="volumeUuid">
          <el-input v-model="formInfo.volumeUuid" class="full-width" :disabled="formEdit"></el-input>
        </el-form-item>
        <el-form-item label="存储类型" prop="volumeType">
          <el-select v-model="formInfo.volumeType" class="full-width" filterable :disabled="formEdit"
                     @change="fnTypeChange" icon="el-icon-circle-close">
            <el-option v-for="item in volumeTypeOptions" :key="item.value" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="volumeCloudStorageId" v-if="formInfo.volumeType == '1'" label="云存储">
          <el-col :span="22">
            <el-select v-model="formInfo.volumeCloudStorageId" class="full-width" filterable :disabled="formEdit"
                       @change="fnStorageChange">
              <el-option v-for="item in volumeCloudOptions" :key="item.storageId" :label="item.storageName"
                         :value="item.storageId"></el-option>
            </el-select>
          </el-col>
          <el-col :span="2" v-if="testBtnEnable">
            <el-button type="success" icon="el-icon-check" size="small" circle :loading="testLoading">
            </el-button>
          </el-col>
        </el-form-item>
        <el-form-item prop="cephCluster" v-if="formInfo.volumeType == '2'" label="ceph集群">
          <el-col :span="22">
            <el-select v-model="formInfo.volumeCloudStorageId" class="full-width" filterable :disabled="formEdit"
                       @change="fnCephChange">
              <el-option v-for="item in volumeCephOptions" :key="item.id" :label="item.name"
                         :value="item.id"></el-option>
            </el-select>
          </el-col>
          <el-col :span="2" v-if="testBtnEnable">
            <el-button type="success" icon="el-icon-check" size="small" circle :loading="testLoading">
            </el-button>
          </el-col>
        </el-form-item>
        <el-form-item label="存储路径" v-if="formInfo.volumeType == '1'" prop="volumeStoragePath">
          <el-input v-model="formInfo.volumeStoragePath" class="full-width" :disabled="formEdit">
            <template slot="prepend">{{preStoragePath}}</template>
          </el-input>
        </el-form-item>
        <el-form-item label="总空间：">
          <el-input v-model="cloudStorageInfo.totleSize" :disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="可用空间：">
          <el-input v-model="cloudStorageInfo.freeSize" :disabled="true">
          </el-input>
        </el-form-item>

        <el-form-item label="总存储空间" prop="volumeMaxSpace">
          <el-input v-model="formInfo.volumeMaxSpace" class="full-width">
            <template slot="append">Gb</template>
          </el-input>
        </el-form-item>
        <div style="margin: 20px auto 0; text-align: center">
          <el-button size="small" type="primary" @click="submitForm('formInfo');testBtnEnable=false"
                     :disabled="submitLoading">保存<i
            v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog title="新增网盘" :visible.sync="dialogFormVisible" class="open-cloud" :fullscreen="true"
               :close-on-click-modal='false'>
      <el-form :model="formCloud" :rules="formCloudRules" ref="formCloud" label-width="100px" class="fit flex-content">
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
        <div class="flex-content" style="padding-bottom: 16px;">
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false; $refs['formCloud'].resetFields()">取消</el-button>
        <el-button size="small" type="primary" @click="fnSaveCloud('formCloud')" :disabled="dialogLoading">添加<i
          v-if="dialogLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {
    clusterGetAll,
    pvQueryAll,
    addPv,
    updatePv,
    deletePv,
    getDiskMounts,
    addStorage,
    queryAllStorage,
    queryAvailableStorage, checkStorage,queryAllceph
  } from '../../axios/api'
  import {isMinus, isIP, isNumber, isSlash} from '../../assets/js/utils'

  var validateUuid = (rule, value, callback) => {
    if (!value) {
      callback(new Error('请输入英文标识'))
    } else if (!isMinus(value)) {
      callback(new Error('请输入英文字母、数字、减号(减号不能在首、尾)'))
    } else {
      callback()
    }
  };
  var validateCloudIP = (rule, value, callback) => {
    if (!value) {
      callback(new Error('请输入IP'))
    } else if (!isIP(value)) {
      callback(new Error('您输入的IP地址格式不正确'))
    } else {
      callback()
    }
  };
  var validateMaxSpace = (rule, value, callback) => {
    if (!value) {
      callback(new Error('请输入总存储空间'))
    } else if (!isNumber(value)) {
      callback(new Error('只允许输入正整数'))
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
    name: 'saveManage',
    data() {
      return {
        rightMenu: false,
        tableData: [],
        formInfo: {
          clusterId: '',
          clusterName: '',
          volumeDisplayName: '',
          volumeUuid: '',
          volumeType: '',
          volumeCloudStorageId: '',
          cephClusterId: '',
          volumeStoragePath: '',
          volumeMaxSpace: '',
        },
        rules: {
          volumeDisplayName: [
            {required: true, message: '请输入存储名称', trigger: 'blur'},
            {required: true, message: '请输入存储名称', trigger: 'change'},
            {min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur'}
          ],
          volumeUuid: [{required: true, validator: validateUuid, trigger: 'blur'}],
          volumeType: [{required: true, message: '请选择存储类型', trigger: 'change'}],
          volumeCloudStorageId: [{required: true, message: '请选择云存储', trigger: 'change'}],
          volumeStoragePath: [
            {required: true, message: '请输入存储路径', trigger: 'blur'},
            {min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur'}
          ],
          volumeMaxSpace: [{required: true, validator: validateMaxSpace, trigger: 'blur'}],
        },
        loading: false,
        formEdit: false,
        volumeTypeOptions: [{value: '1', label: '远程存储'},{value: '2', label: 'ceph存储'}],
        volumeCloudOptions: [],
        volumeCephOptions: [],
        cloudStorageInfo: {
          totleSize: '',
          freeSize: ''
        },
        dialogFormVisible: false,
        formCloud: {
          storageName: '',
          hostIp: '',
          hostUserName: '',
          hostPassword: '',
          hostPort: '',
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
        selectClusterName: {},
        clusterNameInfo: [],
        cloudTableData: [],
        loadingCloud: false,
        dialogLoading: false,
        submitLoading: false,
        testLoading: false,
        testBtnEnable: false,
        preStoragePath: "/",
        saveNameList: [{"value": "日志存储"}, {"value": "数据存储"}],
      }
    },
    mounted() {
      if (this.$route.meta.keepAlive === false && this.$route.name == 'saveManage') {
        clusterGetAll().then((res) => {
          if (res.success) {
            this.clusterNameInfo = res.data;
            if (this.$route.params.id) {
              this.selectClusterName = this.$route.params;
              this.fnGetAll(this.$route.params.id);
            } else {
              this.selectClusterName = {id: this.clusterNameInfo[0].id, value: this.clusterNameInfo[0].name};
              this.fnGetAll(this.clusterNameInfo[0].id);
            }
          }
        });
        this.fnGetCloud();
        this.fnGetCeph();
      }
    },
    methods: {
      /*打开右侧菜单*/
      fnOpenRightMenu() {
        if (this.selectClusterName.id == undefined) {
          this.$message({showClose: true, message: '请先选择集群名称', type: 'warning'});
        } else {
          this.formInfo.clusterName = this.selectClusterName.value;
          this.formInfo.clusterId = this.selectClusterName.id;
          this.rightMenu = true;
          this.formEdit = false;
        }
      },
      /*获取ceph存储*/
      fnGetCeph() {
        queryAllceph().then((res) => {
          if (res.success) {
            this.volumeCephOptions = res.data;
          }
        })
      },
      /*获取云存储*/
      fnGetCloud() {
        queryAllStorage().then((res) => {
          if (res.success) {
            this.volumeCloudOptions = res.data;
          }
        })
      },
      fnGetCloudMemSize(cloudStorageId) {
        this.testLoading = true
        this.volumeCloudOptions.forEach((item) => {
          if (item.storageId == cloudStorageId) {
            getDiskMounts(item).then((res) => {
              if (res.success) {
                this.cloudStorageInfo.freeSize = res.data[0].freeSize;
                this.cloudStorageInfo.totleSize = res.data[0].totleSize;
                this.testLoading = false
                this.testBtnEnable = true
              } else {
                this.testLoading = false
                this.testBtnEnable = false
                this.$message({showClose: true, message: '云盘不可用，请检查或重新选择', type: 'error'});
              }
            })
          }
        })

      },
      /*下拉框选择*/
      fnSelectChange(data) {
        this.formInfo.clusterName = data.value;
        this.formInfo.clusterId = data.id;
        this.fnGetAll(data.id);
      },
      /*根据clusterId查询表格数据*/
      fnGetAll(clusterId) {
        this.loading = true;
        pvQueryAll({clusterId: clusterId}).then((res) => {
          if (res.success) {
            this.tableData = res.data;
          } else {
            if (res.errorMsg) {
              this.$message({showClose: true, message: res.errorMsg, type: 'error'});
            }
          }
          this.loading = false;
        });
      },
      /*存储类型选择*/
      fnTypeChange(data) {
        if (data == "0") {
          this.preStoragePath = '/';
        }
        this.$set(this.formInfo, "volumeCloudStorageId", null)
      },
      /*ceph集群选择*/
      fnCephChange(clusterId) {
        if (clusterId != "") {
          this.cloudStorageInfo.freeSize = '';
          this.cloudStorageInfo.totleSize = '';
          this.volumeCephOptions.forEach((item) => {
            if (item.id == clusterId) {
              this.cloudStorageInfo.freeSize = item.memoryAvaiLabel;
              this.cloudStorageInfo.totleSize = item.memoryTotal;
              this.testLoading = false
              this.testBtnEnable = true
            }
          })
        }
      },
      /*云存储选择*/
      fnStorageChange(storageId) {
        if (storageId != "") {
          this.cloudStorageInfo.freeSize = '';
          this.cloudStorageInfo.totleSize = '';
          this.fnGetCloudMemSize(storageId)
        }
        this.volumeCloudOptions.forEach((item) => {
          if (item.storageId == storageId) {
            this.preStoragePath = item.hostRootPath + '/';
          }
        })
      },
      /*保存表单*/
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitLoading = true;
            if (this.formEdit) {
              updatePv(this.formInfo).then((res) => {
                if (res.success) {
                  this.fnGetAll(this.formInfo.clusterId);
                  this.rightMenu = false;
                  this.formEdit = false;
                  this.$message({showClose: true, message: '修改成功', type: 'success'});
                  this.$refs['formInfo'].resetFields();
                  this.formInfo.volumeId = '';
                } else {
                  this.$message({showClose: true, message: res.errorMsg || "修改失败", type: 'error'});
                }
                this.submitLoading = false;
              })
            } else {
              addPv(this.formInfo).then((res) => {
                if (res.success) {
                  this.fnGetAll(this.formInfo.clusterId);
                  this.rightMenu = false;
                  this.$message({showClose: true, message: '新增成功', type: 'success'});
                  this.$refs['formInfo'].resetFields();
                } else {
                  this.$message({showClose: true, message: res.errorMsg || "保存失败", type: 'error'});
                }
                this.submitLoading = false;
              })
            }
          }
        });
      },
      /*表格类型筛选*/
      filterHandler(value, row, column) {
        const property = column['property'];
        return row[property] === value;
      },
      /*修改节点信息*/
      editRow(row) {
        this.formInfo = Object.assign({}, this.formInfo, {
          volumeId: row.volumeId,
          clusterId: row.clusterId,
          clusterName: row.clusterName,
          volumeDisplayName: row.volumeDisplayName,
          volumeUuid: row.volumeUuid,
          volumeType: row.volumeType,
          volumeCloudStorageId: row.volumeCloudStorageId,
          volumeStoragePath: row.volumeStoragePath,
          volumeMaxSpace: row.volumeMaxSpace
        });
        if (row.volumeType == "0") {
          this.preStoragePath = '/';
        } else {
          this.volumeCloudOptions.forEach((item) => {
            if (item.storageId == row.volumeCloudStorageId) {
              this.preStoragePath = item.hostRootPath + '/';
            }
          })
        }
        this.formEdit = true;
        this.rightMenu = true;
      },
      /*删除节点信息*/
      deleteRow(rows) {
        this.$confirm('此操作将永久删除该节点, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true;
          deletePv(rows).then((res) => {
            if (res.success) {
              this.$message({showClose: true, message: '删除节点成功', type: 'success'});
              this.fnGetAll(rows.clusterId);
            } else {
              this.$message({showClose: true, message: res.errorMsg || '删除节点失败', type: 'error'});
              this.loading = false;
            }
          });
        }).catch(() => {
        });
      },
      /*打开新增网盘*/
      fnPosAdd() {
        this.dialogFormVisible = true;
        if (this.$refs['formCloud']) {
          this.$refs['formCloud'].resetFields();
        }
        this.$set(this.formCloud, "hostPort", 22)
        this.dialogLoading = false;
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
                this.$set(this.formInfo, "volumeCloudStorageId", res.data)
                queryAllStorage().then((res) => {
                  if (res.success) {
                    this.volumeCloudOptions = res.data;
                    this.fnStorageChange(this.formInfo.volumeCloudStorageId);
                  }
                })
                this.dialogFormVisible = false;
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
  .saveManage, .fit {
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

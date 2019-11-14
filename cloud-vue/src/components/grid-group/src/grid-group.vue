<template>
  <el-table :data="tableData" :border="border" style="width: 100%" :row-style="showTr" :span-method="groupMethod">
    <el-table-column v-for="(column, index) in columns" :key="column.id" :label="column.name" :align="index == 0 && groupType == 'tree' ?'left':align" :header-align="align">
      <template slot-scope ="scope">
        <div v-if="groupType == 'tree'">
          <span v-if="index === 0" v-for="(space, levelIndex) in scope.row._level" :key="levelIndex" class="ta-tree-space"></span>
          <el-button type="text" :icon="scope.row._expanded? 'el-icon-caret-bottom' : 'el-icon-caret-right'"  v-if="toggleIconShow(index,scope.row)" @click="toggle(scope.$index)"></el-button>
          <span v-else-if="index === 0" class="ta-tree-space"></span>
          {{scope.row[column.id]}}
        </div>
        <div v-else="groupType == 'group'">
          <div v-if="scope.row._groupBy" class="ta-count-title">
            <el-button type="text" :icon="scope.row._expanded? 'el-icon-remove-outline' : 'el-icon-circle-plus-outline'"  v-if="toggleIconShow(index,scope.row)" @click="toggle(scope.$index)"></el-button>
            {{scope.row._groupName}}：{{scope.row._groupInfo}}<span class="ta-count-num">（共{{scope.row._groupLength}}条）</span>
          </div>
          <span v-else>{{scope.row[column.id]}}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="操作" v-if="isOperate === true" width="260" align="center">
      <template slot-scope="scope">
        <el-button type="text" v-for="(operate,index) in operates" :key="index" :icon="operate.icon" @click="operate.clickEvent(tableData[scope.$index],index,scope.$index)">{{operate.name}}</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script>
  import { treeFormatter, groupFormatter } from './data.js';
  export default {
    name: 'CloudGridGroup',
    props: {
      // 表格是否有border
      border: {
        type: Boolean,
        default: function () {
          return false
        }
      },
      // 对齐方式
      align: {
        type: String,
        default: function () {
          return 'left'
        }
      },
      // 树形格式 tree：树结构 group：字段分组
      groupType: {
        type: String,
        default: function () {
          return 'tree'
        }
      },
      // 对齐方式
      groupBy: {
        type: String,
        default: function () {
          return ''
        }
      },
      // 是否默认展开所有树
      expandAll: {
        type: Boolean,
        default: function () {
          return false
        }
      },
      // 表头显示
      columns: {
        type: Array,
        default: function () {
          return []
        }
      },
      // 表格数据源
      dataSource: {
        type: Array,
        default: function () {
          return []
        }
      },
      // 是否显示操作列
      isOperate: {
        type: Boolean,
        default: function () {
          return true
        }
      },
      // 操作列数据
      operates: {
        type: Array,
        default: function () {
          return []
        }
      },
    },
    computed: {
      // 格式化数据源
      tableData: function () {
        if (this.groupType == 'tree') {
          return treeFormatter(this.dataSource, null, null, this.expandAll);
        }else {
          return groupFormatter(this.dataSource, this.groupBy, this.columns, this.expandAll);
        }
      }
    },
    methods: {
      // 显示行
      showTr: function ({row}) {
        let show;
        if(this.groupType == 'tree'){
          show = (row._parent ? (row._parent._expanded && row._parent._show) : true)
        }
        if(this.groupType == 'group'){
          show = (row._groupBy ?  true : (this.tableData[row._parentIndex]._expanded));
        }
        row._show = show;
        return show ? '' : 'display:none;'
      },
      // 点击展开和关闭的时候，图标的切换
      toggle: function (trIndex) {
        let record = this.tableData[trIndex];
        record._expanded = !record._expanded;
      },
      // 显示展开、关闭按钮
      toggleIconShow (index, row) {
        if(this.groupType == 'tree' && index === 0 && row.children && row.children.length > 0){
          return true;
        }
        if(this.groupType == 'group' && row._groupBy ){
          return true;
        }
        return false;
      },
      // 分组表格总信息合并单元格
      groupMethod({ row, column, rowIndex, columnIndex }){
        if(this.groupType == 'group'){
          if (row._groupBy) {
            if (columnIndex === row._groupIndex) {
              let len = row._columnsLength;
              if(this.operates){ len += 1; }
              return [1, len];
            } else {
              return [0, 0];
            }
          }
        }
      }
    }
  }
</script>
<style scoped>
  .ta-tree-space{position: relative; top: 1px; display: inline-block; font-family: 'Glyphicons Halflings'; font-style: normal; font-weight: 400; line-height: 1; width: 20px; height: 14px; }
  .ta-tree-space::before{content: ""}
  .ta-count-title { text-align: left; }
  .ta-count-num { color: #f59f3d }
</style>

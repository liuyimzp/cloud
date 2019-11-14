import Vue from 'vue';
/*树分组数据处理*/
export function treeFormatter(data, parent, level, expandedAll) {
  let temp = [];
  data.forEach(function (item) {
    if (item._expanded === undefined) {
      Vue.set(item, '_expanded', expandedAll);
    }
    if (parent) {
      Vue.set(item, '_parent', parent);
    }
    let _level = 0;
    if (level !== undefined && level !== null) {
      _level = level + 1;
    }
    Vue.set(item, '_level', _level);
    temp.push(item);
    if (item.children && item.children.length > 0) {
      let children = treeFormatter(item.children, item, _level, expandedAll);
      temp = temp.concat(children);
    }
  })
  return temp;
}
/*按照字段分组数据处理*/
export function groupFormatter(data, groupBy, columns, expandedAll) {
  let groupItem = {}, temp = [], result = []
  data.forEach((item)=>{
    if(item[groupBy] !== undefined){
      if(!groupItem[item[groupBy]]){
        groupItem[item[groupBy]] = item[groupBy];
        temp.push({
          "groupInfo":item[groupBy],
          "groupBy": groupBy,
          "data":[item]
        })
      }else {
        for(let i = 0; i<temp.length; i++){
          if(temp[i].groupInfo == item[groupBy]){
            temp[i].data.push(item);
            break;
          }
        }
      }
    }else {
      if(!groupItem['other']){
        groupItem['other'] = 'other';
        temp.push({
          "groupInfo": '其他',
          "groupBy": 'other',
          "data":[item]
        })
      }else {
        for(let i = 0; i<temp.length; i++){
          if(temp[i].groupBy == 'other'){
            temp[i].data.push(item);
            break;
          }
        }
      }
    }
  });

  let groupIndex, groupName;
  for(let i = 0; i< columns.length; i++){
    if(columns[i].id == groupBy){
      groupIndex = i;
      groupName = columns[i].name;
    }
  }

  temp.forEach((item)=>{
    if(item.groupBy){
      let parentLen = result.length;
      result.push({
        "_groupName": groupName,
        "_groupBy": item.groupBy,
        "_groupInfo": item.groupInfo,
        "_groupLength": item.data.length,
        "_columnsLength": columns.length,
        "_groupIndex": groupIndex,
        "_expanded": expandedAll,
      });
      item.data.forEach((it)=>{
        Vue.set(it, '_parentIndex', parentLen);
        result.push(it);
      })
    }else {
      result.push(item);
    }
  })
  return result;
}

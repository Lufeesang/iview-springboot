/*
* 基本通用函数: linearToLinear
* 功能: 将线性结构添加到线性结构中
* 参数:
* 1. [] source: 源线性结构
* 2. [] destination: 目标线性结构
*/
export const linearToLinear = (source, destination) => {
  let copySource = JSON.parse(JSON.stringify(source))
  let number = copySource.length
  for (let i = 0; i < number; i++) {
    destination.push(copySource[i])
  }
}
/*
* 基本通用函数: deleteLinear
* 功能: 删除 source 中 mapList 有的项
* 参数:
* 1. [] source: 源数组
* 2. [] mapList: 比对数组
* 3. '' key: 比对主键名
*/
export const deleteLinear = (source, mapList, key) => {
  let length = source.length
  let count
  mapList.map((val, i) => {
    count = 0
    length = source.length
    for (let j = 0; j < length; j++) {
      if (val[key] === source[count][key]) {
        source.splice(count, 1)
        count--
      }
      count++
    }
  })
}
/*
* 基本通用函数: updateLinear
* 功能: 合并更新数组
* 参数:
* 1. [] source: 更新数组
* 2. [] destination: 目标数组
* 3. '' key: 比对键名
* 注意:
* 1. key应为唯一值
*/
export const updateLinear = (source, destination, key) => {
  let length
  let flag
  source.map((val, i) => {
    flag = true
    length = destination.length
    for (let j = 0; j < length; j++) {
      if (destination[j][key] === source[i][key]) {
        // console.log('修改了一项')
        destination.splice(j, 1, source[i])
        flag = false
        break
      }
    }
    if (flag) {
      // console.log('添加了一项')
      destination.push(source[i])
    }
  })
}
/*
* 基本通用函数: linearToTrees
* 功能: 线性结构转化成树形结构
* 参数:
* 1. [] linearStructure: 需要组装的线性结构
* 2. [] trees: 存放结果的容器
* 3. '' parentKey: parentId的键名
* 4. '' key: 节点id的键名
* 5. '' childrenKey: 子节点数组的键名
* 注意:
* 1.linearStructure 中节点的 childrenKey 的值必须为 Array 类型
*/
export const linearToTrees = (linearStructure, trees, parentKey, key, childrenKey) => {
  let copyLinearStructure = JSON.parse(JSON.stringify(linearStructure))
  let flag = true // 是根节点
  copyLinearStructure.map((child, index) => {
    flag = true
    copyLinearStructure.map((parent, index) => {
      if (child[parentKey] === parent[key]) {
        flag = false
        parent[childrenKey].push(child)
      }
    })
    if (flag) {
      trees.push(child)
    }
  })
  // console.log(copyLinearStructure)
}
/*
* 基本通用函数: treesToLinear
* 功能: 树形结构转化成线性结构
* 参数:
* 1. [] trees: 需要拆解的树形结构
* 2. [] linearStructure: 存放结果的容器
* 3. '' parentKey: parentId的键名
* 4. '' key: 节点id的键名
* 5. '' childrenKey: 子节点数组的键名
*/
export const treesToLinear = (trees, linearStructure, childrenKey) => {
  let copyTrees = JSON.parse(JSON.stringify(trees))
  let traceback = (trees, linearStructure, childrenKey) => {
    let treeNodesNumber = trees.length
    for (let i = 0; i < treeNodesNumber; i++) {
      if (!trees[i].hasOwnProperty(childrenKey) || trees[i][childrenKey].length === 0) {
        linearStructure.push(trees[i])
      } else {
        linearStructure.push(trees[i])
        traceback(trees[i][childrenKey], linearStructure, childrenKey)
      }
    }
    for (let i = 0; i < linearStructure.length; i++) {
      linearStructure[i][childrenKey] = []
    }
  }
  traceback(copyTrees, linearStructure, childrenKey)
}
/*
* 基本通用函数: batchTreeNodesDelete
* 功能: 树结构节点的批量删除
* 参数:
* 1. [] trees: 需要操作的树结构
* 2. [] linearStructure: 要删除的树节点数组
* 3. '' key: 节点id的键名
* 4. '' childrenKey: 子节点的键名
* 注意:
* 1. trees 与 linearStructure 比对的 key 值相同
*/
export const batchTreeNodesDelete = (trees, linearStructure, key, childrenKey) => {
  let treesNumber = trees.length
  let treesListNumber = linearStructure.length
  let count = 0
  let flag = false
  for (let i = 0; i < treesNumber; i++) {
    if (trees[count].hasOwnProperty(childrenKey) && trees[count][childrenKey].length !== 0) {
      batchTreeNodesDelete(trees[count][childrenKey], linearStructure, key, childrenKey)
    }
    flag = false
    for (let j = 0; j < treesListNumber; j++) {
      if (trees[count][key] === linearStructure[j][key]) {
        flag = true
      }
    }
    if (flag) {
      trees.splice(count, 1)
      count = count - 1
    }
    count = count + 1
  }
}
/*
* 基本通用函数: searchTreeNodes
* 功能: 查找数中的节点
* 参数:
* 1. [] trees: 需要操作的树结构
* 2. '' key: 节点id的键名
* 3. '' value: 键值
* 4. '' childrenKey: 子节点的键名
* 返回值
* {} 树节点,不影响原节点
*/
export const searchTreeNodes = (trees, key, value, childrenKey) => {
  let result = null
  let copyTrees = JSON.parse(JSON.stringify(trees))
  let traceback = (trees, key, value, result, childrenKey) => {
    let treeNodesNumber = trees.length
    for (let i = 0; i < treeNodesNumber; i++) {
      if (!trees[i].hasOwnProperty(childrenKey) || trees[i][childrenKey].length === 0) {
      } else {
        traceback(trees[i][childrenKey], key, value, result, childrenKey)
      }
    }
    for (let i = 0; i < linearStructure.length; i++) {
      linearStructure[i][childrenKey] = []
    }
  }
  traceback(copyTrees, key, value, result, childrenKey)
  return result
}
/*
* 基本通用函数: filterTreesToLinearStructure
* 功能: 筛选出树结构部分子节点至数组中
* 参数:
* 1. [] sourceTrees: 源树结构
* 2. [] mapList: 用于筛选的数组
* 3. '' key: 对应树节点比对的键名
* 4. [] destination: 存放结果的数组
* 5. function f: 比对函数，比对顺序为f(sourceTrees.node['key'], mapList.node)
* 6. '' childrenKey: 子节点的键名
* 注意:
* 1. sourceTrees 前后不改变
* 2. mapList 为基本数据类型的数组
* 3. destination 初始应当为空
*/
export const filterTreesToLinearStructure = (sourceTrees, mapList, key, destination, f, childrenKey) => {
  let copySourceTrees = JSON.parse(JSON.stringify(sourceTrees))
  let traceback = (sourceTrees, mapList, key, destination, f, childrenKey) => {
    let flag = false
    sourceTrees.map((val, index) => {
      flag = false
      if (val.hasOwnProperty(childrenKey) && val[childrenKey].length !== 0) {
        traceback(val[childrenKey], mapList, key, destination, f, childrenKey)
      }
      mapList.map((val2, index2) => {
        if (f(val[key], val2)) {
          flag = true
        }
      })
      if (flag) {
        destination.push(val)
      }
    })
    // destination.map((val, index) => {
    //   val[childrenKey] = []
    // })
  }
  traceback(copySourceTrees, mapList, key, destination, f, childrenKey)
}
/*
* 基本通用函数: filterLinearStructure
* 功能: 按给定多项条件筛选出数组项
* 参数:
* 1. [] source: 源数组
* 2. [] destination: 目标数组
* 3. [] condition: 筛选条件,格式如下:
[{
  mapList: [] 用于筛选的数组
  key: '' 对应节点比对的键名
  f: function 比对函数，比对顺序为f(source.node['key'], mapList.node)
}...]
* 注意:
* 1. 多项条件按逻辑值and连接
* 2. mapList 为基本数据类型的数组
*/
export const filterLinearStructure = (source, destination, condition) => {
  let copySource = JSON.parse(JSON.stringify(source))
  let flag1 = true
  let flag2 = false
  copySource.map((val, i) => {
    flag1 = true
    condition.map((val2, j) => {
      flag2 = false
      condition[j]['mapList'].map((val3, k) => {
        if (condition[j]['f'](copySource[i][condition[j]['key']], condition[j]['mapList'][k])) {
          flag2 = true
        }
      })
      if (!flag2) {
        flag1 = false
      }
    })
    if (flag1) {
      destination.push(copySource[i])
    }
  })
}
/*
* 复合通用函数: treesToTrees
* 功能: 将两个树结构合并
* 参数:
* 1. [] source: 源树结构
* 2. [] destination: 目标树结构
* 3. '' childrenKey: 子节点的键名
* 4. '' parentKey: 父节点的键名
* 5. '' key: 节点id键名
*/
export const treesToTrees = (source, destination, childrenKey, parentKey, key) => {
  let middleLinear = []
  treesToLinear(source, middleLinear, childrenKey)
  treesToLinear(destination, middleLinear, childrenKey)
  destination.splice(0, destination.length)
  linearToTrees(middleLinear, destination, parentKey, key, childrenKey)
}

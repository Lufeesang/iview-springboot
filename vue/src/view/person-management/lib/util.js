/*
* 基本通用函数: linearToLinear
* 功能: 将线性结构添加到线性结构中
* 参数:
* 1. [] source: 源线性结构
* 2. [] destination: 目标线性结构
*/
export const linearToLinear = (source, destination) => {
  let number = source.length
  for (let i = 0; i < number; i++) {
    destination.push(source[i])
  }
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
  let traceback = (trees, linearStructure, key, childrenKey) => {
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
* 基本通用函数: filterTreesToLinearStructure
* 功能: 筛选出树结构部分子节点至数组中
* 参数:
* 1. [] sourceTrees: 源树结构
* 2. [] mapList: 用于筛选的数组
* 3. '' key: 对应树节点比对的键名
* 4. [] destination: 存放结果的数组
* 5. function f: 比对函数，比对顺序为f(sourceTrees.node['key'], destination.node)
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
    destination.map((val, index) => {
      val[childrenKey] = []
    })
  }
  traceback(copySourceTrees, mapList, key, destination, f, childrenKey)
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

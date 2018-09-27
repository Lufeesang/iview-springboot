import axios from './../config/axios'
var baseUrl = 'http://127.0.0.1:80/sys/role'
/*
* 请求接口: addRole
* 功能: 请求在数据库添加一个角色
* 参数格式:
* data: {
*   parentId: number
*   remark: ''
*   roleName: ''
*   roleSign: ''
* }
* 返回数据格式:
* data: {
*   gmtCreate: ''
*   gmtModified: ''
*   parentId: number
*   remark: ''
*   roleId: number
*   roleName: ''
*   roleSign: ''
*   userIdCreate: number
* }
*/
export const addRole = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/add',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}
/*
* 请求接口: batchAddRole
* 功能: 请求在数据库批量导入角色
* 参数格式:
* data: {
* }
* 返回数据格式:
* data: {
* }
*/
export const batchAddRole = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/batchAdd',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}
/*
* 请求接口: deleteRoles
* 功能: 请求在数据库删除复数角色
* 参数格式:
* data: {
*   ids: [
*     number...
*   ]
* }
* 返回数据格式:
* data: {
*   code: number
*   msg: ''
* }
*/
export const deleteRoles = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/batchRemove',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}
/*
* 请求接口: listRole
* 功能: 请求在数据库的角色列表
* 参数格式: -- 均为可选项
* data: {
*   roleId: number
*   parentId: number
*   roleName: ''
*   roleSign: ''
*   remark: ''
*   userIdCreate: number
*   gmtCreate: '' --  日期格式为: yyyy-MM-dd HH:mm:ss
*   gmtModified: ''
* }
* 返回数据格式:
* data: [
*   {
*     gmtCreate: ''
*     gmtModified: ''
*     menuIds: null -- 无效项
*     parentId: number
*     remark: ''
*     roleId: number
*     roleName: ''
*     roleSign: ''
*     userIdCreate: number
*   }...
* ]
*/
export const listRole = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/list',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}
/*
* 请求接口: updateRole
* 功能: 请求在数据库更改一个角色
* 参数格式:
* data: {
*   parentId: number -- 必须项
*   remark: ''
*   roleName: ''
*   roleSign: ''
* }
* 返回数据格式:
* data: {
*   code: number
*   msg: ''
* }
*/
export const updateRole = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/update',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}

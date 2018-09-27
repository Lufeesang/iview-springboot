import axios from './../config/axios'
var baseUrl = 'http://127.0.0.1:80/personUserRole'
/*
* 请求接口: listRoleUser
* 功能: 请求在数据库的角色-用户列表
* 参数格式:
* data: {
*   roleId: number
* }
* 返回数据格式:
* data: {
*   [
*     number...
*   ]
* }
*/
export const listRoleUser = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/getUserRoleList',
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
* 请求接口: updateRoleUser
* 功能: 请求更新数据库的角色-用户列表
* 参数格式:
* data: {
*   roleId: number
*   userIds: [
*     number...
*   ]
* }
* 返回数据格式:
* data: {
*   code: number
*   msg: ''
* }
*/
export const updateRoleUser = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/updateRoleUser',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}

import axios from './../config/axios'
// var baseUrl = 'http://192.168.43.89:9999/roleMenu'
var baseUrl = 'http://127.0.0.1:80/roleMenu'
/*
* 请求接口: listRoleMenu
* 功能: 请求在数据库的角色-菜单列表
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
export const listRoleMenu = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/listMenuIdByRoleId',
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
* 请求接口: updateRoleMenu
* 功能: 请求更新数据库的角色-菜单列表
* 参数格式:
* data: {
*   roleId: number
*   menuIds: [
*     number...
*   ]
* }
* 返回数据格式:
* data: {
*   code: number
*   msg: ''
* }
*/
export const updateRoleMenu = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/updateRoleMenu',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}

import axios from './../config/axios'
var baseUrl = 'http://127.0.0.1:80/personuser'
/*
* 请求接口: login
* 功能: 请求登录
* 参数格式:
* data: {
*   username: ''
*   password: ''
* }
* 返回数据格式:
* data: {
*   code: number
*   msg: ''
* }
*/
export const login = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: 'http://127.0.0.1:80/login',
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
* 请求接口: logout
* 功能: 请求登出
* 参数格式:
* data: {
* }
* 返回数据格式:
* data: {
*   code: number
*   msg: ''
* }
*/
export const logout = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: 'http://127.0.0.1:80/logout',
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
* 请求接口: getUserInfo
* 功能: 获取当前用户信息
* 参数格式:
* data: {
* }
* 返回数据格式:
* data: {
*   code: number
*   msg: ''
* }
*/
export const getUserInfo = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: 'http://127.0.0.1:80/getUserInfo',
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
* 请求接口: listUser
* 功能: 请求在数据库的用户列表
* 参数格式: --未知，暂时为空
* data: ''
* 返回数据格式:
* data: [
*  {
*    userId: number
*    name: ''
*    deptId: number
*    status: number
*    ...
*  }...
*]
*/
export const listUser = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/getAllUserList',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}

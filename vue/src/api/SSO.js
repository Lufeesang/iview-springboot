import axios from './../config/axios'
var baseUrl = 'http://127.0.0.1:80/SSOservice'
/*
* 请求接口: list
* 功能: 请求在数据库的单点登录菜单
* 参数格式:
* data: {
* }
* 返回数据格式:
* data: {
*   code: number
*   msg: ''
*   list: [
*     {
*       menu_id: number
*       name: ''
*       url: ''
*     }...
*   ]
* }
*/
export const list = (data) => {
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
* 请求接口: authorize
* 功能:
* 参数格式:
* data: {
*   client_id: ''
*   response_type: ''
*   redirect_url: ''
* }
* 返回数据格式:
* data: {
* }
*/
export const authorize = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: 'http://127.0.0.1:80' + '/authorize',
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
* 请求接口: deliverToken
* 功能: 请求生成token
* 参数格式:
* data: {
*   menuId: number
* }
* 返回数据格式:
* data: {
*   code: number
*   msg: ''
*   token: ''
*   url: ''
* }
*/
export const deliverToken = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/deliverToken',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}

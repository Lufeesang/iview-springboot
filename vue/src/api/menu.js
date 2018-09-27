import axios from './../config/axios'
// var baseUrl = 'http://192.168.43.89:9999'
var baseUrl = 'http://127.0.0.1:80'
/*
* 请求接口: listMenu
* 功能: 请求在数据库的菜单列表
* 参数格式: --未知，暂时为空
* data: ''
* 返回数据格式:
* data: [
  {
    name: ''
    url: ''
    perms: ''
    menu_id: number
    parent_id: number
    gmt_create: ''
    gmt_modified: ''
  }...
]
*/
export const listMenu = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/getMenuList',
      data,
      method: 'get'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}

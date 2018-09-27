import axios from './../config/axios'
var baseUrl = 'http://127.0.0.1:80/org'
/*
* 请求接口: listdept
* 功能: 请求在数据库的部门列表
* 参数格式: --未知，暂时为空
* data: ''
* 返回数据格式:
* data: [
  {
    id: number
    title: ''
    pid: number
    ...
  }...
]
*/
export const listdept = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: baseUrl + '/info',
      data,
      method: 'get'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}

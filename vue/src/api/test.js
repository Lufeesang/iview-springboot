import axios from './../config/axios'

export const test = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: 'http://127.0.0.1/sys/role/check',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}

export const test2 = (data) => {
  return new Promise((resolve, reject) => {
    axios({
      url: 'http://127.0.0.1/sys/role/test',
      data,
      method: 'post'
    }).then((r) => {
      resolve(r)
    }).catch((e) => {
      reject(e)
    })
  })
}

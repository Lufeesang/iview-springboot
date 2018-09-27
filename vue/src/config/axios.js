import axios from 'axios'
axios.defaults.withCredentials = true
axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest'
axios.defaults.baseURL = 'http://127.0.0.1:80'
// 添加请求拦截器
axios.interceptors.request.use((config) => {
  // 在发送请求之前做些什么
  return config
}, (error) => {
  // 对请求错误做些什么
  return Promise.reject(error)
})

// 添加响应拦截器
axios.interceptors.response.use((response) => {
  // 对响应数据做点什么
  if (response.data['code'] === 401) {
    window.location.href = '/login'
  } else if (response.data['code'] === -988) {
    window.location.href = '/401'
  }
  return response
}, (error) => {
  // 对响应错误做点什么
  return Promise.reject(error)
})
export default axios

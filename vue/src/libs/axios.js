import Axios from 'axios'
import baseURL from '_conf/url'
import { Message } from 'iview'
import Cookies from 'js-cookie'
import { TOKEN_KEY } from '@/libs/util'
class httpRequest {
  constructor() {
    this.options = {
      method: '',
      url: ''
    }
    // 存储请求队列
    this.queue = {}
  }
  // 销毁请求实例
  destroy(url) {
    delete this.queue[url]
    const queue = Object.keys(this.queue)
    return queue.length
  }
  // 请求拦截
  interceptors(instance, url) {
    // 添加请求拦截器
    instance.interceptors.request.use(config => {
      // 在发送请求之前做些什么
      // 
      return config
    }, error => {
      // 对请求错误做些什么
      return Promise.reject(error)
    })

    // 添加响应拦截器
    instance.interceptors.response.use((res) => {
      let { data } = res
        // 后端服务在个别情况下回报201，待确认
        if (data.code === 401) {
          Message.error(data.msg)
          localStorage.removeItem("isLogin")
          window.location.href = '/login'
        } else if (data.code === 403) {
          Message.error(data.msg)
        }
      return res
    }, (error) => {
      // Message.error('服务内部错误233')
      // 对响应错误做点什么
      console.log(error)
      return Promise.reject(error)
    })
  }
  // 创建实例
  create() {
    let conf = {
      baseURL: baseURL,
      withCredentials: true,
      headers: { 'X-Requested-With': 'XMLHttpRequest' },
    }
    return Axios.create(conf)
  }
  // 合并请求实例
  mergeReqest(instances = []) {
    //
  }
  // 请求实例
  request(options) {
    var instance = this.create()
    // 设置拦截器
    this.interceptors(instance, options.url)
    options = Object.assign({}, options)
    this.queue[options.url] = instance
    return instance(options)
  }
}
export default httpRequest

import Vue from 'vue'
import Router from 'vue-router'
import routes from './routers'
import store from '@/store'
import iView from 'iview'
import { canTurnTo } from '@/libs/util'

Vue.use(Router)
const router = new Router({
  routes,
  mode: 'history'
})
const LOGIN_PAGE_NAME = 'login'

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start()
  // const token = Cookies.get(TOKEN_KEY) ? Cookies.get(TOKEN_KEY) : false
  // const token = true
  // console.log(token)
  if (to.name === LOGIN_PAGE_NAME) {
    // 是登录页
    next()
  } else {
    store.dispatch('getUserInfo').then(user => {
      // 拉取用户信息，通过用户权限和跳转的页面的name来判断是否有权限访问;access必须是一个数组，如：['super_admin'] ['super_admin', 'admin']
      // console.log(to.name)
      // console.log(user.access)
      // console.log(routes)
      if (canTurnTo(to.name, user.access, routes)) next() // 有权限，可访问
      else next({ replace: true, name: 'error_401' }) // 无权限，重定向到401页面
    }, e => {
      next({
        name: LOGIN_PAGE_NAME // 跳转到登录页
      })
    })
  }
})

router.afterEach(to => {
  iView.LoadingBar.finish()
  window.scrollTo(0, 0)
})

export default router

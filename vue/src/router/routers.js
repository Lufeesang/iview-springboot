import Main from '@/view/main'
import parentView from '@/components/parent-view'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面不会缓存
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 * }
 */

export default [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: 'Login - 登录',
      hideInMenu: true
    },
    component: () => import('@/view/login/login.vue')
  },
  {
    path: '/',
    name: 'home',
    redirect: '/home',
    component: Main,
    meta: {
      hideInMenu: true,
      notCache: true
    },
    children: [
      {
        path: 'home',
        name: 'home',
        meta: {
          hideInMenu: true,
          notCache: true
        },
        component: () => import('@/view/single-page/home')
      }
    ]
  },
  {
    path: '/org-manage',
    meta: {
      icon: 'md-menu',
      title: '组织管理',
      // access: ['1']
    },
    name: 'orgmanage',
    component: Main,
    children: [{
        path: 'org-info',
        meta: {
          icon: 'ios-book',
          title: '基本信息',
          // access: ['1', '2']
        },
        name: 'orginfo',
        component: () =>
          import ('@/view/org/org-info.vue')
      },
      {
        path: 'org-staffs',
        meta: {
          icon: 'md-grid',
          title: '分配人员',
          // access: ['unk']
        },
        name: 'orgstaffs',
        component: () =>
          import ('@/view/org/org-staffs.vue')
      }
    ]
  },
  {
    path: '/person',
    name: 'person',
    meta: {
      icon: 'ios-book',
      title: '人员管理'
    },
    component: Main,
    children: [
      {
        path: 'person_page1',
        name: 'person_page1',
        meta: {
          icon: '_qq',
          title: '人员信息展示'
        },
        component: () => import('@/view/person-management/tree.vue')
      }
    ]
  },
  {
    path: '/role',
    name: 'role',
    meta: {
      icon: 'logo-buffer',
      title: '角色管理',
      // access: ['sys:role:edit']
    },
    component: Main,
    children: [
      {
        path: 'role_management',
        name: 'role_management',
        meta: {
          icon: 'md-trending-up',
          title: '角色管理',
          // access: ['sys:role']
        },
        component: () => import('@/view/components/role/role.vue')
      },
      {
        path: 'role_detail',
        name: 'role_detail',
        meta: {
          icon: 'md-trending-up',
          title: '角色管理detail',
          hideInMenu: true
        },
        component: () => import('@/view/components/role/role-detail.vue')
      }
    ]
  },
  {
    path:'/menu',
    name:'menu',
    meta: {
      icon: '_qq',
      title: '菜单'
    },
    component: Main,
    children: [
      {
        path: 'manageMenu',
        name: 'manageMenu',
        meta: {
          icon: '_qq',
          title: '管理菜单'
        },
        component: () => import('@/view/canedit_table.vue')
      },
      {
        path: 'batchManagerMenu/export',
        name: 'batchManagerMenu/export',
        meta: {
          icon: '_qq',
          title: '批量导出菜单'
        },
        component: () => import('@/view/batchManagerMenu.vue')
      },
      {
        path: 'batchManagerMenu/import',
        name: 'batchManagerMenu/import',
        meta: {
          icon: '_qq',
          title: '批量导入菜单'
        },
        component: () => import('@/view/batchManagerImport.vue')
      }
    ]
  },
  {
    path: '/log',
    name: 'log',
    meta: {
      icon: 'logo-buffer',
      title: '日志',
    },
    children: [
      {
        path: '/log',
        name: 'log',
        meta: {
          icon: 'logo-buffer',
          title: '日志管理',
        },
        component: () => import('@/view/LogManager.vue')
      }
    ],
    component: Main
  },
  {
    path: '/SSO',
    name: 'SSO',
    meta: {
      icon: 'logo-buffer',
      title: '单点登录',
    },
    children: [
      {
        path: '/sso',
        name: 'sso',
        meta: {
          icon: 'logo-buffer',
          title: '单点登录',
        },
        component: () => import('@/view/components/SSO/SSO.vue')
      }
    ],
    component: Main
  },
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/500.vue')
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/404.vue')
  }
]

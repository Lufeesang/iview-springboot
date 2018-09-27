
export default {
  state: {
    detailRole: {
      roleId: null,
      roleName: '',
      roleSign: '',
      parentId: null,
      userIdCreate: null,
      remark: '',
      gmtCreate: '',
      gmtModified: ''
    }
  },
  mutations: {
    setRoleId (state, roleId) {
      state.detailRole['roleId'] = roleId
    },
    setRoleName (state, roleName) {
      state.detailRole['roleName'] = roleName
    },
    setRoleSign (state, roleSign) {
      state.detailRole['roleSign'] = roleSign
    },
    setParentId (state, parentId) {
      state.detailRole['parentId'] = parentId
    },
    setUserIdCreate (state, userIdCreate) {
      state.detailRole['userIdCreate'] = userIdCreate
    },
    setRemark (state, remark) {
      state.detailRole['remark'] = remark
    },
    setGmtCreate (state, gmtCreate) {
      state.detailRole['gmtCreate'] = gmtCreate
    },
    setGmtModified (state, gmtModified) {
      state.detailRole['gmtModified'] = gmtModified
    }
  },
  actions: {
    setRole ({ commit }, role) {
      commit('setRoleId', role['roleId'])
      commit('setRoleName', role['roleName'])
      commit('setRoleSign', role['roleSign'])
      commit('setParentId', role['parentId'])
      commit('setUserIdCreate', role['userIdCreate'])
      commit('setRemark', role['remark'])
      commit('setGmtCreate', role['gmtCreate'])
      commit('setGmtModified', role['gmtModified'])
    }
  }
}

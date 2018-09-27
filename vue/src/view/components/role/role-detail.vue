<template>
  <div>
    <Row>
      <Tabs type="card">
        <TabPane label="信息修改">
          <Card>
            <Row type="flex" justify="end" align="middle" style="margin: 10px 0;">
              角色编号：{{ currentRole.roleId }}
            </Row>
            <Row type="flex" justify="start" align="middle" style="margin: 10px 0;">
              <i-col span="3">
                角色名称：
              </i-col>
              <i-col span="3">
                <Input clearable placeholder="角色名称" v-model="updateData.roleName" style="width: 200px"/>
              </i-col>
              <i-col offset="3" span="2">
                角色标识：
              </i-col>
              <i-col span="6">
                <Input clearable placeholder="角色标识" v-model="updateData.roleSign" style="width: 200px"/>
              </i-col>
            </Row>
            <Row type="flex" justify="start" align="top" style="margin: 10px 0;">
              <i-col span="2">
                注释：
              </i-col>
              <i-col span="8">
                <Input type="textarea" :autosize="{minRows: 5,maxRows: 5}" placeholder="注释" v-model="updateData.remark"/>
              </i-col>
            </Row>
            <Row style="margin: 10px 0;">
              <i-col offset="20" span="2">
                <Button type="primary" @click="handleUpdateRoleReset">重置</Button>
              </i-col>
              <i-col span="2">
                <Poptip confirm title="确定要保存信息吗？" @on-ok="handleUpdateRoleSubmit">
                  <Button type="primary">提交</Button>
                </Poptip>
              </i-col>
            </Row>
          </Card>
        </TabPane>
        <TabPane label="人员分配">
          <Card>
            <Row type="flex" justify="end" align="middle" style="margin: 10px 0;">
              角色编号：{{ currentRole.roleId }}
            </Row>
            <Row>
              <i-col span="24">
                <Form ref="userSearchForm" :model="userSearchKeys" :rules="userSearchRules" :label-width="80">
                  <Row>
                    <i-col span="4">
                      <FormItem label="人员ID:" prop="userId">
                        <Input v-model="userSearchKeys['userId']"/>
                      </FormItem>
                    </i-col>
                    <i-col offset="2" span="4">
                      <FormItem label="ID区间:" prop="userIdMin">
                        <Input v-model="userSearchKeys['userIdMin']"/>
                      </FormItem>
                    </i-col>
                    <i-col span="3">
                      <FormItem label="~" :label-width="30" prop="userIdMax">
                        <Input v-model="userSearchKeys['userIdMax']"/>
                      </FormItem>
                    </i-col>
                    <i-col offset="2" span="4">
                      <FormItem label="人员姓名:" prop="userName" type="string">
                        <Input v-model="userSearchKeys['userName']"/>
                      </FormItem>
                    </i-col>
                  </Row>
                  <Row>
                    <i-col span="4">
                      <FormItem label="部门:" prop="department">
                        <!-- <Input v-model="userSearchKeys['department']"/> -->
                        <Cascader :data="cascadeData" v-model="userSearchKeys['department']"></Cascader>
                        <!-- <Select v-model="userSearchKeys['department']" filterable clearable style="width:200px">
                          <Option v-for="item in positionKeysList" :key="item.postfix" :label="item.label" :value="item.postfix">
                            <span>{{ item.label }}</span>
                            <span style="float:right;color:#ccc">{{ item.postfix }}</span>
                          </Option>
                        </Select> -->
                      </FormItem>
                    </i-col>
                    <i-col offset="2" span="4">
                      <FormItem label="人员状态:" prop="userStatus">
                        <Select v-model="userSearchKeys['userStatus']" clearable>
                          <Option v-for="item in userStatusSelectList" :value="item.value" :key="item.value">
                            {{ item.label }}
                          </Option>
                        </Select>
                      </FormItem>
                    </i-col>
                  </Row>
                </Form>
              </i-col>
            </Row>
            <Row type="flex" justify="start" style="margin: 10px 0;">
              <i-col offset="20" span="2">
                <Button type="primary" @click="handleUserAssignSearchKeyReset">重置</Button>
              </i-col>
              <i-col span="2">
                <Button type="primary" @click="handleUserAssignSearch">查询</Button>
              </i-col>
            </Row>
          </Card>
          <Card>
            <Row type="flex" justify="center" align="middle">
              <i-col span="11">
                <!-- <Table @on-selection-change="handleUserAssignLeftTableSelectedChanged" :columns="assignedColumns" :data="showAssignedUser" border height="400">
                </Table> -->
                <Table @on-selection-change="handleUserAssignRightTableSelectedChanged" :columns="unassignedColumns" :data="showUnassignedUser" border height="400">
                </Table>
              </i-col>
              <i-col span="2">
                <Row type="flex" justify="center" align="middle" style="margin: 50px 0;">
                  <Button type="primary" @click="handleUserAssignRightAllocate">&gt;&gt;</Button>
                </Row>
                <Row type="flex" justify="center" align="middle" style="margin: 50px 0;">
                  <Button type="primary" @click="handleUserAssignLeftAllocate">&lt;&lt;</Button>
                </Row>
              </i-col>
              <i-col span="11">
                <!-- <Table @on-selection-change="handleUserAssignRightTableSelectedChanged" :columns="unassignedColumns" :data="showUnassignedUser" border height="400">
                </Table> -->
                <Table @on-selection-change="handleUserAssignLeftTableSelectedChanged" :columns="assignedColumns" :data="showAssignedUser" border height="400">
                </Table>
              </i-col>
            </Row>
            <Row type="flex" justify="end" align="middle" style="margin: 10px 0;">
              <i-col offset="20" span="2">
                <Button type="primary" @click="handleUserAssignReset">重置</Button>
              </i-col>
              <i-col span="2">
                <Poptip confirm title="确定要保存信息吗？" @on-ok="handleUserAssignSave">
                  <Button type="primary">保存</Button>
                </Poptip>
              </i-col>
            </Row>
          </Card>
        </TabPane>
        <TabPane label="菜单分配">
          <Card>
            <Row type="flex" justify="end" align="middle" style="margin: 10px 0;">
              角色编号：{{ currentRole.roleId }}
            </Row>
            <Row type="flex" align="middle" style="margin: 10px 0;">
              <i-col offset="0" span="1">
                <Select v-model="positionKey" filterable clearable style="width:200px">
                  <Option v-for="item in positionKeysList" :key="item.postfix" :label="item.label" :value="item.postfix">
                    <span>{{ item.label }}</span>
                    <span style="float:right;color:#ccc">{{ item.postfix }}</span>
                  </Option>
                </Select>
              </i-col>
              <i-col offset="4" span="3">
                <Button type="primary" @click="handleMenuAssignPosition">定位</Button>
              </i-col>
            </Row>
            <Row type="flex" justify="start" align="middle">
              <i-col offset="0" span="6">
                <Card>
                  <p slot="title" class="s1">
                    权限列表
                  </p>
                  <Scroll height="310">
                    <Row justify="start">
                      <Tree ref="tree" :data="treeData" show-checkbox @on-check-change="handleMenuAssignChecked"></Tree>
                    </Row>
                  </Scroll>
                </Card>
                <Row></Row>
              </i-col>
            </Row>
            <Row type="flex" justify="start" align="middle" style="margin: 10px 0;">
              <i-col offset="0" span="4">
                <Button type="primary" @click="handleMenuAssignReset">重置</Button>
              </i-col>
              <i-col span="2">
                <Poptip confirm title="确定要保存信息吗？" @on-ok="handleMenuAssignSave">
                  <Button type="primary">保存</Button>
                </Poptip>
              </i-col>
            </Row>
          </Card>
        </TabPane>
      </Tabs>
    </Row>
  </div>
</template>

<script>
/* eslint-disable */
import {linearToTrees, filterTreesToLinearStructure, filterLinearStructure, updateLinear, deleteLinear, linearToLinear} from './util.js'
import {listMenu} from './../../../api/menu.js'
import {updateRole} from './../../../api/role.js'
import {updateRoleMenu, listRoleMenu} from './../../../api/role-menu.js'
import {listUser} from './../../../api/_user.js'
import {listdept} from './../../../api/dept.js'
import {listRoleUser, updateRoleUser} from './../../../api/role-user.js'
// import menuList from './menu.json'
import checkedMenuIds from './menu-role.json'
export default {
  data () {
    const numberSectionValidatorMin = (rule, value, callback) => {
      let userIdMax = this.userSearchKeys['userIdMax']
      let userIdMin = value
      if (userIdMin !== '' && userIdMax !== '') {
        userIdMax = parseInt(this.userSearchKeys['userIdMax'])
        userIdMin = parseInt(value)
        if (userIdMin <= userIdMax) {
          callback()
        } else {
          callback(new Error('最小值必须小于最大值!'))
        }
      } else {
        callback()
      }
    }
    const numberSectionValidatorMax = (rule, value, callback) => {
      let userIdMin = this.userSearchKeys['userIdMin']
      let userIdMax = value
      this.$refs.userSearchForm.validateField('userIdMin')
      if (userIdMin !== '' && userIdMax !== '') {
        userIdMin = parseInt(this.userSearchKeys['userIdMin'])
        userIdMax = parseInt(value)
        if (userIdMin <= userIdMax) {
          callback()
        } else {
          callback(new Error('最小值必须小于最大值!'))
        }
      } else {
        callback()
      }
    }
    const menuListCheckedRerender = (menuList, checkedMenuIds) => {
      this.treeData = []
      menuList.map((val, index) => {
        val['children'] = []
        val['title'] = val.name
        val['expand'] = false
        val['checked'] = false
        val['selected'] = false
      })
      let checkedMenuList = []
      filterLinearStructure(menuList, checkedMenuList, [{
        mapList: checkedMenuIds,
        key: 'menu_id',
        f: (v1, v2) => {
          return v1 === v2
        }
      }])
      checkedMenuList.map((val, index) => {
        val['checked'] = true
      })
      updateLinear(checkedMenuList, menuList, 'menu_id')
    }
    const showUserListRefresh = (userList, assignedUser, unassignedUser, showAssignedUser, showUnassignedUser, showConditions) => {
      let newShowAssignedUser = []
      let newShowUnassignedUser = []
      filterLinearStructure(assignedUser, newShowAssignedUser, showConditions)
      filterLinearStructure(unassignedUser, newShowUnassignedUser, showConditions)
      showAssignedUser.splice(0, showAssignedUser.length)
      showUnassignedUser.splice(0, showUnassignedUser.length)
      newShowAssignedUser.map((v, i) => {
        showAssignedUser.push(v)
      })
      newShowUnassignedUser.map((v, i) => {
        showUnassignedUser.push(v)
      })
    }
    return {
      currentRole: {},
      positionKey: '',
      positionKeysList: [],
      deptList: [],
      menuListCheckedRerender: menuListCheckedRerender,
      showUserListRefresh: showUserListRefresh,
      checkedMenuIds: [],
      menuList: [],
      treeData: [], 
      userStatusSelectList: [
        {
          value: 0,
          label: '在职'
        },
        {
          value: 1,
          label: '离职'
        }
      ],
      userSearchKeys: {},
      userSearchRules: {
        userId: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入数字',
            trigger: 'change'
          }
        ],
        userIdMin: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入数字',
            trigger: 'change'
          },
          {
            validator: numberSectionValidatorMin,
            trigger: 'change'
          }
        ],
        userIdMax: [
          {
            pattern: /^[0-9]+$/,
            message: '请输入数字',
            trigger: 'change'
          },
          {
            validator: numberSectionValidatorMax,
            trigger: 'change'
          }
        ]
      },
      updateData: {},
      assignedColumns: [
        {
          type: 'selection',
          width: 50,
          align: 'cnter'
        },
        {
          title: '已分配人员',
          align: 'center',
          children: [
            {
              title: '人员ID',
              key: 'userId',
              align: 'center',
              sortable: true,
              width: 105
            },
            {
              title: '人员姓名',
              key: 'name',
              align: 'center',
              sortable: true,
              width: 105
            },
            {
              title: '部门',
              key: 'dept',
              align: 'center',
              sortable: true,
              width: 105
            },
            {
              title: '状态',
              key: 'statusLabel',
              align: 'center',
              sortable: true,
              width: 100
            }
          ]
        }
      ],
      unassignedColumns: [
        {
          type: 'selection',
          width: 50,
          align: 'cnter'
        },
        {
          title: '未分配人员',
          align: 'center',
          children: [
            {
              title: '人员ID',
              key: 'userId',
              align: 'center',
              sortable: true,
              width: 105
            },
            {
              title: '人员姓名',
              key: 'name',
              align: 'center',
              sortable: true,
              width: 105
            },
            {
              title: '部门',
              key: 'dept',
              align: 'center',
              sortable: true,
              width: 105
            },
            {
              title: '状态',
              key: 'statusLabel',
              align: 'center',
              sortable: true,
              width: 100
            }
          ]
        }
      ],
      originalAssignedUser: [],
      originalUnassignedUser: [],
      assignedUser: [],
      unassignedUser: [],
      showAssignedUser: [],
      showUnassignedUser: [],
      //注意：左右替换了，left其实是右边选中的项
      leftSelectedItems: [],
      rightSelectedItems: [],
      userList: [],
      showConditions: [],
      userStatusMap: [
        '在职','离职'
      ],
      deptIdNameMap: {},
      cascadeData: [],
      // cascadeValue: []
    }
  },
  methods: {
    /*
    * 事件响应: handleUpdateRoleReset
    * 功能: 信息修改分页，重置事件响应
    */
    handleUpdateRoleReset () {
      this.updateData = {
        roleName: this.currentRole['roleName'],
        roleSign: this.currentRole['roleSign'],
        remark: this.currentRole['remark']
      }
    },
    /*
    * 事件响应: handleUpdateRoleSubmit
    * 功能: 信息修改分页，提交事件响应
    */
    handleUpdateRoleSubmit () {
      let requestParams = {}
      requestParams['roleId'] = this.currentRole['roleId']
      requestParams['roleName'] = this.updateData['roleName']
      requestParams['roleSign'] = this.updateData['roleSign']
      requestParams['remark'] = this.updateData['remark']
      updateRole(requestParams).then((r) => {
        if (r.data['code'] === 0) {
          this.$store.commit('setRoleName', this.updateData['roleName'])
          this.$store.commit('setRoleSign', this.updateData['roleSign'])
          this.$store.commit('setRemark', this.updateData['remark'])
          localStorage.setItem('currentRole', JSON.stringify(this.loadCurrentRole))
          this.$Message.success('操作成功')
        } else {
          this.$Message.error('操作失败')
        }
      }, (e) => {
        this.$Message.error('请求失败')
        console.log(e)
      })
    },
    /*
    * 事件响应: handleUserAssignSearchKeyReset
    * 功能: 人员分配分页，查询条件重置事件响应
    */
    handleUserAssignSearchKeyReset () {
      this.userSearchKeys = {
        userId: '',
        userIdMin: '',
        userIdMax: '',
        department: [],
        userName: '',
        userStatus: ''
      }
    },
    /*
    * 事件响应: handleUserAssignSearch
    * 功能: 人员分配分页，查询事件响应
    */
    handleUserAssignSearch () {
      // console.log(userSearchKeys)
      let conditions = []
      let condition
      let userSearchKeys = this.userSearchKeys
      if (userSearchKeys['userId'] !== '') {
        let mapList = []
        mapList.push(parseInt(userSearchKeys['userId']))
        condition = {
          mapList: mapList,
          key: 'userId',
          f: (v1, v2) => {
            if (v1 === v2) {
              return true
            } else {
              return false
            }
          }
        }
        conditions.push(condition)
      }
      if (userSearchKeys['userIdMin'] !== '') {
        let mapList = []
        mapList.push(parseInt(userSearchKeys['userIdMin']))
        condition = {
          mapList: mapList,
          key: 'userId',
          f: (v1, v2) => {
            if (v1 >= v2) {
              return true
            } else {
              return false
            }
          }
        }
        conditions.push(condition)
      }
      if (userSearchKeys['userIdMax'] !== '') {
        let mapList = []
        mapList.push(parseInt(userSearchKeys['userIdMax']))
        condition = {
          mapList: mapList,
          key: 'userId',
          f: (v1, v2) => {
            if (v1 <= v2) {
              return true
            } else {
              return false
            }
          }
        }
        conditions.push(condition)
      }
      if (userSearchKeys['department'].length !== 0) {
        let mapList = []
        userSearchKeys['department'].map((v, i) => {
          mapList.push(v)
        })
        // mapList.push(userSearchKeys['department'])
        condition = {
          mapList: mapList,
          key: 'dept',
          f: (v1, v2) => {
            if (v1 === v2) { // 搜索方式待定
              return true
            } else {
              return false
            }
          }
        }
        conditions.push(condition)
      }
      if (userSearchKeys['userName'] !== '') {
        let mapList = []
        mapList.push(userSearchKeys['userName'])
        condition = {
          mapList: mapList,
          key: 'name',
          f: (v1, v2) => {
            if (v1 !== undefined && v1.indexOf(v2) !== -1) {
              return true
            } else {
              return false
            }
          }
        }
        conditions.push(condition)
      }
      if (userSearchKeys['userStatus'] !== '' && userSearchKeys['userStatus'] !== undefined) {
        let mapList = []
        mapList.push(parseInt(userSearchKeys['userStatus']))
        condition = {
          mapList: mapList,
          key: 'status',
          f: (v1, v2) => {
            if (v1 === v2) {
              return true
            } else {
              return false
            }
          }
        }
        conditions.push(condition)
      }
      console.log(conditions)
      this.showConditions = conditions
      this.showUserListRefresh(this.userList, this.assignedUser, this.unassignedUser, this.showAssignedUser, this.showUnassignedUser, this.showConditions)
      // console.log(this.assignedUser)
      // console.log(this.unassignedUser)
      // console.log(userSearchKeys['userStatus'])
      console.log(this.userList)
      console.log(this.deptIdNameMap)
    },
    /*
    * 事件响应: handleUserAssignLeftTableSelectedChanged
    * 功能: 人员分配分页，左表格选项变化事件响应
    */
    handleUserAssignLeftTableSelectedChanged (selection) {
      this.leftSelectedItems.splice(0, this.leftSelectedItems.length)
      selection.map((val, i) => {
        this.leftSelectedItems.push(val)
      })
      // console.log(this.leftSelectedItems)
    },
    /*
    * 事件响应: handleUserAssignRightTableSelectedChanged
    * 功能: 人员分配分页，左表格选项变化事件响应
    */
    handleUserAssignRightTableSelectedChanged (selection) {
      this.rightSelectedItems.splice(0, this.rightSelectedItems.length)
      selection.map((val, i) => {
        this.rightSelectedItems.push(val)
      })
      // console.log(this.rightSelectedItems)
    },
    /*
    * 事件响应: handleRightAllocate
    * 功能: 人员分配分页，>>事件响应
    */
    handleUserAssignRightAllocate () {
      linearToLinear(this.rightSelectedItems, this.assignedUser)
      deleteLinear(this.unassignedUser, this.rightSelectedItems, 'userId')
      this.showUserListRefresh(this.userList, this.assignedUser, this.unassignedUser, this.showAssignedUser, this.showUnassignedUser, this.showConditions)
      this.rightSelectedItems.splice(0, this.rightSelectedItems.length)
    },
    /*
    * 事件响应: handleRightAllocate
    * 功能: 人员分配分页，<<事件响应
    */
    handleUserAssignLeftAllocate () {
      linearToLinear(this.leftSelectedItems, this.unassignedUser)
      deleteLinear(this.assignedUser, this.leftSelectedItems, 'userId')
      this.showUserListRefresh(this.userList, this.assignedUser, this.unassignedUser, this.showAssignedUser, this.showUnassignedUser, this.showConditions)
      this.leftSelectedItems.splice(0, this.leftSelectedItems.length)
    },
    /*
    * 事件响应: handleUserAssignReset
    * 功能: 人员分配分页，分配重置事件响应
    */
    handleUserAssignReset () {
      this.assignedUser.splice(0, this.assignedUser.length)
      this.unassignedUser.splice(0, this.unassignedUser.length)
      this.originalAssignedUser.map((val, i) => {
        this.assignedUser.push(val)
      })
      this.originalUnassignedUser.map((val, i) => {
        this.unassignedUser.push(val)
      })
      this.showUserListRefresh(this.userList, this.assignedUser, this.unassignedUser, this.showAssignedUser, this.showUnassignedUser, this.showConditions)
    },
    /*
    * 事件响应: handleUserAssignSave
    * 功能: 人员分配分页，分配提交事件响应
    */
    handleUserAssignSave () {
      let currentRole = this.currentRole
      if (currentRole['roleId'] !== null && currentRole['roleId'] !== undefined) {
        let data = {}
        data['roleId'] = currentRole['roleId']
        let userIds = []
        this.assignedUser.map((val ,i) => {
          userIds.push(val['userId'])
        })
        data['userIds'] = userIds
        updateRoleUser(data).then((r) => {
          if (r.data['code'] === 0) {
            this.originalAssignedUser.splice(0, this.originalAssignedUser.length)
            this.originalUnassignedUser.splice(0, this.originalUnassignedUser.length)
            this.assignedUser.map((val ,i) => {
              this.originalAssignedUser.push(val)
            })
            this.unassignedUser.map((val ,i) => {
              this.originalUnassignedUser.push(val)
            })
            this.showUserListRefresh(this.userList, this.assignedUser, this.unassignedUser, this.showAssignedUser, this.showUnassignedUser, this.showConditions)
            this.$Message.success('操作成功')
          } else {
            this.$Message.error('操作失败')
          }
        }, (e) => {
          console.log(e)
          this.$Message.error('请求失败')
        })
      }
    },
    /*
    * 事件响应: handleMenuAssignPosition
    * 功能: 菜单分配分页，定位功能响应
    */
    handleMenuAssignPosition () {
      let positionKey = this.positionKey
      console.log(this.treeData)
      console.log(this.positionKey)
      if (positionKey !== '' && positionKey !== undefined) {
        this.menuListCheckedRerender(this.menuList, this.checkedMenuIds)
        console.log(this.menuList)
        console.log(this.checkedMenuIds)
        let flag = true
        let flag2 = false
        let first = true
        while (flag) {
          flag2 = false
          this.menuList.map((val, i) => {
            if (val['menu_id'] === positionKey) {
              val['expand'] = true
              if (first) {
                val['selected'] = true
                first = false
              }
              positionKey = val['parent_id']
              flag2 = true
            }
          })
          if (flag2) {
            continue
          }
          flag = false
        }
        linearToTrees(this.menuList, this.treeData, 'parent_id', 'menu_id', 'children')
      }
    },
    /*
    * 事件响应: handleMenuAssignChecked
    * 功能: 菜单分配分页，树形控件checked事件响应
    */
    handleMenuAssignChecked () {
      // let selectedItems = this.$refs.tree.getCheckedNodes()
      // console.log(selectedItems)
    },
    /*
    * 事件响应: handleMenuAssignSave
    * 功能: 菜单分配分页，保存事件响应
    */
    handleMenuAssignSave () {
      let currentRole = this.currentRole
      if (currentRole['roleId'] !== null && currentRole['roleId'] !== undefined) {
        let selectedItems = this.$refs.tree.getCheckedNodes()
        let requestParams = []
        selectedItems.map((val, index) => {
          requestParams.push(val['menu_id'])
        })
        let requestData = {
          roleId: currentRole['roleId'],
          menuIds: requestParams
        }
        updateRoleMenu(requestData).then((r) => {
          console.log(r)
          if (false || r.data['code'] === 0) {
            console.log(requestParams)
            this.checkedMenuIds = requestParams
            this.menuListCheckedRerender(this.menuList, this.checkedMenuIds)
            linearToTrees(this.menuList, this.treeData, 'parent_id', 'menu_id', 'children')
            this.$Message.success('操作成功')
          } else {
            this.$Message.error('操作失败')
          }
        }, (e) => {
          console.log(e)
          this.$Message.error('请求失败')
        })
      }
    },
    /*
    * 事件响应: handleMenuAssignReset
    * 功能: 菜单分配分页，重置事件响应
    */
    handleMenuAssignReset () {
      this.treeData = []
      this.menuListCheckedRerender(this.menuList, this.checkedMenuIds)
      linearToTrees(this.menuList, this.treeData, 'parent_id', 'menu_id', 'children')
    }
  },
  computed: {
    loadCurrentRole () {
      return this.$store.state.role.detailRole
    }
  },
  beforeMount () {
    if (this.loadCurrentRole['roleId'] === null || this.loadCurrentRole['roleId'] === undefined || this.loadCurrentRole['roleId'] === '') {
      this.currentRole = JSON.parse(localStorage.getItem("currentRole"))
      this.$store.dispatch('setRole', this.currentRole)
    } else {
      this.currentRole = this.loadCurrentRole
      localStorage.setItem('currentRole', JSON.stringify(this.loadCurrentRole))
    }
    if (this.currentRole['roleId'] === null || this.currentRole['roleId'] === undefined || this.currentRole['roleId'] === '') {
      this.$router.push('/role/role_management')
    }
  },
  mounted () {
    this.handleUpdateRoleReset()
    this.handleUserAssignSearchKeyReset()
    listdept(null).then((r) => {
      // console.log(r.data)
      this.deptList = r.data
      this.deptIdNameMap = {}
      this.deptList.map((v, i) => {
        this.deptIdNameMap['m' + v['id'] + ''] = v['title']
        v['children'] = []
        v['label'] = v['title']
        v['value'] = v['title']
      })
      // console.log(this.deptList)
      linearToTrees(this.deptList, this.cascadeData, 'pid', 'id', 'children')
      // console.log(this.cascadeData)
      listUser({}).then((r) => {
        // console.log('ListUser')
        // console.log(r.data)
        r.data.map((val, i) => {
          let temp = {}
          temp['userId'] = val['userId']
          temp['name'] = val['name']
          temp['deptId'] = val['deptId']
          temp['dept'] = this.deptIdNameMap['m' + val['deptId'] + '']
          temp['status'] = val['status']
          temp['statusLabel'] = this.userStatusMap[val['status']]
          this.userList.push(temp)
        })
        // console.log(this.userList)
        listRoleUser({
          roleId: this.currentRole['roleId']
        }).then((r) => {
          // console.log('ListRoleUser')
          // console.log(r.data)
          let flag = false
          this.userList.map((val ,i) => {
            flag = false
            r.data.map((val, j) => {
              if (val === this.userList[i]['userId']) {
                flag = true
              }
            })
            if (flag) {
              this.assignedUser.push(this.userList[i])
              this.originalAssignedUser.push(this.userList[i])
            } else {
              this.unassignedUser.push(this.userList[i])
              this.originalUnassignedUser.push(this.userList[i])
            }
          })
          this.showUserListRefresh(this.userList, this.assignedUser, this.unassignedUser, this.showAssignedUser, this.showUnassignedUser, [])
        }, (e) => {
          this.$Message.error('角色-人员列表,请求失败')
          console.log(e)
        })
      }, (e) => {
        this.$Message.error('人员列表,请求失败')
        console.log(e)
      })
    }, (e) => {
      this.$Message.error('部门列表,请求失败')
      console.log(e)
    })
    listMenu('').then((r) => {
      // console.log(r)
      let copyMenuList = JSON.parse(JSON.stringify(r.data))
      copyMenuList.map((val, index) => {
        val['children'] = []
        val['title'] = val.name
        val['expand'] = true
        // val['selected'] = false
      })
      this.menuList = copyMenuList
      this.positionKeysList = []
      this.menuList.map((val, i) => {
        this.positionKeysList.splice(0, 0, {
          label: val['name'],
          postfix: parseInt(val['menu_id'])
        })
      })
      // console.log(copyMenuList)
      linearToTrees(copyMenuList, this.treeData, 'parent_id', 'menu_id', 'children')
      // console.log(this.treeData)
      // this.$Message.success('操作成功')
      let currentRole = this.currentRole
      if (currentRole['roleId'] !== null && currentRole['roleId'] !== undefined) {
        listRoleMenu({
          roleId: currentRole['roleId']
        }).then((r) => {
          // console.log(r)
          this.checkedMenuIds = r.data
          // console.log(this.menuList)
          this.menuListCheckedRerender(this.menuList, this.checkedMenuIds)
          linearToTrees(this.menuList, this.treeData, 'parent_id', 'menu_id', 'children')
        }, (e) => {
          console.log(e)
        })
      }
    }, (e) => {
      this.$Message.error('请求失败')
      console.log(e)
    })
  }
}
</script>

<style>
</style>

<template>
    <div>
      <Row>
        <Col span='4'>
        <Card>
          <Row>
            <p>选择部门</p>
            <Button type="primary" @click="allUser">所有用户</Button>
          </Row>
          <Tree :data='data1' @on-select-change="selectTree"></Tree>
        </Card>
        </Col>
        <Col span='20'>
          <Row>
            <Row>
              <Form inline :label-width="70">
                <Form-item label="ID" prop="id">
                  <Input type="text" v-model="searchId" clearable placeholder="请输入ID" style="width: 100px"/>
                </Form-item>
                <Form-item label="用户名" prop="username">
                  <Input type="text" v-model="searchUserName" clearable placeholder="请输入用户名" style="width: 100px"/>
                </Form-item>
                <Form-item label="姓名" prop="name">
                  <Input type="text" v-model="searchName" clearable placeholder="请输入姓名" style="width: 100px"/>
                </Form-item>
                <FormItem label="部门选择">
                  <Cascader :data="data1" v-model="deptValue" @on-change="" style="width: 300px"></Cascader>
                </FormItem>
                <Button type="primary" icon="md-search" @click="searchUser">查询</Button>
              </Form>
            </Row>
          </Row>
          <Row>
              <Card>
                <Row>
                  <Col span="3">
                    <Button type="primary" icon="md-add" @click="addUser = true">添加用户</Button>
                  </Col>
                  <Col span="3">
                    <Button type="primary" icon="md-refresh" @click="refresh">刷新数据</Button>
                  </Col>
                  <Col span="4">
                    <Button type="primary" icon="md-trash"  @click="batchRemove">批量删除数据</Button>
                  </Col>
                  <Col span="4">
                    <Button type="primary" icon="md-arrow-down"  @click="getAllUser">批量导出数据</Button>
                  </Col>
                  <Col span="4">
                    <!-- <Upload
                      :on-success = "importData"
                      :format = "['xls','xlsx']"
                      :on-format-error = "errorFormat"
                      action="http://localhost:80/userUpload/doUpload">
                      <Button type="primary" icon="md-arrow-up">批量导入数据</Button>
                    </Upload> -->
                    <Upload action="" :before-upload="handleBatchImport">
                      <Button type="primary" icon="md-arrow-up">批量导入数据</Button>
                    </Upload>
                    <Modal v-model="importUserModal" @on-ok="handleBatchImportSave" ok-text="保存" title="导入数据" width="720">
                      <Card>
                        <Row style="margin: 10px 0;" justify="center" align="middle">
                          <i-col span="24">
                            <Table :columns="importUserColumn" :data="importUserData" height="300" width="660"></Table>
                          </i-col>
                        </Row>
                      </Card>
                    </Modal>
                  </Col>
                </Row>
              </Card>
              <!-- 添加用户的对话框 -->
              <Modal title="添加用户" v-model="addUser" :styles="{top: '20px'}" @on-ok="handleAddUser">
                   <Row>
                    <i-col span = "24" >
                      <Card>
                        <Form :model="formData" :rules="ruleValidate" label-position="left" :label-width="80">
                            <FormItem label="用户名" prop="inputUserName">
                                <Input v-model="formData.inputUserName" placeholder="请输入用户名"></Input>
                            </FormItem>
                            <FormItem label="姓名" prop="inputName">
                                <Input v-model="formData.inputName" placeholder="请输入姓名"></Input>
                            </FormItem>
                            <FormItem label="性别">
                              <RadioGroup v-model="UserSex" type="button" size="large">
                                <Radio label="男">
                                  <span>男</span>
                                </Radio>
                                <Radio label="女">
                                  <span>女</span>
                                </Radio>
                              </RadioGroup>
                            </FormItem>
                            <FormItem label="密码" prop="inputPassword">
                                <Input v-model="formData.inputPassword" placeholder="请输入密码" type="password"></Input>
                            </FormItem>
                            <FormItem label="邮箱" prop="inputEmail">
                                <Input v-model="formData.inputEmail" placeholder="请输入邮箱" type="email"></Input>
                            </FormItem>
                            <FormItem label="部门选择">
                              <Cascader :data="data1" v-model="deptValue" @on-change=""></Cascader>
                            </FormItem>
                            <FormItem label="手机" prop="inputMobile">
                              <Input v-model="formData.inputMobile" placeholder="请输入手机号"></Input>
                            </FormItem>
                            <FormItem label="省份" prop="inputPrivince">
                              <Input v-model="formData.inputPrivince" placeholder="请输入省份"></Input>
                            </FormItem>
                            <FormItem label="城市" prop="inputCity">
                              <Input v-model="formData.inputCity" placeholder="请输入城市"></Input>
                            </FormItem>
                            <FormItem label="区" prop="inputDistrict">
                              <Input v-model="formData.inputDistrict" placeholder="请输入区"></Input>
                            </FormItem>
                        </Form>
                      </Card>
                    </i-col>
                  </Row>
                 </Modal>
                 <!-- 修改用户信息的对话框 -->
                 <Modal title="修改用户信息" v-model="editUserStatus" :styles="{top: '20px'}" @on-ok="handleEditUser" @on-cancel="cancelEditUser">
                   <Row>
                    <i-col span = "24" >
                      <Card>
                        <Form :model="formData" :rules="ruleValidate" label-position="left" :label-width="80">
                            <FormItem label="用户名" prop="inputUserName">
                                <Input v-model="formData.inputUserName" ></Input>
                            </FormItem>
                            <FormItem label="姓名" prop="inputName">
                                <Input v-model="formData.inputName"></Input>
                            </FormItem>
                            <!-- <FormItem label="密码" prop="inputPassword">
                                <Input v-model="formData.inputPassword" type="password"></Input>
                            </FormItem> -->
                            <FormItem label="邮箱" prop="inputEmail">
                                <Input v-model="formData.inputEmail" type="email"></Input>
                            </FormItem>
                            <FormItem label="性别">
                              <RadioGroup v-model="UserSex" type="button" size="large">
                                <Radio label="男">
                                  <span>男</span>
                                </Radio>
                                <Radio label="女">
                                  <span>女</span>
                                </Radio>
                              </RadioGroup>
                            </FormItem>
                            <FormItem label="部门选择">
                              <Cascader :data="data1" v-model="deptValue"></Cascader>
                            </FormItem>
                        </Form>
                      </Card>
                    </i-col>
                  </Row>
                 </Modal>
                <!-- 修改用户密码的对话框 -->
                <Modal title="修改用户密码" v-model="editUserPasswordStatus" :styles="{top: '20px'}" @on-ok="handleEditUserPassword" @on-cancel="cancelEditUserPassword">
                   <Row>
                    <i-col span = "24" >
                      <Card>
                        <Form :model="formDataPassword" label-position="left" :label-width="80">
                            <FormItem label="原始密码" prop="inputOldPassword">
                              <Input v-model="formDataPassword.inputOldPassword" type="password"></Input>
                            </FormItem>
                            <FormItem label="修改密码" prop="inputNewPassword">
                              <Input v-model="formDataPassword.inputNewPassword" type="password"></Input>
                            </FormItem>
                            <FormItem label="校验密码" prop="inputTwoPassword">
                              <Input v-model="formDataPassword.inputTwoPassword" type="password"></Input>
                            </FormItem>
                        </Form>
                      </Card>
                    </i-col>
                  </Row>
                 </Modal>
                 <!-- 角色分配的对话框 -->
                 <Modal title="角色分配" v-model="showRoleModelStatus" :styles="{top: '20px'}" @on-ok="handleEditUserRole" @on-cancel="cancelEditUserRole">
                   <Row>
                    <i-col span = "24" >
                      <Card>
                        <p>已经分配的角色</p>
                        <Tag v-for="item in UserRoleListName" :key="item"> {{ item }}</Tag>
                        <Divider/>
                        <p>请选择要分配的角色</p>
                        <Tree :data='RoleTreeData' show-checkbox @on-check-change="checkRoleTree"></Tree>
                      </Card>
                    </i-col>
                  </Row>
                 </Modal>
                 <!-- 菜单分配的对话框 -->
                 <Modal title="菜单分配" v-model="showMenuModelStatus" :styles="{top: '20px'}" @on-ok="handleEditUserMenu" @on-cancel="cancelEditUserMenu">
                   <Row>
                    <i-col span = "24" >
                      <Card>
                        <p>已经分配的菜单</p>
                        <Tag v-for="item in UserMenuListName" :key="item"> {{ item }}</Tag>
                        <Divider/>
                        <p>请选择要分配的菜单</p>
                        <Tree :data='MenuTreeData' show-checkbox @on-check-change="checkMenuTree"></Tree>
                      </Card>
                    </i-col>
                  </Row>
                 </Modal>
                 <!-- 删除操作的确认对话框 -->
                 <Modal title="删除确认" v-model="showRemoveModel" :styles="{top: '20px'}" @on-ok="handleRemoveUser">
                   <p>确认删除操作？</p>
                 </Modal>
                 <!-- 批量删除操作的确认对话框 -->
                 <Modal title="批量删除确认" v-model="showBatchRemoveModel" :styles="{top: '20px'}" @on-ok="handleBatchRemoveUser">
                   <p>确认删除 {{ selectUserIdLength }} 条数据？</p>
                 </Modal>
          </Row>
          <Row>
            <Card>
              <Table border :columns='columns2' :data='pageRes' @on-select="selectData" @on-select-cancel="cancelSelectData"></Table>
            </Card>
            <Divider/>
            <!-- <Page :total="countData" @on-change="getPage" show-total /> -->
            <Page :current="currentPageNum" :total="countData" @on-change="getPageData" show-total />
            <Divider/>
          </Row>
        </Col>
        <!-- <Row>
          <Page :total=20 @on-change="getPageData" show-total/>
          <div>{{ pageData }}</div>
        </Row> -->
      </Row>
    </div>
</template>
<script>
// import axios from 'axios'
import {linearToTrees} from './lib/util.js'
export default {
  data () {
    return {
      addUser: false,
      // data1 : 部门树的数据
      data1: [],
      // 表格的表头
      columns2: [
        {
          type: 'selection',
          width: 60,
          align: 'center',
          fixed: 'left'
        },
        {
          title: '用户id',
          key: 'userId',
          width: 120,
          sortable: true
        },
        {
          title: '用户名',
          key: 'username',
          width: 120,
          sortable: true
        },
        {
          title: '姓名',
          key: 'name',
          width: 120,
          sortable: true
        },
        {
          title: '性别',
          key: 'sex',
          width: 120,
          render: (h, params) => {
            if (params.row.sex === 0) {
              return h('div', [
                h('Icon', {
                  props: {
                    size: 24,
                    type: 'md-woman'
                  }
                }),
                h('Button', {
                  props: {
                    type: 'text'
                  },
                },
                '女'
                )
              ])
            } 
            else if (params.row.sex === 1) {
              return h('div', [
                h('Icon', {
                  props: {
                    size: 24,
                    type: 'md-man'
                  }
                }),
                h('Button', {
                  props: {
                    type: 'text'
                  },
                },
                '男'
                )
              ])
            }
          }
        },
        {
          title: '部门名称',
          key: 'deptName',
          width: 120
        },
        {
          title: '电子邮箱',
          key: 'email',
          width: 120
        },
        {
          title: '手机',
          key: 'mobile',
          width: 120
        },
        {
          title: '状态',
          key: 'status',
          width: 120,
          render: (h, params) => {
            if (params.row.status === 1){
              return h('i-switch',  {
                props: {
                  size: 'large',
                  value: true
                },
                style: {
                  // marginRight: '5px'
                },
                on: {
                  'on-change': () =>{
                    console.log('关闭')
                    this.updateUserStatus(params.row)
                  }
                }
              }, 
              [
                h('span', {
                  slot: 'open'
                }, '启用'),
                h('span', {
                  slot: 'close'
                }, '禁用')
              ])
            } else {
              return h('i-switch',  {
                props: {
                  size: 'large',
                  value: false
                },
                style: {
                  // marginRight: '5px'
                },
                on: {
                  'on-change': () =>{
                    console.log('开启')
                    this.updateUserStatus(params.row)
                  }
                }
              }, 
              [
                h('span', {
                  slot: 'open'
                }, '启用'),
                h('span', {
                  slot: 'close'
                }, '禁用')
              ])
            }
          }
        },
        {
          title: '创建时间',
          key: 'gmtCreate',
          width: 120
        },
        {
          title: '修改时间',
          key: 'gmtModified',
          width: 120
        },
        {
          title: '省份',
          key: 'province',
          width: 120
        },
        {
          title: '城市',
          key: 'city',
          width: 120
        },
        {
          title: '区',
          key: 'district',
          width: 120
        },
        // 操作render控件
        {
          title: '操作',
          key: 'action',
          width: 300,
          fixed: 'right',
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h(
                'Button',
                {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.userEdit(params.row)
                    }
                  }
                },
                '编辑'
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.userPasswordEdit(params.row)
                    }
                  }
                },
                '重置密码'
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      // this.userPasswordEdit(params.row)
                      this.userRoleEdit(params.row)
                    }
                  }
                },
                '角色'
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      // this.userPasswordEdit(params.row)
                      this.userMenuEdit(params.row)
                    }
                  }
                },
                '菜单'
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'error',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.remove(params.row)
                    }
                  }
                },
                '删除'
              )
            ])
          }
        }
      ],
      importUserColumn: [
        {
          title: '姓名',
          key: 'name'
        },
        {
          title: '用户名',
          key: 'username'
        },
        {
          title: '部门Id',
          key: 'deptId'
        },
        {
          title: '邮箱',
          key: 'email'
        },
        {
          title: '手机',
          key: 'mobile'
        }
      ],
      // this.res : 人员表格的总数据集
      res: [],
      // pageRes : 人员表格的分页数据集
      pageRes: [],
      // this.newlist : 接收原始的表格数据
      newlist: 'yan',
      // this.TreeNode : 获取已经选择的部门树节点
      TreeNode: [],
      // this.selectDept : 获取已经选择的部门树节点的部门id
      selectDept: [],
      // this.countData : 表格数据的计数
      countData: 0,
      // this.formData : 添加人员信息的表单数据
      formData: {
        inputUserName: '',
        inputName: '',
        inputPassword: '',
        inputDept: '',
        inputEmail: '',
        inputMobile: '',
        inputBirth: '',
        inputAddress: '',
        inputHobby: '',
        inputPrivince: '',
        inputCity: '',
        inputDistrict: ''
      },
      // 修改密码所需的变量
      formDataPassword: {
        inputOldPassword: '',
        inputNewPassword: '',
        inputTwoPassword: ''
      },
      // 测试变量，可注释
      test: [],
      // 添加人员的表单的输入框的规则
      ruleValidate: {
        inputUserName: [
          {
            required: true,
            message: '用户名不能为空！',
            trigger: 'blur'
          }
        ],
        inputName: [
          {
            required: true,
            message: '姓名不能为空！',
            trigger: 'blur'
          }
        ],
        inputPassword: [
          {
            required: true,
            message: '密码不能为空！',
            trigger: 'blur'
          }
        ],
        inputEmail: [
          {
            required: true,
            message: '邮箱不能为空！',
            trigger: 'blur'
          }
        ]
      },
      // 查询用户的Id数据
      searchId: '',
      // 查询用户的用户名数据
      searchUserName: '',
      // 查询用户的姓名数据
      searchName: '',
      // 存取人员信息表格的多选信息
      selectUserData: [],
      // 存取人员信息表格的多选的用户ID 和 长度
      selectUserId: [],
      selectUserIdLength: '',
      // 增加用户时选择的部门Id
      addUserDeptId: '',
      // 编辑用户的对话框的状态
      editUserStatus: false,
      // 编辑用户信息, 进行更新操作所需的 userId
      editUserId: '',
      // 修改用户密码的对话框的状态
      editUserPasswordStatus: false,
      // 修改用户密码时，所需的 userId, username
      editUserPasswordId: '',
      editUserPasswordUserName: '',

      // -------------角色分配 start------------------
      showRoleModelStatus: false,
      Model: [],
      RoleDataList: [],
      RoleTreeData: [],
      EditUserRoleId: '',
      UserRoleList: [],
      UserRoleListName: [],
      UserRoleListId: [],
      // -------------角色分配 end  ------------------

      // -------------菜单分配 start------------------
      showMenuModelStatus: false,
      MenuModel: [],
      RoleModel: [],
      MenuDataList: [],
      MenuTreeData: [],
      // 分配菜单所需要的变量
      EditUserMenuId: '',
      UserMenuList: [],
      UserMenuListName: [],
      UserMenuListId: [],
      // -------------菜单分配 end  ------------------

      // 删除用户对话框显示的状态
      showRemoveModel: false,
      // 删除用户的信息
      removeUserData: [],
      // 批量删除用户对话框显示的状态
      showBatchRemoveModel: false,

      //部门级联选择数据
      deptValue: [],

      // 导入数据
      importRoles: [],
      importUserModal: false,
      importUserData: [],

      // 用户状态选择switch
      UserSex: '',

      // 数据分页
      pageData: [],
      // 
      currentPageNum: 1,

      // 切换数据原的标志位
      flag: 1,

      // 选择部门树的id记录
      deptId: ''
    }
  },
  methods: {
    // 部门树的点击选择事件
    selectTree (v) {
      console.log(v)
      this.TreeNode = []
      this.TreeNode.push(v)
      this.selectDept = this.TreeNode[0][0].id
      console.log(this.selectDept)
      this.getUserListByDeptId(this.selectDept)
    },
    // 部门树的点击选择事件的axios请求
    getUserListByDeptId (id) {
      this.deptId = id
      this.flag = 3
      this.currentPageNum = 1
      this.getUserListByDeptIdRequest(0)
    },
    getUserListByDeptIdRequest (start) {
      // 根据部门Id获取所有的用户列表
      this.$axios({
        method: 'post',
        url: '/personuser/getUserList',
        data: {
          'id': this.deptId,
          'offset': start
        }
      }).then(response => {
        this.pageRes = response.data
        this.changeDeptIdToDeptName(this.pageRes)
        // this.countData = this.pageRes.length
        this.getUserListByDeptIdCount()
      })
    },
    getUserListByDeptIdCount () {
      this.$axios({
        method: 'post',
        url: '/personuser/getUserListByDeptIdNum',
        data: ''
      }).then((r) => {
        this.countData = r.data
      })
    },
    // 获取用户数量
    getCount () {
      this.$axios({
        method: 'get',
        url: '/personuser/getCount',
        data: ''
      }).then((response) => {
        this.countData = response.data
      })
    },
    // 获取所有用户的列表
    getAllUser () {
      // 获取用户列表的数据
      this.$axios({
        method: 'post',
        url: '/personuser/getAllUserList',
        data: {}
      }).then(response => {
        this.res = response.data
        this.handleBatchExport()
        // console.log(this.res)
        // this.changeDeptIdToDeptName(this.res)
        // this.pageRes = this.res.slice(0, 10)
        // this.countData = this.res.length
        // console.log(this.res)
      })
      // getAllUser(this.res).then((r) => {
      //   this.changeDeptIdToDeptName(this.res)
      //   this.pageRes = this.res.slice(0, 10)
      //   this.countData = this.res.length
      // })
    },
    allUser () {
      // 获取用户列表的数据
      this.currentPageNum = 1
      this.$axios({
        method: 'post',
        url: '/personuser/getUserListByPage',
        data: {
          'offset': 0
        }
      }).then((response) => {
        let temp = response.data
        this.changeDeptIdToDeptName(temp)
        this.pageRes = temp
      })
      this.getCount()
      this.flag = 1
    },
    // 增加用户
    handleAddUser () {
      // this.deptValue = ''
      this.addUserDeptId = this.deptValue[this.deptValue.length-1]
      this.$axios({
        method: 'post',
        url: '/personuser/saveUser',
        data: {
          'username': this.formData.inputUserName,
          'name': this.formData.inputName,
          'password': this.formData.inputPassword,
          'deptId': this.addUserDeptId,
          'email': this.formData.inputEmail,
          'mobile': this.formData.inputMobile,
          'privince': this.formData.inputPrivince,
          'city': this.formData.inputCity,
          'district': this.formData.inputDistrict,
          'sex': this.UserSex
        }
      }).then((res) => {
        this.$Message.info(res.data)
        this.allUser()
        this.formData.inputUserName = ''
        this.formData.inputName = ''
        this.formData.inputPassword = ''
        this.formData.inputEmail = ''
        this.addUserDeptId = ''
        this.formData.inputMobile = ''
        this.formData.inputPrivince = ''
        this.formData.inputCity = ''
        this.formData.inputDistrict = ''
        this.deptValue = ''
        this.UserSex = ''
      })
    },
    // 多重条件搜索用户
    searchUser () {
      this.flag = 2
      this.currentPageNum = 1
      this.searchUserRequest(0)
    },
    // 查询请求
    searchUserRequest (start) {
      this.addUserDeptId = this.deptValue[this.deptValue.length-1]
      console.log(this.addUserDeptId)
      this.$axios({
        method: 'post',
        url: '/personuser/searchUser',
        data: {
          'userId': this.searchId,
          'username': this.searchUserName,
          'name': this.searchName,
          'deptId': this.addUserDeptId,
          'offset': start
        }
      }).then(
        (response) => {
          let temp = response.data
          this.changeDeptIdToDeptName(temp)
          this.pageRes = temp
          // this.countData = this.pageRes.length
          // this.deptValue = ''
          this.getSearchUserNum()
        }
      )
    },
    // 获取查询用户的数量
    getSearchUserNum () {
      this.$axios({
        method: 'post',
        url: '/personuser/searchUserNum',
        data: ''
      }).then((r) => {
        this.countData = r.data
      })
    },
    // 刷新数据
    refresh () {
      this.allUser()
    },
    // 页面删除表格的行的操作
    remove (index) {
      console.log(index)
      console.log(index.userId)
      console.log(this.showRemoveModel)
      this.showRemoveModel = true
      console.log(this.showRemoveModel)
      this.removeUserData = index
      console.log(this.removeUserData)
    },
    // 删除用户操作
    handleRemoveUser () {
      this.pageRes.splice(this.removeUserData, 1)
      // this.allUser()
      this.$axios({
        method: 'post',
        url: 'personuser/removeUser',
        data: { 'userId': this.removeUserData.userId }
      }).then(
        (r) => {
          if (r.data === 'OK') {
            this.$Message.info('删除成功')
            this.countData = this.countData - 1
          } else {
            this.$Message.info('删除失败')
          }
        },
        this.getPageData(this.currentPageNum)
      )
    },
    // 获取表格的选择数据
    selectData (selection, row) {
      // console.log(selection)
      // console.log(row)
      this.selectUserData = selection
      // console.log(this.selectUserData)
    },
    // 取消表格的选择事件的响应
    cancelSelectData (selection, row) {
      // console.log(selection)
      // console.log(row)
      this.selectUserData = selection
      // console.log(this.selectUserData)
    },
    // 批量删除数据
    batchRemove () {
      this.showBatchRemoveModel = true
      this.selectUserIdLength = this.selectUserData.length
    },
    handleBatchRemoveUser () {
      // console.log(this.selectUserData)
      for (var i = 0; i < this.selectUserData.length; i++) {
        this.selectUserId.push(this.selectUserData[i].userId)
      }
      console.log(this.selectUserId)
      this.$axios({
        method: 'post',
        url: '/personuser/batchRemoveUser',
        data: {'userIdList': this.selectUserId}
      }).then((r) => {
        this.$Message.info('删除' + r.data + '个数据')
        if(r.data >= 10 && this.currentPageNum !== 1){
          this.getPageData(this.currentPageNum-1)
        } else{
          this.getPageData(this.currentPageNum)
        }
        this.getCount()
      })
      this.selectUserId = []
    },
    // 展示编辑用户信息的对话框
    userEdit (data) {
      // console.log(data)
      this.deptValue = ''
      this.editUserStatus = true
      this.editUserId = data.userId
      // this.switchUserStatus = data.status
      this.formData.inputUserName = data.username
      this.formData.inputName = data.name
      this.formData.inputEmail = data.email
      // this.addUserDeptId = data.deptId
      // console.log('测试：' + data.userId + data.username + data.name + data.email + data.deptId)
    },
    // 处理编辑用户信息的过程 即对话框的确定按钮的操作函数
    handleEditUser () {
      this.addUserDeptId = this.deptValue[this.deptValue.length-1]
      this.$axios({
        method: 'post',
        url: '/personuser/updateUser',
        data: {
          'userId': this.editUserId,
          'username': this.formData.inputUserName,
          'name': this.formData.inputName,
          'deptId': this.addUserDeptId,
          'email': this.formData.inputEmail,
          'sex': this.UserSex
        }
      }).then(
        response => {
          this.$Message.info(response.data)
          this.getPageData(this.currentPageNum)
        }
      )
      this.formData.inputUserName = ''
      this.formData.inputName = ''
      this.formData.inputEmail = ''
      this.addUserDeptId = ''
      this.UserSex = ''
    },
    // 编辑用户信息的对话框 的取消按钮
    cancelEditUser () {
      this.formData.inputUserName = ''
      this.formData.inputName = ''
      this.formData.inputEmail = ''
      this.addUserDeptId = ''
      this.deptValue = []
    },
    // 展示修改用户密码的对话框
    userPasswordEdit (data) {
      this.editUserPasswordId = data.userId
      console.log(data.userId)
      this.editUserPasswordUserName = data.username
      console.log(data.username)
      this.editUserPasswordStatus = true
      console.log('测试1：' + data.userId + data.username + data.name + data.email + data.deptId)
    },
    // 处理修改用户密码的过程 即对话框的确定按钮的操作函数
    handleEditUserPassword () {
      if (this.formDataPassword.inputNewPassword === this.formDataPassword.inputTwoPassword) {
        // console.log('test1')
        this.$axios({
          method: 'post',
          url: '/personuser/updateUserPassword',
          data: {
            'userId': this.editUserPasswordId,
            'username': this.editUserPasswordUserName,
            'oldPassword': this.formDataPassword.inputOldPassword,
            'newPassword': this.formDataPassword.inputNewPassword
          }
        }).then((r) => {
          // console.log('test')
          this.$Message.info(r.data)
        })
      }
      this.formDataPassword.inputOldPassword = ''
      this.formDataPassword.inputNewPassword = ''
      this.formDataPassword.inputTwoPassword = ''
    },
    // 修改用户密码的对话框 的取消按钮
    cancelEditUserPassword () {
      this.formDataPassword.inputOldPassword = ''
      this.formDataPassword.inputNewPassword = ''
      this.formDataPassword.inputTwoPassword = ''
    },
    // 分页操作
    getPage (pageNum) {
      console.log('页码为：' + pageNum)
      console.log('数量为：' + this.countData)
      if (pageNum !== 1) {
        var end = pageNum * 10
        console.log('end: ' + end)
        var start = end - 10
        if (end > this.countData) {
          this.pageRes = this.res.slice(start, this.countData)
        } else {
          this.pageRes = this.res.slice(start, end)
        }
      } else {
        this.pageRes = this.res.slice(0, 10)
      }
    },
    // 导出数据
    exportData () {
      // window.open('http://localhost:80/personuser/exportUser', '_parent')
      this.$axios({
        method: 'post',
        url: '/personuser/exportUser',
        responseType: 'blob'
      }).then(res => {
        if (res.data) {
          let blob = res.data
          let a = document.createElement('a')
          let url = window.URL.createObjectURL(blob)
          let filename = 'person.xls'
          a.href = url
          a.download = filename
          a.click()
          window.URL.revokeObjectURL(url)
        }
      })
    },
    // 前端数据导出
    handleBatchExport () {
      // this.getAllUser()
      require.ensure([], () => {　　　　　　　　
        let { export_json_to_excel } = require('./../../libs/Export2Excel.js')　
        let tHeader = ['userId', 'name', 'username', 'deptId', 'email', 'mobile', 'gmtCreate', 'gmtModified', 'province', 'city', 'district']
        let filterVal = ['userId', 'name', 'username', 'deptId', 'email', 'mobile', 'gmtCreate', 'gmtModified', 'province', 'city', 'district']
        let list = this.res
        let fileName = this.exportFileName
        let formatJson = (filterVal, jsonData) => {
          return jsonData.map(v => filterVal.map(j => v[j]));
        }
        let data = formatJson(filterVal, list)
        // console.log(data)
        export_json_to_excel(tHeader, data, fileName)
      })
    },
    // 导入用户数据
    // importData (uploadResponse) {
    //   if (typeof uploadResponse !== 'number') {
    //     this.$Message.info('上传失败，原因: ' + JSON.stringify(uploadResponse) + '\n' + '我也是醉了')
    //   } else {
    //     this.$Message.info('成功上传，总共上传了: ' + JSON.stringify(uploadResponse) + '条数据')
    //   }
    //   this.allUser()
    // },
    /*
    * 事件响应: handleBatchImport
    * 功能: 批量导入事件响应
    */
    handleBatchImport (file) {
      var _this = this
      // let inputDOM = this.$refs.inputer
      // 通过DOM取文件数据
      // console.log(file)
      file = event.currentTarget.files[0]
      var rABS = false //是否将文件读取为二进制字符串
      var f = file
      var reader = new FileReader()
      //if (!FileReader.prototype.readAsBinaryString) {
      FileReader.prototype.readAsBinaryString = function(f) {
        var binary = ''
        var rABS = false //是否将文件读取为二进制字符串
        var pt = this
        var wb //读取完成的数据
        var outdata
        var reader = new FileReader()
        reader.onload = function(e) {
          var bytes = new Uint8Array(reader.result)
          var length = bytes.byteLength
          for(var i = 0; i < length; i++) {
            binary += String.fromCharCode(bytes[i])
          }
          var XLSX = require('xlsx')
          if(rABS) {
            wb = XLSX.read(btoa(fixdata(binary)), { //手动转化
              type: 'base64'
            })
          } else {
            wb = XLSX.read(binary, {
              type: 'binary'
            })
          }
          outdata = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]) //outdata就是你想要的东西
          // console.log(outdata)
          let temp
          _this.importUserData = []
          outdata.map((v, i) => {
            temp = {}
            // temp['userId'] = outdata[i]['userId']
            temp['name'] = outdata[i]['name']
            temp['username'] = outdata[i]['username']
            temp['deptId'] = outdata[i]['deptId']
            temp['email'] = outdata[i]['email']
            temp['mobile'] = outdata[i]['mobile']
            _this.importUserData.push(temp)
          })
          _this.importUserModal = true
          console.log(_this.importUserData)
        }
        reader.readAsArrayBuffer(f)
      }
      if(rABS) {
        reader.readAsArrayBuffer(f)
      } else {
        reader.readAsBinaryString(f)
      }
    },
    // 保存导入数据
    handleBatchImportSave () {
      console.log(this.importUserData)
      this.$axios({
        method: 'post',
        url: '/personuser/importUser',
        data: {
          'userlist': this.importUserData
        }
      }).then((response) => {
        this.$Message.info("已经导入" + response.data + "条数据")
        this.allUser()
      })
    },
    // errorFormat (file) {
    //   this.$Message.info(JSON.stringify(file) + '格式错误')
    // },
    // deptId 转 部门名称 的方法
    changeDeptIdToDeptName (data) {
      data.map((val, index) => {
        // console.log('val' + val['deptId'])
        for (var i = 0; i < this.newlist.length; i++) {
          if (this.newlist[i].id === val['deptId']) {
            val['deptName'] = this.newlist[i].title
          }
        }
      })
      // console.log(data)
    },
    // 菜单分配对话框
    userMenuEdit (data) {
      this.MenuModel = []
      this.UserMenuList = []
      this.UserMenuListName = []
      this.EditUserMenuId = data.userId
      this.getUserMenuList(data.userId)
      // this.getMenuList()
      this.showMenuModelStatus = true
    },
    // 获取菜单列表
    getMenuList () {
      // this.MenuTreeData = []
      this.$axios({
        method: 'post',
        url: '/personuser/getMenuList',
        data: ''
      }).then((r) => {
        this.MenuDataList = r.data
        this.MenuDataList.map((val, index) => {
          val['title'] = val['name']
          val['expand'] = false
          val['children'] = []
        })
        // linearToTrees(this.MenuDataList, this.MenuTreeData, 'parent_id', 'menu_id', 'children')
      })
    },
    // 处理菜单分配的逻辑
    handleEditUserMenu () {
      this.$axios({
        method: 'post',
        url: '/personUserMenu/updateUserMenu',
        data: {
          'userId': this.EditUserMenuId,
          'MenuModel': this.MenuModel
        }
      }).then((response) => {
        this.$Message.info('已经插入' + response.data + '条数据')
        this.MenuModel = []
      })
    },
    // 取消菜单分配的操作
    cancelEditUserMenu () {
      this.MenuModel = []
      this.UserMenuList = []
      this.UserMenuListName = []
      this.UserMenuListId = []
      this.MenuDataList.map((val, index) => {
        val['checked'] = false
      })
    },
    // 获取某个用户所拥有的菜单权限
    getUserMenuList (userId) {
      this.UserMenuList = []
      this.UserMenuListName = []
      this.$axios({
        method: 'post',
        url: '/personUserMenu/getUserMenu',
        data: {
          'userId': userId
        }
      }).then((response) => {
        // this.UserMenuList = response.data
        // console.log(this.MenuDataList)
        this.UserMenuList = response.data
        this.UserMenuList.map((val, index) => {
          this.UserMenuListId.push(val['menuId'])
        })
        if (this.UserMenuList.length !== 0) {
          this.MenuDataList.map((val, index) => {
            if (this.UserMenuListId.indexOf(val['menu_id']) >= 0) {
              this.UserMenuListName.push(val['name'])
              val['checked'] = true
            }
          })
        }
        console.log(this.UserMenuList)
        console.log(this.UserMenuListName)
        this.MenuTreeData = []
        linearToTrees(this.MenuDataList, this.MenuTreeData, 'parent_id', 'menu_id', 'children')
      })
    },
    // 选择菜单树的事件
    checkMenuTree (v) {
      this.MenuModel = []
      v.map((val, index) => {
        this.MenuModel.push(val['menu_id'])
      })
      // console.log(this.MenuModel)
    },
    // 角色分配对话框
    userRoleEdit (data) {
      this.RoleModel = []
      this.UserRoleList = []
      this.UserRoleListName = []
      this.EditUserRoleId = data.userId
      this.getUserRoleList()
      // this.getRoleList()
      this.showRoleModelStatus = true
    },
    // 获取角色列表
    getRoleList () {
      // this.RoleTreeData = []
      this.$axios({
        method: 'post',
        url: '/personuser/getRoleList',
        data: ''
      }).then((response) => {
        this.RoleDataList = response.data
        this.RoleDataList.map((val, index) => {
          val['title'] = val['roleName']
          val['expand'] = true
          val['children'] = []
        })
        // linearToTrees(this.RoleDataList, this.RoleTreeData, 'parentId', 'roleId', 'children')
      })
    },
    // 获取某个用户的所有角色
    getUserRoleList () {
      console.log(this.EditUserRoleId)
      this.UserRoleList = []
      this.UserRoleListName = []
      this.$axios({
        method: 'post',
        url: '/personUserRole/getRoleByUserId',
        data: { 'userId': this.EditUserRoleId }
      }).then((r) => {
        this.UserRoleList = r.data
        // console.log('1')
        console.log(this.UserRoleList)
        this.UserRoleList.map((val, index) => {
          this.UserRoleListId.push(val['roleId'])
        })
        // console.log('2')
        console.log(this.UserRoleListId)
        if (this.UserRoleList.length !== 0) {
          this.RoleDataList.map((val, index) => {
            if (this.UserRoleListId.indexOf(val['roleId']) >= 0) {
              this.UserRoleListName.push(val['roleName'])
              val['checked'] = true
            }
          })
        }
        // console.log('3')
        console.log(this.UserRoleListName)
        this.RoleTreeData = []
        linearToTrees(this.RoleDataList, this.RoleTreeData, 'parentId', 'roleId', 'children')
      })
    },
    // 选择角色树的事件
    checkRoleTree (v) {
      this.RoleModel = []
      v.map((val, index) => {
        this.RoleModel.push(val['roleId'])
      })
    },
    // 处理角色分配的操作
    handleEditUserRole () {
      this.$axios({
        method: 'post',
        url: '/personUserRole/updateUserRole',
        data: {
          'userId': this.EditUserRoleId,
          'RoleModel': this.RoleModel
        }
      }).then((response) => {
        this.$Message.info('已经插入' + response.data + '条数据')
        this.RoleModel = []
        this.UserRoleList = []
        this.UserRoleListName = []
        this.UserRoleListId = []
      })
    },
    // 取消角色分配的操作
    cancelEditUserRole () {
      this.RoleModel = []
      this.UserRoleList = []
      this.UserRoleListName = []
      this.UserRoleListId = []
      this.RoleDataList.map((val, index) => {
        val['checked'] = false
      })
    },
    initData () {
      // 获取部门的列表
      this.$axios({
        method: 'get',
        url: '/org/info',
        data: ''
      }).then((r) => {
        this.newlist = r.data
        // console.log(this.newlist)
        this.newlist.map((val, index) => {
          // val['title'] = val['name']
          val['expand'] = true
          val['children'] = []
          val['value'] = val['id']
          val['label'] = val['title']
        })
        linearToTrees(this.newlist, this.data1, 'pid', 'id', 'children')
        this.allUser()
      })
    },
    // 
    updateUserStatus (data) {
      let id = data.userId
      let status = data.status
      if (status === 1) {
        status = 0
      } else {
        status = 1
      }
      this.$axios({
        method: 'post',
        url: '/personuser/updateUserStatus',
        data: {
          'userId': id,
          'status': status
        }
      }).then((response) => {
        this.$Message.info('操作' + response.data)
      })
    },
    // 后端分页
    getPageData (pageNum) {
      this.currentPageNum = pageNum
      let num = 0
      if(pageNum === 1){
        num = 0
      } else{
        pageNum = pageNum - 1;
        num = pageNum * 10
      }
      if (this.flag === 1) {
        this.$axios({
          method: 'post',
          url: '/personuser/getUserListByPage',
          data: {
            'offset': num
          }
        }).then((response) => {
          let temp = response.data
          this.changeDeptIdToDeptName(temp)
          this.pageRes = temp
        })
      } else if (this.flag === 2) {
        this.searchUserRequest(num)
      } else if (this.flag === 3) {
        this.getUserListByDeptIdRequest(num)
      }
    }
  },
  mounted () {
    this.initData()
    this.getRoleList()
    this.getMenuList()
  }
}
</script>

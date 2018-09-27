<template>
    <div>
       <Row :gutter="4">
       <i-col span="6">
         <Row>
          <Card>
            <Row :gutter="4" style="margin: 10px 0;" >
              <i-col span="12">
                <Upload action="" :before-upload="handleBatchImport">
                  <Button type="primary">批量导入</Button>
                </Upload>
                <Modal v-model="importModal" @on-ok="handleBatchImportSave" ok-text="保存" title="导入数据" width="720">
                  <Card>
                    <Row style="margin: 10px 0;" justify="center" align="middle">
                      <i-col span="24">
                        <Table :columns="importTableColumns" :data="importRoles" height="300" width="660"></Table>
                      </i-col>
                    </Row>
                  </Card>
                </Modal>
                <!-- <Button type="primary" @click="handleTest">测试</Button> -->
              </i-col>
              <i-col span="12">
                <!-- <Input ref="inputer" clearable placeholder="文件路径"/> -->
              </i-col>
            </Row>
            <Row :gutter="4" style="margin: 10px 0;" >
              <i-col span="12">
                <Poptip confirm title="确认是否导出?" @on-ok="handleBatchExport">
                  <Button type="primary">批量导出</Button>
                </Poptip>
              </i-col>
              <i-col span="12">
                <Input v-model="exportFileName" clearable placeholder="文件名" />
              </i-col>
            </Row>
          </Card>
         </Row>
         <Row>
           <Card>
             <Row>
               <i-col span="12">
                 <Button type="primary" @click="addModal = true">新建</Button>
                 <Modal title="新建角色" v-model="addModal" :styles="{top: '20px'}" @on-ok="handleAdd">
                   <Row>
                    <i-col span = "24" >
                      <Card>
                        <p slot="添加角色">
                          请输入要添加的角色信息
                        </p>
                        <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
                          <FormItem label="角色名称" prop="roleName">
                            <Input v-model="formValidate.roleName" placeholder="请输入角色名称"/>
                          </FormItem>
                          <FormItem label="角色标识" prop="roleSign">
                            <Input v-model="formValidate.roleSign" placeholder="请输入角色标识"/>
                          </FormItem>
                          <FormItem label="注释" prop="remark">
                            <Input v-model="formValidate.remark" type="textarea" :autosize="{minRows: 5,maxRows: 5}" placeholder="注释......" />
                          </FormItem>
                        </Form>
                      </Card>
                    </i-col>
                  </Row>
                 </Modal>
               </i-col>
               <i-col span="12">
                 <Poptip confirm title="确认是否删除?" @on-ok="handleDelete(false, null)">
                    <Button type="primary">删除</Button>
                 </Poptip>
               </i-col>
             </Row>
             <Scroll height="310">
               <Row>
                <Tree ref="tree" :data="treeData" show-checkbox @on-select-change="handleSelect(null, 0)"></Tree>
              </Row>
             </Scroll>
           </Card>
         </Row>
       </i-col>
       <i-col span="18">
        <Card>
          <tables ref="tables" :height="420" editable searchable search-place="top" v-model="tableData" :columns="columns"/>
        </Card>
       </i-col>
    </Row>
    </div>
</template>

<script>
import Tables from '_c/tables'
import axios from 'axios'
import {deleteRoles} from './../../../api/role.js'
import {addRole, listRole, batchAddRole} from './../../../api/role.js'
import {login} from './../../../api/_user.js'
import {test, test2} from './../../../api/test.js';
import {linearToTrees, treesToLinear, batchTreeNodesDelete, treesToTrees, linearToLinear, filterTreesToLinearStructure} from './util.js'
import router from './../../../router/index.js'
// import $ from 'jquery'
export default {
  components: {
    Tables
  },
  data () {
    return {
      prefix: 'http://127.0.0.1:80/sys/role',
      addModal: false,
      ruleValidate: {
        roleName: [
          {
            required: true,
            message: '角色名不能为空！',
            trigger: 'blur'
          }
        ]
      },
      formValidate: {
        parentId: -1,
        roleName: '',
        roleSign: '',
        remark: ''
      },
      treeData: [
      ],
      tableData: [
      ],
      columns: [
        {
          title: '角色ID',
          key: 'stringRoleId',
          sortable: true
        },
        {
          title: '名称',
          key: 'roleName',
          sortable: true
        },
        {
          title: '标识',
          key: 'roleSign',
          sortable: true
        },
        {
          title: '操作',
          key: 'handle',
          button: [
            (h, params, vm) => {
              return h('div', [
                h(
                  'Button', 
                  {
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.$store.dispatch('setRole', params.row)
                        // console.log(this.$router)
                        this.$router.push('/role/role_detail')
                      }
                    }
                  }, 
                  ['查看']
                ),
                h(
                  'Poptip', 
                  {
                    props: {
                      confirm: true,
                      title: '确认删除？',
                      transfer: true
                    },
                    on: {
                      'on-ok': () => {
                        let result = []
                        filterTreesToLinearStructure(this.treeData, [params.row['roleId']], 'roleId', result, (v1, v2) => {
                          return v1 === v2
                        }, 'children')
                        let lr = []
                        treesToLinear(result, lr, 'children')
                        console.log(lr)
                        this.handleDelete(true, lr)
                      }
                    }
                  },
                  [
                    h(
                    'Button', 
                    {
                      style: {
                        marginRight: '5px'
                      }
                    },
                    ['删除']
                    )
                  ]
                )
              ])
            }
          ]
        }
      ],
      addItem: {
      },
      exportFileName: '',
      roleList: [],
      importRoles: [],
      importModal: false,
      importTableColumns: [
        {
          title: '角色ID',
          key: 'roleId',
          sortable: true
        },
        {
          title: '角色名',
          key: 'roleName',
          sortable: true
        },
        {
          title: '标识',
          key: 'roleSign',
          sortable: true
        },
        {
          title: '注释',
          key: 'remark',
          sortable: true
        },
        {
          title: '父节点ID',
          key: 'parentId',
          sortable: true
        }
      ]
    }
  },
  methods: {
    handleTest () {
      test2({
        userId: 1
      }).then((r) => {
        console.log(r)
      })
      // router.push('/role/role_detail')
    },
    easyLogin () {
      // var params = new URLSearchParams()
      // params.append('username', 'admin')
      // params.append('password', '111111')
      // axios({
      //   method: 'post',
      //   url: 'http://127.0.0.1:80/login',
      //   data: params
      // }).then(function (r) {
      //   // console.log(r)
      // }).catch(function (error) {
      //   console.log(error)
      // })
      return login({
        username: 'admin',
        password: '111111'
      })
    },
    /*
    * 事件响应：handleDelete
    * 功能：删除角色的事件响应
    */
    handleDelete (mode, list) {
      var checkedList = this.$refs.tree.getCheckedNodes()
      if (mode) {
        checkedList = list
      }
      batchTreeNodesDelete(this.treeData, checkedList, 'roleId', 'children')
      // console.log(checkedList)
      if (this.treeData.length === 0) {
        this.handleSelect(null, -1)
      } else {
        this.handleSelect(this.treeData[0], 0)
      }
      var params = []
      checkedList.map((val, index) => {
        params.push(val['roleId'])
      })
      console.log(params)
      deleteRoles({ids: params}).then((r) => {
        this.roleList.splice(0, this.roleList.length)
        treesToLinear(this.treeData, this.roleList, 'children')
        this.$Message.success('操作成功')
      }, (e) => {
        console.log(e)
        this.$Message.error('请求失败')
      })
    },
    /*
    * 事件响应：handleAdd
    * 功能：添加角色的事件响应
    */
    handleAdd () {
      this.$refs['formValidate'].validate((valid) => {
        if (valid) {
          let parentId = -1
          try {
            parentId = this.$refs.tree.getSelectedNodes()[0].roleId
          } catch (e) {
          }
          this.formValidate.parentId = parentId
          addRole(this.formValidate).then((r) => {
            r.data['children'] = []
            r.data['title'] = r.data.roleName
            r.data['expand'] = false
            r.data['stringRoleId'] = r.data.roleId + ''
            var trees = []
            trees.push(r.data)
            treesToTrees(trees, this.treeData, 'children', 'parentId', 'roleId')
            this.roleList.splice(0, this.roleList.length)
            treesToLinear(this.treeData, this.roleList, 'children')
            this.$Message.success('操作成功')
          }, (e) => {
            console.log(e)
            this.$Message.error('请求失败')
          })
        } else {
        }
      })
    },
    /*
    * 事件响应: handleSelect
    * 功能: 树形控件的select事件响应
    */
    handleSelect (treeNode, mode) {
      if (mode === 0) {
        try {
          let selectItem = treeNode === null ? this.$refs.tree.getSelectedNodes()[0] : treeNode
          if (selectItem === undefined) {
            throw e = new Error('')
          }
          var list = []
          var treeLinearList = []
          var trees = []
          trees.push(selectItem)
          treesToLinear(trees, treeLinearList, 'children')
          treeLinearList.map((val, index) => {
            list.push(val)
          })
          this.tableData = list
        } catch (e) {
        }
      } else {
        this.tableData = []
      }
    },
    /*
    * 事件响应: handleBatchExport
    * 功能: 批量导出事件响应
    */
    handleBatchExport () {
      require.ensure([], () => {　　　　　　　　
        let { export_json_to_excel } = require('./../../../libs/Export2Excel.js')　
        let tHeader = ['roleId', 'roleSign', 'roleName', 'remark', 'parentId']
        let filterVal = ['roleId', 'roleSign', 'roleName', 'remark', 'parentId']
        let list = this.roleList
        let fileName = this.exportFileName
        let formatJson = (filterVal, jsonData) => {
          return jsonData.map(v => filterVal.map(j => v[j]));
        }
        let data = formatJson(filterVal, list)
        // console.log(data)
        export_json_to_excel(tHeader, data, fileName)
      })
    },
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
          _this.importRoles = []
          outdata.map((v, i) => {
            temp = {}
            temp['roleId'] = outdata[i]['roleId']
            temp['stringRoleId'] = temp.roleId + ''
            temp['roleName'] = outdata[i]['roleName']
            temp['roleSign'] = outdata[i]['roleSign']
            temp['remark'] = outdata[i]['remark']
            temp['parentId'] = outdata[i]['parentId']
            _this.importRoles.push(temp)
          })
          _this.importModal = true
          console.log(_this.importRoles)
        }
        reader.readAsArrayBuffer(f)
      }
      if(rABS) {
        reader.readAsArrayBuffer(f)
      } else {
        reader.readAsBinaryString(f)
      }
    },
    /*
    * 事件响应: handleBatchImportSave
    * 功能: 批量导入保存事件响应
    */
    handleBatchImportSave () {
      console.log(this.importRoles)
      batchAddRole({
        roles: this.importRoles
      }).then((r) => {
        if (r.data['code'] === 0) {
          linearToLinear(this.importRoles, this.roleList)
          this.treeData.splice(0, this.treeData.length)
          this.roleList.map((v, i) => {
            v['children'] = []
            v['title'] = v['roleName']
          })
          linearToTrees(this.roleList, this.treeData, 'parentId', 'roleId', 'children')
          this.$Message.success('操作成功')
        } else {
          this.$Message.error('操作失败')
        }
        console.log(r)
      }, (e) => {
        console.log(e)
        this.$Message.error('请求失败')
      })
    },
    /*
    * 方法名: initData
    * 功能: 初始化数据
    */
    initData () {
      var resultTreeData = this.treeData
      listRole({}).then((r) => {
        if (r.status === 200) {
          this.roleList = r.data
          // console.log(r.data)
          this.roleList.map((val, index) => {
            val['stringRoleId'] = val.roleId + ''
            val['title'] = val.roleName
            val['expand'] = false
            val['children'] = []
          })
          linearToTrees(this.roleList, this.treeData, 'parentId', 'roleId', 'children')
          this.handleSelect(this.treeData[0], 0)
          // console.log(this.roleList)
          // console.log(this.treeData)
        } else {
          console.log(r)
        }
      }, (e) => {
        console.log(e)
      })
    }
  },
  mounted () {
    this.initData()
  }
}
</script>

<style>
:m
{
  margin: 50px 10px 20px 10px;
}
</style>

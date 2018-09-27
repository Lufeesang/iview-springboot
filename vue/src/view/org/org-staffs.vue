<template>
  <div>
    <Row :gutter="16">
      <Col span="6">
      <Card>
        <AutoComplete v-model="queryInput" style="width:200px" :data="searchOptions" @on-change="setSearchOptions" @on-select="selectOption" icon="ios-search">
        </AutoComplete>
        <Tree :data="treeData" @on-select-change="handleSelect"></Tree>
      </Card>
      </Col>
      <Col span="18">
      <Card>
        <Divider style="margin: 5px 0">人员信息查询</Divider>
        <Form :model="formRight" label-position="right" :label-width="100" :inline="true">
          <FormItem label="账号ID" class="formNumber">
            <Input v-model="formRight.input1"></Input>
            <div>
              <p v-show="nanId" class="formTip">请输入数字</p>
            </div>
          </FormItem>
          <FormItem label="账号区间" class="formNumber">
            <Input v-model="formRight.input2" style="width:30%"></Input> ~
            <Input v-model="formRight.input3 " style="width:30%"></Input>
            <p v-show="nanIds" class="formTip">请输入数字</p>
          </FormItem>
          <FormItem label="人员姓名 ">
            <Input v-model="formRight.input4 "></Input>
          </FormItem>
          <FormItem label="组织机构 ">
            <Input v-model="formRight.input5 "></Input>
          </FormItem>
          <FormItem label="账号状态 ">
            <Select v-model="formRight.input6 ">
              <Option value="">所有</Option>
              <Option value="1">在用</Option>
              <Option value="0">停用</Option>
            </Select>
          </FormItem>
          <FormItem>
            <Button type="primary" @click="query">查询</Button>
          </FormItem>
          <FormItem>
            <Button @click="resetCondition">重置条件</Button>
          </FormItem>
          <FormItem>
            <Button @click="resetAll">重置所有</Button>
          </FormItem>
        </Form>
        <Transfer :data="staffsData" :target-keys="targetKeys" :render-format="render1" :list-style="listStyle" @on-change="handleChange" :titles="transferTitle"></Transfer>
      </Card>
      </Col>
    </Row>
  </div>
</template>
<script>
/*eslint-disable*/
export default {
  data() {
    return {
      /*树数据*/
      treeData: [{
        id: -1,
        title: '组织机构树',
        expand: false,
        loading: false,
        children: [],
        checked: false,
      }],
      orgsInfo: [],
      orgsTree: [],
      orgsData: [],
      titleMapId: [],
      nodeData: '',
      // 查询数据
      orgsName: [],
      queryInput: '',
      searchOptions: [],
      findFlag: false,
      setTrue: false,
      /*列表数据*/
      orgStaffs: [],
      orgStaffsKeys: [],
      staffsData: [],
      targetKeys: [],
      listStyle: {
        width: '330px',
        height: '250px'
      },
      transferTitle: ['待分配人员列表', '已分配人员列表'],
      leftColName: {
        key: '账号ID',
        label: '人员名称',
        deptId: 0,
        disabled: true
      },
      rightColName: {
        key: ' 账号ID',
        label: '人员名称',
        deptId: 0,
        disabled: true
      },
      /*表单区*/
      formRight: {
        input1: '',
        input2: '',
        input3: '',
        input4: '',
        input5: '',
        input6: '',
      },
      nanId: false,
      nanIds: false,
    }
  },
  methods: {
    /*搜索框*/
    setSearchOptions(val) {
      this.searchOptions = val ? this.orgsName.filter((item) => item.indexOf(val) > -1) : []
    },
    locateTheNode(title, nodeData) {
      if (this.nodeData && this.nodeData.selected == true) {
        this.nodeData.selected = false
      }
      for (let item in nodeData) {
        this.$set(nodeData[item], "selected", false);
        this.$set(nodeData[item], "expand", false);
        if (nodeData[item].title == title) {
          this.nodeData = nodeData[item]
          this.$set(nodeData[item], "selected", true);
          this.findFlag = true
          return
        } else if (nodeData[item].children && nodeData[item].children.length) {
          this.locateTheNode(title, nodeData[item].children)
        }
        if (this.findFlag) {
          if (!nodeData[item].expand || nodeData[item].nodeKey > -1) {
            this.$set(nodeData[item], "expand", true)
          }
          if (nodeData[item].nodeKey == 0) {
            this.findFlag = false
          }
          return
        }
      }
    },
    selectOption(val) {
      this.locateTheNode(val, this.treeData)
    },
    /*组织机构数据*/
    getOrgsInfo() {
      return this.$axios.request({
          url: "/org/info",
          method: "get",
        })
        .then(res => {
          console.log(res)
          return res.data
        })
        .catch(err => {
          console.log(err)
        })
    },
    getOrgsInfoPromise() {
      let _this = this;
      let p = new Promise(function(resolve, reject) {
        resolve(_this.getOrgsInfo());
      })
      return p;
    },
    setOrgsInfo() {
      let _this = this;
      return this.getOrgsInfoPromise().then(function(data) {
        _this.orgsInfo = data;
      })
    },
    setOrgsInfoPromise() {
      let _this = this;
      let p = new Promise(function(resolve, reject) {
        resolve(_this.setOrgsInfo())
      })
      return p;
    },
    setTitleMapId() {
      let _this = this,
        key,
        value;
      return this.setOrgsInfoPromise().then(function(data) {
        for (let item in _this.orgsInfo) {
          _this.titleMapId[item] = {};
          key = _this.orgsInfo[item].title;
          value = _this.orgsInfo[item].id;
          _this.titleMapId[item][key] = value;
          _this.orgsName.push(key);
        }
        _this.titleMapId.unshift({ '组织机构树': -1 })
      })
    },
    setTitleMapIdPromise() {
      let _this = this;
      let p = new Promise(function(resolve, reject) {
        resolve(_this.setTitleMapId())
      })
      return p;
    },
    getOrgsByPid() {
      return this.$axios.request({
          url: "/org/tree",
          method: "get",
          params: {
            pid: -1
          }
        })
        .then(res => {
          console.log(res)
          return res.data
        })
        .catch(err => {
          console.log(err)
        })
    },
    getOrgsByPidPromise() {
      let _this = this;
      let p = new Promise(function(resolve, reject) {
        resolve(_this.getOrgsByPid())
      });
      return p;
    },
    setOrgsData() {
      let _this = this;
      return this.getOrgsByPidPromise().then(function(data) {
        _this.orgsData = data;
      });
    },
    setOrgsDataPromise() {
      let _this = this;
      let p = new Promise(function(resolve, reject) {
        resolve(_this.setOrgsData());
      });
      return p;
    },
    treeFresh() {
      let _this = this;
      this.setOrgsDataPromise().then(function(data) {
        _this.treeData[0].children = _this.orgsData
      })
    },
    handleSelect(val) {
      this.nodeData = val[0] ? val[0] : this.nodeData
      if (!this.nodeData.nodeKey) {
        this.nodeData.id = null
      }
      this.formRight.input5 = this.nodeData.title;
      this.emitRefresh(this.getOrgStaffs, this.nodeData.id);
    },
    /*获取某个节点orgId的人员*/
    getOrgStaffs(args) {
      return this.$axios.request({
        url: '/personuser/searchUsers',
        method: 'post',
        data: {
          deptId: args[0]
        },
      }).then(res => {
        console.log(res.data)
        this.orgStaffs = res.data
        this.orgStaffsKeys = this.orgStaffs.map(item => item.userId)
        return res.data
      }).catch(err => {
        console.log(err)
      })
    },
    changeNodeInfo(root, node, data) {
      this.nodeData = data;
      let _this = this;
      if (!node.nodeKey) {
        data.id = null
      }
      this.formRight.input5 = data.title;
      this.emitRefresh(this.getOrgStaffs, data.id);
    },

    /*人员列表区域*/
    /*utils*/
    // 异步加载后刷新人员列表工具
    createPromise(fuc, args) {
      let _this = this;
      let p = new Promise(function(resolve, reject) {
        resolve(fuc(args))
      });
      return p;
    },
    checkStatus() {
      switch (this.formRight.input6) {
        case '0':
          this.staffsData = this.staffsData.filter(function(item) {
            return item.status == 0
          });
          break;
        case '1':
          this.staffsData = this.staffsData.filter(function(item) {
            return item.status == 1
          });
          break;
        default:
          break;
      }
    },
    refreshStaff(data) {
      if (data[0]) {
        if (data[0].key) {
          this.getTargetKeys();
          return
        }
        data.sort((a, b) => {
          return a.userId - b.userId
        })
        this.staffsData = this.formatSerData(data);
        this.checkStatus();
      } else {
        this.staffsData = []
      }
      this.staffsData.splice(0, 0, this.leftColName);
      this.staffsData.splice(0, 0, this.rightColName);
      this.getTargetKeys();
    },
    emitRefresh(fuc, ...args) {
      let _this = this;
      this.createPromise(fuc, args).then(function(data) {
        _this.refreshStaff(data);
      })
    },
    // 将服务端人员数据转化为前端格式工具
    formatSerData(mockData) {
      let transMockData = [];
      for (let i in mockData) {
        transMockData[i] = mockData[i];
        transMockData[i].key = mockData[i].userId;
        transMockData[i].label = mockData[i].name;
        delete transMockData[i].userId;
        delete transMockData[i].name;
      }
      return transMockData;
    },
    // 将前端数据转化为服务端格式工具
    formatFrontData(mockData) {
      let transMockData = [];
      for (let i in mockData) {
        transMockData[i] = mockData[i];
        transMockData[i].userId = mockData[i].key;
        transMockData[i].name = mockData[i].label;
        delete transMockData[i].key;
        delete transMockData[i].label;
      }
      return transMockData;
    },
    /*未分配列表*/
    getTargetKeys() {
      this.targetKeys.splice(0, this.targetKeys.length);
      console.log(this.orgStaffsKeys)
      for (var i in this.staffsData) {
        if (this.orgStaffsKeys.indexOf(this.staffsData[i].key) !== -1) {
          this.targetKeys.push(this.staffsData[i].key);
        }
      }
      this.targetKeys.unshift("账号ID");
    },
    /*渲染列表*/
    render1(item) {
      String.prototype.times = function(n) { return (new Array(n + 1)).join(this); };
      if (item.key == "账号ID") {
        return '&emsp;&emsp;&emsp;&emsp;' + item.key + '&emsp;&emsp;&emsp;&emsp;&emsp;' + item.label;
      } else if (item.key == " 账号ID") {
        return '&emsp;&emsp;&emsp;&nbsp;' + item.key + '&emsp;&emsp;&emsp;&emsp;&emsp;' + item.label;
      }
      return '&nbsp;'.times(18 - item.key.toString().length) + item.key + '&nbsp;'.times(28 - item.label.toString().length) + item.label;
    },
    /*分配操作*/
    allot(args) {
      return this.$axios.request({
        url: '/personuser/updateUsers',
        method: 'post',
        data: args[0]
      }).then(res => {
        console.log(res)
        return this.staffsData
      }).catch(err => {
        console.log(err)
      })
    },
    handleChange(newTargetKeys, direction, moveKeys) {
      if (this.formRight.input5 == "组织机构树" || this.formRight.input5 == "中移在线") {
        this.$Message.error(this.formRight.input5 + '无法进行分配操作');
        return;
      }
      let _this = this,
        allotDatas = JSON.parse(JSON.stringify(this.staffsData));
      allotDatas.splice(0, 2)
      // deptId变化
      for (var i in allotDatas) {
        if (moveKeys.indexOf(allotDatas[i].key) !== -1) {
          allotDatas[i].deptId = direction == 'right' ? this.nodeData.id : 1;
          this.staffsData[parseInt(i) + 2].deptId = direction == 'right' ? this.nodeData.id : 1
        }
      }
      // orgsStaffKeys变化
      if (direction == 'right') 
        this.orgStaffsKeys = this.orgStaffsKeys.concat(moveKeys)
      else this.orgStaffsKeys = this.orgStaffsKeys.filter(item => moveKeys.indexOf(item) === -1)
        
      allotDatas = this.formatFrontData(allotDatas);
      // 需要重新渲染数据
      this.emitRefresh(this.allot, allotDatas);
    },

    /*人员查询区*/
    // 根据id查找
    getStaffById(args) {
      return this.$axios.request({
        url: '/personuser/searchUsers',
        method: 'post',
        data: {
          userId: args[0],

        }
      }).then(res => {
        return res.data
      }).catch(err => {
        console.log(err)
      })
    },
    // 根据id区间查询
    getStaffByIds(args) {
      return this.$axios.request({
        url: '/personuser/searchUsers',
        method: 'post',
        data: {
          start: args[0],
          end: args[1]
        }
      }).then(res => {
        console.log(res.data)
        return res.data
      }).catch(err => {
        console.log(err)
      })
    },
    // 根据Name查找
    getStaffByName(args) {
      return this.$axios.request({
        url: '/personuser/searchUsers',
        method: 'post',
        data: {
          name: args[0],
        }
      }).then(res => {
        console.log(res)
        return res.data
      }).catch(err => {
        console.log(err)
      })
    },
    // 根据组织机构查找
    getOrgStaffsByTitle(orgName) {
      let key = orgName,
        value;
      for (let item in this.titleMapId) {
        value = this.titleMapId[item][orgName];
        if (value) {
          this.nodeData = this.orgsInfo[value - 1];
          this.emitRefresh(this.getOrgStaffs, value);
        }
      }
    },

    /*按钮区*/
    query() {
      let queryOption;
      for (let item in this.formRight) {
        if (this.formRight[item] !== '') {
          queryOption = item;
          break;
        }
      }
      switch (queryOption) {
        case 'input1':
          this.emitRefresh(this.getStaffById, this.formRight.input1);
          break;
        case 'input2':
          if (this.formRight.input3) {
            this.emitRefresh(this.getStaffByIds, this.formRight.input2, this.formRight.input3);
            break;
          }
        case 'input4':
          this.emitRefresh(this.getStaffByName, this.formRight.input4);
          break;
        case 'input5':
          this.getOrgStaffsByTitle(this.formRight.input5);
          break;
        default:
          break;
      }
    },
    resetCondition() {
      for (let item in this.formRight) {
        this.formRight[item] = ''
      }
    },
    resetAll() {
      this.resetCondition();
      this.staffsData = []
    },
  },
  mounted() {
    this.setTitleMapId()
    this.treeFresh()
  },
  watch: {
    /*表单区*/
    // 表单验证
    formRight: {
      handler(val) {
        if (/\D/.test(val.input1)) {
          this.nanId = true
        } else {
          this.nanId = false
        }
        if (/\D/.test(val.input2)) {
          this.nanIds = true
        } else {
          this.nanIds = false
        }
      },
      deep: true
    }
  }
}

</script>
<style scoped>
.formNumber {
  margin-bottom: 0;
  height: 60px;
}

.formTip {
  height: 27px;
  line-height: 27px;
  color: red;
}

</style>

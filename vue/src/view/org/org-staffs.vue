<template>
  <div>
    <Row :gutter="16">
      <Col span="6">
      <Card>
        <Tree :data="firstNode" :load-data="loadTreeData" :render="renderContent" class="layout-content-tree"></Tree>
      </Card>
      </Col>
      <Col span="18">
      <Card>
        <Divider style="margin: 5px 0">人员信息查询</Divider>
        <Form :model="formRight" label-position="right" :label-width="100" :inline="true">
          <FormItem label="账号ID" class="formNumber">
            <Input v-model="formRight.input1"></Input>
            <div>
            <p v-show="nanId" class="formTip" >请输入数字</p>
            </div>
          </FormItem>
          <FormItem label="账号区间" class="formNumber">
            <Input v-model="formRight.input2" style="width:30%"></Input> ~
            <Input v-model="formRight.input3 " style="width:30%"></Input>
            <p v-show="nanIds" class="formTip" >请输入数字</p>
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
            <Button type="primary " @click="query">查询</Button>
          </FormItem>
          <FormItem>
            <Button type='ghost' style="color:black; " @click="resetCondition">重置条件</Button>
          </FormItem>
          <FormItem>
            <Button type='ghost' style="color:black; " @click="resetAll">重置所有</Button>
          </FormItem>
          <FormItem>
            <Button type='ghost' style="color:black; " @click="test">test</Button>
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
      firstNode: [{
        title: '组织机构树',
        expand: false,
        loading: false,
        children: []
      }],
      orgsInfo: [],
      orgsTree: [],
      titleMapId: [],
      nodeData: '',
      /*列表数据*/
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
        orgId: 0,
        disabled: true
      },
      rightColName: {
        key: ' 账号ID',
        label: '人员名称',
        orgId: 0,
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
    /*组织机构数据*/
    getOrgsInfo() {
      return this.$axios.get("/org/info")
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
    getOrgsTree() {
      return this.$axios.get("/org/tree", {
          params: {
            pid: -1
          }
        })
        .then(res => {
          return res.data
        })
        .catch(err => {
          console.log(err)
        })
    },
    getOrgsTreePromise() {
      let _this = this;
      let p = new Promise(function(resolve, reject) {
        resolve(_this.getOrgsTree())
      });
      return p;
    },
    setOrgsData() {
      let _this = this;
      return this.getOrgsTreePromise().then(function(data) {
        _this.orgsTree = data;
      });
    },
    setOrgsDataPromise() {
      let _this = this;
      let p = new Promise(function(resolve, reject) {
        resolve(_this.setOrgsData());
      });
      return p;
    },
    loadTreeData(item, callback) {
      let _this = this;
      this.setOrgsDataPromise().then(function() {
        callback(_this.orgsTree);
      })
    },
    renderContent(h, { root, node, data }) {
      return h('span', {
        style: {
          display: 'inline-block',
          width: '80%',
        }
      }, [
        h('a', {
          style: {
            color: 'black'
          },
          on: {
            click: () => { this.changeNodeInfo(root, node, data) }
          }
        }, [
          h('span', data.title)
        ]),
        h('span', {
          style: {
            display: 'inline-block',
            float: 'right',
          }
        }, )
      ]);
    },
    /*获取某个节点orgId的人员*/
    getOrgStaffs(args) {
      return this.$axios({
        url: '/staff/getOrgStaffs',
        method: 'get',
        params: {
          id: args[0]
        },
      }).then(res => {
        return res.data
      }).catch(err => {
        console.log(err)
      })
    },
    changeNodeInfo(root, node, data) {
      this.nodeData = data;
      let _this = this;
      if (!node.nodeKey) {
        data.id = -1
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
        transMockData[i].key = mockData[i].id;
        transMockData[i].label = mockData[i].name;
        delete transMockData[i].id;
        delete transMockData[i].name;
      }
      return transMockData;
    },
    // 将前端数据转化为服务端格式工具
    formatFrontData(mockData) {
      let transMockData = [];
      for (let i in mockData) {
        transMockData[i] = mockData[i];
        transMockData[i].id = mockData[i].key;
        transMockData[i].name = mockData[i].label;
        delete transMockData[i].key;
        delete transMockData[i].label;
      }
      return transMockData;
    },
    /*未分配列表*/
    getTargetKeys() {
      this.targetKeys.splice(0, this.targetKeys.length);
      for (var i in this.staffsData) {
        if (this.staffsData[i].orgId == this.nodeData.id) {
          this.targetKeys.push(this.staffsData[i].key);
        }
      }
      this.targetKeys.unshift("账号ID");
    },
    /*渲染列表*/
    render1(item) {
      if (item.key == "账号ID") {
        return '&emsp;&emsp;&emsp;&emsp;' + item.key + '&emsp;&emsp;&emsp;&emsp;&emsp;' + item.label;
      } else if (item.key == " 账号ID") {
        return '&emsp;&emsp;&emsp;&nbsp;' + item.key + '&emsp;&emsp;&emsp;&emsp;&emsp;' + item.label;
      }
      return '&emsp;&emsp;&emsp;&emsp;&emsp;' + item.key + '&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;' + item.label;
    },
    /*分配操作*/
    allot(args) {
      return this.$axios({
        url: '/staff/allot',
        method: 'post',
        data: args[0]
      }).then(res => {
        console.log(res)
        console.log(this.staffsData)
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
      let _this = this;
      this.staffsData.splice(0, 2)
      console.log(this.staffsData)
      // orgid变化
      for (var i in this.staffsData) {
        if (moveKeys.indexOf(this.staffsData[i].key) !== -1) {
          this.staffsData[i].orgId = direction == 'right' ? this.nodeData.id : 1
        }
      }
      let allotDatas = this.formatFrontData(this.staffsData);
      // 需要重新渲染数据
      this.emitRefresh(this.allot, allotDatas);
    },

    /*人员查询区*/
    // 根据id查找
    getStaffById(args) {
      return this.$axios({
        url: '/staff/getStaffById',
        method: 'get',
        params: {
          id: args[0]
        }
      }).then(res => {
        return res.data
      }).catch(err => {
        console.log(err)
      })
    },
    // 根据id区间查询
    getStaffByIds(args) {
      return this.$axios({
        url: '/staff/getStaffByIds',
        method: 'get',
        params: {
          start: args[0],
          end: args[1]
        }
      }).then(res => {
        return res.data
        console.log(res.data)
      }).catch(err => {
        console.log(err)
      })
    },
    // 根据Name查找
    getStaffByName(args) {
      return this.$axios({
        url: '/staff/getStaffByName',
        method: 'get',
        params: {
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
    test() {

    }
  },
  mounted() {
    this.setTitleMapId()
  },
  watch: {
    /*表单区*/
    // 表单验证
    formRight: {
      handler(val) {
        if (/\D+/.test(val.input1)) {
          this.nanId = true
        }else{
          this.nanId = false
        }
        if(/\D+/.test(val.input2)){
          this.nanIds = true
        }else{
          this.nanIds = false
        }
      },
      deep: true
    }
  }
}

</script>
<style scoped>
.formNumber{
  margin-bottom: 0;
  height: 60px;
}
.formTip{
  height: 27px;
  line-height :27px; 
  color: red;
}

</style>

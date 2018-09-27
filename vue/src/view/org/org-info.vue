<template>
  <div>
    <Modal v-model="modalAdd" title="新增机构" width="300" @on-ok="confirmAppend">
      <Form :rules="ruleInline">
        <FormItem prop="addOrgName" label="机构名称">
          <Input v-model="newOrgName"></Input>
        </FormItem>
      </Form>
    </Modal>
    <Modal v-model="modalDel" title="删除机构" width="300" @on-ok="confirmDel">
      是否删除{{delTitles}}
    </Modal>
    <div label="基本信息">
      <Row :gutter="16">
        <Col span="6">
        <Card>
          <!-- :load-data="loadTreeData" -->
          <Tree :data="treeData" :render="renderContent" class="layout-content-tree" show-checkbox multiple @on-check-change="handleChecked"></Tree>
        </Card>
        </Col>
        <Col span="18">
        <Card>
          <Form :model="orgInfo" ref="orgInfo" label-position="right" :label-width="100" class="layout-content-info" :inline="true">
            <FormItem label="组织机构名称">
              <Input v-model="orgInfo.name"></Input>
            </FormItem>
            <FormItem label="上级组织">
              <Input v-model="orgInfo.parent" readonly></Input>
            </FormItem>
            <FormItem label="组织机构状态">
              <Select v-model="orgInfo.status">
                <Option value="1">在用</Option>
                <Option value="0">停用</Option>
              </Select>
            </FormItem>
            <FormItem label="组织机构类型">
              <Select ref="prov" v-model="orgInfo.type" placeholder="请选择省份" style="width: 110%">
                <Option v-for="item in provList" :value="item" :key="item">{{ item }}</Option>
              </Select>
            </FormItem>
            <FormItem label="主管">
              <Input v-model="orgInfo.manager"></Input>
            </FormItem>
            <FormItem label="所属租户">
              <Input v-model="orgInfo.tenament" readonly></Input>
            </FormItem>
            <FormItem label="组织机构描述" prop="desc">
              <Input v-model="orgInfo.desc" type="textarea" :autosize="{minRows: 5,maxRows: 8}" placeholder="Enter something..." size="large" :rows="5" style="width:440px"></Input>
            </FormItem>
            <br>
            <FormItem>
              <Button type="primary" @click="save">保存</Button>
            </FormItem>
            <FormItem>
              <Button @click="reset">重置</Button>
            </FormItem>
          </Form>
        </Card>
        </Col>
      </Row>
    </div>
  </div>
</template>
<script>
/*eslint-disable*/
import areaData from 'area-data';
export default {
  data() {
    return {
      /*utitls*/
      provList: areaData[86],
      /*模态框区*/
      modalAdd: false,
      modalDel: false,
      newOrgName: '',
      ruleInline: {
        addOrgName: [
          { required: true, message: '请输入组织名称', trigger: 'blur' }
        ],
      },
      /*组织树区*/
      orgsData: [],
      treeData: [{
        id: -1,
        title: '组织机构树',
        expand: false,
        loading: false,
        children: [],
        checked: false,
      }],
      nodeData: null,
      nodeNode: null,
      rootNode: null,
      checkNodes: [],
      checkIds: [],
      delTitles: [],
      delDatas: [],
      children: [],
      /*信息显示区*/
      orgInfo: {
        name: '',
        parent: '',
        status: '',
        type: '',
        manager: '',
        tenament: '',
        desc: '',
      },
    }
  },
  methods: {
    /*组织树区*/
    // 加载机构树数据
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
    // 当树数据想要是异步加载的话
    // loadTreeData(item, callback) {
    //   let _this = this;
    //   this.setOrgsDataPromise().then(function() {
    //     callback(_this.orgsData); // 赋值完orgsData再调用callback
    //   })
    // },
    // 渲染树函数
    renderContent(h, { root, node, data }) {
      return h('span', {
        style: {
          display: 'inline-block',
          width: '80%',
        }
      }, [
        h('a', {
          domProps: {
            id: node.nodeKey,
          },
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
        }, [
          h('Button', {
            props: Object.assign({}, this.buttonProps, {
              icon: 'md-add'
            }),
            style: {
              marginRight: '8px',
              width: '27px',
              height: '25px',
              padding: '2px',
            },
            on: {
              click: () => { this.append(data) }
            }
          }),
          h('Button', {
            props: Object.assign({}, this.buttonProps, {
              icon: 'md-remove'
            }),
            style: {
              width: '27px',
              height: '25px',
              padding: '2px',
            },
            on: {
              click: () => {
                this.delete(root, node, data)
              }
            }
          })
        ])
      ]);
    },
    // 显示组织信息
    changeNodeInfo(root, node, data) {
      this.rootNode = root;
      this.nodeData = data;
      this.orgInfo.name = node.node.title;
      // 根节点没有以下信息
      this.orgInfo.parent = node.nodeKey ? root[node.parent].node.title : '';
      this.orgInfo.status = node.nodeKey ? node.node.status.toString() : '';
      this.orgInfo.type = node.nodeKey ? node.node.type : '';
      this.orgInfo.manager = node.nodeKey ? node.node.manager : '';
      this.orgInfo.tenament = node.nodeKey ? this.findTenament(root, node) : '';
      this.orgInfo.desc = node.nodeKey ? node.node.description : '';
    },
    // 多选
    handleChecked() {
      this.checkNodes = arguments[0];
    },

    // 增加节点
    append(data) {
      this.modalAdd = true;
      this.nodeData = data;
      this.newOrgName = '';
    },
    confirmAppend() {
      let _this = this;
      let orginChildren = this.nodeData.children;
      const children = orginChildren || [];
      children.push({
        title: this.newOrgName,
        status: 1,
        type: this.nodeData.type,
        manager: '',
        description: '',
        pid: this.nodeData.id,
        isParent: 0,
      });
      this.children = children;
      // this.emitRefresh(orginChildren)
      this.addNode(orginChildren)
    },
    addNode(orginChildren) {
      /*设置该节点isParent为1*/
      let _this = this;
      if (!orginChildren) {
        this.$axios.request({
          url: "/org/setIsParent",
          method: 'post',
          data: { id: this.nodeData.id }
        }).then(res => {
          console.log(res);
        }).catch(err => {
          console.log(err);
        })
      }
      /*提交新节点初始数据到服务端*/
      return this.$axios.request({
        url: "/org/add",
        method: 'post',
        data: {
          title: this.newOrgName,
          status: 1,
          type: this.nodeData.type,
          manager: '',
          description: '',
          pid: this.nodeData.id,
          isParent: 0,
        }
      }).then(res => {
        _this.$set(_this.nodeData, "children", _this.children);
        _this.$Message.success("添加成功")
        console.log(res)
      }).catch(err => {
        _this.$Message.error("添加失败：" + err)
        console.log(err)
      });
    },
    /**
     * 以下代码为原本添加节点后重新刷新树结构，因考虑友好性，不重新请求
     * 刷新树结构
     */
    // addNodePromise(orginChildren) {
    //   let _this = this;
    //   let p = new Promise(function(resolve, reject) {
    //     resolve(_this.addNode(orginChildren));
    //   })
    //   return p;
    // },
    // emitRefresh(orginChildren) {
    //   let _this = this;
    //   this.addNodePromise(orginChildren).then(function(data) {
    //     _this.treeFresh();
    //   })
    // },
    // 删除节点
    delete(root, node, data) {
      this.delDatas = []
      this.delTitles = []
      this.checkIds = []
      // 直接删除
      if (!node.nodeKey && !this.checkNodes.length) {
        this.$Message.error("无法删除根节点");
        return
      }
      // 批量删除
      if (this.checkNodes.length) {
        let checkKey,
          index,
          checkNode,
          parentNode;
        if (this.checkNodes[0].nodeKey == 0) {
          this.$Message.error("无法删除根节点");
          return
        }
        for (let item in this.checkNodes) {
          checkKey = this.checkNodes[item].nodeKey;
          checkNode = root[checkKey].node;
          parentNode = root[node.parent].node;
          index = parentNode.children.indexOf(checkNode);
          this.delDatas[index] = parentNode
          this.delTitles.push(checkNode.title);
          this.checkIds.push(checkNode.id)
        }
      } else {
        // 直接删除
        const parentKey = root.find(el => el === node).parent;
        const parent = root.find(el => el.nodeKey === parentKey).node;
        const index = parent.children.indexOf(data);
        if (data.children && data.children.length) {
          this.$Message.error(data.title + "还有子节点，请通过多选来删除多个节点");
          return;
        }
        this.delDatas[index] = parent
        this.delTitles.push(data.title);
        this.checkIds.push(data.id)
      }
      this.modalDel = true;
    },
    confirmDel() {
      let _this = this;
      // 提交删除数组
      this.$axios.request({
        url: "/org/delete",
        method: 'post',
        data: {
          ids: this.checkIds
        },
        dataType: 'json'
      }).then(res => {
        console.log(res)
        _this.$Message.success("删除成功")
        // 前端删除节点
        for (let item in _this.delDatas) {
          if (_this.delDatas[item] == _this.delDatas[item - 1]) {
            _this.delDatas[item].children.splice(item - 1, 1);
            // 若没有子节点则设置为非父节点
            if (!_this.delDatas[item].children.length) {
              _this.delDatas[item].isParent = 0
            }
          } else {
            _this.delDatas[item].children.splice(item, 1);
            if (!_this.delDatas[item].children.length) {
              _this.delDatas[item].isParent = 0
            }
          }
        }
      }).catch(err => {
        _this.$Message.error("删除失败")
        console.log(err)
      });
    },

    /*信息查看和修改区*/
    // 修改信息
    update() {
      let _this = this;
      return this.$axios.request({
        url: "/org/update",
        method: 'post',
        data: {
          id: this.nodeData.id,
          title: this.orgInfo.name,
          pid: this.nodeData.pid,
          isParent: this.nodeData.isParent,
          status: parseInt(this.orgInfo.status),
          type: this.orgInfo.type,
          manager: this.orgInfo.manager,
          description: this.orgInfo.desc
        }
      }).then(res => {
        console.log(res)
        _this.$Message.success("保存成功");
      }).catch(err => {
        console.log(err)
        _this.$Message.error("保存失败");
      });
    },
    updatePromise() {
      let _this = this;
      let p = new Promise(function(resolve, reject) {
        resolve(_this.update())
      });
      return p;
    },
    // 寻找租户
    findTenament(root, node) {
      while (node.parent !== 0) {
        node = root[node.parent];
      }
      return node.node.title;
    },
    /*按钮区*/
    // 保存重载数据
    save() {
      let _this = this;
      this.updatePromise().then(function(data) {
        // _this.treeFresh();
        _this.nodeData.title = _this.orgInfo.name;
        _this.nodeData.status = _this.orgInfo.status;
        _this.nodeData.type = _this.orgInfo.type;
        _this.nodeData.manager = _this.orgInfo.manager;
        _this.nodeData.description = _this.orgInfo.desc;
      })
    },
    reset() {
      for (let i in this.orgInfo) {
        this.orgInfo[i] = ''
      }
    },
  },
  mounted() {
    this.treeFresh();
  }
}

</script>
<style scoped>


</style>

<style lang="less">
.show-edit-btn {
  display: none;
  margin-left: -10px;
}
.ivu-table-cell:hover .show-edit-btn {
  display: inline-block;
}
</style>

<template>
    <div>
      <Row>
        <Col>
            <Table  weight = "1000" :ref="refs"  :columns="columnsList" :data="thisTableData" border disabled-hover></Table>
        </Col>
      </Row>
      <Modal
        @on-ok="handleSubmit"
        width = "80"
        :title= ModalTitle
        ok-text="保存"
        v-model="showModal"
         :styles="{top: '20px'}"
      >
          <Card>
            <Row>
                <Col span="8">
                        <span>用户状态 : </span><Select v-model="status1" clearable style="width:200px" @on-change="statusChange">
                            <Option value="1">正常</Option>
                            <Option value="0" >禁用</Option>
                        </Select>
                    </Col>
                <Col span="16">
                        <span>用户创建时间范围 : </span>
                        <DatePicker 
                            format = "yyyy-MM-dd HH:mm:ss"
                            @on-change="handleChange" type="datetimerange" placement="bottom-end" 
                            placeholder="Select date" style="width: 350px">
                        </DatePicker>
                    </Col>
                </Row>
                <br>
                <Row>
                    <Col span="8">
                        <span>用户所在部门 : </span><Select v-model="department1" clearable style="width:200px" @on-change="departmentChange">
                            <Option value="登录">登录</Option>
                            <Option value="请求" >请求</Option>
                        </Select>
                    </Col>
                    <Col span="8">
                        <span>用户名称 : </span><Input clearable style="width: 200px" v-model="name1" placeholder="支持模糊查询" />
                    </Col>
                </Row>
                <br>
                <Row>
                        <Col span="8">
                            <span>起始用户ID  ：</span>
                    
                                <Input clearable style="width: 200px" v-model="beginId1" placeholder="请输入搜索用户范围开始ID" number/>
                        
                        </Col>
                        <Col span="8">
                                <span>结束用户ID ：</span>
                                <Input clearable style="width: 200px" v-model="endId1" placeholder="请输入搜索用户范围结束ID" number/>
                        </Col>
                </Row>
                <br>
                <Row>
                    <Col span="3">
                        <Button  type="info" @click = "limitSearch">开始查询</Button>
                    </Col>
                    <Col span="3">
                        <Button  type="info" @click = "reSet">重置</Button>
                    </Col>
                </Row>
            </Card>
            <Card>
                <Row type="flex" justify="center">
                    <Col>
                        <Transfer
                            :titles="transfertitle" 
                            :list-style="list_style"
                            :data="data1"
                            :target-keys="targetKeys1"
                            :render-format="render1"
                            @on-change="transferChange">
                        </Transfer>
                    </Col>
                </Row>
            </Card>
      </Modal>
    </div>
</template>

<script>
const editButton = (vm, h, currentRow, index) => {
  return h(
    "Button",
    {
      props: {
        type: currentRow.editting ? "success" : "primary",
        size: "small",
        loading: currentRow.saving
      },
      style: {
        margin: "0 5px"
      },
      on: {
        click: () => {
          if (!currentRow.editting) {
            if (currentRow.edittingCell) {
              for (let name in currentRow.edittingCell) {
                currentRow.edittingCell[name] = false;
                vm.edittingStore[index].edittingCell[name] = false;
              }
            }
            vm.edittingStore[index].editting = true;
            vm.thisTableData = JSON.parse(JSON.stringify(vm.edittingStore));
          } else {
            vm.edittingStore[index].saving = true;
            vm.thisTableData = JSON.parse(JSON.stringify(vm.edittingStore));
            let edittingRow = vm.edittingStore[index];
            console.log(
              "第一个edittingRow" + JSON.stringify(vm.edittingStore[index])
            );
            edittingRow.editting = false;
            edittingRow.saving = false;
            //通过vm调用外面的方法
            //vm.updateEdittingRow(edittingRow)
            vm.$axios
              .request({
                url: "/updateMenu",
                method: "put",
                data: edittingRow
              })
              .then(function(response) {
                var daa = response.data;
                console.log("这是 daa" + JSON.stringify(daa));
                let result = daa;
                if (result > 0) {
                  vm.$Message.success("保存成功");
                } else {
                  vm.$Message.error("保存失败");
                }
              });
            vm.thisTableData = JSON.parse(JSON.stringify(vm.edittingStore));
            console.log("vm.thisTableData:" + JSON.stringify(vm.thisTableData));
            vm.$emit("input", vm.handleBackdata(vm.thisTableData));
            vm.$emit("on-change", vm.handleBackdata(vm.thisTableData), index);
            //这里点击保存之后产生的操作。
            var stf = JSON.stringify(vm.thisTableData);
            console.log(stf);
            console.log("canEditTable 50 line  //这里点击保存之后产生的操作。");
          }
        }
      }
    },
    currentRow.editting ? "保存" : "编辑"
  );
};
const deleteButton = (vm, h, currentRow, index) => {
  return h(
    "Poptip",
    {
      props: {
        confirm: true,
        title: "确定删除本菜单，以及该菜单下的所有子菜单吗？",
        transfer: true
      },
      on: {
        "on-ok": () => {
          vm.on_ok_delete(currentRow.menu_id);
        }
      }
    },
    [
      h(
        "Button",
        {
          style: {
            margin: "0 5px"
          },
          props: {
            type: "error",
            size: "small",
            placement: "top"
          }
        },
        "删除"
      )
    ]
  );
};
const diliverButton = (vm, h, currentRow, index) => {
  return h(
    "Poptip",
    {
      props: {
        confirm: true,
        title: "确定为本菜单分配人员？",
        transfer: true
      },
      on: {
        "on-ok": () => {
          //vm.on_ok_delete(currentRow.menu_id);
          vm.data1 = [];
          vm.targetKeys1 = [];
          vm.on_ok_diliver(currentRow.menu_id, currentRow.name);
        }
      }
    },
    [
      h(
        "Button",
        {
          style: {
            margin: "0 5px"
          },
          props: {
            type: "primary",
            size: "small",
            placement: "top"
          }
        },
        "分配人员"
      )
    ]
  );
};
const incellEditBtn = (vm, h, param) => {
  if (vm.hoverShow) {
    return h(
      "div",
      {
        class: {
          "show-edit-btn": vm.hoverShow
        }
      },
      [
        h("Button", {
          props: {
            type: "text",
            icon: "edit"
          },
          on: {
            click: event => {
              vm.edittingStore[param.index].edittingCell[
                param.column.key
              ] = true;
              vm.thisTableData = JSON.parse(JSON.stringify(vm.edittingStore));
            }
          }
        })
      ]
    );
  } else {
    return h("Button", {
      props: {
        type: "text",
        icon: "edit"
      },
      on: {
        click: event => {
          vm.edittingStore[param.index].edittingCell[param.column.key] = true;
          vm.thisTableData = JSON.parse(JSON.stringify(vm.edittingStore));
        }
      }
    });
  }
};
const saveIncellEditBtn = (vm, h, param) => {
  return h("Button", {
    props: {
      type: "text",
      icon: "checkmark"
    },
    on: {
      click: event => {
        vm.edittingStore[param.index].edittingCell[param.column.key] = false;
        vm.thisTableData = JSON.parse(JSON.stringify(vm.edittingStore));
        vm.$emit("input", vm.handleBackdata(vm.thisTableData));
        vm.$emit(
          "on-cell-change",
          vm.handleBackdata(vm.thisTableData),
          param.index,
          param.column.key
        );
      }
    }
  });
};
const cellInput = (vm, h, param, item) => {
  return h("Input", {
    props: {
      type: "text",
      value: vm.edittingStore[param.index][item.key]
    },
    on: {
      "on-change"(event) {
        let key = item.key;
        vm.edittingStore[param.index][key] = event.target.value;
      }
    }
  });
};
export default {
  name: "canEditTable",
  props: {
    refs: String,
    columnsList: Array,
    value: Array,
    url: String,
    editIncell: {
      type: Boolean,
      default: false
    },
    hoverShow: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      //穿梭框
      list_style: {
        height: "400px",
        width: "400px"
      },
      transfertitle: ["未拥有菜单权限得人员列表", "已拥有菜单权限得人员列表"],
      // data1: this.getMockData(),
      // targetKeys1: this.getTargetKeys(),
      data1: [],
      targetKeys1: [],
      temp: [],
      deparet_name: [],
      //查询后返回的所有userIds
      allUserIds: [],
      //用户修改后，不具有权限的userIds
      whithPermsUserIds: [],
      ModalTitle: "",
      searchConName2: "",
      searchConTel2: "",
      columns: [],
      thisTableData: [],
      edittingStore: [],
      goingDelete: [],
      //模态框状态
      showModal: false,
      //选择框参数
      name1: "",
      department1: "",
      status1: "",
      startdate1: "",
      enddate1: "",
      beginId1: "",
      endId1: "",
      menu_name: "",
      //最终条件查询发送的参数
      menu_id: -1,
      name: "",
      department: -1,
      status: -1,
      startdate: "",
      enddate: "",
      beginId: -1,
      endId: -1,

      //搜索框搜索结果
      searchResult: []
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      let vm = this;
      let editableCell = this.columnsList.filter(item => {
        if (item.editable) {
          if (item.editable === true) {
            return item;
          }
        }
      });
      let cloneData = JSON.parse(JSON.stringify(this.value));
      let res = [];
      res = cloneData.map((item, index) => {
        let isEditting = false;
        if (this.thisTableData[index]) {
          if (this.thisTableData[index].editting) {
            isEditting = true;
          } else {
            for (const cell in this.thisTableData[index].edittingCell) {
              if (this.thisTableData[index].edittingCell[cell] === true) {
                isEditting = true;
              }
            }
          }
        }
        if (isEditting) {
          return this.thisTableData[index];
        } else {
          this.$set(item, "editting", false);
          let edittingCell = {};
          editableCell.forEach(item => {
            edittingCell[item.key] = false;
          });
          this.$set(item, "edittingCell", edittingCell);
          return item;
        }
      });
      this.thisTableData = res;
      this.edittingStore = JSON.parse(JSON.stringify(this.thisTableData));
      this.columnsList.forEach(item => {
        if (item.editable) {
          item.render = (h, param) => {
            let currentRow = this.thisTableData[param.index];
            if (!currentRow.editting) {
              if (this.editIncell) {
                return h(
                  "Row",
                  {
                    props: {
                      type: "flex",
                      align: "middle",
                      justify: "center"
                    }
                  },
                  [
                    h(
                      "Col",
                      {
                        props: {
                          span: "22"
                        }
                      },
                      [
                        !currentRow.edittingCell[param.column.key]
                          ? h("span", currentRow[item.key])
                          : cellInput(this, h, param, item)
                      ]
                    ),
                    h(
                      "Col",
                      {
                        props: {
                          span: "2"
                        }
                      },
                      [
                        currentRow.edittingCell[param.column.key]
                          ? saveIncellEditBtn(this, h, param)
                          : incellEditBtn(this, h, param)
                      ]
                    )
                  ]
                );
              } else {
                return h("span", currentRow[item.key]);
              }
            } else {
              return h("Input", {
                props: {
                  type: "text",
                  value: currentRow[item.key]
                },
                on: {
                  "on-change"(event) {
                    let key = param.column.key;
                    vm.edittingStore[param.index][key] = event.target.value;
                  }
                }
              });
            }
          };
        }
        if (item.handle) {
          item.render = (h, param) => {
            let currentRowData = this.thisTableData[param.index];
            let children = [];
            item.handle.forEach(item => {
              if (item === "edit") {
                children.push(editButton(this, h, currentRowData, param.index));
              } else if (item === "delete") {
                children.push(
                  deleteButton(this, h, currentRowData, param.index)
                );
              } else if (item === "diliver") {
                children.push(
                  diliverButton(this, h, currentRowData, param.index)
                );
              }
            });
            return h("div", children);
          };
        }
      });
    },
    on_ok_diliver(currentRow_menu_id, currentRow_name) {
      this.showModal = true;
      console.log(
        "################3id##################" +
          currentRow_menu_id +
          currentRow_name
      );
      this.menu_id = currentRow_menu_id;
      this.menu_name = currentRow_name;
      this.ModalTitle = "";
      this.ModalTitle =
        "当前菜单是：>>>" + this.menu_name + " 菜单ID是： >>>" + this.menu_id;
    },

    on_ok_delete(currentRow_menu_id) {
      //获取当前点击了删除行的，行所在数据的menu_id
      let _this = this;
      var deleteArray = new Array();
      deleteArray.push(currentRow_menu_id);

      return this.$axios
        .request({
          url: "/batchdeleteMenu",
          method: "delete",
          data: {
            deleteMenuList: deleteArray
          }
        })
        .then(function(daa) {
          _this.goingDelete = [];

          console.log("这是 daa" + JSON.stringify(daa));
          console.log("这是菜单Id" + currentRow_menu_id);
          let result = daa.data.result;
          console.log(daa.data.deleteArray[0]);
          console.log(
            "daa.data.deleteArray" + JSON.stringify(daa.data.deleteArray)
          );

          for (var i = 0, ilen = daa.data.deleteArray.length; i < ilen; i++) {
            console.log("iiiiiii=" + daa.data.deleteArray[i]);

            for (var j = 0, len = _this.thisTableData.length; j < len; j++) {
              // console.log("menuid"+ _this.thisTableData[j].menu_id);
              if (daa.data.deleteArray[i] == _this.thisTableData[j].menu_id) {
                _this.goingDelete.push(_this.thisTableData[j]);
                _this.thisTableData.splice(j, 1);
                len = len - 1;
              }
            }
          }
          _this.$emit("fleshTree", {});
          _this.$Message.success({
            content:
              "成功删除" +
              result +
              "条菜单数据" +
              "\n" +
              "删除的菜单ID有以下" +
              daa.data.deleteArray +
              "\n",
            duration: 10,
            closable: true
          });
        });
    },
    handleBackdata(data) {
      let clonedData = JSON.parse(JSON.stringify(data));
      clonedData.forEach(item => {
        delete item.editting;
        delete item.edittingCell;
        delete item.saving;
      });
      return clonedData;
    },

    render1(item) {
      return item.label;
    },
    transferChange(newTargetKeys, direction, moveKeys) {
      console.log(newTargetKeys);
      console.log(direction);
      console.log(moveKeys);
      this.targetKeys1 = newTargetKeys;
      this.whithPermsUserIds = newTargetKeys;
      console.log("**************");
      console.log(this.whithPermsUserIds);
    },

    //多条件搜索框得method
    handleChange(date) {
      this.startdate1 = date[0];
      this.enddate1 = date[1];
    },
    reSet() {
      this.name1 = "";
      this.department1 = "";
      this.status1 = "";
      this.startdate1 = "";
      this.enddate1 = "";
      this.beginId1 = "";
      this.endId1 = "";
    },
    //模态框提交
    handleSubmit() {
      let _this = this;

      let aa = {
        menuId: this.menu_id,
        allUserIds: this.allUserIds,
        whithPermsUserIds: this.whithPermsUserIds
      };
      console.log("保存操作");
      console.log(this.whithPermsUserIds);
      console.log(this.allUserIds);
      //_this.postAsync()
      this.$axios
        .request({
          url: "/UserMenu/updateUserMenu",
          method: "post",
          data: aa
        })
        .then(function(response) {
          if (response.data.code == 0) {
            _this.$Message.success(response.data.msg);
          } else {
            _this.$Message.error("保存失败,请重试");
          }
        });
    },

    // //条件查询
    limitSearch() {
      this.data1 = [];
      this.targetKeys1 = [];
      if (
        this.name1 != "" &&
        this.department1 != "" &&
        this.status1 != "" &&
        this.startdate1 != "" &&
        this.enddate1 != "" &&
        this.beginId1 != "" &&
        this.endId1 != ""
      ) {
        //执行全查询
        this.getLimitData();
      } else {
        console.log(typeof this.beginId1);
        if (this.name1 != "") this.name = this.name1;
        if (this.department1 != "") this.department = this.department1;
        if (this.status1 != "") this.status = this.status1;
        if (this.beginId1 != "") {
          this.beginId = this.beginId1;
          if (typeof this.beginId1 != "number") {
            vm.$Message.error("起始用户ID 只能是数字");
            return 0;
          }
        }

        if (this.endId1 != "") {
          this.endId = this.endId1;
          if (typeof this.endId1 != "number") {
            vm.$Message.error("结束用户ID 只能是数字");
            return 0;
          }
        }
        console.log("$$$$$$$$$$$$测试break是否友校$$$$$$$$$$$$$$$");
        if (this.startdate1 != "") this.startdate = this.startdate1;
        if (this.enddate1 != "") this.enddate = this.enddate1;
        this.getLimitData();
        this.name = "";
        this.department = -1;
        this.status = -1;
        this.startdate = "";
        this.enddate = "";
        this.beginId = -1;
        this.endId = -1;
      }
    },
    //获取用户权限
    getLimitData() {
      let _this = this;
      (this.allUserIds = []), (this.whithPermsUserIds = []);
      // alert("开始获取 Limit User");
      let aa = {
        menuId: this.menu_id,
        department: this.department,
        status: this.status,
        name: this.name,
        endId: this.endId,
        beginId: this.beginId,
        startdate: this.startdate,
        enddate: this.enddate
      };
      this.$axios
        .request({
          method: "get",
          url: "/org/info"
        })
        .then(function(response) {
          _this.deparet_name = response.data;
          console.log(JSON.stringify(_this.deparet_name));
        });
      //this.limitRunAsync2()
      this.$axios
        .request({
          method: "post",
          url: "/getUserByLimit",
          data: aa
        })
        .then(function(result) {
          var response = result.data;
          console.log("条件查询结果" + JSON.stringify(response));
          _this.searchResult = response.result;

          console.log(JSON.stringify(_this.searchResult));
          for (var i = 0, len = _this.searchResult.length; i < len; i++) {
            console.log("长度 :" + _this.searchResult.length + " i :" + i);
            for (var j = 0, len1 = _this.deparet_name.length; j < len1; j++) {
              if (_this.searchResult[i].deptId == _this.deparet_name[j].id) {
                var department_name = _this.deparet_name[j].title;
              }
            }

            _this.allUserIds.push(_this.searchResult[i].userId);
            _this.data1.push({
              key: _this.searchResult[i].userId,
              label:
                "工号为" +
                _this.searchResult[i].userId +
                department_name +
                _this.searchResult[i].name,
              description: "未拥有菜单权限" + _this.searchResult[i].name
            });
            if (_this.searchResult[i].hasAuthority == true) {
              _this.targetKeys1.push(_this.searchResult[i].userId);
            }
          }
          console.log("data1");
          console.log(_this.data1);
          console.log("targetKeys1");
          console.log(_this.targetKeys1);
          _this.reSet();
        });
    },

    //用户状态框选择
    statusChange(value) {
      this.status1 = value;
    },

    //所属部门选择框变动函数
    departmentChange(value) {
      this.department1 = value;
    }
  },

  watch: {
    value(data) {
      this.init();
    }
  }
};
</script>

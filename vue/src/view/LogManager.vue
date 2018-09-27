<template>
<div>
  <Card>
      <Row>
           <Col span="8">
                <span>用户ID : </span>
                <Input clearable style="width: 200px" v-model="userid1" placeholder="请输入菜单名称" />
           </Col>
           <Col span="16">
                <span>日志的记录时间范围 : </span>
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
                <span>用户操作选择 : </span><Select v-model="model8" clearable style="width:200px" @on-change="userOpeartionChange">
                    <Option value="登录">登录</Option>
                    <Option value="操作" >全部操作</Option>
                    <Option value="查询" >查询</Option>
                    <Option value="修改" >修改</Option>
                    <Option value="添加" >添加</Option>
                    <Option value="删除" >删除</Option>
                </Select>
            </Col>
            <Col span="8">
                <span>用户名称 : </span><Input clearable style="width: 200px" v-model="name1" placeholder="支持模糊查询" />
            </Col>
        </Row>
        <br>
        <Row>
            <Col span="8">
                <span>日志起始ID  ：</span>
                <Input clearable style="width: 200px" v-model="beginId1" placeholder="请输入日志范围开始ID"/>
            </Col>
            <Col span="8">
                <span>日志结束ID ：</span>
                <Input clearable style="width: 200px" v-model="endId1" placeholder="请输入日志范围结束ID"/>
            </Col>
            <Col span="3">
                <Button  type="info" @click = "limitSearch">开始查询</Button>
            </Col>
            <Col span="3">
                <Button  type="info" @click = "reSet">重置</Button>
            </Col>
        </Row>
  </Card>
  <Card>
        <div>
            <Row>
                <Table @on-selection-change = "selected" @on-select-all = "selectAll" border ref="selection" :columns="columns4" :data="data1" ></Table>
            </Row>
            <br>
            <Row>
                <Page :total="logCount" show-elevator show-total @on-change="handlePageChange" ref="page">
                </Page>
            </Row> 
            <br>   
            <Row :gutter="4">
                <Col span="2">
                <Button @click="handleSelectAll(true)">全选</Button>
                </Col>
                <Col span="3">
                <Button @click="handleSelectAll(false)">取消全选</Button>
                </Col>
                <Col span="5">
                <Button  type="info" @click = "sureDelete">删除选择完成</Button>
                </Col>
            </Row>
    </div>
  </Card>
</div>
</template>
<script>
import * as excel from "./data/table2excel.js";
export default {
  data() {
    return {
      //显示的表格数据，列表名称
      data1: [],
      newdata1: [],
      selectData: [],
      flag: "",
      afterdelete: [],
      columns4: excel.LogColumns,
      //自定义搜索框的数据
      userid1: "",
      userOpeartion1: "",
      name1: "",
      startdate1: "",
      enddate1: "",
      beginId1: "",
      endId1: "",
      //自定义搜索框的数据
      //自定义发送的数据
      userid: 0,
      userOpeartion: "",
      name: "",
      startdate: "",
      enddate: "",
      beginId: -1,
      endId: -1,
      //自定义发送的数据

      //选择框moval 18
      model8: "",

      //定义需要删除的menuidList
      deleteMenuList: [],
      
      logCount: 0,
      condition: {}
    };
  },
  methods: {
    //获取下拉框选择后的数据
    userOpeartionChange: function(value) {
      this.userOpeartion1 = value;
    },
    //确认框
    sureDelete() {
      this.$Modal.confirm({
        title: "确定删除？",
        onOk: () => {
          this.batchDeleteData();
        },
        onCancel: () => {}
      });
    },
    //进行全选
    handleSelectAll(status) {
      this.$refs.selection.selectAll(status);
    },
    //获取用户勾选的数据
    selected: function(selection) {
      this.deleteMenuList = [];

      this.selectData = selection;
      let _this = this;
      for (var i in selection) {
        this.deleteMenuList.push(selection[i].id);
      }

    },
    selectAll: function(selection) {
      this.selectData = selection;
    },

    handleChange(date) {
      this.startdate1 = date[0];
      this.enddate1 = date[1];
    },
    //进行条件重置
    reSet() {
      this.userid1 = "";
      this.name1 = "";
      this.startdate1 = "";
      this.enddate1 = "";
      this.beginId1 = "";
      this.endId1 = "";
      this.userOperation1 = "";
    },

    //进行条件搜索
    limitSearch() {
      if (this.name1 != null && this.name1 != "") this.name = this.name1;
      if (this.userOpeartion1 != null && this.userOpeartion1 != "")
        this.userOpeartion = this.userOpeartion1;
      if (this.userid1 != null && this.userid1 != "")
        this.userid = this.userid1;
      if (this.beginId1 != "" && this.beginId1 != null)
        this.beginId = this.beginId1;
      if (this.endId1 != null && this.endId1 != "") this.endId = this.endId1;
      if (this.startdate1 != null && this.startdate1 != "")
        this.startdate = this.startdate1;
      if (this.enddate1 != null && this.enddate1 != "")
        this.enddate = this.enddate1;
      this.getLimitData();
      this.id = 0;
      this.userOpeartion = "";
      this.name = "";
      this.startdate = "";
      this.enddate = "";
      this.beginId = -1;
      this.endId = -1;
    },

    //根据查询条件获取对应的数据
    getLimitData() {
      let _this = this;
      let aa = {
        name: this.name,
        id: this.id,
        parent_id: this.parent_id,
        endId: this.endId,
        beginId: this.beginId,
        startdate: this.startdate,
        enddate: this.enddate,
        userOperation: this.userOpeartion,
        page: 1
      };
      this.$axios.request({
        method: "post",
        url: "/logCount",
        data: aa
      }).then((r) => {
        console.log(r.data)
        if (r.data['code'] === 0) {
          _this.logCount = r.data['count']
        } else {
          _this.$Message.error(r.data['msg'])  
        }
      }, (e) => {
        console.log(e)
        _this.$Message.error('发送失败')
      })
      _this.condition = aa
      this.$axios
        .request({
          method: "post",
          url: "/getLoglist",
          data: aa
        })
        .then(function(response) {
          _this.data1 = response.data.result
        })
    },

    //删除数据
    batchDeleteData() {
      let currentPage = this.$refs.page.currentPage
      let _this = this;
      this.$axios.request({
        url: "/batchdeleteLog",
        method: "delete",
        data: {
          deleteMenuList: this.deleteMenuList
        }
      }).then((response) => {
        if (response.data.result == 0) {
          _this.$Message.error(response.data.message + "\n" + "该信息不存在");
        } else {
          _this.$Message.success(
            response.data.message +
              "\n" +
              "共删除" +
              response.data.result +
              "条数据"
          )
        }
        _this.$axios.request({
          method: "post",
          url: "/logCount",
          data: _this.condition
        }).then((r) => {
          // console.log(r.data)
          if (r.data['code'] === 0) {
            _this.logCount = r.data['count']
            _this.handlePageChange(currentPage)
          } else {
            _this.$Message.error(r.data['msg'])  
          }
        }, (e) => {
          console.log(e)
          _this.$Message.error('发送失败')
        })
      }, (e) => {
        console.log(e)
        this.$Message.error('发送失败')
      })
    },
    handlePageChange (page) {
      let _this = this 
      this.condition['page'] = page
      this.$axios
      .request({
        method: "post",
        url: "/getLoglist",
        data: this.condition
      })
      .then((response) => {
        if (response.data['result'].length === 0 && page > 1) {
          this.handlePageChange(page - 1)
        } else {
          _this.data1 = response.data.result
        }
      }, (e) => {
        console.log(e)
        this.$Message.error('发送失败')
      })
    }
  },
  mounted () {
  }
};
</script>

<template>
<div>
  <Card>
      <Row>
           <Col span="8">
                <span>菜单ID : </span>
                <Input clearable style="width: 200px" v-model="id1" placeholder="请输入菜单ID" number/>
           </Col>
           <Col span="16">
                <span>选择菜单创建时间范围 : </span>
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
                <span>菜单父ID : </span><Input clearable style="width: 200px" v-model="parent_id1" placeholder="请输入菜单名称" number/>
            </Col>
            <Col span="8">
                <span>菜单名称 : </span><Input clearable style="width: 200px" v-model="name1" placeholder="支持模糊查询" />
            </Col>
        </Row>
        <br>
        <Row>
            <Col span="8">
                <span>起始ID</span>
                <Input clearable style="width: 200px" v-model="beginId1" placeholder="请输入菜单范围开始ID" number/>
            </Col>
            <Col span="8">
                <span>结束ID</span>
                <Input clearable style="width: 200px" v-model="endId1" placeholder="请输入菜单范围结束ID" number/>
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
            <Row :gutter="4">
                <Col span="2">
                <Button @click="handleSelectAll(true)">全选</Button>
                </Col>
                <Col span="3">
                <Button @click="handleSelectAll(false)">取消全选</Button>
                </Col>
                <Col span="3">
                <a id="hrefToExportTable" style="postion: absolute;left: -10px;top: -10px;width: 0px;height: 0px;"></a>
                <Button  type="info" @click = "changeTableData">导出选择完成</Button>
                </Col>
                <Col span="5">
                <Button  type="info" @click = "sureDelete">批量删除选择完成</Button>
                </Col>
            </Row>
    </div>
  </Card>
   <!--导出excel 的表单模态框-->
    <Modal
        @on-ok="handleExport"
        title="下面是将要导出的数据，请再次确认"
        ok-text="确认导出到excel"
        v-model="exportModal"
        width="800"
        :styles="{top: '20px'}">
        <Row>
            <Table stripe :columns="exportTableColumns2" :data="selectData" ref="tableExcel"></Table>
        </Row>
    </Modal>
</div>
</template>
<script>
import  * as excel from './data/table2excel.js';
import table2excel from '@/libs/table2excel.js';
    export default {
        data () {
            return {
                //定义需要导出的数据到excel需要用到的变量
                selectData:[],
                exportTableColumns: excel.exportTableColumns,
                exportTableColumns2: excel.exportTableColumns2,
                excelFileName:'',
                //显示的表格数据，列表名称
                data1: [],
                newdata1: [],
                columns4: excel.exportTableColumns,
                //自定义搜索框的数据
                id1: '',
                parent_id1: '',
                name1: '',
                startdate1 : '',
                enddate1 : '',
                beginId1 : '',
                endId1 : '',
                //自定义搜索框的数据
                //自定义发送的数据
                id: 0,
                parent_id: -1,
                name: '',
                startdate : '',
                enddate : '',
                beginId : -1,
                endId : -1,
                //自定义发送的数据
                
                //控制模态框是否显示
                exportModal: false,

                //定义需要删除的menuidList
                deleteMenuList: [],
            };
        },
        methods: {
            exportExcel () {
                table2excel.transform(this.$refs.tableExcel, 'hrefToExportTable', this.excelFileName);
            },
             sureDelete(){
                this.$Modal.confirm({
                    title: '确定删除？',
                    onOk: () => {
                        this.batchDeleteData();
                    },
                    onCancel: () => {
                    }
                });
            },
            handleSelectAll (status) {
                this.$refs.selection.selectAll(status);
            },

            //获取用户勾选的数据
            selected:function(selection ){
                this.deleteMenuList = [];
                 
                this.selectData = selection ;
                let _this = this;
                for(var i in selection){
                    console.log("id"+selection[i].menu_id);
                   // _this.deleteMenuList.push(selection[i].menu_id);
                   //获取被选择的menu 的id ，组成list
                   this.deleteMenuList.push(selection[i].menu_id);
                }
                console.log(JSON.stringify(this.deleteMenuList));
                //this.deleteMenuList.push(row.menu_id);
                //console.log(JSON.stringify(this.selectData));

            },
            selectAll:function(selection){
               
                this.selectData = selection;
               
               
            },
            handleExport(){
                table2excel.transform(this.$refs.tableExcel, 'hrefToExportTable', this.excelFileName);
            
            },
            handleChange(date){
            this.startdate1 = date[0];
            this.enddate1 = date[1];
             
            },
            //选择完成后，展示模态框
            changeTableData(){
                
                this.exportModal = true;
            },
        
            //进行条件重置
            reSet(){
                this.id1 = '';
                this.name1 = '';
                this.startdate1 = '';
                this.enddate1 = '';
                this.beginId1 = '';
                this.endId1 = '';
                this.parent_id1 ='';
            },

            //进行条件搜索
            limitSearch(){
                 
                
                console.log(this.name+this.id+this.enddate+this.startdate+this.beginId+this.endId);
                if(this.id1  =='' && this.parent_id1 == '' 
                    && this.name1  == '' && this.startdate1  == '' 
                    && this.enddate1  == ''
                    && this.beginId1 == '' && this.endId1 == ''){
                        //如果输入查询条件为空，则返回所有结果
                        this.getTableData();
                    }else{
                        if(this.name1 != '') this.name = this.name1;
                        if(this.parent_id1 != '') {
                            this.parent_id = this.parent_id1;
                            if((typeof this.parent_id11 != 'number')){
                                this.$Message.error("请注意：父菜单ID只能是数字");
                                return 0;
                            }
                        }
                        if(this.id1 != '') 
                        {
                            this.id = this.id1;
                            if((typeof this.id1 != 'number')){
                                this.$Message.error("请注意：菜单ID只能是数字");
                                return 0;
                            }
                        };
                        
                        if(this.beginId1 != '' ) 
                            {
                                this.beginId = this.beginId1;
                                if((typeof this.beginId1 != 'number')){
                           
                                    this.$Message.error("起始用户ID 只能是数字");
                                    return 0;
                                }
                            } ;

                        if(this.endId1 != '' ) 
                            {
                                this.endId = this.endId1;
                                if( typeof this.endId1 != 'number'){
                                    this.$Message.error("结束用户ID 只能是数字");
                                    return 0;
                                }
                            } 
                        // if(this.beginId1 != '') this.beginId = this.beginId1;
                        // if(this.endId1 != '') this.endId = this.endId1;
                        if(this.startdate1 != '') this.startdate = this.startdate1;
                        if(this.enddate1 != '') this.enddate = this.enddate1;
                        console.log("name"+this.name);
                        console.log("id"+this.id);
                        console.log("beginid"+this.beginId);
                        console.log("endid"+this.endId);
                        console.log("sdate"+this.startdate);
                        console.log("edate"+this.enddate);
                        this.getLimitData();
                        this.id = 0;
                        this.parent_id = -1;
                        this.name = '';
                        this.startdate = '';
                        this.enddate  = '';
                        this.beginId = -1;
                        this.endId = -1;
                        
                    }
                },
            getTableData(){
                let _this = this;
                //获取菜单列表数据构建表格
                return this.$axios.request({
                    method: "get",
                    url: "/getMenuList"
                })
                    .then(function(menu_table_data){
                        console.log("菜单表格数据"+JSON.stringify(menu_table_data.data));
                        _this.data1 = menu_table_data.data;
                        
                    })
                    .catch(err => {
                    console.log(err)
                    });
                },
            
            //根据查询条件获取对应的数据
            getLimitData(){
                let _this = this;
                //获取菜单列表数据构建表格
               // this.limitRunAsync2()
                     let aa = {
                            name : this.name,
                            id : this.id,
                            parent_id: this.parent_id,
                            endId : this.endId,
                            beginId : this.beginId,
                            startdate : this.startdate,
                            enddate : this.enddate,
                        }
                  this.$axios.request({
                      method : 'post',
                      url: '/getLimitMenuList',
                      data: aa,
                  })
               
                    .then(function(response){
                        console.log("条件查询结果"+JSON.stringify(response.data));
                        _this.$Message.success(response.data.message);
                        _this.data1 = response.data.result;
                        
                    });
                    
            },
            

            //删除数据
            batchDeleteData(){
                console.log("开始批量删除");
                let _this = this;
                //获取菜单列表数据构建表格
                
                this.$axios.request({
                      method : 'delete',
                      url: '/batchdeleteMenu',
                      data: {
                            deleteMenuList: this.deleteMenuList
                        }
                    })
                    .then(function(response){
                       // console.log(JSON.stringify(response))
                        if(response.data.result > 0){
                            _this.$Message.success("成功删除"+response.data.result+"条数据"+"\n"
                            +"分别有一下："+response.data.deleteArray);
                        }
                        let newList = []
                        _this.data1.map((v, i) => {
                            var flag = false
                            _this.deleteMenuList.map((v, j) => {
                                if (_this.data1[i]['menu_id'] === _this.deleteMenuList[j]) {
                                    flag = true
                                }
                            })
                            if (!flag) {
                                newList.push(_this.data1[i])
                            } 
                        })
                        console.log(newList)
                        _this.data1.splice(0, _this.data1.length)
                        newList.map((v, i) => {
                            _this.data1.push(v)
                        })
                    });
            },
           

        }
    }
</script>

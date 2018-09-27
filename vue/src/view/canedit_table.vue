<style lang="less">
    .show-edit-btn{
    display: none;
    margin-left: -10px;
}
.ivu-table-cell:hover .show-edit-btn{
    display: inline-block;
}
.margin-top-8{
    margin-top: 8px;
}
.margin-top-10{
    margin-top: 10px;
}
.margin-top-20{
    margin-top: 20px;
}
.margin-left-10{
    margin-left: 10px;
}
.margin-bottom-10{
    margin-bottom: 10px;
}
.margin-bottom-100{
    margin-bottom: 100px;
}
.margin-right-10{
    margin-right: 10px;
}
.padding-left-6{
    padding-left: 6px;
}
.padding-left-8{
    padding-left: 5px;
}
.padding-left-10{
    padding-left: 10px;
}
.padding-left-20{
    padding-left: 20px;
}
.height-100{
    height: 100%;
}
.height-120px{
    height: 100px;
}
.height-200px{
    height: 200px;
}
.height-492px{
    height: 492px;
}
.height-460px{
    height: 460px;
}
.line-gray{
    height: 0;
    border-bottom: 2px solid #dcdcdc;
}
.notwrap{
    word-break:keep-all; 
    white-space:nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
.padding-left-5{
    padding-left: 10px;
}
[v-cloak]{
    display: none;
}
</style>

<template>
    <div>
        <Row :gutter="16" >
                <Col span="6">

                    <Card>
                        <Tree :data="treeData"  ref="menutree" :render="renderContent" ></Tree>
                    
                    </Card>
                </Col>

            <Col span = "18">
                <Row :gutter="16">
                    <Col span="24">
                        <Card>
                            <!--下面是搜索框 modal-->
                                <p slot="title">
                                    <Icon type="help-buoy"></Icon>
                                        请输入菜单名称部分字段进行搜索
                                </p>
                                <Row>
                                <Col span = "8">
                                    <Input  v-model="searchMenuName"  @on-enter="handleSearch2"  placeholder="输入菜单名称后，点击enter键进搜索... " style="width: 200px" />
                                </Col>
                                <Col span = "4">
                                <Button type="success" @click="handleSearch2">开始查询</Button>
                                </Col>
                                </Row>
                                <!--上面是搜索框 modal-->
                                <!--下面使用来填写增加菜单信息的模态框 modal-->
                                <Modal
                                    @on-ok="handleSubmit"
                                    title="请填写新增得菜单信息"
                                    ok-text="保存"
                                    v-model="addMenumodal"
                                    :styles="{top: '20px'}">
                                    <Row>
                                        <Col span = "24" >
                                            
                                            <Card>
                                                <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
                                                    <FormItem label="菜单名称" prop="menu_name">
                                                        <Input v-model="formValidate.menu_name" placeholder="请输入菜单名称"></Input>
                                                    </FormItem>
                                                    <FormItem label="父菜单ID" prop="parent_menu">
                                                        <Input v-model="formValidate.ModalParentMenuId" disabled/>
                                                    </FormItem>
                                                    <FormItem label="父菜单名称" prop="parent_menu_name">
                                                        <Input v-model="formValidate.ModalParentMenuName" disabled/>
                                                    </FormItem>
                                                    <FormItem label="菜单类型" prop="menu_type">
                                                         <span><InputNumber :max="2" :min="0" v-model="menu_type" ></InputNumber>"请输入菜单类型 0:目录，1:按钮/操作，2：子系统</span>
                                                    </FormItem>
                                                    <FormItem label="菜单地址" prop="menu_url">
                                                        <Input v-model="formValidate.menu_url" placeholder="请输入菜单地址url"></Input>
                                                    </FormItem>
                                                    <FormItem label="菜单权限" prop="menu_perm">
                                                        <Input v-model="formValidate.menu_perm" placeholder="请输入菜单访问权限"></Input>
                                                    </FormItem>
                                                    <FormItem>
                                                        <Button @click="reset" style="margin-left: 8px">Reset</Button>
                                                    </FormItem>
                                                </Form>
                                            </Card>
                                        </Col>
                                    </Row>
                                </Modal>
                                <!--上面使用来填写增加菜单信息的模态框 modal-->
                        
                        </Card>
                    </Col>
                </Row>


    <!--下面使用来可编辑 edit_table-->
                <Row class="margin-top-10">
                    <Col span="24">
                        <Card>
                            <div class="edittable-table-height-con">
                                <can-edit-table  ref="table2" v-model="editInlineData" :columns-list="editInlineColumns" @fleshTree='getTree'></can-edit-table>
                            </div>
                            <br>
                            <Row>
                                <Page  :total="total" show-elevator show-total @on-change="handlePageChange" ref="page">
                            </Page>
            </Row> 
                        </Card>
                    </Col>
                </Row>
            </Col>
        </Row>
    </div>
    <!--上面使用来可编辑 edit_table-->
</template>
<script>
import canEditTable from './components/tables/canEditTable.vue';
import * as table from './data/search';

export default {
    name: 'editable-table',
    components: {
        canEditTable
    },
    data () {
        return {
            //存储点击了的树节点的menuId
            onclickMenuId: '',
            
            page : 0,

            total: 0,

            //被点击的节点及其下面的子节点列表
            onClickTree: [],

            //存储点击添加树状图 得到得data
                addChildren : '',
            //模态框中的参数信息
                formValidate: {

                    menu_name: '',
                    parent_menu: '',
                    menu_url:'',
                    menu_perm:'',
                    menu_desc: '',
                    ModalParentMenuId:'',
                    ModalParentMenuName:'',
                },
                ruleValidate: {
                    menu_name: [
                        { required: true, message: '菜单名称不能为空', trigger: 'blur'  }
                    ],
                },
            //模态框中的参数信息
            buttonProps: {
                type: 'default',
                size: 'small',
            },
            //预设值的menu type
            menu_type: 1,
            addMenumodal: false,
            searchMenuName: '',
            editInlineColumns: [],
            editInlineData: [],
            initTable2: [],
            dada: [],
            treeData: [{
                menuId: 0,
                title: '菜单目录',
                expand: true,
                loading: false,
                children: [],
                checked: false,
            }],
            
            //测试用的data数据

            //判断模态框是否填写
            isEdit : false,
        };
    },
    methods: {

        // fleshTree : ()=>{
        //     let _this = this;
        //     alert("ooooooooo");
        //     getTree();
        // },
        //分页栏页面切换
        handlePageChange(page){
    
            this.page = page-1;
            this.getData(this.page);

        },
        getTree(){
             //获取树形菜单数据
            let _this = this;
            this.$axios.request({
                url:'/',
                method:'get'
                })
                .then(function(daa){
                    console.log("这是 菜单树daa"+JSON.stringify(daa));
                    _this.dada = daa.data;
                    _this.treeData[0].children = _this.dada;
                });
    
        },
        //getData用于获取edit_table需要展示的数据
        getData () {
            //this.editInlineColumns= table.columns1;
            //this.editInlineData = this.initTable2  = table.searchTable2;
            let _this = this;
            //获取菜单列表数据构建表格
            //this.runAsync2()
            this.$axios.request({
                    url: '/getMenuList/page',
                    method: 'get',
                    params: {
                        page : this.page
                    }
                })
                .then(function(response){
                    console.log(JSON.stringify(response))
                    var menu_table_data = response.data.menuDomains;
                    _this.total = response.data.total;
                    console.log("菜单表格数据"+JSON.stringify(menu_table_data));
                    _this.editInlineData = _this.initTable2 = menu_table_data;
                    _this.editInlineColumns = table.columns1;
                    console.log("initTable2"+JSON.stringify(_this.initTable2));
                });
            //获取树形菜单数据
            //this.runAsync()
            this.getTree();
        },
        

        handleNetConnect (state) {
            this.breakConnect = state;
        },
        handleLowSpeed (state) {
            this.lowNetSpeed = state;
        },
        getCurrentData () {
            this.showCurrentTableData = true;
        },
        handleDel (val, index) {
            this.$Message.success('删除了第' + (index + 1) + '行数据');
        },
        handleCellChange (val, index, key) {
            this.$Message.success('修改了第 ' + (index + 1) + ' 行列名为 ' + key + ' 的数据');
        },
        handleChange (val, index) {
            this.$Message.success('修改了第' + (index + 1) + '行数据');
        },

        /**合并jason对象  */
        mergeJson (jsonObject1, jsonObject2) {
            //let res = data;
            var count = 0;
            var resultJsonObject =[];
            for(var attr in jsonObject1){
                resultJsonObject[attr] = jsonObject1 [attr];
                console.log("check menuID:"+jsonObject1 [attr].menuId );
                count = count +1;
            }
            //用于给jasonObject2进行赋值
            var count2 = count;
            console.log("计数count= "+count);
            for(var attr in jsonObject2){
                var flag = 1;
                for ( var i = 0 ; i < count ; i++ ) {
                        if(resultJsonObject [i].menuId == jsonObject2 [attr].menuId){
                            console.log("出现ID相同"+jsonObject2 [attr].menuId);
                            flag = 0;
                            break;
                        }
                }
                if(flag == 1 ){
                    console.log("进行了赋值");
                    resultJsonObject[count2] = jsonObject2[attr];
                    count2 = count2 +1;
                }
            }
            console.log("resultObject count :"+ count +JSON.stringify(resultJsonObject));
            return resultJsonObject;
        },

        //根据条件数据查找
        search (data, argumentObj) {
                let res = data;
                let dataClone = data;
                var res2;
                for (let argu in argumentObj) {
                    console.log("argu is " + argu);
                    if (argumentObj[argu].length > 0) {
                        console.log("argumentObj[argu] is" +argumentObj[argu]);
                        res = dataClone.filter(d => {
                            console.log("d[argu].indexOf(argumentObj[argu] is " + d[argu].indexOf(argumentObj[argu]));
                            return d[argu].indexOf(argumentObj[argu]) > -1;
                            
                        });
                        dataClone = res;
                        
                    }
                }
                return res;
        },
        
        //根据输入的搜索条件进行查找
        handleSearch2 (limit) {
                 
                if(limit != null && limit !=''){
                    this.editInlineData = this.initTable2;
                    console.log("this.ediInlineData");
                    console.log(this.editInlineData);
                    
                    console.log("$$$$$$$$$$this. editInlineData"+this.editInlineData);
                    this.editInlineData = this.search(this.editInlineData, {name: this.searchMenuName});
                    this.total = this.editInlineData.length;
                }else{
             
                    this.editInlineData = this.initTable2;
                    //console.log("$$$$$$$$$$this. editInlineData"+this.editInlineData);
                    this.editInlineData = this.search(this.editInlineData, {name: limit});
    
                }
                
                
                
       },

        //用户查找

        

            //树状图render渲染，及按钮
            renderContent (h, { root, node, data }) {
                return h('span', {
                    style: {
                        display: 'inline-block',
                        width: '100%'
                    }
                },  
                    // [
                    //     h('a',{
                    //         style:{ color: 'black'},
                    //         on:{
                    //             click:() =>{
                    //                 this.clickNode(root,node,data)
                    //             }
                    //         }
                    //     }),
                    // ]

                    [
                        h('a',{
                            style:{ color: 'black'},
                            on:{
                                click:() =>{
                                    this.clickNode(root,node,data)
                                }
                            }
                        },
                        [
                            h('span',data.title)
                        ]),
                    h('span', {
                        style: {
                            display: 'inline-block',
                            float: 'right',
                            marginRight: '32px'
                        }
                    }, [
                        h('Button', {
                            props: Object.assign({}, this.buttonProps, {
                                icon: 'ios-add'
                            }),
                            style: {
                                marginRight: '8px'
                            },
                            on: {
                                click: () => { 
                                    this.append(data); }
                            }
                        }),
                        h('Button', {
                            props: Object.assign({}, this.buttonProps, {
                                icon: 'ios-remove'
                            }),
                            on: {
                                click: () => { 
                                    this.$Modal.confirm({
                                        title: '确定删除？',
                                        onOk: () => {
                                            this.remove(root, node, data);
                                        },
                                        onCancel: () => {
                                        }
                                    }); 
                                }
                            }
                        })
                    ])
                ]);
            },
            //点击树节点，产生的操作
            clickNode(root,node,data){
                let _this = this;
                console.log("****data******"+data.menuId);
                this.onclickMenuId = data.menuId;
                this.$axios.request({
                    url : '/getTreeList',
                    params : {
                        menuId : this.onclickMenuId
                    }
                })
                .then(function(result){
                            console.log(JSON.stringify(result))
                            var childrenList = [];
                            childrenList = result.data;
                            _this.editInlineData = _this.initTable2 = childrenList;
                            _this.total = _this.editInlineData.length
                            // childrenList.push(_this.onclickMenuId);
                            // _this.editInlineData = _this.initTable2;
                            // for(var i in childrenList){
                            //     for(var j in _this.editInlineData){
                            //         if(childrenList[i] == _this.editInlineData[j].menu_id){
                            //             _this.onClickTree.push(_this.editInlineData[j]);
                            //         }
                            //     }
                            // }
                            // console.log(_this.onClickTree);
                            // _this.editInlineData = _this.onClickTree;
                            // _this.onClickTree = [];
                            _this.onclickMenuId = '';
                        });
                
            },
            
            //
            addRootChildren(){
                this.formValidate.ModalParentMenuId = 0;
                this.formValidate.ModalParentMenuName = "主目录";
                this.addMenumodal = true;
            },
            append (data) {
                console.log(JSON.stringify(data));
                if(typeof data != 'number'){
                    this.reset();
       
       
                    this.addChildren = data;
                    this.formValidate.ModalParentMenuId = this.addChildren.menuId;
                    this.formValidate.ModalParentMenuName = this.addChildren.title;
                  
                  this.addMenumodal = true;
                    console.log(data);
                }else{
                   
                    const children = this.addChildren.children || [];
                   
                    children.push({
                         title: this.formValidate.menu_name,
                         expand: true,
                         menuId: data
                     });
                     this.$set(this.addChildren, 'children', children);
                     this.formValidate.menu_name = '';
                     this.formValidate.menu_url = '';
                     this.formValidate.menu_perm = '';
                     this.formValidate.menu_desc = '' ;
                }
                 
                
                
            },

            remove (root, node, data) {
                 
                if(data.menuId == 0) {
                    this.$Message.error("该目录为根目录，不能被删除")
                }else{
                    const parentKey = root.find(el => el === node).parent;
                    const parent = root.find(el => el.nodeKey === parentKey).node;
                    const index = parent.children.indexOf(data);
                    parent.children.splice(index, 1);
                    this.$refs.table2.on_ok_delete(data.menuId);
                }
                
            },

            //模态框按钮函数
            //重置模态框属性
            reset(){
                this.formValidate.menu_name = '';
                this.formValidate.menu_url = '';
                this.formValidate.menu_perm = '';
                this.formValidate.menu_desc = '' ;
                this.menu_type = 1;
            },

         handleSubmit () {
             let _this = this;
                if(this.formValidate.menu_name != '' &&  this.formValidate.menu_name != null){
                        this.isEdit = true;
                }
                if(! this.isEdit){
                    _this.$Message.error("保存失败，你没有正确填写菜单信息");
                }else{
                    let aa = {
                        parent_id : this.formValidate.ModalParentMenuId,
                        name : this.formValidate.menu_name,
                        perms : this.formValidate.menu_perm,
                        url : this.formValidate.menu_url,
                        type : this.menu_type,

                    }
                    console.log(aa);
                    // this.addAsync()
                    this.$axios.request({
                        url:'/addMenu',
                        method:'post',
                        data: aa
                    })
                        .then(function(result){
                            let response = result.data;
                            console.log(response.message);
                            if(response.result > 0){
                                //保存成功，则更新数据表，并更新树状图
                                _this.$Message.success("保存成功");
                                console.log("response" + JSON.stringify(response));
                                console.log(response.result);
                                console.log(response.domain);
                               // _this.editInlineData.push(response.domain);
                                _this.append(response.result);
                            }
                        });
                }
                console.log(this.addChildren);
        },
       
        
    },
    
    mounted () {
        this.getData();
    }
};
</script>

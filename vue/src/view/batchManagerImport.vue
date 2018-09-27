<template>
<div>
    <Card>
        <p slot="title">
              <Icon type="help-buoy"></Icon>
              如无反应，请重新刷新页面：
        </p>
        
        <input name = "file" type ="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
            ,application/vnd.ms-excel" @change=importMenuData(this)>
    </Card>
    <Card>
        <p slot="title">
              <Icon type="help-buoy"></Icon>
              上传数据结果如下：
        </p>
        <can-edit-table  refs="table2" v-model="editInlineData" 
            :columns-list="editInlineColumns"></can-edit-table>

    </Card>
</div>
</template>
<script>
import canEditTable from './components/tables/canEditTable.vue';
import * as table from './data/search';
// 这里的上传和保存逻辑在demo1-1中
    export default {
        name: 'editable-table',
        components: {
            canEditTable
        },
        data () {
            return {
                //用于表单展示
                editInlineColumns: [],
                editInlineData: [],
                initTable2 : [],
            };
        },
        methods: {

            importMenuData(obj){
                //console.log("正在上传"+obj.name);
                let _this = this;
                let param = new FormData();
                let lll = event.currentTarget.files[0];
                console.log(JSON.stringify(lll));
                param.append('file',event.currentTarget.files[0]);
                this.$axios.request({
                    url:'/doUpload',
                    method:'post',
                    data:param
                    })
                    .then(function(response){
                        let uploadResponse = response.data;
                        console.log(JSON.stringify(response))
                        if(uploadResponse.result < 0){
                             _this.$Notice.error({
                                title: "上传失败 : ",
                                duration: 0,
                                desc: uploadResponse.msg ,
                                onClose : reloadPage => {
                                             window.location.reload();
                                            },
                            });
                        } 
                        if(uploadResponse.result > 0){
                            _this.$Message.success("上传成功，共上传"+uploadResponse.result+"条数据");
                            _this.editInlineData = _this.initTable2 = uploadResponse.DomainList;
                            _this.editInlineColumns = table.columns1;
                        }else if(uploadResponse.result == 0){
                            _this.$Notice.error({
                                title: "上传失败 : ",
                                duration: 0,
                                desc: uploadResponse.result + "\n"
                                        +"**错误提示** : " + uploadResponse.errorTips
                                        +"**警告提示**: "+ uploadResponse.warn ,
                                onClose : reloadPage => {
                                             window.location.reload();
                                            },
                            });
                        }
                        
                    })
            }

        
        },
        
    }
</script>
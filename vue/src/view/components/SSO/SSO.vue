<template>
    <div>
      <Row :gutter="4">
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
import { list, deliverToken, authorize } from './../../../api/SSO.js'
export default {
  components: {
    Tables
  },
  data () {
    return {
      menuList: [],
      tableData: [],
      columns: [
        {
          title: '菜单ID',
          key: 'stringMenuId',
          sortable: true
        },
        {
          title: '名称',
          key: 'menuName',
          sortable: true
        },
        {
          title: '操作',
          key: 'handle',
          button: [
            (h, params, vm) => {
              return h('Button', {
                props: {
                },
                on: {
                  click: () => {
                    let url = params.row['menuUrl']
                    let strings = url.split('?')[1].split('&')
                    // console.log(strings)
                    let client_id = ''
                    let response_type = ''
                    let redirect_url = ''
                    strings.map((v, i) => {
                      let s = v.split('=')
                      let key = s[0]
                      let value = s[1]
                      client_id = key === 'client_id' ? value : client_id
                      response_type = key === 'response_type' ? value : response_type
                      redirect_url = key === 'redirect_url' ? value : redirect_url
                    })
                    // console.log(client_id)
                    // console.log(response_type)
                    // console.log(redirect_url)
                    authorize({
                      client_id: client_id,
                      response_type: response_type,
                      redirect_url: redirect_url
                    }).then((r) => {
                      if (r.data['code'] !== undefined && r.data['redirect_url'] !== undefined) {
                        let url = r.data['redirect_url'] + '?code=' + r.data['code']
                        window.open(url)
                      } else{
                        this.$Message.error('登录失败')
                      }
                      console.log(r)
                    }, (e) => {
                      console.log(e)
                    })
                  }
                }
              }, ['进入'])
            }
          ]
        }
      ],
    }
  },
  methods: {
  },
  mounted () {
    list(null).then((r) => {
      if (r.data['code'] !== undefined && r.data['code'] === 0) {
        let list = r.data['list']
        this.menuList = list
        // console.log(list)
        list.map((v, i) => {
          this.tableData.push({
            stringMenuId: v['menu_id'] + '',
            menuId: v['menu_id'],
            menuName: v['name'],
            menuUrl: v['url']
          })
        })
      } else {
        console.log(e)
        this.$Message.error('操作失败')
      }
    }, (e) => {
      console.log(e)
      this.$Message.error('请求失败')
    })
  }
}
</script>

<style>
</style>

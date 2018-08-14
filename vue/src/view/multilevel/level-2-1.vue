<template>
  <div>
    <div>多级菜单 -> 二级-1</div>
    <Button @click="axiosReq">获取服务端数据</Button>
    <Button @click="test">跳转</Button>
    <a href="#5">跳转</a>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div>test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="3">test</div>
    <div id="5">test</div>
  </div>
</template>
<script>
/*eslint-disable*/
import axios from 'axios'

export default {
  name: 'level_2_1',
  data() {
    return {
      res: {}
    }
  },
  methods: {
    axiosReq() {
      axios.get('http://localhost:8083/org/info', {
          params: {
            id: '1'
          }
        })
        .then(function(response) {
          console.log(response);
        })
        .catch(function(error) {
          console.log(error);
        });
    },
    test(){
      location.href = "#5"
    }
  }
}

</script>

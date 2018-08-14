<template>
  <div>
    <input type="text" v-model="input">
  </div>
</template>
<script>
/* eslint-disable */
export default {
  name: 'level_2_3',
  data() {
    return {
      input: ''
    }
  },
  watch: {
    input: function(val, oldval) {
      console.log(val)
      console.log(oldval)
    }
  }
}

</script>

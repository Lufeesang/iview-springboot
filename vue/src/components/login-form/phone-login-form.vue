<template>
  <Form ref="loginForm" :model="form" :rules="rules" @keydown.enter.native="handleSubmit">
    <FormItem prop="userPhone">
      <Row>
        <i-Col span="18">
          <Input v-model="form.userPhone" placeholder="请输入手机号">
            <span slot="prepend">
              <Icon :size="16" type="md-phone-portrait"></Icon>
            </span>
          </Input>
        </i-Col>
        <i-Col offset="1" span="4">
          <Button :class="{disabled: !this.count.canClick}" @click="countDown">{{ count.content }}</Button>
        </i-Col>
      </Row>
    </FormItem>
    <FormItem prop="code">
      <Input v-model="form.code" placeholder="请输入验证码">
        <span slot="prepend">
          <Icon :size="14" type="ios-barcode"></Icon>
        </span>
      </Input>
    </FormItem>
    <FormItem>
      <Button @click="handleSubmit" type="primary" long>登录</Button>
    </FormItem>
  </Form>
</template>
<script>
export default {
  name: 'PhoneLoginForm',
  props: {
    userPhoneRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '手机号不能为空', trigger: 'blur' }
        ]
      }
    },
    codeRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '验证码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  data () {
    return {
      form: {
        userPhone: 'admin',
        code: '',
      },
      count: {
        canClick: true,
        content: '发送',
        totalTime: 10
      }
    }
  },
  computed: {
    rules () {
      return {
        userPhone: this.userPhoneRules,
        code: this.codeRules
      }
    }
  },
  methods: {
    handleSubmit () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$emit('on-success-valid', {
            userPhone: this.form.userPhone,
            code: this.form.code
          })
        }
      })
    },
    countDown () {
      if (!this.count.canClick) return
      this.count.canClick = false
      this.count.content = this.count.totalTime + 's'
      let clock = window.setInterval(() => {
        this.count.totalTime--
        this.count.content = this.count.totalTime + 's'
        if (this.count.totalTime < 0) {
          window.clearInterval(clock)
          this.count.content = '发送'
          this.count.totalTime = 10
          this.count.canClick = true
        }
      }, 1000)
    }
  }
}
</script>
<style>
.disabled{
  background-color: #ddd;
  border-color: #ddd;
  color: #57a3f3;
  cursor: not-allowed;
}
</style>

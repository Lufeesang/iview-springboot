<style lang="less">
  @import './login.less';
</style>

<template>
  <div class="login">
    <div class="login-con">
      <Card icon="log-in" title="欢迎登录" :bordered="false">
        <div class="form-con">
          <Tabs>
            <TabPane label="普通登录">
              <login-form @on-success-valid="handleSubmit"></login-form>
              <p class="login-tip">输入任意用户名和密码即可</p>
            </TabPane>
            <TabPane label="手机登录">
              <phone-login-form @on-success-valid="handlePhoneSubmit"></phone-login-form>
            </TabPane>
          </Tabs>
        </div>
      </Card>
    </div>
  </div>
</template>

<script>
import LoginForm from '_c/login-form'
import PhoneLoginForm from './../../components/login-form/phone-login-form'
import { mapActions } from 'vuex'
export default {
  components: {
    LoginForm,
    PhoneLoginForm
  },
  methods: {
    ...mapActions([
      'handleLogin'
    ]),
    handleSubmit ({ userName, password }) {
      this.handleLogin({ userName, password }).then(res => {
        if (res.data['code'] !== 0) {
          this.$Message.error('登录失败')
        } else {
          this.$router.push({
            name: 'home'
          })
        }
      }, e => {
        this.$Message.error('请求失败')
      })
    },
    handlePhoneSubmit ({ userPhone, code }) {
      console.log(userPhone)
      console.log(code)
    }
  }
}
</script>

<style>
</style>

<template>
  <view class="login-container">
    <view class="logo-area">
      <image src="/static/logo.png" mode="aspectFit" class="logo"></image>
      <text class="app-name">法律学习平台</text>
    </view>

    <!-- 切换登录/注册的标签页 -->
    <view class="tab-header">
      <text 
        class="tab-item" 
        :class="{ active: activeTab === 'login' }"
        @click="activeTab = 'login'"
      >登录</text>
      <text 
        class="tab-item" 
        :class="{ active: activeTab === 'register' }"
        @click="activeTab = 'register'"
      >注册</text>
    </view>

    <!-- 登录表单 -->
    <view class="form-container" v-if="activeTab === 'login'">
      <view class="input-group">
        <text class="iconfont icon-user"></text>
        <input 
          type="text" 
          v-model="loginForm.username" 
          placeholder="请输入手机号/用户名"
        />
      </view>
      <view class="input-group">
        <text class="iconfont icon-password"></text>
        <input 
          type="password" 
          v-model="loginForm.password" 
          placeholder="请输入密码"
        />
      </view>
      <view class="forgot-password" @click="forgotPassword">忘记密码？</view>
      <button class="submit-btn" @click="handleLogin">登录</button>
    </view>

    <!-- 注册表单 -->
    <view class="form-container" v-if="activeTab === 'register'">
      <view class="input-group">
        <text class="iconfont icon-phone"></text>
        <input 
          type="number" 
          v-model="registerForm.phone" 
          placeholder="请输入手机号"
        />
      </view>
      <view class="input-group verification">
        <input 
          type="number" 
          v-model="registerForm.code" 
          placeholder="请输入验证码"
        />
        <button 
          class="verify-btn" 
          :disabled="isCountingDown"
          @click="sendVerificationCode"
        >
          {{ countDownText }}
        </button>
      </view>
      <view class="input-group">
        <text class="iconfont icon-password"></text>
        <input 
          type="password" 
          v-model="registerForm.password" 
          placeholder="请设置密码"
        />
      </view>
      <view class="input-group">
        <text class="iconfont icon-password"></text>
        <input 
          type="password" 
          v-model="registerForm.confirmPassword" 
          placeholder="请确认密码"
        />
      </view>
      <button class="submit-btn" @click="handleRegister">注册</button>
    </view>

    <!-- 其他登录方式 -->
    <view class="other-login">
      <view class="divider">
        <text>其他登录方式</text>
      </view>
      <view class="social-login">
        <view class="social-item">
          <text class="iconfont icon-wechat"></text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'login',
      loginForm: {
        username: '',
        password: ''
      },
      registerForm: {
        phone: '',
        code: '',
        password: '',
        confirmPassword: ''
      },
      countdown: 60,
      isCountingDown: false
    }
  },
  computed: {
    countDownText() {
      return this.isCountingDown ? `${this.countdown}s` : '获取验证码'
    }
  },
  methods: {
    handleLogin() {
      if (!this.loginForm.username || !this.loginForm.password) {
        uni.showToast({
          title: '请填写完整登录信息',
          icon: 'none'
        })
        return
      }
      
      // 模拟登录成功
      uni.showLoading({
        title: '登录中...'
      })
      
      // 这里应该是实际的登录接口调用
      setTimeout(() => {
        uni.hideLoading()
        
        // 存储登录状态和用户信息
        uni.setStorageSync('token', 'mock_token')
        uni.setStorageSync('userInfo', {
          id: '1',
          username: this.loginForm.username
        })
        
        // 显示登录成功提示
        uni.showToast({
          title: '登录成功',
          icon: 'success',
          duration: 1500
        })
        
        // 延迟跳转到首页
        setTimeout(() => {
          uni.reLaunch({
            url: '/pages/index/index'
          })
        }, 1500)
      }, 1000)
    },
    handleRegister() {
      if (!this.registerForm.phone || !this.registerForm.code || 
          !this.registerForm.password || !this.registerForm.confirmPassword) {
        uni.showToast({
          title: '请填写完整注册信息',
          icon: 'none'
        })
        return
      }
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        uni.showToast({
          title: '两次输入的密码不一致',
          icon: 'none'
        })
        return
      }
      // TODO: 实现注册逻辑
      console.log('注册表单：', this.registerForm)
    },
    sendVerificationCode() {
      if (!this.registerForm.phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        })
        return
      }
      this.isCountingDown = true
      this.countdown = 60
      const timer = setInterval(() => {
        this.countdown--
        if (this.countdown <= 0) {
          clearInterval(timer)
          this.isCountingDown = false
        }
      }, 1000)
      // TODO: 实现发送验证码逻辑
    },
    forgotPassword() {
      // TODO: 实现忘记密码逻辑
      uni.showToast({
        title: '忘记密码功能开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style>
.login-container {
  padding: 40rpx;
  min-height: 100vh;
  background-color: #fff;
}

.logo-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 60rpx 0;
}

.logo {
  width: 160rpx;
  height: 160rpx;
}

.app-name {
  margin-top: 20rpx;
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.tab-header {
  display: flex;
  justify-content: center;
  margin-bottom: 40rpx;
}

.tab-item {
  padding: 20rpx 40rpx;
  font-size: 32rpx;
  color: #666;
  position: relative;
}

.tab-item.active {
  color: #007AFF;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 40rpx;
  right: 40rpx;
  height: 4rpx;
  background-color: #007AFF;
}

.input-group {
  display: flex;
  align-items: center;
  padding: 20rpx;
  margin-bottom: 30rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
}

.input-group input {
  flex: 1;
  margin-left: 20rpx;
}

.verification {
  position: relative;
}

.verify-btn {
  position: absolute;
  right: 20rpx;
  top: 50%;
  transform: translateY(-50%);
  font-size: 24rpx;
  padding: 10rpx 20rpx;
  background-color: #007AFF;
  color: #fff;
  border-radius: 8rpx;
}

.verify-btn[disabled] {
  background-color: #ccc;
}

.forgot-password {
  text-align: right;
  font-size: 24rpx;
  color: #007AFF;
  margin-bottom: 30rpx;
}

.submit-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  background-color: #007AFF;
  color: #fff;
  border-radius: 44rpx;
  font-size: 32rpx;
  margin-top: 40rpx;
}

.other-login {
  margin-top: 80rpx;
}

.divider {
  position: relative;
  text-align: center;
  margin-bottom: 40rpx;
}

.divider text {
  background-color: #fff;
  padding: 0 20rpx;
  color: #999;
  font-size: 24rpx;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1rpx;
  background-color: #eee;
  z-index: -1;
}

.social-login {
  display: flex;
  justify-content: center;
}

.social-item {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 20rpx;
}

.social-item text {
  font-size: 40rpx;
  color: #333;
}
</style> 
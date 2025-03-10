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
      <view class="forgot-password" >
        <!-- <text class="iconfont icon-eye"></text> -->
        <uni-data-checkbox v-model="radioval" :localdata="selectedRole" @change="handleRoleChange"></uni-data-checkbox>
      </view>
      <view class="forgot-password" @click="forgotPassword">忘记密码？</view>
      <button class="primary" @click="handleLogin">登录</button>
    </view>

    <!-- 注册表单 -->
    <view class="form-container" v-if="activeTab === 'register'">
      <view class="input-group">
        <text class="iconfont icon-user"></text>
        <input 
          type="text" 
          v-model="registerForm.username" 
          placeholder="请输入用户名"
        />
      </view>
      <view class="input-group">
        <text class="iconfont icon-phone"></text>
        <input 
          type="text" 
          v-model="registerForm.phone" 
          placeholder="请输入手机号"
        />
      </view>
      <view class="input-group">
        <text class="iconfont icon-password"></text>
        <input 
          type="password" 
          v-model="registerForm.password" 
          placeholder="请输入密码"
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
import { apiRequest } from '@/utils/api'; // 引入 API 请求方法

export default {
  data() {
    return {
      activeTab: 'login',
      loginForm: {
        username: '',
        password: ''
      },
      registerForm: {
        username: '',
        phone: '',
        password: '',
        confirmPassword: ''
      },
      countdown: 60,
      isCountingDown: false,
      selectedRole: [{
					text: '用户',
					value: 0
				}, {
					text: '管理员',
					value: 1
				}],
        radioval: 0
    }
  },
  computed: {
    countDownText() {
      return this.isCountingDown ? `${this.countdown}s` : '获取验证码'
    }
  },
  methods: {
    handleRoleChange(value) {
      console.log(value.detail.value);
      this.radioval = value.detail.value
    },
    async handleLogin() {
      if (!this.loginForm.username || !this.loginForm.password) {
        uni.showToast({
          title: '请填写完整登录信息',
          icon: 'none'
        });
        return;
      }
      
      // 模拟登录成功
      uni.showLoading({
        title: '登录中...'
      });
      
      try {
        const response = await apiRequest('users/login', 'post', {
          username: this.loginForm.username,
          password: this.loginForm.password
        });
        
        if (response) {
          // 存储登录状态和用户信息
          uni.setStorageSync('token', 'mock_token'); // 这里可以替换为实际的 token
          uni.setStorageSync('userInfo', {
            id: response.id, // 这里可以替换为实际的用户 ID
            username:response.username,
            role:response.role,
            nickname:response.nickname,
            createdAt: response.createdAt, // 这里可以替换为实际的创建时间
            updatedAt:response.updatedAt,
            avatar:response.avatar
          });
          
          // 显示登录成功提示
          uni.showToast({
            title: '登录成功',
            icon: 'success',
            duration: 1500
          });
          // 当用户在单选框选择了管理员时，跳转到管理员界面
          if(response.role == "admin" && this.radioval==1){
            uni.reLaunch({
              url: '/pages/admin/dashboard'
            });
          }else{
            uni.reLaunch({
              url: '/pages/index/index'
            });
          }
          
        }else{
          uni.showToast({
            title: '用户名或密码错误',
            icon: 'error',
            duration: 1500
          });
        }
      } catch (error) {
        console.error("登录失败", error);
        uni.showToast({
          title: '登录失败，请重试',
          icon: 'none'
        });
      } finally {
        uni.hideLoading();
      }
    },
    async loginWithWeChat() {
      // 调用微信登录接口
      wx.login({
        success: (res) => {
          if (res.code) {
            // 获取用户信息
            wx.getUserInfo({
              success: (userInfoRes) => {
                const { nickName, avatarUrl } = userInfoRes.userInfo;
                this.registerForm.nickname = nickName;
                this.registerForm.phone = ''; // 这里可以通过其他方式获取用户手机号
                this.registerUser(nickName, avatarUrl);
              },
              fail: (error) => {
                console.error("获取用户信息失败", error);
              }
            });
          } else {
            console.error("微信登录失败", res.errMsg);
          }
        }
      });
    },
    registerUser(nickName, avatarUrl) {
      // 发送注册请求到后端
      this.$http.post('/api/register', { nickName, phone: this.registerForm.phone, avatarUrl })
        .then(response => {
          // 处理注册成功的逻辑
          console.log("注册成功", response);
        })
        .catch(error => {
          console.error("注册失败", error);
        });
    },
    forgotPassword() {
      // TODO: 实现忘记密码逻辑
      uni.showToast({
        title: '忘记密码功能开发中',
        icon: 'none'
      })
    },
    async handleRegister() {
      if (!this.registerForm.username || !this.registerForm.phone || 
          !this.registerForm.password || !this.registerForm.confirmPassword) {
        uni.showToast({
          title: '请填写完整注册信息',
          icon: 'none'
        });
        return;
      }
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        uni.showToast({
          title: '两次输入的密码不一致',
          icon: 'none'
        });
        return;
      }
      
      // 使用封装的 API 方法发送注册请求
      try {
        const response = await apiRequest('users/register', 'post', {
          username: this.registerForm.username,
          phone: this.registerForm.phone,
          password: this.registerForm.password
        });
        
        console.log("注册成功", response);
        uni.showToast({
          title: '注册成功',
          icon: 'success'
        });
        // 可以在这里跳转到登录页面或其他页面
      } catch (error) {
        console.error("注册失败", error);
        uni.showToast({
          title: '注册失败，请重试',
          icon: 'none'
        });
      }
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
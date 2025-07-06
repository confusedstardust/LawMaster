<template>
  <view class="login-container">
    <view class="logo-area">
      <!-- <image src="/static/logo.png" mode="aspectFit" class="logo"></image> -->
      <image src="/static/mainpic.jpg" mode="aspectFill" class="background-image"></image>
      <view class="blur-overlay"></view>
      <view class="welcome-text-1">Hello！</view>
      
      <view class="welcome-text">欢迎来到法治慧</view>
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
      <view class="forgot-password" @click="activeTab = 'forgot'">忘记密码？</view>
      <button class="primary" @click="handleLogin">登录</button>
    </view>
    <!-- 其他登录方式 -->
    <view class="form-container" v-if="activeTab === 'other-login'">
      <!-- 🔹 新增：手机号验证码登录 -->
        <view class="input-group">
          <text class="iconfont icon-phone"></text>
          <input type="text" v-model="phoneLoginForm.pnumber" placeholder="请输入手机号" />
        </view>
        <view class="input-group">
          <text class="iconfont icon-code"></text>
          <input type="text" v-model="phoneLoginForm.verifycode" placeholder="请输入验证码" />
          
            <!-- <text class="iconfont icon-eye"></text> -->
            
          <button class="code-btn" :disabled="isCountingDown" @click="sendCode">{{ countDownText }}</button>
      </view>
      <uni-data-checkbox v-model="radioval" :localdata="selectedRole" @change="handleRoleChange"></uni-data-checkbox>
      <button class="primary" @click="handleVeryifyCodeLogin">登录</button>
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

    <!-- 🔹 忘记密码 -->
    <view class="form-container" v-if="activeTab === 'forgot'">
      <view class="input-group">
          <text class="iconfont icon-phone"></text>
          <input type="text" v-model="forgetpswForm.pnumber" placeholder="请输入手机号" />
        </view>
        <view class="input-group">
          <text class="iconfont icon-code"></text>
          <input type="text" v-model="forgetpswForm.verifycode" placeholder="请输入验证码" />
          
            <!-- <text class="iconfont icon-eye"></text> -->
            
          <button class="code-btn" :disabled="isCountingDown" @click="sendForgotCode">{{ countDownText }}</button>
      </view>

      <view class="input-group">
        <text class="iconfont icon-password"></text>
        <input type="password" v-model="forgetpswForm.password" placeholder="请输入新密码" />
      </view>
      <view class="input-group">
        <text class="iconfont icon-password"></text>
        <input type="password" v-model="forgetpswForm.confirmPassword" placeholder="再次输入新密码" />
      </view>
      <button class="submit-btn" @click="handleForgotPassword">重置密码</button>
    </view>


    <!-- 其他登录方式 -->
    <view class="other-login" @click="activeTab = 'other-login'">
      <view class="divider">
        <text>手机号码方式登录</text>
        <view class="social-login">
          <uni-icons type="phone-filled" size="30"></uni-icons>
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
      forgetpswForm: {
        pnumber: '',
        verifycode: '',
        password: ''

      },
      registerForm: {
        username: '',
        phone: '',
        password: '',
        confirmPassword: ''
      },
      phoneLoginForm: {
        pnumber: '',
        verifycode: ''
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
    async sendCode() {
      if (!this.phoneLoginForm.pnumber) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        });
        return;
      }
      try {
        await apiRequest(`users/sendcode/${this.phoneLoginForm.pnumber}`, 'POST');
        this.isCountingDown = true;
        let interval = setInterval(() => {
          this.countdown--;
          if (this.countdown === 0) {
            clearInterval(interval);
            this.isCountingDown = false;
            this.countdown = 60;
          }
        }, 1000);
      }
      catch (error) {
        console.error('发送验证码失败:', error);
        uni.showToast({
          title: '发送验证码失败',
          icon: 'none'
        });
      }
    },
    async sendForgotCode() {
      if (!this.forgetpswForm.pnumber) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        });
        return;
      }
      try {
        await apiRequest(`users/sendforgetpasswordcode/${this.forgetpswForm.pnumber}`, 'POST');
        this.isCountingDown = true;
        let interval = setInterval(() => {
          this.countdown--;
          if (this.countdown === 0) {
            clearInterval(interval);
            this.isCountingDown = false;
            this.countdown = 60;
          }
        }, 1000);
      }
      catch (error) {
        console.error('发送验证码失败:', error);
        uni.showToast({
          title: '发送验证码失败',
          icon: 'none'
        });
      }
    },
    async handleForgotPassword() {
      if (!this.forgetpswForm.pnumber || !this.forgetpswForm.verifycode || !this.forgetpswForm.password || !this.forgetpswForm.confirmPassword) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        });
        return;
      }else if(this.forgetpswForm.password !== this.forgetpswForm.confirmPassword) {
        uni.showToast({
          title: '密码不匹配',
          icon: 'none'
        })
        return;
      }else{
        try{
          await apiRequest(`users/resetpassword`, 'POST', {
            pnumber: this.forgetpswForm.pnumber,
            code: this.forgetpswForm.verifycode,
            password: this.forgetpswForm.password
          })
          uni.showToast({
            title: '修改成功',
            icon: 'success'
          });
          setTimeout(() => {
            uni.navigateTo({ url: '/pages/login/login' });
          }, 1000);
        }catch (error) {
          console.error('修改密码失败:', error);
          uni.showToast({
            title: '修改密码失败',
            icon: 'none'
          });
        }
      }
    },
    async handleVeryifyCodeLogin() {
      if (!this.phoneLoginForm.pnumber || !this.phoneLoginForm.verifycode) {
        uni.showToast({
          title: '请填写完整登录信息',
          icon: 'none'
        });
        return;
      }
      try {
        const response = await apiRequest(`users/loginbycode`, 'POST',{
          pnumber: this.phoneLoginForm.pnumber,
          verifycode: this.phoneLoginForm.verifycode
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
      }
      catch (error) {
        console.error('登录失败', error);
        uni.showToast({
          title: '登录失败',
          icon: 'none'
        });
      }
    },
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
          pnumber: this.registerForm.phone,
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
  margin-top: 130px;
}

.logo-area {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 30rpx 0;
}

.logo {
  width: 160rpx;
  height: 160rpx;
}

.background-image {
  width: 100%;
  height: 350rpx;
  object-fit: cover;
  position: absolute;
  bottom: -70rpx;
}


.welcome-text {
  position: absolute;
  bottom: -50rpx;
  left: 20rpx;  
  color: #0b0101;
  font-size: 46rpx;
  font-weight: bold;
  z-index: 2;
}
.welcome-text-1 {
  position: absolute;
  bottom: -5rpx;
  left: 20rpx;  
  color: #0b0101;
  font-size: 46rpx;
  font-weight: bold;
  z-index: 2;
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

.primary {
  background-color: #007AFF; /* 蓝色 */
  color: #fff;
  border-radius: 44rpx;
  font-size: 32rpx;
  margin-top: 40rpx;
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
}
</style> 
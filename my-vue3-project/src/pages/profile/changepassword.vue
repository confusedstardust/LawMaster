<template>
    <view class="password-container">
      <view class="title">修改密码</view>
  
      <view class="input-group">
        <input
          class="input-field"
          type="password"
          v-model="oldPassword"
          placeholder="请输入旧密码"
        />
      </view>
  
      <view class="input-group">
        <input
          class="input-field"
          type="password"
          v-model="newPassword"
          placeholder="请输入新密码"
        />
      </view>
  
      <view class="input-group">
        <input
          class="input-field"
          type="password"
          v-model="confirmPassword"
          placeholder="请确认新密码"
        />
      </view>
  
      <button class="save-btn" @click="handleChangePassword">
        确认修改
      </button>
    </view>
  </template>
  
  <script>
  import { apiRequest } from '@/utils/api'; // 引入 API 请求方法

  export default {
    data() {
      return {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
      };
    },
    methods: {
      async handleChangePassword() {
        if (!this.oldPassword || !this.newPassword || !this.confirmPassword) {
          return uni.showToast({ title: "请填写完整信息", icon: "none" });
        }
        if (this.newPassword !== this.confirmPassword) {
          return uni.showToast({ title: "两次输入的新密码不一致", icon: "none" });
        }
        if (this.newPassword.length < 6) {
          return uni.showToast({ title: "密码长度不能少于6位", icon: "none" });
        }
  
        // 这里可以调用 API 修改密码
        const res = await apiRequest('users/changePassword', 'POST', {
          oldPassword: this.oldPassword,
          newPassword: this.newPassword,
          userid: uni.getStorageSync('userInfo').id
        })
        if (res) {
          uni.showToast({ title: "密码修改成功", icon: "success" });
          uni.navigateTo({ url: '/pages/login/login' });
          uni.removeStorageSync('userInfo')
        }
          
          else{
            uni.showToast({ title: "密码修改失败,检查原密码是否正确", icon: "success" });
          }

  
        // 可能需要跳转回个人中心
        // setTimeout(() => {
        //   uni.navigateBack();
        // }, 1000);
      },
    },
  };
  </script>
  
  <style scoped>
  .password-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 40rpx;
    background-color: #f8f8f8;
    min-height: 100vh;
  }
  
  .title {
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 50rpx;
  }
  
  .input-group {
    width: 80%;
    margin-bottom: 30rpx;
  }
  
  .input-field {
    width: 100%;
    padding: 20rpx;
    font-size: 32rpx;
    border-radius: 10rpx;
    border: 1px solid #ddd;
    background-color: #fff;
  }
  
  .submit-btn {
    width: 80%;
    padding: 20rpx;
    font-size: 34rpx;
    font-weight: bold;
    color: #fff;
    text-align: center;
    border-radius: 10rpx;
    background: linear-gradient(to right, #4facfe, #00f2fe);
    box-shadow: 0 4rpx 10rpx rgba(0, 0, 0, 0.1);
    margin-top: 20rpx;
  }

  .save-btn {
  margin-top: 60rpx;
  background-color: #007AFF;
  color: #fff;
  border-radius: 44rpx;
  height: 88rpx;
  line-height: 88rpx;
  font-size: 32rpx;
}
  </style>
  
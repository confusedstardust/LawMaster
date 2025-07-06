<template>
  <view class="edit-container">
    <view class="form-item avatar-item">
      <text class="label">头像</text>
      <view class="avatar-wrapper" @click="changeAvatar">
        <image class="avatar" :src="tempAvatar || (userInfo.avatar ? 'http://localhost:8080/files/download/' + userInfo.avatar : '/static/default-avatar.png')"></image>
        <text class="tip">点击更换头像</text>
      </view>
    </view>
    
    <view class="form-item">
      <text class="label">昵称</text>
      <input 
        type="text"
        v-model="userInfo.nickname"
        placeholder="请输入昵称"
        class="input"
      />
    </view>

    <button class="save-btn" @click="handleSave">保存</button>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api';

export default {
  data() {
    return {
      userInfo: {
        avatar: '',
        nickname: ''
      },
      tempAvatar: '', // 临时存储本地选择的头像
      successfalg:false
    };
  },
  onLoad() {
    const userInfo = uni.getStorageSync('userInfo');
    if (userInfo) {
      this.userInfo = { ...userInfo };
    }
  },
  methods: {
    changeAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0];
          this.tempAvatar = tempFilePath; // 立即更新本地显示的头像
          this.uploadAvatar(tempFilePath);
        }
      });
    },
    async uploadAvatar(filePath) {
      const randomFileName = Math.random().toString(36).substring(2, 10);

      uni.uploadFile({
        url: `http://localhost:8080/files/upload`,
        filePath: filePath,
        name: "file",
        formData: { filename: randomFileName },
        success: (res) => {
          try {
            const response = JSON.parse(res.data);
            if (response.success) {
              this.userInfo.avatar = response.url;
              uni.setStorageSync('userInfo', this.userInfo);
              uni.showToast({ title: "上传成功", icon: "success" });
              this.successfalg= true ;
            } else {
              uni.showToast({ title: "上传失败，请重试", icon: "none" });
            }
          } catch (error) {
            console.error("解析响应出错:", error);
            uni.showToast({ title: "上传失败，请重试", icon: "none" });
          }
        },
        fail: (error) => {
          console.error("上传失败:", error);
          uni.showToast({ title: "上传失败，请重试", icon: "none" });
        }
      });
    while (!this.successfalg) {
      await new Promise(resolve => setTimeout(resolve, 1000)); // 每秒检查一次
    }
    const userAvatarName = this.userInfo.avatar ? this.userInfo.avatar.split('/').pop() : 'default_avatar.png';
    console.log('用户头像上传名称:', userAvatarName);
    try {
      const response = await apiRequest('users/update', 'post', {
        avatar: userAvatarName,
        id:this.userInfo.id
      });
    } catch (error) {
      console.error("更新用户信息失败:", error);
      uni.showToast({ title: "头像更新失败，请重试", icon: "none" });
    }
    },
   async handleSave() {
      if (!this.userInfo.nickname.trim()) {
        uni.showToast({ title: '请输入昵称', icon: 'none' });
        return;
      }
      const response = await apiRequest('users/update', 'post', {
        nickname: this.userInfo.nickname.trim(),
        id:this.userInfo.id
      });
      
      uni.setStorageSync('userInfo', this.userInfo);
      
      uni.showToast({ title: "保存成功", icon: 'success', duration: 1500 });
      
      setTimeout(() => {
        uni.navigateBack();
      }, 1500);
    }
  }
};
</script>

<style>
.edit-container {
  padding: 30rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.form-item {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
  border-radius: 12rpx;
}

.avatar-item {
  display: flex;
  align-items: center;
}

.label {
  font-size: 28rpx;
  color: #333;
  margin-right: 30rpx;
}

.avatar-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  margin-bottom: 10rpx;
}

.tip {
  font-size: 24rpx;
  color: #999;
}

.input {
  flex: 1;
  font-size: 28rpx;
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

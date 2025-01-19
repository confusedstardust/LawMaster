<template>
  <view class="edit-container">
    <view class="form-item avatar-item">
      <text class="label">头像</text>
      <view class="avatar-wrapper" @click="changeAvatar">
        <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'"></image>
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
export default {
  data() {
    return {
      userInfo: {
        avatar: '',
        nickname: ''
      }
    }
  },
  onLoad() {
    const userInfo = uni.getStorageSync('userInfo')
    if (userInfo) {
      this.userInfo = JSON.parse(JSON.stringify(userInfo))
    }
  },
  methods: {
    changeAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          // 这里应该上传图片到服务器，获取URL
          // 暂时直接使用本地路径
          this.userInfo.avatar = res.tempFilePaths[0]
        }
      })
    },
    handleSave() {
      if (!this.userInfo.nickname.trim()) {
        uni.showToast({
          title: '请输入昵称',
          icon: 'none'
        })
        return
      }
      
      // 保存到本地存储
      uni.setStorageSync('userInfo', this.userInfo)
      
      uni.showToast({
        title: '保存成功',
        icon: 'success',
        duration: 1500
      })
      
      setTimeout(() => {
        uni.navigateBack()
      }, 1500)
    }
  }
}
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
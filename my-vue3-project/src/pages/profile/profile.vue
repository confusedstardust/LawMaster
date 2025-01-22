<template>
  <view class="profile-container">
    <!-- 用户信息头部 -->
    <view class="user-header">
      <view class="user-info">
        <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'" @click="changeAvatar"></image>
        <view class="info-content">
          <text class="nickname">{{userInfo.nickname || '未设置昵称'}}</text>
        </view>
      </view>
      <view class="edit-btn" @click="navigateTo('/pages/profile/edit')">
        <text class="iconfont icon-edit"></text>
        编辑资料
      </view>
    </view>
    

    <!-- 功能菜单 -->
    <view class="menu-list">
      <view class="menu-group">
        <view class="menu-item" @click="navigateTo('/pages/profile/collections')">
          <uni-icons type="star-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">点赞收藏</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
        <view class="menu-item" @click="navigateTo('/pages/profile/history')">
          <uni-icons type="time-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">浏览历史</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
        <view class="menu-item" @click="navigateTo('/pages/profile/notify')">
          <uni-icons type="notification-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">我的通知</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="navigateTo('/pages/profile/study')">
          <uni-icons type="flag-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">学习记录</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
        <view class="menu-item" @click="navigateTo('/pages/profile/questions')">
          <uni-icons type="help-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">我的问答</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="handleLogout">
          <uni-icons type="poweroff" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">退出登录</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
      </view>
    </view>
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
  onShow() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      const userInfo = uni.getStorageSync('userInfo')
      if (userInfo) {
        this.userInfo = userInfo
      }
    },
    navigateTo(url) {
      uni.navigateTo({
        url
      })
    },
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.reLaunch({
              url: '/pages/login/login'
            })
          }
        }
      })
    }
  }
}
</script>

<style>
.profile-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.user-header {
  background-color: #fff;
  padding: 40rpx 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  margin-right: 30rpx;
}

.info-content {
  display: flex;
  flex-direction: column;
}

.nickname {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
}

.user-id {
  font-size: 24rpx;
  color: #999;
}

.edit-btn {
  padding: 10rpx 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 30rpx;
  font-size: 24rpx;
  color: #666;
  display: flex;
  align-items: center;
}

.edit-btn .iconfont {
  margin-right: 10rpx;
  font-size: 28rpx;
}

.stats-bar {
  background-color: #fff;
  display: flex;
  padding: 30rpx 0;
  margin-bottom: 20rpx;
}

.stats-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.stats-item:not(:last-child):after {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 1rpx;
  height: 50%;
  background-color: #eee;
}

.stats-item .num {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
}

.stats-item .label {
  font-size: 24rpx;
  color: #999;
}

.menu-list {
  padding: 0 20rpx;
}

.menu-group {
  background-color: #fff;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  position: relative;
}

.menu-item:not(:last-child):after {
  content: '';
  position: absolute;
  left: 30rpx;
  right: 30rpx;
  bottom: 0;
  height: 1rpx;
  background-color: #eee;
}

.menu-item .iconfont {
  font-size: 40rpx;
  color: #007AFF;
  margin-right: 20rpx;
}

.menu-item .menu-text {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.menu-item .icon-arrow-right {
  font-size: 32rpx;
  color: #999;
}
</style> 
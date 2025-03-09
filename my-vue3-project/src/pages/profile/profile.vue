<template>
  <view class="profile-container">
    <!-- 用户信息头部 -->
    <view class="user-header">
      <view class="user-info">
        <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'" @click="changeAvatar"></image>
        <view class="info-content">
          <text class="nickname">{{ userInfo.nickname || '未设置昵称' }}</text>
          <text class="user-id">用户名: {{ userInfo.username || '未设置用户名' }}</text>
          <text class="user-role">角色: {{ userInfo.role || '未设置角色' }}</text>
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
        <view class="menu-item" @click="navigateTo('/pages/profile/my-likes')">
          <uni-icons type="heart-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">我的点赞</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
        <view class="menu-item" @click="navigateTo('/pages/profile/my-collections')">
          <uni-icons type="star-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">我的收藏</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
        <!-- <view class="menu-item" @click="navigateTo('/pages/profile/notify')">
          <uni-icons type="notification-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">我的通知</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view> -->
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="fetchStudyRecords">
          <uni-icons type="flag-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">学习记录</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
        <view class="menu-item" @click="fetchUserPosts">
          <uni-icons type="pyq" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">我的发布</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="handleLogout">
          <uni-icons type="undo-filled" size="30" color="#007AFF"></uni-icons>
          <text class="menu-text">退出登录</text>
          <uni-icons type="right" size="24" color="#999"></uni-icons>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api';

export default {
  data() {
    return {
      userInfo: {
        avatar: '',
        nickname: '',
        username: '',
        role: ''
      }
    }
  },
  mounted() {
    // 从本地存储中获取用户信息
    const storedUserInfo = uni.getStorageSync('userInfo');
    if (storedUserInfo) {
      this.userInfo = {
        avatar: "http://localhost:8080/files/download/"+storedUserInfo.avatar, // 获取用户头像storedUserInfo.avatar,
        nickname: storedUserInfo.nickname,
        username: storedUserInfo.username,
        role: storedUserInfo.role,
        id:storedUserInfo.id
      }; // 更新 userInfo
    }
  },
  methods: {
    navigateTo(url) {
      uni.navigateTo({
        url
      })
    },
    async fetchStudyRecords() {
      const userId = this.userInfo.id; // 获取用户 ID
      try {
        // 请求用户的学习记录
        const response = await apiRequest(`userAnswers/distinct/${userId}`, 'get');
        const questionIds = response.map(item => item.questionId); // 提取问题 ID

        // 请求问题的详细信息
        const questionsResponse = await apiRequest(`questions/array?ids=${questionIds.join(',')}`, 'get');

        // 将问题数据传递到 wrong-questions 页面
        uni.navigateTo({
          url: `/pages/quiz/wrong-questions?questions=${encodeURIComponent(JSON.stringify(questionsResponse))}&Type=record`
        });
      } catch (error) {
        console.error("获取学习记录失败", error);
        uni.showToast({
          title: '获取学习记录失败，请重试',
          icon: 'none'
        });
      }
    },
    async fetchUserPosts() {
      const userId = this.userInfo.id; // 获取用户 ID
      try {
        // 请求用户发布的帖子
        const response = await apiRequest(`posts/user/${userId}`, 'get');

        // 将帖子数据传递到 user-posts 页面
        uni.navigateTo({
          url: `/pages/profile/user-posts?posts=${encodeURIComponent(JSON.stringify(response))}`
        });
      } catch (error) {
        console.error("获取用户发布的帖子失败", error);
        uni.showToast({
          title: '获取用户发布的帖子失败，请重试',
          icon: 'none'
        });
      }
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

.user-role {
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
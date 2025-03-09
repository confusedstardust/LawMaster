<template>
  <view class="detail-container">
    <view class="detail-header">
      <text class="detail-title">{{ article.title }}</text>
      <view class="detail-meta">
        <text class="detail-source">{{ article.source }}</text>
        <text class="detail-time">{{ article.time }}</text>
      </view>
    </view>
    <view class="detail-content">
      <view class="content-text" v-html="article.content||article.description"></view>
    </view>
    <view class="back-button" @click="goBack">
      <text>返回</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      article: {}
    }
  },
  onLoad(options) {
    console.log('Received article options:', options); // 添加调试信息
    this.article = JSON.parse(options.article);
  },
  methods: {
    goBack() {
      uni.navigateBack();
    }
  }
}
</script>

<style>
.detail-container {
  padding: 20rpx;
  background-color: #f9f9f9;
  min-height: 100vh;
}

.detail-header {
  margin-bottom: 20rpx;
  padding: 20rpx;
  background-color: #fff;
  border-radius: 10rpx;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.detail-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.detail-meta {
  display: flex;
  justify-content: space-between;
  margin-top: 10rpx;
}

.detail-source {
  font-size: 24rpx;
  color: #007AFF;
}

.detail-time {
  font-size: 20rpx;
  color: #999;
}

.detail-content {
  margin-top: 20rpx;
  padding: 20rpx;
  background-color: #fff;
  border-radius: 10rpx;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.content-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.5;
}

.back-button {
  margin-top: 30rpx;
  padding: 10rpx 20rpx;
  background-color: #007AFF;
  color: #fff;
  border-radius: 5rpx;
  text-align: center;
  cursor: pointer;
}
.back-button:hover {
  background-color: #0056b3;
}
</style> 
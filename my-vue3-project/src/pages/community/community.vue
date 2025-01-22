<template>
  <view class="community-container">
    <!-- 顶部搜索栏 -->
    <view class="search-bar">
      <input type="text" placeholder="搜索感兴趣的法律话题" v-model="searchText" />
    </view>

    <!-- 话题分类标签 -->
    <scroll-view scroll-x class="topic-tags">
      <view 
        class="tag-item" 
        :class="{ active: currentTag === tag.id }"
        v-for="tag in tags" 
        :key="tag.id"
        @click="switchTag(tag.id)"
      >
        {{tag.name}}
      </view>
    </scroll-view>

    <!-- 瀑布流内容区 -->
    <scroll-view 
      scroll-y 
      class="content-list"
      @scrolltolower="loadMore"
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherrefresh="onRefresh"
    >
      <view class="waterfall">
        <view class="waterfall-left">
          <view 
            class="post-item" 
            v-for="item in leftList" 
            :key="item.id"
            @click="navigateToDetail(item.id)"
          >
            <image 
              class="post-image" 
              :src="item.cover" 
              mode="widthFix"
              v-if="item.type === 'image'"
            ></image>
            <view class="video-container" v-if="item.type === 'video'">
              <video 
                :src="item.video"
                :poster="item.cover"
                class="post-video"
              ></video>
              <view class="video-duration">{{item.duration}}</view>
            </view>
            <view class="post-content">
              <text class="post-title">{{item.title}}</text>
              <view class="post-footer">
                <view class="user-info">
                  <image class="user-avatar" :src="item.userAvatar"></image>
                  <text class="username">{{item.username}}</text>
                </view>
                <view class="post-stats">
                  <text class="stats-item">
                    <uni-icons 
                      :type="item.isLiked ? 'heart-filled' : 'heart'" 
                      size="16" 
                      :color="item.isLiked ? '#007AFF' : '#666'"
                    ></uni-icons>
                    <text>{{item.likes}}</text>
                  </text>
                  <text class="stats-item">
                    <uni-icons type="chatbubble" size="16" color="#666"></uni-icons>
                    <text>{{item.comments}}</text>
                  </text>
                </view>
              </view>
            </view>
          </view>
        </view>
        <view class="waterfall-right">
          <view 
            class="post-item" 
            v-for="item in rightList" 
            :key="item.id"
            @click="navigateToDetail(item.id)"
          >
            <image 
              class="post-image" 
              :src="item.cover" 
              mode="widthFix"
              v-if="item.type === 'image'"
            ></image>
            <view class="video-container" v-if="item.type === 'video'">
              <video 
                :src="item.video"
                :poster="item.cover"
                class="post-video"
              ></video>
              <view class="video-duration">{{item.duration}}</view>
            </view>
            <view class="post-content">
              <text class="post-title">{{item.title}}</text>
              <view class="post-footer">
                <view class="user-info">
                  <image class="user-avatar" :src="item.userAvatar"></image>
                  <text class="username">{{item.username}}</text>
                </view>
                <view class="post-stats">
                  <text class="stats-item">
                    <uni-icons 
                      :type="item.isLiked ? 'heart-filled' : 'heart'" 
                      size="16" 
                      :color="item.isLiked ? '#007AFF' : '#666'"
                    ></uni-icons>
                    <text>{{item.likes}}</text>
                  </text>
                  <text class="stats-item">
                    <uni-icons type="chatbubble" size="16" color="#666"></uni-icons>
                    <text>{{item.comments}}</text>
                  </text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 悬浮发布按钮 -->
    <view class="float-btn" @click="navigateToPublish">
      <text class="iconfont icon-add"></text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchText: '',
      currentTag: 'all',
      isRefreshing: false,
      tags: [
        { id: 'all', name: '全部' },
        { id: 'civil', name: '民事法律' },
        { id: 'criminal', name: '刑事法律' },
        { id: 'business', name: '商业法律' },
        { id: 'labor', name: '劳动法律' },
        { id: 'property', name: '房产法律' }
      ],
      leftList: [
        {
          id: 1,
          type: 'image',
          cover: '/static/post1.jpg',
          title: '租房合同纠纷怎么解决？分享一个真实案例',
          userAvatar: '/static/avatar1.jpg',
          username: '法律顾问小王',
          likes: 1234
        },
        {
          id: 2,
          type: 'video',
          video: '/static/video1.mp4',
          cover: '/static/cover1.jpg',
          duration: '2:30',
          title: '一分钟了解劳动合同签订注意事项',
          userAvatar: '/static/avatar2.jpg',
          username: '法律小课堂',
          likes: 2345
        }
      ],
      rightList: [
        {
          id: 3,
          type: 'image',
          cover: '/static/post2.jpg',
          title: '遇到交通事故如何正确处理？',
          userAvatar: '/static/avatar3.jpg',
          username: '资深律师张三',
          likes: 3456
        }
      ]
    }
  },
  methods: {
    switchTag(tagId) {
      this.currentTag = tagId
      // TODO: 根据标签加载对应内容
    },
    loadMore() {
      // TODO: 加载更多内容
    },
    async onRefresh() {
      this.isRefreshing = true
      await new Promise(resolve => setTimeout(resolve, 1000))
      this.isRefreshing = false
    },
    navigateToDetail(id) {
      uni.navigateTo({
        url: `/pages/community/post-detail?id=${id}`
      })
    },
    navigateToPublish() {
      uni.navigateTo({
        url: '/pages/community/publish'
      })
    }
  }
}
</script>

<style>
.community-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 100rpx;
}

.search-bar {
  padding: 20rpx;
  background: #fff;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.search-bar input {
  background: #f5f5f5;
  padding: 15rpx 30rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
}

.topic-tags {
  background: #fff;
  padding: 20rpx;
  white-space: nowrap;
  position: fixed;
  top: 100rpx;
  left: 0;
  right: 0;
  z-index: 100;
}

.tag-item {
  display: inline-block;
  padding: 10rpx 30rpx;
  margin-right: 20rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
  color: #666;
  background: #f5f5f5;
}

.tag-item.active {
  color: #fff;
  background: #007AFF;
}

.content-list {
  margin-top: 180rpx;
  height: calc(100vh - 180rpx);
}

.waterfall {
  display: flex;
  padding: 20rpx;
}

.waterfall-left, .waterfall-right {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.waterfall-left {
  margin-right: 10rpx;
}

.waterfall-right {
  margin-left: 10rpx;
}

.post-item {
  background: #fff;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.post-image {
  width: 100%;
}

.video-container {
  position: relative;
  width: 100%;
}

.post-video {
  width: 100%;
}

.video-duration {
  position: absolute;
  right: 10rpx;
  bottom: 10rpx;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 4rpx 10rpx;
  border-radius: 4rpx;
  font-size: 24rpx;
}

.post-content {
  padding: 20rpx;
}

.post-title {
  font-size: 28rpx;
  color: #333;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 40rpx;
  height: 40rpx;
  border-radius: 20rpx;
  margin-right: 10rpx;
}

.username {
  font-size: 24rpx;
  color: #666;
}

.post-stats {
  font-size: 24rpx;
  color: #999;
}

.stats-item {
  display: flex;
  align-items: center;
  margin-left: 20rpx;
  font-size: 24rpx;
  color: #666;
}

.stats-item text {
  margin-left: 4rpx;
}

.float-btn {
  position: fixed;
  right: 30rpx;
  bottom: 120rpx;
  width: 100rpx;
  height: 100rpx;
  background: #007AFF;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.2);
  z-index: 100;
}

.float-btn .iconfont {
  color: #fff;
  font-size: 48rpx;
}
</style> 
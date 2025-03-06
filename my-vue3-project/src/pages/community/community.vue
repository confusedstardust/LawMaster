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
            @click="navigateToDetail(item.id,item.comments)"
          >
            <image 
              class="post-image" 
              :src="item.images[0]" 
              mode="widthFix"
              v-if="item.images.length > 0"
            ></image>
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
                    <text>{{item.comments.length}}</text>
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
              :src="item.images[0]" 
              mode="widthFix"
              v-if="item.images.length > 0"
            ></image>
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
                    <text>{{item.comments.length}}</text>
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
      <image src="/static/tabbar/plus.png" mode="aspectFit" class="add-icon" />
    </view>
    
  </view>
</template>

<script>
import { apiRequest, getUserImage } from '@/utils/api'; // 引入 API 请求方法

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
      leftList: [],
      rightList: []
    }
  },
  mounted() {
    this.fetchPosts(); // 组件挂载时获取帖子
  },
  methods: {
    async fetchPosts() {
      try {
        const response = await apiRequest('posts', 'get'); // 获取帖子数据
        const posts = await Promise.all(response.map(async post => {
          // 解析图片数组
          const imagesArray = JSON.parse(post.images); // 将字符串解析为数组

          // 拼接每个图片的完整 URL
          const imagesUrls = imagesArray.map(imageName => {
            return `http://localhost:8080/files/download/${imageName}`; // 拼接完整的图片 URL
          });
          const commentsCount = await this.fetchComments(post.id); // 获取评论

          return {
            id: post.id,
            title: post.title,
            content: post.content,
            images: imagesUrls, // 使用拼接的图片 URL
            userAvatar: post.Avatar, // 这里可以替换为实际的用户头像
            username: '用户' + post.userId,
            likes: post.likes,
            comments: commentsCount,
            isLiked: false
          };
        }));

        this.leftList = posts.filter((_, index) => index % 2 === 0);
        this.rightList = posts.filter((_, index) => index % 2 !== 0);
      } catch (error) {
        console.error('获取帖子失败:', error);
      }
    },
        async fetchComments(postId) {
      try {
        const response = await apiRequest(`comments/post/${postId}`, 'get');
        return response
      } catch (error) {
        console.error('获取评论失败:', error);
        return 0; // 如果失败，返回 0
      }
    },
    switchTag(tagId) {
      this.currentTag = tagId;
      // TODO: 根据标签加载对应内容
    },
    loadMore() {
      // TODO: 加载更多内容
    },
    async onRefresh() {
      this.isRefreshing = true;
      await new Promise(resolve => setTimeout(resolve, 1000));
      this.isRefreshing = false;
    },
    navigateToDetail(id,comments) {
      uni.navigateTo({
        url: `/pages/community/post-detail?id=${id}&comments=${JSON.stringify(comments)}`
        
      });
    },
    navigateToPublish() {
      uni.navigateTo({
        url: '/pages/community/publish',
        userInfo:userInfo
      });
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
  display: flex;
  flex-direction: column;
}

.post-image {
  width: 100%;
  height: 200rpx;
  object-fit: cover;
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
  width: 80rpx;
  height: 80rpx;
  background: #FF0000;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.2);
  z-index: 100;
}

.float-btn .add-icon {
  width: 50rpx;
  height: 50rpx;
}
</style> 
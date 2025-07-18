<template>
  <view class="community-container">
    <!-- 顶部搜索栏 -->
    <view class="search-bar">
      <input type="text" placeholder="搜索我的发布" v-model="searchText" />
    </view>

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
        <view 
          class="post-item" 
          v-for="item in posts" 
          :key="item.id"
          @click="navigateToPost(item.id, item.comments)"
          @longpress="confirmDelete(item.id)"
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
                <view class="stats-item">
                  <uni-icons 
                    :type="item.isLiked ? 'heart-filled' : 'heart'" 
                    size="16" 
                    :color="item.isLiked ? '#007AFF' : '#666'"
                  ></uni-icons>
                  <text>{{item.likes}}</text>
                </view>
                <view class="stats-item">
                  <uni-icons type="chatbubble" size="16" color="#666"></uni-icons>
                  <text>{{item.comments.length}}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api';

export default {
  data() {
    return {
      posts: [],
      searchText: '',
      isRefreshing: false,
    };
  },
  onLoad(options) {
    if (options.posts) {
      this.posts = JSON.parse(decodeURIComponent(options.posts));
      this.fetchInfo();
    }
  },
  methods: {
    async fetchInfo() {
      this.posts = await Promise.all(this.posts.map(async postItem => {
        const imagesArray = JSON.parse(postItem.images);
        const imagesUrls = imagesArray.map(imageName => {
          return `http://localhost:8080/files/download/${imageName}`;
        });
        const commentsCount = await this.fetchComments(postItem.id);

        return {
          id: postItem.id,
          title: postItem.title,
          content: postItem.content,
          images: imagesUrls,
          userAvatar: postItem.Avatar,
          username: '用户' + postItem.userId,
          likes: postItem.likes,
          comments: commentsCount,
          isLiked: false
        };
      }));
    },
    async fetchComments(postId) {
      try {
        const response = await apiRequest(`comments/post/${postId}`, 'get');
        return response;
      } catch (error) {
        console.error('获取评论失败:', error);
        return [];
      }
    },
    navigateToPost(postId, comments) {
      uni.navigateTo({
        url: `/pages/community/post-detail?id=${postId}&comments=${JSON.stringify(comments)}`
      });
    },
    loadMore() {
      // TODO: 加载更多内容
    },
    async onRefresh() {
      this.isRefreshing = true;
      await new Promise(resolve => setTimeout(resolve, 1000));
      this.isRefreshing = false;
    },
    confirmDelete(postId) {
      uni.showModal({
        title: '确认删除',
        content: '您确定要删除这条帖子吗？',
        success: async (res) => {
          if (res.confirm) {
            await this.deletePost(postId);
          }
        }
      });
    },
    async deletePost(postId) {
      try {
        await apiRequest(`/posts/delete/${postId}`, 'post');
        uni.showToast({
          title: '删除成功',
          icon: 'success'
        });
        // 从列表中移除已删除的帖子
        this.posts = this.posts.filter(post => post.id !== postId);
      } catch (error) {
        console.error('删除失败', error);
        uni.showToast({
          title: '删除失败，请重试',
          icon: 'none'
        });
      }
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

.content-list {
  margin-top: 80rpx; /* Adjusted for search bar height */
  height: calc(100vh - 80rpx);
}

.waterfall {
  display: flex;
  flex-wrap: wrap;
  justify-content: center; /* Center the items */
  padding: 20rpx;
}

.post-item {
  background: #fff;
  border-radius: 12rpx;
  margin: 10rpx;
  overflow: hidden;
  width: calc(95% - 20rpx); /* Adjust width for wider display */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.post-image {
  width: 100%;
  height: 200rpx;
  object-fit: cover;
}

.post-content {
  padding: 20rpx;
}

.post-title {
  font-size: 28rpx;
  color: #333;
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
  display: flex; /* Ensure stats are in a row */
  align-items: center;
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
</style>
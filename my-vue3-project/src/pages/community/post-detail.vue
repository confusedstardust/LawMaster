<template>
  <view class="detail-container">
    <!-- 帖子内容 -->
    <scroll-view 
      scroll-y 
      class="post-content"
      :style="{ paddingBottom: keyboardHeight + 'px' }"
    >
      <!-- 作者信息 -->
      <view class="author-info">
        <image class="avatar" :src="postDetail.userAvatar"></image>
        <view class="author-detail">
          <text class="nickname">{{ postDetail.username }}</text>
          <text class="time">{{ postDetail.createdAt }}</text>
        </view>
        <button class="follow-btn" @click="handleFollow">
          {{ postDetail.isFollowed ? '已关注' : '关注' }}
        </button>
      </view>

      <!-- 帖子标题和内容 -->
      <view class="post-detail">
        <text class="title">{{ postDetail.title }}</text>
        <br/>
        <text class="content">{{ postDetail.content }}</text>
      </view>

      <!-- 图片或视频展示 -->
      <view class="media-content">
        <block v-if="postDetail.type === 'image'">
          <swiper indicator-dots autoplay>
            <swiper-item v-for="(img, index) in postDetail.images" :key="index">
              <image 
                :src="img"
                mode="widthFix"
                class="post-image"
                @click="previewImage(index)"
              ></image>
            </swiper-item>
          </swiper>
        </block>
        <video 
          v-if="postDetail.type === 'video'"
          :src="postDetail.video"
          class="post-video"
          object-fit="cover"
        ></video>
      </view>

      <!-- 话题标签 -->
      <view class="topic-tags">
        <text 
          class="tag-item" 
          v-for="tag in postDetail.tags" 
          :key="tag"
        >#{{ tag }}</text>
      </view>

      <!-- 互动数据 -->
      <view class="interaction-bar">
        <view class="interaction-item">
          <text class="count">{{ postDetail.views }}</text>
          <text class="label">浏览</text>
        </view>
        <view class="interaction-item">
          <text class="count">{{ postDetail.likes }}</text>
          <text class="label">点赞</text>
        </view>
        <view class="interaction-item">
          <text class="count">{{ postDetail.collected }}</text>
          <text class="label">收藏</text>
        </view>
      </view>

      <!-- 评论区 -->
      <view class="comment-section">
        <view class="section-title">评论 {{ postDetail.comments.length }}</view>
        <view 
          class="comment-item" 
          v-for="comment in postDetail.comments" 
          :key="comment.id"
        >
          <image class="comment-avatar" :src="comment.userAvatar"></image>
          <view class="comment-content">
            <view class="comment-header">
              <text class="comment-username">{{ comment.username }}</text>
              <text class="comment-time">{{ comment.time }}</text>
            </view>
            <text class="comment-text">{{ comment.content }}</text>
            <view class="comment-footer">
              <text class="reply-btn" @click="replyComment(comment)">
                <uni-icons type="chatbubble" size="24"></uni-icons>
                回复
              </text>
              <text class="like-count">
                <uni-icons 
                  :type="comment.isLiked ? 'heart-filled' : 'heart'" 
                  size="24" 
                  :color="comment.isLiked ? '#007AFF' : '#666'"
                ></uni-icons>
                {{ comment.likes }}
              </text>
            </view>
            <!-- 回复列表 -->
            <view 
              class="reply-list" 
              v-if="comment.replies && comment.replies.length"
            >
              <view 
                class="reply-item"
                v-for="reply in comment.replies"
                :key="reply.id"
              >
                <text class="reply-username">{{ reply.username }}</text>
                <text class="reply-text">{{ reply.content }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 底部操作栏 -->
    <view class="bottom-bar" :style="{ bottom: keyboardHeight + 'px' }">
      <view class="comment-input">
        <uni-icons type="chatbubble" size="24" color="#999"></uni-icons>
        <input 
          type="text"
          v-model="commentText"
          :placeholder="replyTo ? `回复 ${replyTo}：` : '说点什么..'"
          :focus="showKeyboard"
          @focus="onFocus"
          @blur="onBlur"
        />
      </view>
      <view class="action-buttons">
        <view class="action-item" :class="{ active: postDetail.isLiked }" @click="handleLike">
          <uni-icons 
            :type="postDetail.isLiked ? 'heart-filled' : 'heart'" 
            size="30" 
            :color="postDetail.isLiked ? '#007AFF' : '#666'"
          ></uni-icons>
          <text>点赞</text>
        </view>
        <view class="action-item" :class="{ active: postDetail.isCollected }" @click="handleCollect">
          <uni-icons 
            :type="postDetail.isCollected ? 'star-filled' : 'star'" 
            size="30" 
            :color="postDetail.isCollected ? '#007AFF' : '#666'"
          ></uni-icons>
          <text>收藏</text>
        </view>
        <view class="action-item" @click="handleShare">
          <uni-icons type="redo" size="30" color="#666"></uni-icons>
          <text>分享</text>
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
      postId: '',
      keyboardHeight: 0,
      showKeyboard: false,
      commentText: '',
      replyTo: '',
      postDetail: {
        id: '',
        type: '',
        userAvatar: '',
        username: '',
        createdAt: '',
        isFollowed: false,
        title: '',
        content: '',
        images: [],
        tags: [],
        views: 0,
        likes: 0,
        collected: 0, // 添加收藏数
        isLiked: false,
        isCollected: false,
        comments: []
      }
    }
  },
  onLoad(options) {
    this.postId = options.id;
    this.fetchPostDetail();
    this.userInfo=uni.getStorageSync('userInfo'); // 根据ID获取帖子详情
  },
  methods: {
    async fetchPostDetail() {
      try {
        const response = await apiRequest(`posts/${this.postId}`, 'get'); // 获取帖子详情

        // 解析图片数组
        const imagesArray = JSON.parse(response.images); // 将字符串解析为数组
        const imagesUrls = imagesArray.map(imageName => {
          return `http://localhost:8080/files/download/${imageName}`; // 拼接完整的图片 URL
        });
        
        const userAvatarurl = "http://localhost:8080/files/download/"+response.avatar; // 获取用户头像
        console.log(response.avatar)
        this.postDetail = {
          id: response.id,
          type: imagesArray.length > 0 ? 'image' : 'video',
          userAvatar: userAvatarurl, // 使用获取的用户头像
          username: response.username,
          createdAt: response.createdAt,
          isFollowed: false,
          title: response.title,
          content: response.content,
          images: imagesUrls, // 使用拼接的图片 URL
          tags: JSON.parse(response.tags),
          views: response.views,
          likes: response.likes,
          collected: response.collected, // 使用获取的收藏数
          isLiked: false,
          isCollected: false,
          comments: []
        };
      } catch (error) {
        console.error('获取帖子详情失败:', error);
      }
    },
    handleFollow() {
      this.postDetail.isFollowed = !this.postDetail.isFollowed;
      uni.showToast({
        title: this.postDetail.isFollowed ? '已关注' : '已取消关注',
        icon: 'none'
      });
    },
    previewImage(index) {
      uni.previewImage({
        urls: this.postDetail.images,
        current: index
      });
    },
    async handleLike() {
      this.postDetail.isLiked = !this.postDetail.isLiked;
      this.postDetail.likes += this.postDetail.isLiked ? 1 : -1;

      // 发送点赞请求到后端
      try {
        await apiRequest(`posts/edit`, 'post', {
          likes: this.postDetail.likes,
          id: this.postDetail.id,
          views: this.postDetail.views + 1
        });

        // 发送用户点赞请求
        await apiRequest(`favorites/togglefavorites`, 'post', {
          userId: this.userInfo.id, // 假设 userId 是在 data 中定义的
          postId: this.postDetail.id
        });

        uni.showToast({
          title: this.postDetail.isLiked ? '已点赞' : '已取消点赞',
          icon: 'none'
        });
      } catch (error) {
        console.error("点赞失败", error);
        uni.showToast({
          title: '点赞失败，请重试',
          icon: 'none'
        });
      }
    },
    async handleCollect() {
      this.postDetail.isCollected = !this.postDetail.isCollected;
      this.postDetail.collected += this.postDetail.isCollected ? 1 : -1;

      // 发送收藏请求到后端
      try {
        await apiRequest(`posts/edit`, 'post', {
          collected: this.postDetail.collected,
          id: this.postDetail.id,
          views: this.postDetail.views + 1
        });

        // 发送用户收藏请求
        await apiRequest(`collects/togglecollect`, 'post', {
          userId: this.userInfo.id, // 假设 userId 是在 data 中定义的
          postId: this.postDetail.id
        });

        uni.showToast({
          title: this.postDetail.isCollected ? '已收藏' : '已取消收藏',
          icon: 'none'
        });
      } catch (error) {
        console.error("收藏失败", error);
        uni.showToast({
          title: '收藏失败，请重试',
          icon: 'none'
        });
      }
    },
    handleShare() {
      uni.showShareMenu({
        withShareTicket: true,
        menus: ['shareAppMessage', 'shareTimeline']
      });
    },
    replyComment(comment) {
      this.replyTo = comment.username;
      this.showKeyboard = true;
    },
    onFocus(e) {
      this.keyboardHeight = e.detail.height || 0;
    },
    onBlur() {
      this.keyboardHeight = 0;
      this.showKeyboard = false;
      this.replyTo = '';
    }
  }
}
</script>

<style>
.detail-container {
  min-height: 100vh;
  background: #fff;
}

.post-content {
  height: calc(100vh - 100rpx);
}

.author-info {
  padding: 30rpx;
  display: flex;
  align-items: center;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 40rpx;
  margin-right: 20rpx;
}

.author-detail {
  flex: 1;
}

.nickname {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.time {
  font-size: 24rpx;
  color: #999;
  margin-top: 6rpx;
}

.follow-btn {
  padding: 10rpx 30rpx;
  font-size: 26rpx;
  border-radius: 30rpx;
  background: #007AFF;
  color: #fff;
}

.follow-btn.followed {
  background: #f5f5f5;
  color: #666;
}

.post-detail {
  padding: 0 30rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.content {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.media-content {
  margin: 30rpx 0;
}

.post-image {
  width: 100%;
  max-width: 300rpx;
  max-height: 200rpx;
  object-fit: cover;
  margin-bottom: 10rpx;
}

.post-video {
  width: 100%;
}

.topic-tags {
  padding: 0 30rpx;
  margin-bottom: 30rpx;
}

.tag-item {
  display: inline-block;
  margin-right: 20rpx;
  color: #007AFF;
  font-size: 26rpx;
}

.interaction-bar {
  display: flex;
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.interaction-item {
  flex: 1;
  text-align: center;
}

.interaction-item .count {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.interaction-item .label {
  font-size: 24rpx;
  color: #999;
  margin-left: 10rpx;
}

.comment-section {
  padding: 30rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
}

.comment-item {
  display: flex;
  margin-bottom: 30rpx;
}

.comment-avatar {
  width: 60rpx;
  height: 60rpx;
  border-radius: 30rpx;
  margin-right: 20rpx;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 10rpx;
}

.comment-username {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.comment-time {
  font-size: 24rpx;
  color: #999;
  margin-left: 20rpx;
}

.comment-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.5;
}

.comment-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 10rpx;
}

.reply-btn {
  font-size: 24rpx;
  color: #999;
}

.like-count {
  font-size: 24rpx;
  color: #999;
}

.reply-list {
  background: #f5f5f5;
  padding: 20rpx;
  margin-top: 20rpx;
  border-radius: 8rpx;
}

.reply-item {
  margin-bottom: 10rpx;
}

.reply-username {
  font-size: 26rpx;
  color: #007AFF;
}

.reply-text {
  font-size: 26rpx;
  color: #666;
}

.bottom-bar {
  position: fixed;
  left: 0;
  right: 0;
  height: 100rpx;
  background: #fff;
  border-top: 1rpx solid #eee;
  display: flex;
  align-items: center;
  padding: 0 30rpx;
}

.comment-input {
  flex: 1;
  margin-right: 20rpx;
}

.comment-input input {
  background: #f5f5f5;
  padding: 15rpx 30rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
}

.action-buttons {
  display: flex;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 30rpx;
  font-size: 24rpx;
  color: #666;
}

.action-item.active {
  color: #007AFF;
}

.action-item .iconfont {
  font-size: 40rpx;
  margin-bottom: 6rpx;
}
</style> 
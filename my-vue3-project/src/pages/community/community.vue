<template>
  <view class="community-container">
    <!-- 顶部搜索栏 -->
    <view class="search-bar">
      <input type="text" placeholder="搜索案例或问答" v-model="searchText" />
    </view>

    <!-- 分类标签页 -->
    <view class="tab-header">
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'cases' }"
        @click="switchTab('cases')"
      >案例分享</view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'qa' }"
        @click="switchTab('qa')"
      >法律问答</view>
    </view>

    <!-- 案例分享列表 -->
    <scroll-view 
      v-if="currentTab === 'cases'" 
      scroll-y 
      class="content-list"
      @scrolltolower="loadMore"
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherrefresh="onRefresh"
    >
      <view class="case-item" v-for="item in caseList" :key="item.id" @click="navigateToDetail('case', item.id)">
        <view class="case-header">
          <image class="user-avatar" :src="item.userAvatar" mode="aspectFill"></image>
          <text class="username">{{item.username}}</text>
          <text class="time">{{item.time}}</text>
        </view>
        <view class="case-content">
          <view class="case-title">{{item.title}}</view>
          <view class="case-desc">{{item.description}}</view>
          <view class="case-images" v-if="item.images && item.images.length">
            <image 
              v-for="(img, index) in item.images" 
              :key="index" 
              :src="img" 
              mode="aspectFill"
            ></image>
          </view>
        </view>
        <view class="case-footer">
          <view class="action-item">
            <text class="iconfont icon-like"></text>
            <text>{{item.likes}}</text>
          </view>
          <view class="action-item">
            <text class="iconfont icon-comment"></text>
            <text>{{item.comments}}</text>
          </view>
          <view class="action-item">
            <text class="iconfont icon-share"></text>
            <text>分享</text>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 法律问答列表 -->
    <scroll-view 
      v-if="currentTab === 'qa'" 
      scroll-y 
      class="content-list"
      @scrolltolower="loadMore"
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherrefresh="onRefresh"
    >
      <view class="qa-item" v-for="item in qaList" :key="item.id" @click="navigateToDetail('qa', item.id)">
        <view class="qa-header">
          <text class="qa-tag" :class="item.status">{{item.statusText}}</text>
          <text class="qa-title">{{item.title}}</text>
        </view>
        <view class="qa-content">{{item.content}}</view>
        <view class="qa-footer">
          <view class="qa-info">
            <text class="username">{{item.username}}</text>
            <text class="time">{{item.time}}</text>
          </view>
          <view class="qa-stats">
            <text>{{item.answers}}回答</text>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 悬浮发布按钮 -->
    <view class="float-btn" @click="showPublishModal">
      <text class="iconfont icon-add"></text>
    </view>

    <!-- 发布选择弹窗 -->
    <uni-popup ref="popup" type="bottom">
      <view class="popup-content">
        <view class="popup-title">发布内容</view>
        <view class="popup-item" @click="navigateToPublish('case')">
          <text class="iconfont icon-case"></text>
          <text>发布案例</text>
        </view>
        <view class="popup-item" @click="navigateToPublish('qa')">
          <text class="iconfont icon-question"></text>
          <text>发布问题</text>
        </view>
        <view class="popup-cancel" @click="hidePublishModal">取消</view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchText: '',
      currentTab: 'cases',
      isRefreshing: false,
      caseList: [
        {
          id: 1,
          userAvatar: '/static/default-avatar.png',
          username: '法律专家',
          time: '2小时前',
          title: '小张在看与书信',
          description: '与书信和丁禹锡组cp',
          images: ['/static/case1.jpg', '/static/case2.jpg'],
          likes: 128,
          comments: 32
        },
        // 更多案例数据...
      ],
      qaList: [
        {
          id: 1,
          status: 'unsolved',
          statusText: '待解答',
          title: '关于abcd我好饿了的咨询',
          content: '一会儿去宜家吃热狗？',
          username: '用户123',
          time: '1小时前',
          answers: 5
        },
        // 更多问答数据...
      ]
    }
  },
  methods: {
    switchTab(tab) {
      this.currentTab = tab
    },
    loadMore() {
      // TODO: 加载更多数据
      console.log('加载更多')
    },
    async onRefresh() {
      this.isRefreshing = true
      // TODO: 刷新数据
      await new Promise(resolve => setTimeout(resolve, 1000))
      this.isRefreshing = false
    },
    navigateToDetail(type, id) {
      uni.navigateTo({
        url: `/pages/community/${type}-detail?id=${id}`
      })
    },
    showPublishModal() {
      this.$refs.popup.open()
    },
    hidePublishModal() {
      this.$refs.popup.close()
    },
    navigateToPublish(type) {
      this.hidePublishModal()
      uni.navigateTo({
        url: `/pages/community/publish?type=${type}`
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
}

.search-bar input {
  background: #f5f5f5;
  padding: 15rpx 30rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
}

.tab-header {
  display: flex;
  background: #fff;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #eee;
}

.tab-item {
  flex: 1;
  text-align: center;
  font-size: 30rpx;
  color: #666;
  position: relative;
  padding: 10rpx 0;
}

.tab-item.active {
  color: #007AFF;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -20rpx;
  left: 50%;
  transform: translateX(-50%);
  width: 40rpx;
  height: 4rpx;
  background: #007AFF;
}

.content-list {
  height: calc(100vh - 200rpx);
}

.case-item {
  background: #fff;
  margin: 20rpx;
  border-radius: 12rpx;
  padding: 20rpx;
}

.case-header {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.user-avatar {
  width: 60rpx;
  height: 60rpx;
  border-radius: 30rpx;
  margin-right: 15rpx;
}

.username {
  font-size: 28rpx;
  color: #333;
  margin-right: 15rpx;
}

.time {
  font-size: 24rpx;
  color: #999;
}

.case-content {
  margin-bottom: 20rpx;
}

.case-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.case-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.case-images {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10rpx;
  margin-top: 20rpx;
}

.case-images image {
  width: 100%;
  height: 200rpx;
  border-radius: 8rpx;
}

.case-footer {
  display: flex;
  justify-content: space-around;
  border-top: 1rpx solid #eee;
  padding-top: 20rpx;
}

.action-item {
  display: flex;
  align-items: center;
  font-size: 24rpx;
  color: #666;
}

.action-item .iconfont {
  margin-right: 10rpx;
  font-size: 32rpx;
}

.qa-item {
  background: #fff;
  margin: 20rpx;
  border-radius: 12rpx;
  padding: 20rpx;
}

.qa-header {
  margin-bottom: 15rpx;
}

.qa-tag {
  display: inline-block;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
  font-size: 24rpx;
  margin-right: 10rpx;
}

.qa-tag.unsolved {
  background: #fef0f0;
  color: #f56c6c;
}

.qa-tag.solved {
  background: #f0f9eb;
  color: #67c23a;
}

.qa-title {
  font-size: 30rpx;
  font-weight: bold;
}

.qa-content {
  font-size: 28rpx;
  color: #666;
  margin: 15rpx 0;
}

.qa-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 24rpx;
  color: #999;
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
}

.float-btn .iconfont {
  color: #fff;
  font-size: 48rpx;
}

.popup-content {
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding: 30rpx;
}

.popup-title {
  text-align: center;
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
}

.popup-item {
  display: flex;
  align-items: center;
  padding: 30rpx 0;
  font-size: 30rpx;
}

.popup-item .iconfont {
  margin-right: 20rpx;
  font-size: 40rpx;
  color: #007AFF;
}

.popup-cancel {
  text-align: center;
  padding: 30rpx 0;
  color: #999;
  border-top: 1rpx solid #eee;
  margin-top: 20rpx;
}
</style> 
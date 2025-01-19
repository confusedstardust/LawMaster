<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input type="text" placeholder="搜索法律知识、新闻资讯" />
    </view>
    
    <!-- 轮播图区域 -->
    <swiper class="banner" :indicator-dots="true" :autoplay="true">
      <swiper-item v-for="(item, index) in bannerList" :key="index">
        <image :src="item.image" mode="aspectFill"></image>
      </swiper-item>
    </swiper>

    <!-- 分类标签页 -->
    <view class="tab-header">
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'knowledge' }"
        @click="switchTab('knowledge')"
      >法律知识</view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'news' }"
        @click="switchTab('news')"
      >新闻动态</view>
    </view>

    <!-- 法律知识列表 -->
    <scroll-view 
      v-if="currentTab === 'knowledge'" 
      scroll-y 
      class="content-list"
      @scrolltolower="loadMore"
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherrefresh="onRefresh"
    >
      <view class="content-item" v-for="item in knowledgeList" :key="item.id" @click="navigateToDetail('knowledge', item.id)">
        <view class="item-main">
          <view class="item-info">
            <text class="item-title">{{item.title}}</text>
            <text class="item-desc">{{item.description}}</text>
          </view>
          <image v-if="item.cover" :src="item.cover" mode="aspectFill" class="item-image"></image>
        </view>
        <view class="item-footer">
          <view class="item-stats">
            <text class="stats-item">
              <text class="iconfont icon-view"></text>
              {{item.views}}
            </text>
            <text class="stats-item">
              <text class="iconfont icon-like"></text>
              {{item.likes}}
            </text>
          </view>
          <text class="item-time">{{item.time}}</text>
        </view>
      </view>
    </scroll-view>

    <!-- 新闻动态列表 -->
    <scroll-view 
      v-if="currentTab === 'news'" 
      scroll-y 
      class="content-list"
      @scrolltolower="loadMore"
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherrefresh="onRefresh"
    >
      <view class="content-item" v-for="item in newsList" :key="item.id" @click="navigateToDetail('news', item.id)">
        <view class="item-main">
          <view class="item-info">
            <text class="item-title">{{item.title}}</text>
            <text class="item-desc">{{item.description}}</text>
          </view>
          <image v-if="item.cover" :src="item.cover" mode="aspectFill" class="item-image"></image>
        </view>
        <view class="item-footer">
          <view class="item-source">{{item.source}}</view>
          <text class="item-time">{{item.time}}</text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'knowledge',
      isRefreshing: false,
      bannerList: [
        { image: '/static/banner1.jpg' },
        { image: '/static/banner2.jpg' },
        { image: '/static/banner3.jpg' }
      ],
      knowledgeList: [
        {
          id: 1,
          title: '常见合同纠纷解析',
          description: '详解合同签订、履行、违约等常见法律问题...',
          cover: '/static/knowledge1.jpg',
          views: 1234,
          likes: 88,
          time: '2小时前'
        },
        // 更多法律知识数据...
      ],
      newsList: [
        {
          id: 1,
          title: '规定规定',
          description: '规定规定规定规定',
          cover: '/static/news1.jpg',
          source: '最高人民法院',
          time: '1小时前'
        },
        // 更多新闻数据...
      ]
    }
  },
  methods: {
    switchTab(tab) {
      this.currentTab = tab
    },
    loadMore() {
      console.log('加载更多')
    },
    async onRefresh() {
      this.isRefreshing = true
      await new Promise(resolve => setTimeout(resolve, 1000))
      this.isRefreshing = false
    },
    navigateToDetail(type, id) {
      uni.navigateTo({
        url: `/pages/${type}/detail?id=${id}`
      })
    }
  }
}
</script>

<style>
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
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

.banner {
  height: 300rpx;
}

.banner image {
  width: 100%;
  height: 100%;
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
  height: calc(100vh - 450rpx);
}

.content-item {
  background: #fff;
  margin: 20rpx;
  border-radius: 12rpx;
  padding: 20rpx;
}

.item-main {
  display: flex;
  margin-bottom: 20rpx;
}

.item-info {
  flex: 1;
  margin-right: 20rpx;
}

.item-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.item-desc {
  font-size: 26rpx;
  color: #666;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.item-image {
  width: 200rpx;
  height: 140rpx;
  border-radius: 8rpx;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 24rpx;
  color: #999;
}

.item-stats {
  display: flex;
  align-items: center;
}

.stats-item {
  margin-right: 20rpx;
}

.stats-item .iconfont {
  margin-right: 6rpx;
}

.item-source {
  color: #007AFF;
}

.item-time {
  color: #999;
}
</style>

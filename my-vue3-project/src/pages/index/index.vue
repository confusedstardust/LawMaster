<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <!-- <input type="text" placeholder="搜索法律知识、新闻资讯" v-model="searchKeyword" />
      <uni-icons @click="searchArticles" type="search" size="30" color="#007AFF"></uni-icons> -->
      <uni-easyinput prefixIcon="search" v-model="searchKeyword" placeholder="搜索法律知识、新闻资讯" @iconClick="searchArticles">
			</uni-easyinput>
      <!-- <button @click="searchArticles">搜索</button> -->
    </view>
    
    <!-- 轮播图区域 -->
    <swiper class="banner" :indicator-dots="true" :autoplay="true">
      <swiper-item v-for="(item, index) in bannerList" :key="index" @click="navigateToPost(item.id,item.userId)">
        <image :src="item.images" mode="aspectFill"></image>
        <view class="banner-title">{{ item.title }}</view>
      </swiper-item>
    </swiper>


     <!-- 导航按钮组件 -->
     <NavBar :navList="navList" @navClick="onNavClick" />

    <!-- 分类标签页 -->
    <view class="tab-header">
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'news' }"
        @click="switchTab('news')"
      >法律新闻</view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'cases' }"
        @click="switchTab('cases')"
      >法律案例</view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'knowledge' }"
        @click="switchTab('knowledge')"
      >法律知识</view>
      
     
    </view>


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
          </view>
          <image v-if="item.cover" :src="item.cover" mode="aspectFill" class="item-image"></image>
        </view>
        <view class="item-footer">
          <view class="item-source">{{item.source}}</view>
          <view class="item-stats">
            
            <text class="stats-item">
              <text class="iconfont icon-view"></text>
              {{item.views}}
            </text>
          </view>
          <text class="item-time">{{item.time}}</text>
        </view>
      </view>
    </scroll-view>

    
    <!-- 案例 -->
    <scroll-view 
          v-if="currentTab === 'cases'" 
          scroll-y 
          class="content-list"
          @scrolltolower="loadMore"
          refresher-enabled
          :refresher-triggered="isRefreshing"
          @refresherrefresh="onRefresh"
        >
          <view class="content-item" v-for="item in casesList" :key="item.id" @click="navigateToDetail('cases', item.id)">
            <view class="item-main">
              <view class="item-info">
                <text class="item-title">{{item.title}}</text>
              </view>
              <image v-if="item.cover" :src="item.cover" mode="aspectFill" class="item-image"></image>
            </view>
            <view class="item-footer">
              <view class="item-stats">
                <text class="stats-item">
                  <uni-icons 
                          :type="'eye'" 
                          size="16" 
                        ></uni-icons>
                  <text class="iconfont icon-view"></text>
                  {{item.views}}
                </text>
              </view>
              <text class="item-time">{{item.time}}</text>
            </view>
          </view>
        </scroll-view>

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
          </view>
          <image v-if="item.cover" :src="item.cover" mode="aspectFill" class="item-image"></image>
        </view>
        <view class="item-footer">
          <view class="item-stats">
            <text class="stats-item">
              <uni-icons 
                      :type="'eye'" 
                      size="16" 
                    ></uni-icons>
              <text class="iconfont icon-view"></text>
              {{item.views}}
            </text>
          </view>
          <text class="item-time">{{item.time}}</text>
        </view>
      </view>
    </scroll-view>



    <!-- 模态框 -->
    <scroll-view>
      <view v-if="isModalVisible" class="modal">
      <view class="modal-content">
        <text class="modal-title">搜索结果</text>
        <text class="modal-description">找到 {{ filteredKnowledgeList.length + filteredNewsList.length }} 篇文章</text>
        <view>
          <!-- <text v-for="item in filteredKnowledgeList" :key="item.id">{{ item.title }}</text> -->
          <!-- <text v-for="item in filteredNewsList" :key="item.id">{{ item.title }}</text> -->
<!-- 模态框内容 -->
            <view class="content-item" v-for="item in filteredKnowledgeList" :key="item.id" @click="navigateToSearchDetail(item.id)">
              <view class="item-main">
                <view class="item-info">
                  <text class="item-title">{{item.title}}</text>
                </view>
                <!-- <image v-if="item.cover" :src="item.cover" mode="aspectFill" class="item-image"></image> -->
              </view>
              <!-- <view class="item-footer">
                <view class="item-source">{{item.source}}</view>
                <view class="item-stats">
                  
                  <text class="stats-item">
                    <text class="iconfont icon-view"></text>
                    {{item.views}}
                  </text>
                </view>
                <text class="item-time">{{item.time}}</text>
              </view> -->
            </view>







        </view>
        <button @click="closeModal">关闭</button>
      </view>
    </view>
    </scroll-view>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api'; // 引入 API 请求方法
import NavBar from '@/components/NavBar.vue'; // 引入导航按钮组件

export default {
  data() {
    return {
      currentTab: 'knowledge',
      isRefreshing: false,
      bannerList: [], // 初始化为空数组
      knowledgeList: [], // 初始化为空数组
      newsList: [], // 初始化为空数组
      searchKeyword: '',
      filteredKnowledgeList: [],
      filteredNewsList: [],
      casesList: [],
      isModalVisible: false, // 控制模态框的显示
      selectedItem: {}, // 存储选中的项目
      navList: [
        { title: "法律新闻", icon: "map-filled" },
        { title: "法律案例", icon: "wallet-filled" },
        { title: "法律知识", icon: "email-filled" },
        { title: "智能问答", icon: "headphones" }
      ],
    }
  },
  created() {
    this.fetchBannerList(); // 在组件创建时获取轮播图数据
    this.fetchArticles(); // 获取法律知识和新闻数据
  },onShow(){
      this.fetchBannerList(); // 在组件创建时获取轮播图数据
    this.fetchArticles(); // 获取法律知识和新闻数据
    },
  methods: {
    async fetchBannerList() {
      try {
const response = await apiRequest('posts/top', 'get'); // 获取轮播图数据
      let parsedImages = [];
      
      this.bannerList = response.map(item => {
       try {
        parsedImages = Array.isArray(item.images) ? item.images: JSON.parse(item.images)||[];
       }
       catch (error) {
         console.error("解析封面图失败", error);
       }
       
        return {
          ...item,
          images: parsedImages.length > 0 
            ? `http://localhost:8080/files/download/${parsedImages[0]}` 
            : "", // 避免 undefined
        };

      });
      } catch (error) {
        console.error("获取轮播图失败", error);
        uni.showToast({
          title: '获取轮播图失败，请重试',
          icon: 'none'
        }); 
      }
    },
    async fetchArticles() {
      try {
        const response = await apiRequest('articles/all', 'get'); // 获取法律知识和新闻数据
        const articles = response.filter(article => article.visible === 1); // 过滤可见的文章

        this.knowledgeList = articles.filter(article => article.type === '知识'&&article.visible === 1 ).map(article => ({
          id: article.id,
          title: article.title,
          description: article.content,
          cover: article.cover || '', // 假设有封面图字段
          views: article.views,
          likes: article.likes,
          time: new Date(article.createdAt).toLocaleString() // 格式化时间
        }));

        this.newsList = articles.filter(article => article.type === '新闻' && article.visible === 1).map(article => ({
          id: article.id,
          title: article.title,
          description: article.content,
          source: article.source || '未知',
          time: new Date(article.createdAt).toLocaleString() // 格式化时间
        }));

        this.casesList = articles.filter(a => a.type === "案例" && a.visible === 1).map(a => ({
          id: a.id, title: a.title,views: a.views, description: a.content, time: new Date(a.createdAt).toLocaleString()
        }));
      } catch (error) {
        console.error("获取文章失败", error);
        uni.showToast({
          title: '获取文章失败，请重试',
          icon: 'none'
        });
      }
    },async fetchComments(postId) {
      try {
        const response = await apiRequest(`comments/post/${postId}`, 'get');
        return response
      } catch (error) {
        console.error('获取评论失败:', error);
        return 0; // 如果失败，返回 0
      }
    },async getUserInfo(id){
      try {
        const response = await apiRequest(`users/info/${id}`, 'get');
        return response
      } catch (error) {
        console.error('获取用户昵称失败:', error);
        return '';
      }
    },
    searchArticles() {
      this.filterArticles(); // 点击按钮时调用过滤方法
    },
    async filterArticles() {
      if (this.searchKeyword) {
        try {
          const response = await apiRequest(`articles/search/${this.searchKeyword}`, 'get');
          const articles = response.filter(article => article.visible === 1);
          this.filteredKnowledgeList = articles;
          // this.filteredNewsList = articles.filter(article => article.type === '新闻');
          this.showModal(); // 显示模态框
        } catch (error) {
          console.error("搜索失败", error);
          uni.showToast({
            title: '搜索失败，请重试',
            icon: 'none'
          });
        }
      } else {
        this.filteredKnowledgeList = this.knowledgeList;
        this.filteredNewsList = this.newsList;
      }
    },onNavClick(index) {
      switch (this.navList[index].title) {
        case "法律新闻":
        uni.navigateTo({
        url: `/pages/index/articleList?pagetitle=法律新闻`
      });
        break;
        case "法律案例":
        uni.navigateTo({
        url: `/pages/index/articleList?pagetitle=法律案例`
      });
        break;
        case "法律知识":
        uni.navigateTo({
        url: `/pages/index/articleList?pagetitle=法律知识`
      });
        break;
        case "智能问答":
        uni.navigateTo({
        url: `/pages/index/chater`
      });
        break;
        
      }
      console.log("点击了导航按钮：", this.navList[index].title);
    },
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
    async navigateToDetail(type, id) {
      const list = [...this.knowledgeList, ...this.newsList, ...this.casesList];
      const article = list.find(item => item.id === id);
      const res= await apiRequest(`articles/addviews/${id}`, 'post');

      uni.navigateTo({
        url: `/pages/index/articleDetails?article=${JSON.stringify(article)}`
      });
    },
    nav() {

        uni.navigateTo({
        url: `/pages/index/chater`
      });
    },
    async navigateToSearchDetail( id) {
      // const list = type === 'knowledge' ? this.knowledgeList : this.newsList;
      const article = this.filteredKnowledgeList.find(item => item.id === id);
      const res= await apiRequest(`articles/addviews/${id}`, 'post');
      uni.navigateTo({
        url: `/pages/index/articleDetails?article=${JSON.stringify(article)}`
      });
    },
   async navigateToPost(id,userId) {
      let commentsCount,info,userNickName,userAvatar;
        commentsCount = await this.fetchComments(id);
        info = await this.getUserInfo(userId);
        userNickName = info.nickname;
        userAvatar=  `http://localhost:8080/files/download/${info.avatar} `;
      uni.navigateTo({
        // url: `/posts/${id}`
        url: `/pages/community/post-detail?id=${id}&comments=${JSON.stringify(commentsCount)}&userAvatar=${userAvatar}&username=${userNickName}`
      })
    }
    ,
    showModal() {
      this.isModalVisible = true; // 显示模态框
    },
    closeModal() {
      this.isModalVisible = false; // 隐藏模态框
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
  display: flex;
}

.search-bar input {
  background: #f5f5f5;
  padding: 15rpx 30rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
  flex: 1;
}

.search-bar button {
  margin-left: 10rpx;
  padding: 15rpx 30rpx;
  background-color: #007AFF;
  color: #fff;
  border: none;
  border-radius: 30rpx;
  cursor: pointer;
  height: 100%;
}

.search-bar button:hover {
  background-color: #0056b3;
}

.banner {
  height: 300rpx;
  position: relative;
}

.banner image {
  width: 100%;
  height: 100%;
}

.banner-title {
  position: absolute;
  bottom: 10rpx;
  left: 10rpx;
  color: #fff;
  background-color: rgba(0, 0, 0, 0.5);
  padding: 10rpx;
  border-radius: 5rpx;
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
  display: flex;
  align-items: center;
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

/* 模态框样式 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: #fff;
  padding: 20rpx;
  border-radius: 10rpx;
  width: 80%;
}

.modal-title {
  font-size: 28rpx;
  font-weight: bold;
}

.modal-description {
  font-size: 24rpx;
  margin-top: 10rpx;
}

.modal button {
  margin-top: 20rpx;
  padding: 10rpx 20rpx;
  background-color: #007AFF;
  color: #fff;
  border: none;
  border-radius: 5rpx;
  cursor: pointer;
}
.modal button:hover {
  background-color: #0056b3;
}

.add-button {
  position: fixed;
  right: 30rpx;
  bottom: 100rpx;
  width: 100rpx;
  height: 100rpx;
  background-color: #2979ff;
  /* color:#0056b3; */
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
  z-index: 999;
}

</style>
<!-- .popup-btn {
  min-width: 160rpx;
  height: 72rpx;
  line-height: 72rpx;
  border-radius: 36rpx;
  font-size: 28rpx;
  color: #fff;
  border: none;
  transition: all 0.3s ease;
  background-color: #2979ff;
} -->
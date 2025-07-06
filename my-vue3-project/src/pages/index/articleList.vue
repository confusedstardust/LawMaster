<template>
  <view>
    <uni-list>
      <!-- 法律知识 -->
      <!-- <uni-list-item :title="pageTitle" clickable @click="toggleList('knowledge')"></uni-list-item> -->
        <uni-list-item 
          v-for="item in catList" 
          :key="item.id" 
          :title="item.title" 
          :note="formatTime(item.time)"
          clickable 
          @click="navigateToDetail('knowledge', item.id)">
          <template v-slot:footer>
          <view class="stats-item">
            <uni-icons type="eye" size="16"></uni-icons>
            <text class="iconfont icon-view"></text>
            <text class="views">{{ item.views }}</text> 
          </view>
        </template>
        </uni-list-item>


    </uni-list>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api'; // 引入 API 请求方法
import { onShow } from '@dcloudio/uni-app';

export default {
  data() {
    return {
      pageTitle: "",
      catList: [],
      knowledgeList: [],
      newsList: [],
      casesList: []
    };
  },
  onShow() {
    console.log("onShow 触发");
    const pages = getCurrentPages();
    const options = pages[pages.length - 1]?.options || {};
    this.pageTitle = options.pageTitle || options.pagetitle || "";

    console.log("onShow 赋值后 pageTitle:", this.pageTitle);
    
    this.fetchArticles(); // 进入页面时自动获取文章
  },
  methods: {
    async fetchArticles() {
      try {
        const articles = await apiRequest("articles/all", "get");
        // const articles = response.filter(article => article.visible === 1);
        
        this.knowledgeList = articles.filter(a => a.type === "知识").map(a => ({
          id: a.id, title: a.title, description: a.content, cover: a.cover || "", views: a.views, likes: a.likes, time: new Date(a.createdAt).toLocaleString()
        }));

        this.newsList = articles.filter(a => a.type === "新闻").map(a => ({
          id: a.id, views: a.views,title: a.title, description: a.content, source: a.source || "未知", time: new Date(a.createdAt).toLocaleString()
        }));

        this.casesList = articles.filter(a => a.type === "案例").map(a => ({
          id: a.id, title: a.title,views: a.views, description: a.content, time: new Date(a.createdAt).toLocaleString()
        }));

        switch (this.pageTitle) {
          case "法律知识":
            this.catList = this.knowledgeList;
            break;
          case "法律新闻":
            this.catList = this.newsList;
            break;
          case "法律案例":
            this.catList = this.casesList;
            break;
          default:
            this.catList = [];
        }

        console.log("更新后的 catList:", this.catList);

      } catch (error) {
        console.error("获取文章失败", error);
        uni.showToast({ title: "获取文章失败，请重试", icon: "none" });
      }
    },

    async navigateToDetail(type, id) {
      let list = this.catList;

      const article = list.find(item => item.id === id);
      console.log(article);
      const addViewsResponse = await apiRequest(`articles/addviews/${id}`, 'post');
      if (article) {
        uni.navigateTo({
          url: `/pages/index/articleDetails?article=${encodeURIComponent(JSON.stringify(article))}`
        });
      }
    }
    ,formatTime(time) {
      // 解析成 Date 对象
    const date = new Date(time);
    
    // 格式化为 "YYYY-MM-DD"
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // 月份从 0 开始，需要 +1
    const day = String(date.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
    }
  }
};
</script>

<style>
.stats-item {
  display: flex;
  align-items: center;
  gap: 4px; /* 控制图标和文字之间的间距 */
  font-size: 14px;
  color: #666; /* 设置颜色 */
}

.views {
  margin-left: 4px;
}
/* 这里可以添加样式 */
</style>

<template>
  <view class="news-management">
    <uni-card title="新闻管理" is-full>
      
      <!-- 🔹 搜索框 -->
      <view class="search-bar">
        <uni-data-select class="uni-px-5"
          v-model="Typevalue"
          :localdata="range"
          @change="fetchNews"
          :clear="true"
          placeholder="查询类型"
        ></uni-data-select>
        <uni-easyinput class="uni-mt-5" suffixIcon="search" v-model="searchQuery" @iconClick="filterNews"></uni-easyinput>
      </view>

      <!-- 🔹 新闻列表 -->
      <uni-table ref="table" class="table" :loading="loading" border stripe emptyText="暂无更多数据">
        <uni-tr>
          <uni-th width="130" align="left">标题</uni-th>
          <uni-th width="100" align="left">类型</uni-th>
          <uni-th width="100" align="left">发布日期</uni-th>
        </uni-tr>
        <uni-tr v-for="(newsItem, index) in paginatedNews" :key="index" @click="showPopup(newsItem)">
          <uni-td style="font-size: small;">{{ newsItem.title }}</uni-td>
          <uni-td style="font-size: small;"><view class="name">{{ getCategoryById(newsItem.categoryId) }}</view></uni-td>
          <uni-td style="font-size: small;" align="left">{{ formatDate(newsItem.createdAt) }}</uni-td>
        </uni-tr>
      </uni-table>

      <!-- 🔹 分页组件 -->
      <uni-pagination 
        :total="filteredNews.length" 
        :pageSize="pageSize" 
        :current="currentPage" 
        @change="handlePageChange"
      />
    </uni-card>

    <!-- 🔹 新闻详情弹出框 -->
    <uni-popup ref="popup" type="center">
      <view class="popup-container">
        
        <!-- 🔹 标题 -->
        <view class="popup-header">
          <text class="popup-title">{{ selectedNews.title }}</text>
        </view>

        <!-- 🔹 内容（固定高度 + 滚动条） -->
        <view class="popup-body">
          <text class="popup-content" v-html="selectedNews.content"></text>
        </view>

        <!-- 🔹 操作按钮 -->
        <view class="popup-actions">
          <button class="popup-btn delete" @click="handleDeleteNews">删除新闻</button>
          <button class="popup-btn highlight" @click="handleSetAsHeadline">设为头条</button>
        </view>

      </view>
    </uni-popup>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api';

export default {
  data() {
    return {
      loading: false,
      searchQuery: '',
      newsData: [], // 存储所有新闻数据
      filteredNews: [], // 经过搜索过滤的新闻
      currentPage: 1,
      pageSize: 10,
      Typevalue: 0,
      categoryList:[],
      range: [
        { value: 1, text: "新闻ID" },
        { value: 2, text: "内容" }
      ],
      selectedNews: {}, // 存储选中的新闻
    };
  },
  computed: {
    paginatedNews() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredNews.slice(start, end);
    },
  },
  mounted() {
    this.fetchNews();
  },
  methods: {
    async fetchNews() {
      this.loading = true;
      let apiUrl = '';
      try {
        switch (this.Typevalue) {
          case 1: // 新闻ID
            apiUrl = `articles/${this.searchQuery}`;
            break;
          case 2: // 内容
            apiUrl = `articles/search/${this.searchQuery}`;
            break;
          default:
            apiUrl = `articles/all`;
        }

        const response = await apiRequest(apiUrl, 'get');
        this.newsData = Array.isArray(response) ? response : [response];
        this.filteredNews = [...this.newsData];
      } catch (error) {
        console.error('获取新闻失败:', error);
      } finally {
        this.loading = false;
      }
    },
    formatDate(dateString) {
      // 预处理字符串，去掉最后的 " 00:00"
      const cleanedDateString = dateString.replace(" 00:00", "");
      
      // 解析成 Date 对象
      const date = new Date(cleanedDateString);
      
      // 格式化为 "YYYY-MM-DD"
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0"); // 月份从 0 开始，需要 +1
      const day = String(date.getDate()).padStart(2, "0");

      return `${year}-${month}-${day}`;
    },
    filterNews() {
      this.fetchNews();
    },
    handlePageChange(event) {
      this.currentPage = event.current;
    },
    showPopup(newsItem) {
      this.selectedNews = newsItem;
      this.$refs.popup.open();
    },
    getCategoryById(id){
      const categoryVal=this.categoryList.filter(item=>item.id==id)
      console.log(categoryVal)
      return categoryVal[0].name
    },
    async handleDeleteNews() {
      if (!this.selectedNews.id) return;
      try {
        await apiRequest(`news/delete/${this.selectedNews.id}`, 'DELETE');
        this.newsData = this.newsData.filter(news => news.id !== this.selectedNews.id);
        this.filteredNews = [...this.newsData];
        uni.showToast({ title: "删除成功", icon: "success" });
        this.$refs.popup.close();
      } catch (error) {
        console.error("删除新闻失败:", error);
        uni.showToast({ title: "删除失败", icon: "none" });
      }
    },
    async handleSetAsHeadline() {
      if (!this.selectedNews.id) return;
      try {
        await apiRequest(`news/set-headline/${this.selectedNews.id}`, 'POST');
        uni.showToast({ title: "已设为头条", icon: "success" });
        this.$refs.popup.close();
      } catch (error) {
        console.error("设为头条失败:", error);
        uni.showToast({ title: "操作失败", icon: "none" });
      }
    },
  },
};
</script>

<style scoped>
.news-management {
  padding: 16px;
}

.search-bar {
  display: flex;
  margin-bottom: 16px;
}

.table {
  margin-top: 10px;
}

/* 🔹 弹出框样式 */
.popup-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  text-align: center;
  width: 320px;
}

/* Section 1: 标题 */
.popup-header {
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.popup-title {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

/* Section 2: 内容（固定高度 + 滚动条） */
.popup-body {
  max-height: 420px;
  overflow-y: auto;
  padding: 10px;
  text-align: left;
}

.popup-content {
  font-size: 14px;
  color: #555;
  line-height: 1.5;
}

/* Section 3: 操作按钮 */
.popup-actions {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-top: 1px solid #ddd;
}

.popup-btn {
  padding: 10px;
  font-size: 14px;
  width: 45%;
  border-radius: 5px;
  cursor: pointer;
}

.delete {
  background-color: #e74c3c;
  color: white;
}

.highlight {
  background-color: #3498db;
  color: white;
}
</style>

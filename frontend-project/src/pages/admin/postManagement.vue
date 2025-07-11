<template>
  <view class="post-management">
    <uni-card title="帖子管理" is-full>
      <view class="search-bar">
        <uni-data-select class="uni-px-5"
          v-model="Typevalue"
          :localdata="range"
          @change="fetchPosts"
          :clear="true"
          placeholder="查询类型"
        ></uni-data-select>
        <uni-easyinput class="uni-mt-5" suffixIcon="search" v-model="value" @iconClick="fetchPosts"></uni-easyinput>
      </view>

      <uni-table ref="table" class="table" :loading="loading" border stripe emptyText="暂无更多数据">
        <uni-tr>
          <uni-th width="130" align="left">标题</uni-th>
          <uni-th width="70" align="left">作者</uni-th>
          <uni-th width="100" align="left">日期</uni-th>
        </uni-tr>
        <uni-tr v-for="(item, index) in paginatedPosts" :key="index" @click="showPopup(item)">
          <uni-td style="font-size: small;">{{ item.title }}</uni-td>
          <uni-td style="font-size: small;"><view class="name">{{ item.username }}</view></uni-td>
          <uni-td style="font-size: small;" align="left">{{ formatDate(item.createdAt) }}</uni-td>
        </uni-tr>
      </uni-table>

      <!-- 分页组件 -->
      <uni-pagination 
        :total="tableData.length" 
        :pageSize="pageSize" 
        :current="currentPage" 
        @change="onPageChange"
      />
    </uni-card>

    <!-- 🔹 帖子详情弹出框 -->
    <uni-popup ref="popup" type="center">
      <view class="popup-container">
        
        <!-- 🔹 Section 1: 标题 -->
        <view class="popup-header">
          <text class="popup-title">{{ selectedPost.title }}</text>
        </view>

        <!-- 🔹 Section 2: 内容 -->
        <view class="popup-body">
          <text class="popup-content">{{ selectedPost.content }}</text>
        </view>

        <!-- 🔹 Section 3: 操作按钮 -->
        <view class="popup-actions">
          <button class="popup-btn delete" @click="handleDeletePost">删除帖子</button>
          <button 
            class="popup-btn edit" 
            @click="handleSetAsCarousel()"
          >
            {{ selectedPost.istop === 1 ? '取消轮播' : '设为轮播' }}
          </button>
        </view>

      </view>
    </uni-popup>
  </view>
</template>

<script>
import { apiRequest, formatDate } from '@/utils/api';

export default {
  data() {
    return {
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      Typevalue: 0,
      value: "",
      range: [
        { value: 1, text: "用户ID" },
        { value: 2, text: "帖子名" },
        { value: 3, text: "帖子ID" },
        { value: 4, text: "标签" }
      ],
      loading: false,
      selectedPost: {},
    };
  },
  computed: {
    paginatedPosts() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.tableData.slice(start, end);
    },
  },
  mounted() {
    this.fetchPosts();
  },
  methods: {
    async fetchPosts() {
      this.loading = true;
      let apiUrl = '';
      try {
        switch (this.Typevalue) {
          case 1: // 用户ID
            apiUrl = `posts/user/${this.value}`;
            break;
          case 2: // 帖子名
            apiUrl = `posts/keyword/${this.value}`;
            break;
          case 3: // 帖子ID
            apiUrl = `posts/postIds?postIds=${this.value}`;
            break;
          case 4: // 标签
            apiUrl = `tag/${this.value}`;
            break;
          default:
            apiUrl = `posts`;
        }

        const response = await apiRequest(apiUrl, 'get');
            this.tableData = await Promise.all(
            response.map(async post => {
              const info = await this.getUserInfo(post.userId);
              return {
                id: post.id,
                title: post.title,
                content: post.content,
                userAvatar: post.Avatar,
                username: info.nickname,
                likes: post.likes,
                createdAt: post.createdAt,
                istop:post.istop
              };
            })
          );
      } catch (error) {
        console.error('获取帖子失败:', error);
      } finally {
        this.loading = false;
      }
    },
    closePopup() {
      this.$refs.popup.close();
    },
    async getUserInfo(id){
      try {
        const response = await apiRequest(`users/info/${id}`, 'get');
        return response
      } catch (error) {
        console.error('获取用户昵称失败:', error);
        return '';
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
    onPageChange(event) {
      this.currentPage = event.current;
    },
    showPopup(post) {
      this.selectedPost = post;
      this.$refs.popup.open();
    },
    async handleDeletePost() {
      if (!this.selectedPost.id) return;
      try {
        await apiRequest(`posts/delete/${this.selectedPost.id}`, 'Post');
        this.tableData = this.tableData.filter(post => post.id !== this.selectedPost.id);
        uni.showToast({ title: "删除成功", icon: "success" });
        this.$refs.popup.close();
      } catch (error) {
        console.error("删除帖子失败:", error);
        uni.showToast({ title: "删除失败", icon: "none" });
      }
    },
    async handleSetAsCarousel() {
      if (!this.selectedPost.id) return;
      try {
        await apiRequest(`posts/settoptoggle/${this.selectedPost.id}`, 'POST');
        uni.showToast({ title: "已更改", icon: "success" });
        this.$refs.popup.close();
        this.fetchPosts();
      } catch (error) {
        console.error("设为轮播贴失败:", error);
        uni.showToast({ title: "操作失败", icon: "none" });
      }
    },
  },
};
</script>
<style scoped>
.post-management {
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

/* Section 2: 内容 */
.popup-body {
  max-height: 400px; /* 设置固定高度 */
  overflow-y: auto; /* 超出部分显示滚动条 */
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



.delete {
  background-color: #e74c3c;
  color: white;
}

.carousel {
  background-color: #3498db;
  color: white;
}

.popup-btn {
  min-width: 160rpx;
  height: 72rpx;
  line-height: 72rpx;
  border-radius: 36rpx;
  font-size: 28rpx;
  border: none;
  transition: all 0.3s ease;
}

.popup-btn.delete {
  background-color: #ff4d4f;
  color: #fff;
}

.popup-btn.delete:active {
  background-color: #cf1322;
}

.popup-btn.edit {
  background-color: #2979ff;
  color: #fff;
}

.popup-btn.edit:active {
  background-color: #2567db;
}
</style>
<template>
  <view class="user-management">
    <uni-card title="用户管理" is-full>
      
      <!-- 🔹 搜索框 -->
      <view class="search-bar">
        <uni-data-select class="uni-px-5"
          v-model="Typevalue"
          :localdata="range"
          @change="fetchUsers"
          :clear="true"
          placeholder="查询类型"
        ></uni-data-select>
        <uni-easyinput class="uni-mt-5" suffixIcon="search" v-model="searchQuery" @iconClick="filterUsers"></uni-easyinput>
      </view>

      <!-- 🔹 用户列表 -->
      <uni-table ref="table" class="table" :loading="loading" border stripe emptyText="暂无更多数据">
        <uni-tr>
          <uni-th width="100" align="left">用户名</uni-th>
          <uni-th width="60" align="left">权限</uni-th>
          <uni-th width="80" align="left">注册日期</uni-th>
        </uni-tr>
        <uni-tr v-for="(user, index) in paginatedUsers" :key="index" @click="showPopup(user)">
          <uni-td style="font-size: small;">{{ user.username }}</uni-td>
          <uni-td style="font-size: small;"><view class="email">{{ user.role }}</view></uni-td>
          <uni-td style="font-size: small;" align="left">{{ formatDate(user.created_at)}}</uni-td>
        </uni-tr>
      </uni-table>

      <!-- 🔹 分页组件 -->
      <uni-pagination 
        :total="filteredUsers.length" 
        :pageSize="pageSize" 
        :current="currentPage" 
        @change="handlePageChange"
      />
    </uni-card>

    <!-- 🔹 用户详情弹出框 -->
    <uni-popup ref="popup" type="center">
      <view class="popup-container">
        
        <!-- 🔹 标题 -->
        <view class="popup-header">
          <text class="popup-title">{{ selectedUser.username }}</text>
        </view>

        <!-- 🔹 内容（固定高度 + 滚动条） -->
        <view class="popup-body">
          <text class="popup-content">邮箱：{{ selectedUser.role }}</text>
          <text class="popup-content">注册日期：{{ formatDate(selectedUser.created_at) }}</text>
        </view>

        <!-- 🔹 操作按钮 -->
        <view class="popup-actions">
          <button class="popup-btn delete" @click="handleDeleteUser">删除用户</button>
          <button class="popup-btn role" @click="handleChangeRole">修改权限</button>
          <button class="popup-btn role" @click="handleChangeRole">修改密码</button>

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
      usersData: [], // 存储所有用户数据
      filteredUsers: [], // 经过搜索过滤的用户
      currentPage: 1,
      pageSize: 10,
      Typevalue: 0,
      range: [
        { value: 1, text: "用户ID" },
        { value: 2, text: "用户名" },
        { value: 3, text: "用户类型" }
      ],
      selectedUser: {}, // 存储选中的用户
    };
  },
  computed: {
    paginatedUsers() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredUsers.slice(start, end);
    },
  },
  mounted() {
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
      this.loading = true;
      let apiUrl = '';
      try {
        switch (this.Typevalue) {
          case 1: // 用户ID
            apiUrl = `http://localhost:8080/users/${this.searchQuery}`;
            break;
          case 2: // 用户名
            apiUrl = `http://localhost:8080/users/username/${this.searchQuery}`;
            break;
          case 3: // 用户类型
            apiUrl = `http://localhost:8080/users/role/${this.searchQuery}`;
            break;
          default:
            apiUrl = `http://localhost:8080/users/allusers`;
        }

        const response = await apiRequest(apiUrl, 'get');
        this.usersData = response.map(user => ({
          id: user.id,
          username: user.username,
          role: user.role,
          created_at: user.createdAt
        }));
        this.filteredUsers = [...this.usersData];
      } catch (error) {
        console.error('获取用户失败:', error);
      } finally {
        this.loading = false;
      }
    },
    filterUsers() {
      this.fetchUsers();
    },
    handlePageChange(event) {
      this.currentPage = event.current;
    },
    showPopup(user) {
      this.selectedUser = user;
      this.$refs.popup.open();
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
    async handleDeleteUser() {
      if (!this.selectedUser.id) return;
      try {
        await apiRequest(`users/delete/${this.selectedUser.id}`, 'DELETE');
        this.usersData = this.usersData.filter(user => user.id !== this.selectedUser.id);
        this.filteredUsers = [...this.usersData];
        uni.showToast({ title: "删除成功", icon: "success" });
        this.$refs.popup.close();
      } catch (error) {
        console.error("删除用户失败:", error);
        uni.showToast({ title: "删除失败", icon: "none" });
      }
    },
    async handleChangeRole() {
      if (!this.selectedUser.id) return;
      try {
        await apiRequest(`users/change-role/${this.selectedUser.id}`, 'POST');
        uni.showToast({ title: "权限已修改", icon: "success" });
        this.$refs.popup.close();
      } catch (error) {
        console.error("修改权限失败:", error);
        uni.showToast({ title: "操作失败", icon: "none" });
      }
    },
  },
};
</script>

<style scoped>
.user-management {
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
  max-height: 150px;
  overflow-y: auto;
  padding: 10px;
  text-align: left;
}

.popup-content {
  font-size: 14px;
  color: #555;
  line-height: 1.5;
  display: block;
  margin-bottom: 5px;
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

.role {
  background-color: #3498db;
  color: white;
}
</style>

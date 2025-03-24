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
       <!-- 新增按钮 -->
    <view class="add-button" @click="showAddForm">
      <uni-icons type="plusempty" size="24" color="#fff"></uni-icons>
    </view>

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
          <text class="popup-title">用户详情</text>
        </view>

        <!-- 🔹 内容 -->
        <view class="popup-body">
          <view class="detail-item">
            <text class="detail-label">用户昵称：</text>
            <uni-easyinput
              v-model="selectedUser.nickname"
              placeholder="请输入用户昵称"
              trim="both"
            />
          </view>

          <view class="detail-item">
            <text class="detail-label">手机号码：</text>
            <uni-easyinput
              v-model="selectedUser.pnumber"
              placeholder="请输入手机号码"
              trim="both"
            />
          </view>

          <view class="detail-item">
            <text class="detail-label">用户权限：</text>
            <uni-data-select
              v-model="selectedUser.role"
              :localdata="[
                { value: 'admin', text: '管理员' },
                { value: 'user', text: '普通用户' }
              ]"
              placeholder="请选择用户权限"
            />
          </view>

          <view class="form-item">
            <text class="label">用户名：</text>
            <text>{{ selectedUser.username }}</text>
          </view>
          <view class="form-item">
            <text class="label">注册日期：</text>
            <text>{{ formatDate(selectedUser.created_at) }}</text>
          </view>
        </view>

        <!-- 🔹 操作按钮 -->
        <view class="popup-actions">
          <button class="popup-btn edit" @click="handleSaveChanges">保存修改</button>
          <button class="popup-btn delete" @click="handleDeleteUser">删除用户</button>
        </view>
      </view>
    </uni-popup>

    <!-- 新增表单弹窗 -->
    <uni-popup ref="addFormPopup" type="center">
      <view class="form-container">
        <view class="form-header">
          <text class="form-title">新增内容</text>
        </view>
        
        <view class="form-body">
          <uni-forms ref="form" :model="formData">
            <uni-forms-item label="用户名" required>
              <uni-easyinput v-model="formData.username" placeholder="请输入用户名"/>
            </uni-forms-item>
            
            <uni-forms-item label="权限" required>
              <uni-data-select
                v-model="formData.role"
                :localdata="typeOptions"
                placeholder="请选择类型"
              />
            </uni-forms-item>

            <uni-forms-item label="昵称" required>
              <uni-easyinput v-model="formData.nickname" placeholder="请输入昵称"/>
            </uni-forms-item>
            <uni-forms-item label="电话号码" required>
              <uni-easyinput v-model="formData.pnumber" placeholder="请输入电话号码"/>
            </uni-forms-item>
            <uni-forms-item label="用户密码" required>
              <uni-easyinput
                v-model="formData.password"
                type="password"
                placeholder="请输入密码"
              />
            </uni-forms-item>
          </uni-forms>
        </view>

        <view class="form-actions">
          <button class="popup-btn delete" @click="closeAddForm">取消</button>
          <button class="popup-btn edit" @click="submitForm">新增</button>
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
      formData: {
        password: '',
        role: '',
        username: '',
        pnumber: ''
      },
      typeOptions: [
        { value: 'admin', text: 'admin' },
        { value: 'user', text: 'user' }
      ],
      range: [
        { value: 1, text: "用户ID" },
        { value: 2, text: "用户名" },
        { value: 3, text: "用户类型" }
      ],
      selectedUser: {}, // 存储选中的用户
      roleOptions: [
        { value: 'admin', text: '管理员' },
        { value: 'user', text: '普通用户' }
      ],
      originalUserData: {}, // 用于存储未修改的用户数据
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
            apiUrl = `users/${this.searchQuery}`;
            break;
          case 2: // 用户名
            apiUrl = `users/username/${this.searchQuery}`;
            break;
          case 3: // 用户类型
            apiUrl = `users/role/${this.searchQuery}`;
            break;
          default:
            apiUrl = `users/allusers`;
        }

        const response = await apiRequest(apiUrl, 'get');
        this.usersData = response.map(user => ({
          id: user.id,
          username: user.username,
          nickname: user.nickname,
          pnumber: user.pnumber,
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
    
    async submitForm() {
      if (!this.formData.password || !this.formData.role || !this.formData.username || !this.formData.pnumber) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        });
        return;
      }
      
      try {
        // this.formData.content=this.formData.content.detail.html;
        const response = await apiRequest('users/register', 'post',this.formData )
        if (response) {
          uni.showToast({
          title: '添加成功',
          icon: 'success'
        });
        this.closeAddForm();
        this.fetchNews();
        }
      } catch (error) {
        console.error('添加失败:', error);
        uni.showToast({
          title: '添加失败',
          icon: 'none'
        });
      }
    },
    
    async fetchNews() {
      this.loading = true;
      try {
        const response=await apiRequest('users/allusers','get');
        this.usersData = response.map(user => ({
          id: user.id,
          username: user.username,
          nickname: user.nickname,
          pnumber: user.pnumber,
          role: user.role,
          created_at: user.createdAt
        }));
        this.filteredUsers = [...this.usersData];
      } catch (error) {
        console.error('获取新闻失败:', error);
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
      this.selectedUser = { ...user };
      this.originalUserData = { ...user }; // 保存原始数据
      this.$refs.popup.open();
    },
    closePopup() {
      this.$refs.popup.close();
    },
    handleRoleChange(value) {
      this.selectedUser.role = value;
    },    showAddForm() {
      this.formData = {
        password: '',
        role: '',
        username: '',
        pnumber: '',
        password: '',
        nickname: '',
      };
      this.$refs.addFormPopup.open();
    },
    
    closeAddForm() {
      this.$refs.addFormPopup.close();
      this.formData = {
        password: '',
        role: '',
        username: '',
        pnumber: '',
        password: '',
        nickname: '',
      };
    },
    async handleSaveChanges() {
      if (!this.selectedUser.id) return;
      
      try {
        // 只有当角色发生变化时才发送请求
        if (this.selectedUser.role !== this.originalUserData.role || this.selectedUser.nickname !== this.originalUserData.nickname || this.selectedUser.pnumber !== this.originalUserData.pnumber) {
          await apiRequest(`users/update`, 'POST', {
            id: this.selectedUser.id,
            role: this.selectedUser.role,
            nickname: this.selectedUser.nickname,
            pnumber: this.selectedUser.pnumber
          });
          
          // 更新本地数据
          const userIndex = this.usersData.findIndex(u => u.id === this.selectedUser.id);
          if (userIndex !== -1) {
            this.usersData[userIndex] = { ...this.selectedUser };
            this.filteredUsers = [...this.usersData];
          }
          
          uni.showToast({ title: "更新成功", icon: "success" });
          this.$refs.popup.close();
        } else {
          this.$refs.popup.close();
        }
      } catch (error) {
        console.error("更新失败:", error);
        uni.showToast({ title: "更新失败", icon: "none" });
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
    async handleDeleteUser() {
      if (!this.selectedUser.id) return;
      try {
        await apiRequest(`users/delete/${this.selectedUser.id}`, 'post');
        this.usersData = this.usersData.filter(user => user.id !== this.selectedUser.id);
        this.filteredUsers = [...this.usersData];
        uni.showToast({ title: "删除成功", icon: "success" });
        this.$refs.popup.close();
      } catch (error) {
        console.error("删除用户失败:", error);
        uni.showToast({ title: "删除失败", icon: "none" });
      }
    },
  },
};
</script>

<style scoped>
.user-management {
  padding: 16px;
}
.form-container {
  background-color: #fff;
  border-radius: 24rpx;
  width: 80vw;
  max-height: 80vh;
  padding: 30rpx;
}.form-header {
  margin-bottom: 30rpx;
  text-align: center;
}.form-btn.cancel {
  background-color: #f5f5f5;
  color: #666;
}

.form-btn.submit {
  background-color: #2979ff;
  color: #fff;
}

.form-title {
  font-size: 32rpx;
  font-weight: bold;
}

.form-body {
  max-height: 60vh;
  overflow-y: auto;
}.rich-editor {
  width: 100%;
  height: 300rpx;
  border: 1rpx solid #eee;
  border-radius: 8rpx;
}

.form-actions {
  margin-top: 30rpx;
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
}

.form-btn {
  padding: 16rpx 40rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}
.search-bar {
  display: flex;
  margin-bottom: 16px;
}

.table {
  margin-top: 10px;
}

.popup-container {
  background-color: #fff;
  border-radius: 24rpx;
  width: 600rpx;
  min-height: 400rpx;
  max-height: 800rpx;
  padding: 30rpx;
  display: flex;
  flex-direction: column;
}

.popup-header {
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid #eee;
  flex-shrink: 0;
}

.popup-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.popup-body {
  flex: 1;
  overflow-y: auto;
  padding: 0 20rpx;
}

.detail-item {
  margin-bottom: 24rpx;
  display: flex;
  align-items: center;
  padding: 16rpx;
  border-radius: 12rpx;
  background-color: #f8f9fa;
}

.detail-label {
  width: 160rpx;
  color: #666;
  font-size: 28rpx;
  flex-shrink: 0;
}

.detail-item :deep(.uni-easyinput__content),
.detail-item :deep(.uni-data-select) {
  flex: 1;
  background-color: #fff;
  border-radius: 8rpx;
  overflow: hidden;
}

.detail-item :deep(.uni-easyinput__content) {
  min-height: 70rpx;
  border: 2rpx solid #ddd;
}

.detail-item :deep(.uni-data-select) {
  border: 2rpx solid #ddd;
}

.detail-item :deep(.uni-easyinput__content-input) {
  font-size: 28rpx;
  color: #333;
}

.popup-actions {
  margin-top: 30rpx;
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
  padding-top: 20rpx;
  border-top: 2rpx solid #eee;
  flex-shrink: 0;
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

.add-button {
  position: fixed;
  right: 30rpx;
  bottom: 30rpx;
  width: 100rpx;
  height: 100rpx;
  background-color: #2979ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
  z-index: 999;
}

</style>

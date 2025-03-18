<template>
  <view class="question-bank-management">
    <uni-card title="题库管理" is-full>
      
      <!-- 🔹 搜索框 -->
      <view class="search-bar">
        <uni-data-select class="uni-px-5"
          v-model="Typevalue"
          :localdata="range"
          @change="fetchQuestions"
          :clear="true"
          placeholder="查询类型"
        ></uni-data-select>
        <uni-easyinput class="uni-mt-5" suffixIcon="search" v-model="searchQuery" @iconClick="filterQuestions"></uni-easyinput>
      </view>

      <!-- 🔹 题库列表 -->
      <uni-table ref="table" class="table" :loading="loading" border stripe emptyText="暂无更多数据">
        <uni-tr>
          <uni-th width="70" align="left">ID</uni-th>
          <uni-th width="130" align="left">题目</uni-th>
          <uni-th width="100" align="left">类型</uni-th>
        </uni-tr>
        <uni-tr v-for="(question, index) in paginatedQuestions" :key="index" @click="showPopup(question)">
          <uni-td style="font-size: small;">{{ question.id }}</uni-td>
          <uni-td style="font-size: small;"><view class="type">{{ question.title }}</view></uni-td>
          <uni-td style="font-size: small;" align="left">{{ getCategoryById(question.categoryId) }}</uni-td>
        </uni-tr>
      </uni-table>

      <!-- 🔹 分页组件 -->
      <uni-pagination 
        :total="filteredQuestions.length" 
        :pageSize="pageSize" 
        :current="currentPage" 
        @change="handlePageChange"
      />
    </uni-card>

    <!-- 🔹 题目详情弹出框 -->
    <uni-popup ref="popup" type="center">
      <view class="popup-container">
        
        <!-- 🔹 标题 -->
        <view class="popup-header">
          <text class="popup-title">{{ selectedQuestion.title }}</text>
        </view>

        <!-- 🔹 内容（固定高度 + 滚动条） -->
        <view class="popup-body">
          <text class="popup-content">题目类型：{{ getCategoryById(selectedQuestion.categoryId) }}</text>
          <text class="popup-content">创建日期：{{ formatDate(selectedQuestion.creationDate) }}</text>
        </view>

        <!-- 🔹 操作按钮 -->
        <view class="popup-actions">
          <button class="popup-btn delete" @click="handleDeleteQuestion">删除题目</button>
          <button class="popup-btn edit" @click="handleEditQuestion">编辑题目</button>
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
      questionsData: [], // 存储所有题目数据
      filteredQuestions: [], // 经过搜索过滤的题目
      currentPage: 1,
      pageSize: 10,
      Typevalue: 0,
      categoryList:[],
      range: [
        { value: 1, text: "题目ID" },
        { value: 2, text: "题目内容" },
        { value: 3, text: "题目类型" }
      ],
      selectedQuestion: {}, // 存储选中的题目
    };
  },
  computed: {
    paginatedQuestions() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredQuestions.slice(start, end);
    },
  },
  mounted() {
    this.fetchQuestions();
  },
  methods: {
    async fetchQuestions() {
      this.loading = true;
      let apiUrl = '';
      try {
        switch (this.Typevalue) {
          case 1: // 题目ID
            apiUrl = `questions/${this.searchQuery}?ids=${this.searchQuery}`;
            break;
          case 2: // 题目内容
            apiUrl = `questions/search/${this.searchQuery}`;
            break;
          default:
            apiUrl = `questions/all`;
        }

        const response = await apiRequest(apiUrl, 'get');
        this.questionsData = response.map(question => ({
          id: question.id,
          title: question.questionText,
          categoryId: question.categoryId,
        }));
        const categoryArray=await apiRequest('categories/all','get');
        this.categoryList=categoryArray
        this.filteredQuestions = [...this.questionsData];
      } catch (error) {
        console.error('获取题目失败:', error);
      } finally {
        this.loading = false;
      }
    },
    getCategoryById(id){
      const categoryVal=this.categoryList.filter(item=>item.id==id)
      console.log(categoryVal)
      return categoryVal[0].name
    },
    formatDate(dateString) {
      return dateString.split(" ")[0];
    },
    filterQuestions() {
      this.fetchQuestions();
    },
    handlePageChange(event) {
      this.currentPage = event.current;
    },
    showPopup(question) {
      this.selectedQuestion = question;
      this.$refs.popup.open();
    },
    async handleDeleteQuestion() {
      if (!this.selectedQuestion.id) return;
      try {
        await apiRequest(`questions/delete/${this.selectedQuestion.id}`, 'DELETE');
        this.questionsData = this.questionsData.filter(question => question.id !== this.selectedQuestion.id);
        this.filteredQuestions = [...this.questionsData];
        uni.showToast({ title: "删除成功", icon: "success" });
        this.$refs.popup.close();
      } catch (error) {
        console.error("删除题目失败:", error);
        uni.showToast({ title: "删除失败", icon: "none" });
      }
    },
    async handleEditQuestion() {
      if (!this.selectedQuestion.id) return;
      try {
        // 这里可以跳转到编辑题目的页面
        uni.navigateTo({
          url: `/pages/editQuestion/editQuestion?id=${this.selectedQuestion.id}`
        });
      } catch (error) {
        console.error("编辑题目失败:", error);
        uni.showToast({ title: "操作失败", icon: "none" });
      }
    },
  },
};
</script>

<style scoped>
.question-bank-management {
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

.edit {
  background-color: #3498db;
  color: white;
}
</style>

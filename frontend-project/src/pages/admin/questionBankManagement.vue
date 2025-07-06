<template>
  <view class="question-bank-management">
    <uni-card title="题库管理" is-full>
      
      <!-- 搜索框 -->
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

      <!-- 题库列表 -->
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

      <!-- 分页组件 -->
      <uni-pagination 
        :total="filteredQuestions.length" 
        :pageSize="pageSize" 
        :current="currentPage" 
        @change="handlePageChange"
      />
    </uni-card>

    <!-- 题目详情弹出框 -->
    <uni-popup ref="popup" type="center">
      <view class="popup-container">
        
        <!-- 标题 -->
        <view class="popup-header">
          <text class="popup-title">{{ selectedQuestion.title }}</text>
        </view>

        <!-- 内容（固定高度 + 滚动条） -->
        <view class="popup-body">
          <view class="detail-item">
            <text class="detail-label">题目类型：</text>
            <uni-data-select
              v-model="selectedQuestion.categoryId"
              :localdata="categoryList.map(item => ({
                value: item.id,
                text: item.name
              }))"
              placeholder="请选择题目类型"
            />
          </view>

          <view class="detail-item">
            <text class="detail-label">选项A：</text>
            <uni-easyinput
              v-model="selectedQuestion.optionA"
              placeholder="请输入选项A"
              trim="both"
            />
          </view>

          <view class="detail-item">
            <text class="detail-label">选项B：</text>
            <uni-easyinput
              v-model="selectedQuestion.optionB"
              placeholder="请输入选项B"
              trim="both"
            />
          </view>

          <view class="detail-item">
            <text class="detail-label">选项C：</text>
            <uni-easyinput
              v-model="selectedQuestion.optionC"
              placeholder="请输入选项C"
              trim="both"
            />
          </view>

          <view class="detail-item">
            <text class="detail-label">选项D：</text>
            <uni-easyinput
              v-model="selectedQuestion.optionD"
              placeholder="请输入选项D"
              trim="both"
            />
          </view>

          <view class="detail-item">
            <text class="detail-label">正确选项：</text>
            <uni-data-select
              v-model="selectedQuestion.correctOption"
              :localdata="answerOptions"
              placeholder="请选择正确选项"
            />
          </view>
        </view>
        <!-- 操作按钮 -->
        <view class="popup-actions">
          <button class="popup-btn delete" @click="handleDeleteQuestion">删除题目</button>
          <button class="popup-btn edit" @click="handleEditQuestion">编辑题目</button>
        </view>

      </view>
    </uni-popup>

    <!-- 新增按钮 -->
    <view class="add-button" @click="showAddForm">
      <uni-icons type="plusempty" size="24" color="#fff"></uni-icons>
    </view>

    <!-- 新增表单弹窗 -->
    <uni-popup ref="addFormPopup" type="center">
      <view class="form-container">
        <view class="form-header">
          <text class="form-title">新增题目</text>
        </view>
        
        <view class="form-body">
          <uni-forms ref="form" :model="formData">
            <uni-forms-item label="题目" required>
              <uni-easyinput v-model="formData.title" placeholder="请输入题目"/>
            </uni-forms-item>
            
            <uni-forms-item label="选项A" required>
              <uni-easyinput v-model="formData.optionA" placeholder="请输入选项A"/>
            </uni-forms-item>

            <uni-forms-item label="选项B" required>
              <uni-easyinput v-model="formData.optionB" placeholder="请输入选项B"/>
            </uni-forms-item>

            <uni-forms-item label="选项C" required>
              <uni-easyinput v-model="formData.optionC" placeholder="请输入选项C"/>
            </uni-forms-item>

            <uni-forms-item label="选项D" required>
              <uni-easyinput v-model="formData.optionD" placeholder="请输入选项D"/>
            </uni-forms-item>

            <uni-forms-item label="题目类型" required>
              <uni-data-select
                v-model="formData.categoryId"
                :localdata="categoryList.map(item => ({
                  value: item.id,
                  text: item.name
                }))"
                placeholder="请选择题目类型"
              />
            </uni-forms-item>

            <uni-forms-item label="正确选项" required>
              <uni-data-select
                v-model="formData.correctAnswer"
                :localdata="answerOptions"
                placeholder="请选择正确选项"
              />
            </uni-forms-item>
          </uni-forms>
        </view>

        <view class="form-actions">
          <button class="form-btn cancel" @click="closeAddForm">取消</button>
          <button class="form-btn submit" @click="submitForm">提交</button>
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
      formData: {
        title: '',
        optionA: '',
        optionB: '',
        optionC: '',
        optionD: '',
        correctAnswer: '',
        categoryId: ''
      },
      answerOptions: [
        { value: 'A', text: 'A' },
        { value: 'B', text: 'B' },
        { value: 'C', text: 'C' },
        { value: 'D', text: 'D' }
      ]
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
          optionA: question.optionA,
          optionB: question.optionB,
          optionC: question.optionC,
          optionD: question.optionD,
          correctOption: question.correctOption
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
        await apiRequest(`questions/delete/${this.selectedQuestion.id}`, 'post');
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
        const response=await apiRequest(`/questions/edit`, 'post',this.selectedQuestion);
        if(response){
          uni.showToast({ title: "编辑成功", icon: "success" });
          this.$refs.popup.close();
        }
      } catch (error) {
        console.error("编辑题目失败:", error);
        uni.showToast({ title: "操作失败", icon: "none" });
      }
    },
    showAddForm() {
      this.$refs.addFormPopup.open();
    },
    
    closeAddForm() {
      this.$refs.addFormPopup.close();
      this.formData = {
        title: '',
        optionA: '',
        optionB: '',
        optionC: '',
        optionD: '',
        correctAnswer: '',
        categoryId: ''
      };
    },
    
    async submitForm() {
      if (!this.formData.title || !this.formData.optionA || !this.formData.optionB || 
          !this.formData.optionC || !this.formData.optionD || !this.formData.correctAnswer ||
          !this.formData.categoryId) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        });
        return;
      }
      
      try {
        const questionData = {
          questionText: this.formData.title,
          optionA: this.formData.optionA,
            optionB: this.formData.optionB,
            optionC: this.formData.optionC,
            optionD: this.formData.optionD,
          correctOption: this.formData.correctAnswer,
          categoryId: this.formData.categoryId
        };
        
        await apiRequest('questions', 'POST', questionData);
        uni.showToast({
          title: '添加成功',
          icon: 'success'
        });
        this.closeAddForm();
        this.fetchQuestions();
      } catch (error) {
        console.error('添加失败:', error);
        uni.showToast({
          title: '添加失败',
          icon: 'none'
        });
      }
    }
  }
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

.form-container {
  background-color: #fff;
  border-radius: 24rpx;
  width: 80vw;
  height: 70vh;
  padding: 30rpx;
  display: flex;
  flex-direction: column;
}

.form-header {
  margin-bottom: 20rpx;
  text-align: center;
  flex-shrink: 0;
}

.form-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}

.form-body {
  flex: 1;
  overflow-y: auto;
  padding-right: 20rpx;
}

.form-body :deep(.uni-forms-item) {
  margin-bottom: 16rpx;
}

.form-body::-webkit-scrollbar {
  width: 6rpx;
}

.form-body::-webkit-scrollbar-thumb {
  background-color: #ddd;
  border-radius: 3rpx;
}

.form-actions {
  margin-top: 20rpx;
  display: flex;
  justify-content: flex-end;
  gap: 24rpx;
  flex-shrink: 0;
}

.form-btn {
  min-width: 160rpx;
  height: 72rpx;
  line-height: 72rpx;
  border-radius: 36rpx;
  font-size: 28rpx;
  border: none;
  transition: all 0.3s ease;
}

.form-btn.cancel {
  background-color: #f5f5f5;
  color: #666;
}

.form-btn.cancel:active {
  background-color: #e8e8e8;
}

.form-btn.submit {
  background-color: #2979ff;
  color: #fff;
}

.form-btn.submit:active {
  background-color: #2567db;
}
</style>

<template>
  <view class="quiz-container">
    <view class="question" v-if="currentQuestion">
      <text class="question-text">{{ currentQuestion.questionText }}</text>
      <view class="options">
        <view 
          class="option" 
          :class="{ selected: selectedOption === 'A' }" 
          @click="selectOption('A')"
        >{{ currentQuestion.optionA }}</view>
        <view 
          class="option" 
          :class="{ selected: selectedOption === 'B' }" 
          @click="selectOption('B')"
        >{{ currentQuestion.optionB }}</view>
        <view 
          class="option" 
          :class="{ selected: selectedOption === 'C' }" 
          @click="selectOption('C')"
        >{{ currentQuestion.optionC }}</view>
        <view 
          class="option" 
          :class="{ selected: selectedOption === 'D' }" 
          @click="selectOption('D')"
        >{{ currentQuestion.optionD }}</view>
      </view>
    </view>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api'; // 引入 API 请求方法

export default {
  data() {
    return {
      currentQuestion: null, // 当前题目
      categoryId: null, // 类别 ID
      questionIndex: 0, // 当前题目的索引
      questions: [], // 存储所有题目
      selectedOption: null // 当前选中的选项
    }
  },
  created() {
    this.categoryId = this.$route.query.id; // 获取传入的类别 ID
    this.fetchQuestions(); // 获取题库数据
  },
  methods: {
    async fetchQuestions() {
      try {
        const response = await apiRequest(`questions/category/${this.categoryId}`, 'get'); // 获取对应类型的题库
        this.questions = response; // 存储所有题目
        this.loadNextQuestion(); // 加载第一道题目
      } catch (error) {
        console.error("获取题库失败", error);
        uni.showToast({
          title: '获取题库失败，请重试',
          icon: 'none'
        });
      }
    },
    loadNextQuestion() {
      if (this.questionIndex < this.questions.length) {
        this.currentQuestion = this.questions[this.questionIndex]; // 加载当前题目
        this.selectedOption = null; // 重置选中状态
      } else {
        uni.showToast({
          title: '所有题目已完成！',
          icon: 'success'
        });
        // 可以在这里跳转到结果页面或其他页面
      }
    },
    selectOption(option) {
      this.selectedOption = option; // 设置当前选中的选项
      this.questionIndex++; // 增加题目索引
      this.loadNextQuestion(); // 加载下一题
    }
  }
}
</script>

<style>
.quiz-container {
  padding: 30rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.question {
  margin-bottom: 40rpx;
  padding: 20rpx; /* 添加内边距 */
  background-color: #fff; /* 背景颜色 */
  border-radius: 12rpx; /* 圆角 */
  box-shadow: 0 2rpx 5rpx rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.question-text {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 20rpx;
}

.options {
  display: flex;
  flex-direction: column;
}

.option {
  background-color: #f0f0f0; /* 默认背景颜色 */
  color: #333; /* 默认文字颜色 */
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 10rpx;
  cursor: pointer;
  text-align: center; /* 文字居中 */
  transition: background-color 0.3s; /* 添加过渡效果 */
}

.option.selected {
  background-color: #007AFF; /* 选中时的背景颜色 */
  color: #fff; /* 选中时的文字颜色 */
}

.option:hover {
  background-color: #e0e0e0; /* 悬停时的背景颜色 */
}
</style> 
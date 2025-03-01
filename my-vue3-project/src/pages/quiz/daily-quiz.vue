<template>
  <view class="daily-quiz-container">
    <view class="progress-container">
      <image class="progress-icon" src="/static/progress-icon.png" mode="aspectFit" />
      <text class="progress-text">今日进度: {{ currentQuestionIndex + 1 }} / {{ questions.length }}</text>
    </view>
    <view class="quiz-title">每日答题</view>
    <view class="quiz-question">
      <text class="question-text">{{ currentQuestion.text }}</text>
    </view>
    <view class="options">
      <view 
        class="option" 
        v-for="(option, index) in currentQuestion.options" 
        :key="index"
        :class="{ selected: selectedOption === index }"
        @click="selectOption(index)"
      >
        <text>{{ option }}</text>
      </view>
    </view>
    <button class="next-btn" @click="nextQuestion">下一题</button>
  </view>
</template>

<script>
import { apiRequest } from '../../utils/api.js'; // 导入 API 请求方法

export default {
  data() {
    return {
      currentQuestionIndex: 0,
      selectedOption: null,
      questions: [] // 初始化为空数组
    }
  },
  computed: {
    currentQuestion() {
      return this.questions[this.currentQuestionIndex];
    }
  },
  onLoad() {
    this.fetchQuestions(); // 页面加载时获取问题
  },
  methods: {
    fetchQuestions() {
      apiRequest('/api/daily-quiz') // 使用通用的 API 请求方法
        .then(data => {
          this.questions = data; // 假设返回的数据格式正确
        })
        .catch(err => {
          console.error(err);
        });
    },
    selectOption(index) {
      this.selectedOption = index;
      // 处理选项选择逻辑
      console.log(`选择了选项 ${index + 1}`);
    },
    nextQuestion() {
      if (this.currentQuestionIndex < this.questions.length - 1) {
        this.currentQuestionIndex++;
        this.selectedOption = null; // 重置选项
      } else {
        uni.showToast({
          title: '已完成所有题目',
          icon: 'success'
        });
        // 可以跳转到结果页面
      }
    }
  }
}
</script>

<style>
.daily-quiz-container {
  padding: 30rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.progress-container {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.progress-icon {
  width: 40rpx;
  height: 40rpx;
  margin-right: 10rpx;
}

.progress-text {
  font-size: 28rpx;
  color: #333;
}

.quiz-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.quiz-question {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 5rpx rgba(0, 0, 0, 0.1);
}

.question-text {
  font-size: 28rpx;
  color: #333;
}

.options {
  margin-top: 20rpx;
}

.option {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 15rpx;
  margin-bottom: 10rpx;
  cursor: pointer;
  text-align: center;
  box-shadow: 0 1rpx 3rpx rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s;
}

.option:hover {
  background-color: #e0f7fa;
}

.option.selected {
  background-color: #007AFF; /* 蓝色 */
  color: #fff;
}

.next-btn {
  width: 100%;
  height: 50rpx;
  background-color: #007AFF;
  color: #fff;
  border-radius: 25rpx;
  font-size: 28rpx;
  margin-top: 20rpx;
}
</style> 
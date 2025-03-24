<template>
  <view class="daily-quiz-container">
    <view class="progress-container">
      <image class="progress-icon" src="/static/progress-icon.png" mode="aspectFit" />
      <text class="progress-text">今日进度: {{ currentQuestionIndex + 1 }} / {{ questions.length }}</text>
    </view>
    <view class="quiz-title">每日答题</view>
    <view class="quiz-content">
      <view class="question-item" v-if="currentQuestion">
        <text class="question-text">{{ currentQuestionIndex + 1 }}. {{ currentQuestion.questionText }}</text>
        <view class="options">
          <view 
            class="option" 
            :class="{ correct: selectedOptions[currentQuestion.id] === 'A' && currentQuestion.correctOption === 'A', 
                      wrong: selectedOptions[currentQuestion.id] === 'A' && currentQuestion.correctOption !== 'A', 
                      selected: selectedOptions[currentQuestion.id] === 'A' }" 
            @click="selectOption(currentQuestion.id, 'A')">
            {{ currentQuestion.optionA }}
          </view>
          <view 
            class="option" 
            :class="{ correct: selectedOptions[currentQuestion.id] === 'B' && currentQuestion.correctOption === 'B', 
                      wrong: selectedOptions[currentQuestion.id] === 'B' && currentQuestion.correctOption !== 'B', 
                      selected: selectedOptions[currentQuestion.id] === 'B' }" 
            @click="selectOption(currentQuestion.id, 'B')">
            {{ currentQuestion.optionB }}
          </view>
          <view 
            class="option" 
            :class="{ correct: selectedOptions[currentQuestion.id] === 'C' && currentQuestion.correctOption === 'C', 
                      wrong: selectedOptions[currentQuestion.id] === 'C' && currentQuestion.correctOption !== 'C', 
                      selected: selectedOptions[currentQuestion.id] === 'C' }" 
            @click="selectOption(currentQuestion.id, 'C')">
            {{ currentQuestion.optionC }}
          </view>
          <view 
            class="option" 
            :class="{ correct: selectedOptions[currentQuestion.id] === 'D' && currentQuestion.correctOption === 'D', 
                      wrong: selectedOptions[currentQuestion.id] === 'D' && currentQuestion.correctOption !== 'D', 
                      selected: selectedOptions[currentQuestion.id] === 'D' }" 
            @click="selectOption(currentQuestion.id, 'D')">
            {{ currentQuestion.optionD }}
          </view>
        </view>
      </view>
    </view>
    <button class="finish-btn" @click="submitAnswers" v-if="currentQuestionIndex === questions.length - 1">完成答题</button>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api';
export default {
  data() {
    return {
      currentQuestionIndex: 0,
      selectedOptions: {}, // 存储用户选择的选项
      userAnswers: [], // 存储用户答题结果
      questions: [] // 题目数据
    };
  },
  computed: {
    currentQuestion() {
      return this.questions[this.currentQuestionIndex];
    }
  },
  onLoad(options) {
    this.questions = JSON.parse(options.questions);
  },
  methods: {
    selectOption(questionId, selectedOption) {
      // 记录用户选择
      this.$set(this.selectedOptions, questionId, selectedOption);
      const isCorrect = selectedOption === this.currentQuestion.correctOption ? 1 : 0;

      // 存储答题记录
      const answer = {
        userId: uni.getStorageSync('userInfo')?.id || '', // 确保 userId 存在
        questionId,
        selectedOption,
        isCorrect,
        timestamp: new Date().toISOString()
      };

      // 检查是否已有记录，更新或新增
      const existingIndex = this.userAnswers.findIndex(a => a.questionId === questionId);
      if (existingIndex !== -1) {
        this.$set(this.userAnswers, existingIndex, answer);
      } else {
        this.userAnswers.push(answer);
      }

      // 2 秒后自动跳转下一题
      if (this.nextQuestionTimer) {
  clearTimeout(this.nextQuestionTimer);
}
this.nextQuestionTimer = setTimeout(() => this.nextQuestion(), 2000);

    },
    nextQuestion() {
      if (this.currentQuestionIndex < this.questions.length - 1) {
        this.currentQuestionIndex++;
      }
    },
    async submitAnswers() {
      try {
        const response = await apiRequest('/userAnswers/list', 'POST', this.userAnswers);
  
        if (response) {
          uni.showToast({ title: '提交成功', icon: 'success' });
        }
        // uni.navigateTo({
        //   url: `/pages/quiz/quiz`
        // });
        uni.showToast({ 
            title: '提交成功', 
            icon: 'success',
            success: () => {
              setTimeout(() => {
                uni.navigateBack();
              }, 1500);
            }
          });
      } catch (error) {
        console.error('提交答案出错:', error);
        uni.showToast({ title: '提交失败', icon: 'none' });
      }
    }
  }
};
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
.quiz-content {
  margin-top: 20rpx;
}
.question-item {
  margin-bottom: 20rpx;
  padding: 20rpx;
  background-color: #fff;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 5rpx rgba(0, 0, 0, 0.1);
}
.question-text {
  font-size: 28rpx;
  margin-bottom: 10rpx;
}
.options {
  display: flex;
  flex-direction: column;
}
.option {
  background-color: #f0f0f0;
  color: #333;
  border-radius: 12rpx;
  padding: 15rpx;
  margin-bottom: 10rpx;
  text-align: center;
  transition: background-color 0.3s;
  cursor: pointer;
}
.option.correct {
  background-color: #28a745;
  color: #fff;
}
.option.wrong {
  background-color: #dc3545;
  color: #fff;
}
.option.selected {
  opacity: 0.5;
}
.finish-btn {
  width: 100%;
  height: 50rpx;
  background-color: #28a745;
  color: #fff;
  border-radius: 25rpx;
  font-size: 28rpx;
  margin-top: 20rpx;
}
</style>

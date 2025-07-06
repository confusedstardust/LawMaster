<template>
    <view class="wrong-questions-container">
      <text class="title">{{titleType}}</text>
      <view v-for="(question, index) in questions" :key="index" class="question-item" @click="showCorrectAnswer(question)">
        <text class="question-text">{{ question.questionText }}</text>
        <view class="options">
          <view v-for="(option, optionIndex) in getOptions(question)" :key="optionIndex" class="option-item" :class="{ 'hover-effect': true }">
            <text class="option-text">
              {{ option }}
            </text>
          </view>
        </view>
        <text v-if="correctAnswerVisible && currentQuestionId === question.id" class="correct-answer">
          <text class="icon">✔️</text> 正确答案: {{ getCorrectAnswer(question) }}
        </text>
      </view>
    </view>
  </template>
  
  <script>
  
  export default {
    data() {
      return {
        questions: [], // 存储错题数据
        correctAnswerVisible: false, // 控制正确答案的显示
        currentQuestionId: null, // 当前显示正确答案的题目 ID
        titleType:null
      };
    },
    onLoad(options) {
      if (options.Type=="record")
    {
      this.titleType="学习记录"
    }
    else
      this.titleType="错题集"
      // 获取传递过来的错题数据
      if (options.questions) {
        this.questions = JSON.parse(decodeURIComponent(options.questions));
      }
    },
    methods: {
      getOptions(question) {
        // 根据选项数据构建选项数组
        return [
          question.optionA,
          question.optionB,
          question.optionC,
          question.optionD
        ];
      },
      getCorrectAnswer(question) {
        // 根据 correctOption 返回正确答案的内容
        switch (question.correctOption) {
          case 'A':
            return question.optionA;
          case 'B':
            return question.optionB;
          case 'C':
            return question.optionC;
          case 'D':
            return question.optionD;
          default:
            return '';
        }
      },
      showCorrectAnswer(question) {
        this.currentQuestionId = question.id; // 设置当前题目 ID
        this.correctAnswerVisible = true; // 显示正确答案
  
        // 设置定时器，几秒后隐藏正确答案
        setTimeout(() => {
          this.correctAnswerVisible = false;
          this.currentQuestionId = null; // 重置当前题目 ID
        }, 3000); // 3秒后消失
      }
    }
  }
  </script>
  
  <style>
  .wrong-questions-container {
    padding: 20px;
    background-color: #f5f5f5; /* 背景颜色 */
    min-height: 100vh; /* 确保容器至少占满整个视口 */
  }
  
  .title {
    font-size: 28px;
    font-weight: bold;
    margin-bottom: 20px;
    color: #333; /* 标题颜色 */
  }
  
  .question-item {
    background-color: #fff; /* 每个问题的背景颜色 */
    border-radius: 12px; /* 圆角 */
    padding: 20px; /* 内边距 */
    margin-bottom: 15px; /* 问题之间的间距 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
    transition: transform 0.2s; /* 动画效果 */
  }
  
  .question-item:hover {
    transform: scale(1.02); /* 悬停时放大 */
  }
  
  .question-text {
    font-size: 20px;
    margin-bottom: 15px;
    color: #444; /* 问题文本颜色 */
  }
  
  .options {
    margin-left: 10px;
  }
  
  .option-item {
    background-color: #f9f9f9; /* 选项背景颜色 */
    border-radius: 8px; /* 圆角 */
    padding: 10px; /* 内边距 */
    margin-bottom: 8px; /* 选项之间的间距 */
    transition: background-color 0.2s; /* 动画效果 */
  }
  
  .option-item:hover {
    background-color: #e0e0e0; /* 悬停时背景颜色 */
  }
  
  .option-text {
    font-size: 16px; /* 统一选项字体大小 */
    color: #333; /* 统一选项字体颜色 */
  }
  
  .correct-answer {
    margin-top: 10px;
    font-size: 16px;
    color: blue; /* 正确答案的颜色 */
  }
  
  .icon {
    margin-right: 5px; /* 图标与文本之间的间距 */
  }
  </style>
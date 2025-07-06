<template>
  <view class="quiz-container">
    <!-- 答题部分 -->
    <view class="section">
      <view class="section-title">答题</view>
      <view class="section-content">
        <view class="card" @click="navigateToDailyQuiz">
          <uni-icons type="medal-filled" color="#2979ff" size="30"></uni-icons>
          <text class="card-title">每日答题</text>
        </view>
        <view class="card" @click="navigateToWrongQuestions">
          <uni-icons type="info" color="#2979ff" size="30"></uni-icons>
          <text class="card-title">错题集</text>
        </view>
      </view>
    </view>

    <!-- 专项答题部分 -->
    <view class="section">
      
      <view class="section-title">专项答题</view>
      <view class="section-content">
        <view class="card" v-for="category in categories" :key="category.id" @click="navigateToSpecialQuiz(category.id)">
          <uni-icons type="wallet-filled" color="#2979ff" size="30"></uni-icons>
          <text class="card-title">{{ category.name }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api'; // 引入 API 请求方法

export default {
  data() {
    return {
      categories: [] // 初始化为空数组
    }
  },
  created() {
    this.fetchCategories(); // 在组件创建时获取法律类型
  },
  methods: {
    async fetchCategories() {
      try {
        const response = await apiRequest('categories/all', 'get'); // 获取所有类型
        this.categories = response; // 假设返回的数据结构是 { data: [...] }
        console.log(this.categories)
      } catch (error) {
        console.error("获取法律类型失败", error);
        uni.showToast({
          title: '获取法律类型失败，请重试',
          icon: 'none'
        });
      }
    },
    navigateToDailyQuiz() {
      // 获取所有题目并随机选择 10 道题目
      apiRequest('questions/all', 'get')
        .then(response => {
          const allQuestions = response; // 假设返回的数据结构是 { data: [...] }
          const dailyQuestions = this.getRandomQuestions(allQuestions, 10); // 随机选择 10 道题目
          uni.navigateTo({
            url: `/pages/quiz/daily-quiz?questions=${JSON.stringify(dailyQuestions)}` // 将题目传递到每日答题页面
          });
        })
        .catch(error => {
          console.error("获取题目失败", error);
          uni.showToast({
            title: '获取题目失败，请重试',
            icon: 'none'
          });
        });
    },
    async navigateToWrongQuestions() {
      const userId = uni.getStorageSync('userInfo').id; // 获取用户 ID
      try {
        const response = await apiRequest(`userAnswers/incorrect/${userId}`, 'get'); // 请求用户的错题

        if (response && response.length > 0) {
          // 提取 questionId 并去重
          const questionIds = [...new Set(response.map(item => item.questionId))];

          // 请求 API 获取错题的详细信息
          const questionsResponse = await apiRequest(`questions/array?ids=${questionIds.join(',')}`, 'get');

          // 处理获取到的错题信息
          console.log("获取到的错题信息:", questionsResponse);
          
          // 将错题数据传递到错题集页面
          uni.navigateTo({
            url: `/pages/quiz/wrong-questions?questions=${JSON.stringify(questionsResponse)}` // 将错题数据传递到下一个页面
          });
        } else {
          uni.showToast({
            title: '没有找到错题',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error("获取错题失败", error);
        uni.showToast({
          title: '获取错题失败，请重试',
          icon: 'none'
        });
      }
    },
    async navigateToSpecialQuiz(categoryId) {
      // 在这里可以根据 categoryId 获取对应的题库
      try {
        const response = await apiRequest(`questions/all`, 'get'); // 获取所有问题
        const filteredQuestions = response.filter(question => question.categoryId === categoryId); // 根据 categoryId 过滤问题

        console.log("获取到的题库数据", filteredQuestions);
        
        // 将过滤后的问题传递到下一个页面
        uni.navigateTo({
          url: `/pages/quiz/daily-quiz?id=${categoryId}&questions=${JSON.stringify(filteredQuestions)}`
        });
      } catch (error) {
        console.error("获取题库失败", error);
        uni.showToast({
          title: '获取题库失败，请重试',
          icon: 'none'
        });
      }
    },
    getRandomQuestions(questions, count) {
      const shuffled = questions.sort(() => 0.5 - Math.random()); // 打乱数组
      return shuffled.slice(0, count); // 返回前 count 道题目
    }
  }
}
</script>

<style>
/* 页面整体 */
.quiz-container {
  background-color: #f2f2f2;
  min-height: 100vh;
  padding-bottom: 20px;
}

/* 头部 */
.header {
  background-color: #008CFF;
  color: white;
  padding: 20rpx;
  font-size: 34rpx;
  font-weight: bold;
  text-align: center;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 每个模块 */
.section {
  background: #ffffff;
  border-radius: 20rpx;
  margin: 20rpx;
  padding: 20rpx;
}

/* 模块标题 */
.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

/* 答题部分（两列布局） */
.section-content {
  display: flex;
  justify-content: space-between;
  gap: 20rpx;
  flex-wrap: wrap;
}

/* 卡片样式 */
.card {
  flex: 1;
  background: white;
  padding: 40rpx;
  border-radius: 20rpx;
  text-align: center;
  box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.1);
  transition: 0.3s;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.card:hover {
  transform: scale(1.05);
}

/* 卡片标题 */
.card-title {
  display: block;
  margin-top: 10rpx;
  font-size: 28rpx;
  font-weight: bold;
}

/* 专项答题（列表布局） */
.list-container {
  display: flex;
  flex-direction: column;
}

.list-item {
  background: #f8f8f8;
  padding: 20rpx;
  margin-top: 10rpx;
  font-size: 28rpx;
  color: #333;
  border-radius: 10rpx;
  text-align: center;
  transition: 0.3s;
}

.list-item:hover {
  background: #e6e6e6;
}
</style> 
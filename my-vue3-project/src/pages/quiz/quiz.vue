<template>
  <view class="quiz-container">
    <!-- 答题部分 -->
    <view class="section">
      <view class="section-title">答题</view>
      <view class="section-content">
        <view class="card" @click="navigateToDailyQuiz">
          <text class="card-title">每日答题</text>
        </view>
        <view class="card" @click="navigateToWrongQuestions">
          <text class="card-title">错题集</text>
        </view>
      </view>
    </view>

    <!-- 专项答题部分 -->
    <view class="section">
      <view class="section-title">专项答题</view>
      <view class="section-content">
        <view class="card" v-for="category in categories" :key="category.id" @click="navigateToSpecialQuiz(category.id)">
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
.quiz-container {
  padding: 30rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.section {
  margin-bottom: 40rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.section-content {
  display: flex;
  flex-wrap: wrap;
}

.card {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 30rpx;
  margin-right: 20rpx;
  margin-bottom: 20rpx;
  flex: 1 1 45%; /* 使卡片在一行中占据一定比例 */
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2rpx 5rpx rgba(0, 0, 0, 0.1);
}

.card-title {
  font-size: 28rpx;
  color: #007AFF;
}
</style> 
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
      uni.navigateTo({
        url: '/pages/quiz/daily-quiz'
      })
    },
    navigateToWrongQuestions() {
      uni.navigateTo({
        url: '/pages/quiz/wrong-questions'
      })
    },
    async navigateToSpecialQuiz(categoryId) {
      // 在这里可以根据 categoryId 获取对应的题库
      try {
        const response = await apiRequest(`questions/category/${categoryId}`, 'get'); // 获取对应类型的题库
        // 假设返回的数据结构是 { data: [...] }
        // 处理获取到的题库数据，例如存储到状态管理或跳转到题库页面
        console.log("获取到的题库数据", response.data);
        uni.navigateTo({
          url: `/pages/quiz/special-quiz?id=${categoryId}`
        });
      } catch (error) {
        console.error("获取题库失败", error);
        uni.showToast({
          title: '获取题库失败，请重试',
          icon: 'none'
        });
      }
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
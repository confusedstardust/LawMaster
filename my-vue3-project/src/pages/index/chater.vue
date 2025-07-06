<template>
  <view class="chat-container">
    <scroll-view class="chat-messages" scroll-y>
      <view v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
        <text>{{ msg.text }}</text>
      </view>
    </scroll-view>

    <view class="chat-input">
      <uni-easyinput v-model="userInput" placeholder="输入你的问题..." />
      <button class="popup-btn" @click="sendMessage">发送</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInput: '',
      messages: [],
      loadingMessageIndex: null, // 记录加载动画的消息索引
      loadingInterval: null, // 控制动画
    };
  },
  methods: {
    async sendMessage() {
      if (!this.userInput.trim()) return;

      // 🟢 添加用户消息
      this.messages.push({ role: 'user', text: this.userInput });
      const question = this.userInput;
      this.userInput = '';

      // 🟢 添加 "获取回答中" 动画
      this.loadingMessageIndex = this.messages.length;
      this.messages.push({ role: 'bot', text: '获取回答中' });

      let dots = '';
      this.loadingInterval = setInterval(() => {
        dots = dots.length < 3 ? dots + '.' : '';
        this.messages[this.loadingMessageIndex].text = `获取回答中${dots}`;
      }, 500);

      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort('Timeout'), 30000); // 30秒超时

      try {
        const response = await fetch('http://localhost:8080/tellQuestion', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ question }),
          signal: controller.signal,
        });

        clearTimeout(timeoutId); // 请求成功后清除超时
        clearInterval(this.loadingInterval); // 停止动画

        if (!response.ok) {
          throw new Error(`HTTP 错误！状态码: ${response.status}`);
        }

        const data = await response.json();
        const botMessage = data.choices[0]?.message?.content || '未能获取答案';

        // ✅ 替换 "获取回答中" 为实际回答
        this.messages[this.loadingMessageIndex] = { role: 'bot', text: botMessage };

      } catch (error) {
        console.error('获取回答失败:', error);
        clearInterval(this.loadingInterval); // 停止动画
        this.messages[this.loadingMessageIndex] = { 
          role: 'bot', 
          text: error.message === 'Timeout' ? '请求超时，请稍后再试。' : '服务器错误，请稍后再试。' 
        };
      }
    }
  }
};
</script>

<style>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f5f5;
}
.chat-messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}
.message {
  margin: 5px 0;
  padding: 8px;
  border-radius: 5px;
  max-width: 70%;
  word-wrap: break-word;
}
.user {
  align-self: flex-end;
  background: #007aff;
  color: white;
  margin-left: 70px;
}
.bot {
  align-self: flex-start;
  background: #fff;
  border: 1px solid #ddd;
}
.chat-input {
  display: flex;
  padding: 10px;
  background: white;
  border-top: 1px solid #ddd;
}
.send-btn {
  margin-left: 10px;
  padding: 5px 10px;
  background: #007aff;
  color: white;
  border-radius: 5px;
}
.popup-btn {
  min-width: 160rpx;
  height: 72rpx;
  line-height: 72rpx;
  border-radius: 36rpx;
  font-size: 28rpx;
  color: #fff;
  border: none;
  transition: all 0.3s ease;
  background-color: #2979ff;
}
</style>

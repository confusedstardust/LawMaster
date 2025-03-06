<template>
  <view class="publish-container">
    <view class="input-group">
      <input type="text" v-model="title" placeholder="请输入标题" class="input-title" />
    </view>
    <view class="input-group">
      <textarea v-model="content" placeholder="请输入内容" class="input-content" @input="handleInput" />
      <view class="tags-container">
        <view class="tag" v-for="(tag, index) in tags" :key="index">
          {{ tag }}
          <text class="remove-tag" @click="removeTag(index)">×</text>
        </view>
      </view>
    </view>
    <view class="image-upload">
      <button @click="chooseImage(userId, username)" class="upload-button">上传图片</button>
      <view class="image-preview" v-if="imageUrls.length">
        <image v-for="(url, index) in imageUrls" :key="index" :src="url" class="preview-image" />
      </view>
    </view>
    <button @click="submitPost" class="submit-button">发布</button>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api'; // 引入 API 请求方法

export default {
  data() {
    return {
      title: '',
      content: '',
      imageUrls: [],
      tags: [] // 存储标签
    };
  },
  methods: {
    chooseImage() {
      uni.chooseImage({
        success: (res) => {
          this.imageUrls = res.tempFilePaths; // 获取选择的图片路径
          this.imageArray=res.tempFiles.map(file=>{
            const fileExtension = file.name.substring(file.name.lastIndexOf(".")); // 提取后缀名
            const randomStr = Math.random().toString(36).substring(2, 8); // 生成6位随机字符串
             // 组合新的文件名
            const storedUserInfo = uni.getStorageSync('userInfo');
            const newFileName = `${storedUserInfo.id}_${storedUserInfo.username}_${randomStr}${fileExtension}`;
            return newFileName
          });
        console.log("aaa",this.imageArray)
        }
      });
    },
    async convertBlobToBase64(blobUrl) {
        const blob = await fetch(blobUrl).then((res) => res.blob()); // 获取 Blob 对象
        return new Promise((resolve) => {
            const reader = new FileReader();
            reader.readAsDataURL(blob);
            reader.onloadend = () => resolve(reader.result); // 读取 Base64
        });
    },
    async handleUpload() {
        let base64Images = await Promise.all(
        imageUrls.map((blobUrl) => convertBlobToBase64(blobUrl))
    );},
    handleInput() {
      const tagPattern = /#([\u4e00-\u9fa5\w]+)/g; // 匹配以#开头的中文或字母数字单词
      const matches = this.content.match(tagPattern);
      this.tags = matches ? matches.map(tag => tag.substring(1)) : []; // 提取标签
    },

    
    removeTag(index) { 
      this.tags.splice(index, 1); // 移除标签
    },
    async submitPost() {
      if (!this.title || !this.content) {
        uni.showToast({
          title: '标题和内容不能为空',
          icon: 'none'
        });
        return;
      }

      const postData = {
        title: this.title,
        content: this.content,
        images: this.imageUrls,
        tags: this.tags, // 添加标签到数据中
        imageArray:this.imageArray
      };

      try {
        const response = await apiRequest('posts', 'post', postData);
        uni.showToast({
          title: '发布成功',
          icon: 'success'
        });
        // 返回到社区页面
        uni.navigateBack();
      } catch (error) {
        console.error('发布失败:', error);
        uni.showToast({
          title: '发布失败，请重试',
          icon: 'none'
        });
      }
    }
  }
}
</script>

<style>
.publish-container {
  padding: 20rpx;
  background-color: #f9f9f9; /* 背景颜色 */
  border-radius: 10rpx; /* 圆角 */
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.input-group {
  margin-bottom: 20rpx;
}

.input-title, .input-content {
  width: 100%;
  padding: 15rpx;
  border: 1rpx solid #ddd;
  border-radius: 8rpx; /* 圆角 */
  background-color: #fff; /* 输入框背景 */
  font-size: 28rpx; /* 字体大小 */
}

.input-content {
  height: 200rpx; /* 高度 */
  resize: none; /* 禁止调整大小 */
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10rpx;
}

.tag {
  background-color: #FF6F61; /* 标签颜色 */
  color: white;
  padding: 5rpx 10rpx;
  border-radius: 15rpx; /* 圆角 */
  margin-right: 10rpx;
  margin-bottom: 10rpx;
  display: flex;
  align-items: center;
}

.remove-tag {
  margin-left: 5rpx;
  cursor: pointer;
  font-weight: bold;
}

.image-upload {
  margin-bottom: 20rpx;
}

.upload-button {
  background-color: #FF6F61; /* 上传按钮颜色 */
  color: white;
  padding: 10rpx;
  border: none;
  border-radius: 5rpx; /* 圆角 */
  width: 100%;
  font-size: 28rpx; /* 字体大小 */
}

.preview-image {
  width: 100px;
  height: 100px;
  margin-right: 10rpx;
  border-radius: 5rpx; /* 圆角 */
}

.submit-button {
  background-color: #007AFF; /* 发布按钮颜色 */
  color: white;
  padding: 10rpx;
  border: none;
  border-radius: 5rpx; /* 圆角 */
  width: 100%;
  font-size: 28rpx; /* 字体大小 */
}
</style> 
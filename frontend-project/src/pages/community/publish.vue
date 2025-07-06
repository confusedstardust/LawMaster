<template>
  <view class="publish-container">
    <view class="input-group">
      <input type="text" v-model="title" placeholder="请输入标题" class="input-title" />
    </view>
    <view class="input-group">
      <!-- <textarea v-model="content" placeholder="请输入内容" class="input-content" @input="handleInput"maxlength="1000" /> -->
      <RichEditor :value="content" @input="handleInput"/>
      <view class="tags-container">
        <view class="tag" v-for="(tag, index) in tags" :key="index">
          {{ tag }}
          <text class="remove-tag" @click="removeTag(index)">×</text>
        </view>
      </view>
    </view>
    <view class="image-upload">
      <button @click="chooseImage" class="upload-button">上传图片</button>
      <view class="image-preview" v-if="imageArray.length">
        <image v-for="(url, index) in imageArray" :key="index" :src="url" class="preview-image" />
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
      imageArray: [], // 存储图片本地路径
      uploadedUrls: [], // 存储上传后的 URL
      tags: [] // 存储标签
    };
  },
  methods: {
    // 选择图片并生成唯一文件名
    chooseImage() {
      uni.chooseImage({
        success: (res) => {
          const storedUserInfo = uni.getStorageSync('userInfo'); // 获取用户信息
          this.imageArray = res.tempFilePaths; // 本地文件路径
          this.imageNames = res.tempFiles.map(file => {
            const fileExtension = file.name.substring(file.name.lastIndexOf(".")); // 获取后缀名
            const randomStr = Math.random().toString(36).substring(2, 8); // 生成随机字符串
            return `${storedUserInfo.id}_${storedUserInfo.username}_${randomStr}${fileExtension}`;
          });
        }
      });
    },

    // 处理标签提取
    handleInput(e) {
      const tagPattern = /#([\u4e00-\u9fa5\w]+)/g; // 匹配 #标签
      const matches = this.content.match(tagPattern);
      this.tags = matches ? matches.map(tag => tag.substring(1)) : []; // 提取标签
      this.content = e;
    },

    removeTag(index) {
      this.tags.splice(index, 1);
    },

    // 批量上传图片
    async uploadImages() {
      if (!this.imageArray.length) return []; // 没有图片直接返回空数组

      const uploadPromises = this.imageArray.map((filePath, index) => {
        return new Promise((resolve, reject) => {
          const fileNameWithoutExt = this.imageNames[index].replace(/\.[^/.]+$/, ""); // 去掉扩展名
          uni.uploadFile({
            url: `http://localhost:8080/files/upload`, // 后端上传接口
            filePath: filePath,
            name: "file",
            formData: {
              filename: fileNameWithoutExt// 传递新的文件名
            },
            success: (res) => {
              try {
                const response = JSON.parse(res.data);
                if (response.success) {
                  resolve(response.url); // 上传成功返回 URL
                } else {
                  reject("上传失败，请重试");
                }
              } catch (error) {
                console.error("解析响应出错:", error);
                reject("上传失败，请重试");
              }
            },
            fail: (error) => {
              console.error("上传失败:", error);
              reject("上传失败，请重试");
            }
          });
        });
      });

      try {
        const uploadedUrls = await Promise.all(uploadPromises); // 等待所有图片上传
        console.log("上传成功的图片 URL:", uploadedUrls);
        return uploadedUrls;
      } catch (error) {
        uni.showToast({
          title: error,
          icon: 'none'
        });
        throw error;
      }
    },

    async checkSensitiveFromFile(text) {
  try {
    const res = await new Promise((resolve, reject) => {
      uni.request({
        url: '/static/Sensitive.txt',
        method: 'GET',
        success: resolve,
        fail: reject
      });
    });

    // 将文件内容按行分割成数组并去除空白行
    const wordList = res.data
      .split(/\r?\n/)
      .map(word => word.trim())
      .filter(word => word);

    // 检查是否有敏感词
    const matched = wordList.filter(word => text.includes(word));
    return matched.length > 0 ? matched : null;
  } catch (err) {
    console.error('读取敏感词库失败', err);
    return null; // 或考虑直接通过校验
  }
},


    // 先上传图片，然后提交帖子信息
    async submitPost() {
      const [titleBad, contentBad] = await Promise.all([
      this.checkSensitiveFromFile(this.title),
      this.checkSensitiveFromFile(this.content)
    ]);
      if (!this.title || !this.content ) {
        uni.showToast({
          title: '标题和内容不能为空',
          icon: 'none'
        });
        return;
      }else if(titleBad || contentBad) {
        uni.showToast({
          title: '标题和内容含有敏感词',
          icon: 'none'
        });
        return;
      }

      try {
        // 1. 先上传图片
        this.uploadedUrls = await this.uploadImages();
        this.uploadedUrls = this.uploadedUrls.map(url => url.substring(url.lastIndexOf('/') + 1)); // 只保留文件名

        // 2. 组织帖子数据
        const postData = {
          title: this.title,
          content: this.content,
          images: JSON.stringify(this.uploadedUrls), // 这里替换为上传后的 URL
          tags: JSON.stringify(this.tags),
          userId: uni.getStorageSync('userInfo').id
        };

        // 3. 发送帖子信息到后端
        const response = await apiRequest('posts/add', 'post', postData);

        // 4. 显示成功提示
        uni.showToast({
          title: '发布成功',
          icon: 'success'
        });

        // 5. 返回到社区页面
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
  background-color: #f9f9f9;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.input-group {
  margin-bottom: 20rpx;
}

.input-title, .input-content {
  width: 100%;
  padding: 15rpx;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  background-color: #fff;
  font-size: 28rpx;
}

.input-content {
  height: 200rpx;
  resize: none;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10rpx;
}

.tag {
  background-color: #FF6F61;
  color: white;
  padding: 5rpx 10rpx;
  border-radius: 15rpx;
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
  background-color: #FF6F61;
  color: white;
  padding: 10rpx;
  border: none;
  border-radius: 5rpx;
  width: 100%;
  font-size: 28rpx;
}

.preview-image {
  width: 100px;
  height: 100px;
  margin-right: 10rpx;
  border-radius: 5rpx;
}

.submit-button {
  background-color: #007AFF;
  color: white;
  padding: 10rpx;
  border: none;
  border-radius: 5rpx;
  width: 100%;
  font-size: 28rpx;
}
</style>

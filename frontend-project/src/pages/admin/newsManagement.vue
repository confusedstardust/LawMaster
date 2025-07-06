

<template>
  <view class="news-management">
    <uni-card title="新闻/知识/案例管理" is-full>
      
      <!-- 🔹 搜索框 -->
      <view class="search-bar">
        <uni-data-select class="uni-px-5"
          v-model="Typevalue"
          :localdata="range"
          @change="fetchNews"
          :clear="true"
          placeholder="查询类型"
        ></uni-data-select>
        <uni-easyinput class="uni-mt-5" suffixIcon="search" v-model="searchQuery" @iconClick="filterNews"></uni-easyinput>
      </view>

      <view v-if="selectedType" class="selected-type">
        <text>{{ selectedType }}</text>
        <uni-icons 
          type="close" 
          size="20" 
          @click="clearTypeFilter"
          class="close-icon"
        ></uni-icons>
      </view>

      <!-- 🔹 新闻列表 -->
      <uni-table ref="table" class="table" :loading="loading" border stripe emptyText="暂无更多数据">
        <uni-tr>
          <uni-th width="130" align="left">标题</uni-th>
          <uni-th width="70" align="left">
            类型
            <uni-icons 
              type="more" 
              size="20" 
              @click="showTypeList"
              class="type-icon"
            ></uni-icons>
          </uni-th>
          <uni-th width="100" align="left">发布日期</uni-th>
        </uni-tr>
        <uni-tr v-for="(newsItem, index) in paginatedNews" :key="index" @click="showPopup(newsItem)">
          <uni-td style="font-size: small;">{{ newsItem.title }}</uni-td>
          <uni-td style="font-size: small;"><view class="name">{{ newsItem.type }}</view></uni-td>
          <uni-td style="font-size: small;" align="left">{{ formatDate(newsItem.createdAt) }}</uni-td>
        </uni-tr>
      </uni-table>

      <!-- 🔹 分页组件 -->
      <uni-pagination 
        :total="filteredNews.length" 
        :pageSize="pageSize" 
        :current="currentPage" 
        @change="handlePageChange"
      />
    </uni-card>

    <!-- 🔹 新闻详情弹出框 -->
    <uni-popup ref="editFormPopup" type="center">
      <view class="popup-container">
        
        <!-- 🔹 标题 -->
        <view class="popup-header">
          <text class="popup-title" v-if="editable===false" >{{ selectedNews.title }}</text>
        </view>

        <!-- 🔹 内容（固定高度 + 滚动条） -->
        <view class="popup-body">
          <text class="popup-content" v-if="editable===false" v-html="selectedNews.content"></text>
        </view>

        <uni-forms v-if="editable" ref="editForm" :model="selectedNews">
        <uni-forms-item label="标题" required>
          <uni-easyinput v-model="selectedNews.title" placeholder="请输入标题"/>
        </uni-forms-item>
        
        <uni-forms-item label="类型" required>
          <uni-data-select
            v-model="selectedNews.type"
            :localdata="typeOptions"
            placeholder="请选择类型"
          />
        </uni-forms-item>

        <uni-forms-item label="法系" required>
          <uni-data-select
            v-model="selectedNews.categoryId"
            :localdata="categoryList.map(item => ({
              value: item.id,
              text: item.name
            }))"
            placeholder="请选择法系"
          />
        </uni-forms-item>

        <uni-forms-item label="内容" required>
          <editor id="editor" class="ql-container" placeholder="开始输入..." show-img-size show-img-toolbar
						show-img-resize  @ready="onEditorReady" @input="onEditorInput" >
					</editor>
          
        </uni-forms-item>
        
      </uni-forms>

        <!-- 🔹 操作按钮 -->
        <view class="popup-actions">
          <button class="popup-btn delete" @click="handleDeleteNews">删除新闻</button>
          <button 
            class="popup-btn highlight" 
            @click="toggleCarousel()"
          >
            {{ selectedNews.visible === 1 ? '取消推荐' : '设为推荐' }}
          </button>
          <button class="popup-btn highlight" @click="editContent" v-if="!editable">编辑内容</button>
          <button class="popup-btn highlight" @click="saveAndSubmit" v-if="editable" >确定</button>
          <!-- <button class="popup-btn highlight" @click="toggleEditMode"> {{ selectedNews.isEditing ? '确定' : '编辑' }}</button> -->
        </view>

      </view>
    </uni-popup>

    <!-- 新增按钮 -->
    <view class="add-button" @click="showAddForm">
      <uni-icons type="plusempty" size="24" color="#fff"></uni-icons>
    </view>

    <!-- 新增表单弹窗 -->
    <uni-popup ref="addFormPopup" type="center">
      <view class="form-container">
        <view class="form-header">
          <text class="form-title">新增内容</text>
        </view>
        
        <view class="form-body">
          <uni-forms ref="form" :model="formData">
            <uni-forms-item label="标题" required>
              <uni-easyinput v-model="formData.title" placeholder="请输入标题"/>
            </uni-forms-item>
            
            <uni-forms-item label="类型" required>
              <uni-data-select
                v-model="formData.type"
                :localdata="typeOptions"
                placeholder="请选择类型"
              />
            </uni-forms-item>

            <uni-forms-item label="法系" required>
              <uni-data-select
                v-model="formData.categoryId"
                :localdata="categoryList.map(item => ({
                  value: item.id,
                  text: item.name
                }))"
                placeholder="请选择法系"
              />
            </uni-forms-item>

            <uni-forms-item label="内容" required>
              <!-- <editor
                class="rich-editor"
                :value="formData.content"
                @input="handleEditorInput"
                placeholder="请输入内容"
              /> -->
              
            </uni-forms-item>
            <RichEditor :value="formData.content" @input="handleEditorInput"/>
          </uni-forms>
        </view>

        <view class="form-actions">
          <button class="popup-btn cancel" @click="closeAddForm">取消</button>
          <button class="popup-btn highlight" @click="submitForm">提交</button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import { apiRequest } from '@/utils/api';
import RichEditor from '@/components/Richtext.vue';
export default {
  components: {
    RichEditor
  },
  data() {
    return {
      editable: false,
      loading: false,
      searchQuery: '',
      newsData: [], // 存储所有新闻数据
      filteredNews: [], // 经过搜索过滤的新闻
      currentPage: 1,
      pageSize: 10,
      Typevalue: 0,
      categoryList:[],
      range: [
        { value: 1, text: "新闻ID" },
        { value: 2, text: "内容" }
      ],
      selectedNews: {}, // 存储选中的新闻
      selectedType: null, // 存储用户选择的类型
      formData: {
        title: '',
        type: '',
        categoryId: '',
        content: ''
      },
      typeOptions: [
        { value: '案例', text: '案例' },
        { value: '新闻', text: '新闻' },
        { value: '知识', text: '知识' }
      ]
    };
  },
  computed: {
    paginatedNews() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredNews.slice(start, end);
    },
  },
  mounted() {
    this.fetchNews();
  },
  methods: {
    async fetchNews() {
      this.loading = true;
      let apiUrl = '';
      try {
        switch (this.Typevalue) {
          case 1: // 新闻ID
            apiUrl = `articles/${this.searchQuery}`;
            break;
          case 2: // 内容
            apiUrl = `articles/search/${this.searchQuery}`;
            break;
          default:
            apiUrl = `articles/all`;
        }
        const categoryArray=await apiRequest('categories/all','get');
        this.categoryList=categoryArray;
        const response = await apiRequest(apiUrl, 'get');
        this.newsData = Array.isArray(response) ? response : [response];
        this.filteredNews = [...this.newsData];
        this.filterByType(); // 新增：在获取新闻后应用类型过滤
      } catch (error) {
        console.error('获取新闻失败:', error);
      } finally {
        this.loading = false;
      }
    },onEditorReady() {
  // #ifdef MP-BAIDU
  this.editorCtx = requireDynamicLib('editorLib').createEditorContext('editor');
  // #endif

  // #ifdef APP-PLUS || MP-WEIXIN || H5
  uni.createSelectorQuery()
    .select('#editor')
    .context((res) => {
      if (res && res.context) {
        this.editorCtx = res.context;
        // 🔹 这里正确地设置 HTML 内容
        this.editorCtx.setContents({
          html: this.selectedNews.content
        });
      }
    }).exec();
  // #endif
},
async toggleCarousel(){
  if (!this.selectedNews.id) return;
  try {
    await apiRequest(`http://localhost:8080/articles/visible/${this.selectedNews.id}/${this.selectedNews.visible === 1 ? 0 : 1}`, 'POST', {
      visible: this.selectedNews.visible === 1 ? 0 : 1
    });
    this.fetchNews();
    uni.showToast({ title: "操作成功", icon: "success" });
    this.$refs.editFormPopup.close();
  } catch (error) {
    console.error("切换轮播贴失败:", error);

}},
    formatDate(dateString) {
      // 预处理字符串，去掉最后的 " 00:00"
      const cleanedDateString = dateString.replace(" 00:00", "");
      
      // 解析成 Date 对象
      const date = new Date(cleanedDateString);
      
      // 格式化为 "YYYY-MM-DD"
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0"); // 月份从 0 开始，需要 +1
      const day = String(date.getDate()).padStart(2, "0");

      return `${year}-${month}-${day}`;
    },  
    filterNews() {
      this.fetchNews();
    },
    handlePageChange(event) {
      this.currentPage = event.current;
    },
    showPopup(newsItem) {
      this.editable=false;
      this.selectedNews = newsItem;
      this.$refs.editFormPopup.open();
    },
    getCategoryById(id){
      const categoryVal=this.categoryList.filter(item=>item.id==id)
      console.log(categoryVal)
      return categoryVal[0].name
    },
    async handleDeleteNews() {
      if (!this.selectedNews.id) return;
      try {
        await apiRequest(`articles/delete/${this.selectedNews.id}`, 'post');
        this.newsData = this.newsData.filter(news => news.id !== this.selectedNews.id);
        this.filteredNews = [...this.newsData];
        uni.showToast({ title: "删除成功", icon: "success" });
        this.$refs.editFormPopup.close();
      } catch (error) {
        console.error("删除新闻失败:", error);
        uni.showToast({ title: "删除失败", icon: "none" });
      }
    },
    async editContent() {
      this.editable = true;
    },
    showTypeList() {
      const uniqueTypes = [...new Set(this.newsData.map(item => item.type))];
      uni.showActionSheet({
        itemList: uniqueTypes,
        success: (res) => {
          this.selectedType = uniqueTypes[res.tapIndex];
          this.filterByType();
        },
        fail: (res) => {
          console.log(res.errMsg);
        }
      });
    },
    filterByType() {
      if (this.selectedType) {
        this.filteredNews = this.newsData.filter(news => news.type === this.selectedType);
      } else {
        this.filteredNews = [...this.newsData];
      }
    },
    clearTypeFilter() {
      this.selectedType = null;
      this.filteredNews = [...this.newsData];
    },
    showAddForm() {
      this.editable = false;
      this.$refs.addFormPopup.open();
    },
    
    closeAddForm() {
      this.$refs.addFormPopup.close();
      this.formData = {
        title: '',
        type: '',
        categoryId: '',
        content: ''
      };
    },
    
    handleEditorInput(e) {
      this.formData.content = e;
    },
    async submitForm() {
      if (!this.formData.title || !this.formData.type || !this.formData.categoryId || !this.formData.content) {
        console.log(this.formData);
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        });
        return;
      }
      
      try {
        // this.formData.content=this.formData.content.detail.html;
        await apiRequest('articles', 'POST', this.formData);
        uni.showToast({
          title: '添加成功',
          icon: 'success'
        });
        this.closeAddForm();
        this.fetchNews();
      } catch (error) {
        console.error('添加失败:', error);
        uni.showToast({
          title: '添加失败',
          icon: 'none'
        });
      }
    }
    ,async saveAndSubmit() {
      if (!this.selectedNews.title || !this.selectedNews.type || !this.selectedNews.categoryId || !this.selectedNews.content) {
        console.log(this.selectedNews);
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        });
        return;
      }
      try {
        // this.selectedNews.content=this.selectedNews.content.detail.html;
        await apiRequest(`articles/edit`, 'POST', {
          id: this.selectedNews.id,
          title: this.selectedNews.title,
          type: this.selectedNews.type,
          categoryId: this.selectedNews.categoryId,
          content: this.selectedNews.content
        });
        uni.showToast({
          title: '保存成功',
          icon: 'success'
        });
        this.$refs.editFormPopup.close();
        this.fetchNews();
      } catch (error) {
        console.error('保存失败:', error);
        uni.showToast({
          title: '保存失败',
          icon: 'none'
        });
      }
    },onEditorInput(event) {
    console.log("编辑器输入:", event.detail.html);
    this.selectedNews.content = event.detail.html;
  }
    ,toggleEditMode() {
    if (this.selectedNews.isEditing) {
      // 如果当前是“确定”状态，提交数据
      this.updateNewsContent();
    } else {
      // 进入编辑模式
      this.selectedNews.isEditing = true;
    }
  },

  // // 🔹 监听编辑器输入
  // handleEditorInput(e) {
  //   this.selectedNews.content = e;
  // },

  // 🔹 提交更新到后端
  async updateNewsContent() {
    if (!this.selectedNews.id) return;
    
    try {
      await apiRequest(`articles/update/${this.selectedNews.id}`, 'POST', {
        content: this.selectedNews.content.detail.html // 获取 HTML 内容
      });

      uni.showToast({
        title: "更新成功",
        icon: "success"
      });

      // 退出编辑模式
      this.selectedNews.isEditing = false;

      // 刷新数据
      this.fetchNews();
    } catch (error) {
      console.error("更新失败:", error);
      uni.showToast({
        title: "更新失败",
        icon: "none"
      });
    }
  }
  },
};
</script>

<style scoped>
.news-management {
  padding: 16px;
}

.search-bar {
  display: flex;
  margin-bottom: 16px;
}

.table {
  margin-top: 10px;
}

/* 🔹 弹出框样式 */
.popup-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  text-align: center;
  width: 320px;
}

/* Section 1: 标题 */
.popup-header {
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.popup-title {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

/* Section 2: 内容（固定高度 + 滚动条） */
.popup-body {
  max-height: 420px;
  overflow-y: auto;
  padding: 10px;
  text-align: left;
}

.popup-content {
  font-size: 14px;
  color: #555;
  line-height: 1.5;
}

/* Section 3: 操作按钮 */
.popup-actions {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-top: 1px solid #ddd;
}


.popup-btn {
  min-width: 160rpx;
  height: 72rpx;
  line-height: 72rpx;
  border-radius: 36rpx;
  font-size: 28rpx;
  border: none;
  transition: all 0.3s ease;
}

.popup-btn.delete {
  background-color: #ff4d4f;
  color: #fff;
}

.popup-btn.delete:active {
  background-color: #cf1322;
}

.popup-btn.edit {
  background-color: #2979ff;
  color: #fff;
}

.popup-btn.edit:active {
  background-color: #2567db;
}

.delete {
  background-color: #e74c3c;
  color: white;
}

.highlight {
  background-color: #3498db;
  color: white;
}

.type-icon {
  margin-left: 5px;
  cursor: pointer;
  color: #666;
}

.selected-type {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding: 5px 10px;
  background-color: #f0f0f0;
  border-radius: 5px;
}

.close-icon {
  margin-left: 5px;
  cursor: pointer;
  color: #666;
}

.add-button {
  position: fixed;
  right: 30rpx;
  bottom: 30rpx;
  width: 100rpx;
  height: 100rpx;
  background-color: #2979ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
  z-index: 999;
}

.form-container {
  background-color: #fff;
  border-radius: 24rpx;
  width: 80vw;
  max-height: 80vh;
  padding: 30rpx;
}

.form-header {
  margin-bottom: 30rpx;
  text-align: center;
}

.form-title {
  font-size: 32rpx;
  font-weight: bold;
}

.form-body {
  max-height: 60vh;
  overflow-y: auto;
}

.rich-editor {
  width: 100%;
  height: 300rpx;
  border: 1rpx solid #eee;
  border-radius: 8rpx;
}

.form-actions {
  margin-top: 30rpx;
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
}

.form-btn {
  padding: 16rpx 40rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.form-btn.cancel {
  background-color: #f5f5f5;
  color: #666;
}

.form-btn.submit {
  background-color: #2979ff;
  color: #fff;
}
.popup-type {
  font-size: 28rpx;
} 
.ql-container {
		box-sizing: border-box;
		padding: 12px 15px;
		width: 100%;
		min-height: 30vh;
		margin-top: 20px;
		font-size: 16px;
		line-height: 1.5;
    border: 1rpx solid #eee;
	}
</style>

<template>
  <view class="category-management">
     <uni-card title="类型管理" is-full>
    <view class="type-management">
      <!-- 页面标题 -->
      
      <view class="header">
        
        <!-- <button class="add-btn" @click="openAddModal">新增类型</button> -->
        <view class="add-btn" @click="openAddModal">
      <uni-icons type="plusempty" size="24" color="#fff"></uni-icons>
    </view>
      </view>
  
      <!-- 类型列表 -->
      <view class="type-list">
        <uni-table>
            <uni-tr>
          <uni-th width="80" align="left">类型</uni-th>
          <uni-th width="80" align="left">操作</uni-th>
          <uni-th width="80" align="left">操作</uni-th>
        </uni-tr>
        <uni-tr v-for="(item, index) in typeList" :key="index">
          <uni-td>{{ item.name }}</uni-td>
          <!-- 操作按钮 -->
           <uni-td>
            <view class="actions">
            <button class="popup-btn edit" @click="openEditModal(item)">编辑</button>
            <!-- <button class="popup-btn delete" @click="deleteType(index)">删除</button> -->
          </view>
           </uni-td>
           <uni-td>
            <view class="actions">
            <!-- <button class="popup-btn edit" @click="openEditModal(item)">编辑</button> -->
            <button class="popup-btn delete" @click="deleteType(index)">删除</button>
          </view>
           </uni-td>
        </uni-tr>
        </uni-table> 
      </view>
      
  
      <!-- 新增/编辑类型弹窗 -->
      <uni-popup ref="typePopup" type="dialog">
        <view class="popup-container">
          <input v-model="currentType.name" placeholder="请输入类型名称" class="input" />
  
          <view class="actions">
            <button class="popup-btn delete" @click="closePopup">取消</button>
            <button class="popup-btn edit" @click="saveType">确认</button>
          </view>
        </view>
      </uni-popup>
    </view>
</uni-card>
</view>
  </template>
  <script>
  import { apiRequest } from '@/utils/api';

  export default {
    data() {
      return {
        typeList: [],
        currentType: { name: "" },
        editIndex: -1,
      };
    },
    mounted() {
      this.fetchTypes();
    },
    methods: {
      // 打开新增弹窗
      openAddModal() {
        this.currentType = { name: "" };
        this.editIndex = -1;
        this.$refs.typePopup.open();
      },
  
      // 打开编辑弹窗
      openEditModal(item) {
        this.currentType = { ...item };
        this.editIndex = this.typeList.indexOf(item);
        this.$refs.typePopup.open();
      },
      async fetchTypes(){
        try{
            const response=await apiRequest('categories/all','get');
            this.typeList=response;
        }catch(error){
          console.error('获取类型失败:', error);

        }
      },
      // 关闭弹窗
      closePopup() {
        this.$refs.typePopup.close();
      },
  
      // 保存类型（新增 / 编辑）
      async saveType() {
        if (!this.currentType.name) {
          uni.showToast({ title: "请输入类型名称", icon: "none" });
          return;
        }
        const res=await apiRequest('categories/upsert','post',this.currentType);
        if(res){
          uni.showToast({ title: "添加成功", icon: "success" });
          this.closePopup();
          this.fetchTypes();
        }
        if (this.editIndex === -1) {
          // 新增
          this.typeList.push({ ...this.currentType });
        } else {
          // 编辑
          this.typeList[this.editIndex] = { ...this.currentType };
        }
  
        this.closePopup();
      },
  
      // 删除类型（带确认）
      async deleteType(index) {
        uni.showModal({
          title: "确认删除",
          content: "确定要删除该类型吗？",
          success: async (res) => {
            if (res.confirm) {
              const response=await apiRequest(`categories/delete/${this.typeList[index].id}`,'post');
              if(response){
                uni.showToast({ title: "删除成功", icon: "success" });
                this.typeList.splice(index, 1);
              }else{
                uni.showToast({ title: "删除失败", icon: "none" });
              }
            }
          },
        });

      },
    },
  };
  </script>
  
  <style scoped>
  .type-management {
    padding: 16px;
  }
  
  /* 搜索栏 */
  .search-bar {
    display: flex;
    margin-bottom: 16px;
  }
  
  .search-input {
    flex: 1;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 5px;
  }
  
  .search-btn {
    margin-left: 8px;
    padding: 8px 16px;
    background-color: #2979ff;
    color: white;
    border-radius: 5px;
  }
  
  /* 类型列表 */
  .type-list {
    background: #fff;
    border-radius: 10px;
    padding: 10px;
  }
  
  .type-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px;
    border-bottom: 1px solid #eee;
  }
  
  .type-name {
    font-size: 16px;
  }
  
  /* 操作按钮 */
  .actions {
    display: flex;
    gap: 10px;
  }
  
  /* 弹出框 */
  .popup-container {
    padding: 20px;
    background-color: #fff;
    border-radius: 10px;
    width: 320px;
    text-align: center;
  }
  
  .popup-header {
    padding: 10px 0;
    border-bottom: 1px solid #ddd;
  }
  
  .popup-title {
    font-size: 18px;
    font-weight: bold;
  }
  
  .popup-body {
    padding: 10px;
  }
  
  .input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 5px;
  }
  
  .popup-btn {
  min-width: 60rpx;
  height: 50rpx;
  
  border-radius: 36rpx;
  font-size: 20rpx;
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

  /* 新增按钮 */
  .add-btn {
    position: fixed;
    right: 30px;
    bottom: 30px;
    width: 60px;
    height: 60px;
    background-color: #2979ff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: rgb(255, 255, 255);
    font-size: 24px;
    font-weight: bold;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    cursor: pointer;
  }
  .category-management {
  padding: 16px;
}
  </style>
  
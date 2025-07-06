<template>
    <view class="nav-bar">
      <view class="nav-item" v-for="(item, index) in navList" :key="index" @click="handleClick(index)">
        <uni-icons :type="item.icon" color="#2979ff" size="30"></uni-icons>
        <text>{{ item.title }}</text>
      </view>
    </view>
  </template>
  
  <script>
import { onMounted } from 'vue';

  export default {
    props: {
      navList: {
        type: Array,
        required: true
      }
    },
    methods: {
      created() {

        alert(1);
  },
      handleClick(index) {
        this.$emit("navClick", index);
      }
    }
  };
  </script>
  
  <style scoped>
  .nav-bar {
    display: flex;
    justify-content: space-around;
    margin: 10px 0;
  }
  
  .nav-item {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  .nav-icon {
    width: 50px;
    height: 50px;
  }
  </style>
  
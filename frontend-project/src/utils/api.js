import axios from 'axios';

// 创建 Axios 实例
axios.defaults.timeout=50000;
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/', // 目标 API 服务器
  timeout: 10000, // 请求超时时间
});

// 请求拦截器
apiClient.interceptors.request.use(
  (config) => {
    // 在请求发送之前做些什么，比如添加认证头
    config.headers['Content-Type'] = 'application/json';
    return config;
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// 响应拦截器
apiClient.interceptors.response.use(
  (response) => {
    // 对响应数据做些什么
    return response.data;
  },
  (error) => {
    // 对响应错误做些什么
    console.error('响应错误:', error);
    return Promise.reject(error);
  }
);

// 封装 API 请求方法
export const apiRequest = async (path, method, data = null) => {
  try {
    const response = await apiClient({
      url: path,
      method: method,
      data: data,
    });
    return response;
  } catch (error) {
    console.error('API请求错误:', error);
    throw error;
  }
};

// 新增获取用户图片的 API 方法
export const getUserImage = async (imageName) => {
  try {
    const response = await apiClient.get(`files/download/${imageName}`);
    return response; // 返回图片的 URL
  } catch (error) {
    console.error('获取用户图片失败:', error);
    throw error;
  }
}; 

// 新增根据用户 ID 获取用户信息的 API 方法
export const getUserInfoById = async (userId) => {
  try {
    const response = await apiClient.get(`users/${userId}`);
    const userInfo = {
      ...response,
      userAvatar: response.avatar // 假设返回的数据结构中包含用户头像
    };
    return userInfo; // 返回用户信息
  } catch (error) {
    console.error('获取用户信息失败:', error);
    throw error;
  }
};

export const formatDate= (dateString)=> {
  // 预处理字符串，去掉最后的 " 00:00"
  const cleanedDateString = dateString.replace(" 00:00", "");
  
  // 解析成 Date 对象
  const date = new Date(cleanedDateString);
  
  // 格式化为 "YYYY-MM-DD"
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0"); // 月份从 0 开始，需要 +1
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
  }


const http = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 1000,
  headers: {'Content-Type': 'application/json'}
});

export default http;



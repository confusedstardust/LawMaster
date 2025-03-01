// module.exports = {
//     devServer: {
//       proxy: {
//         '/users': {
//           target: 'http://localhost:8080', // 目标 API 服务器
//           changeOrigin: true, // 是否改变源
//           pathRewrite: { '^/users': '' }, // 重写路径
//         },
//       },
//     },
//   };
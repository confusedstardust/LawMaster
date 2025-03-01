<script>
export default {
  onLaunch: function () {
    console.log('App Launch')
    // 配置 tabBar 图标
    uni.setTabBarItem({
      index: 0,
      iconPath: '../../static/tabbar/home.png',
      selectedIconPath: '../../static/tabbar/home.png',
      text: '首页'
    })
    uni.setTabBarItem({
      index: 1,
      iconPath: '../../static/tabbar/binoculars.png',
      selectedIconPath: '../../static/tabbar/binoculars.png',
      text: '社区'
    })
    uni.setTabBarItem({
      index: 2,
      iconPath: '../../static/tabbar/file-text.png',
      selectedIconPath: '../../static/tabbar/file-text.png',
      text: '答题'
    })
    uni.setTabBarItem({
      index: 3,
      iconPath: '/static/tabbar/user.png',
      selectedIconPath: '/static/tabbar/user.png',
      text: '我的'
    })
  },
  onShow: function () {
    console.log('App Show')
    
  },
  onHide: function () {
    console.log('App Hide')
  },
}
</script>

<style>
/*每个页面公共css */
</style>

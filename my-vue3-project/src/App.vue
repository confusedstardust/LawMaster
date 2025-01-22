<script>
export default {
  onLaunch: function () {
    console.log('App Launch')
    // 配置 tabBar 图标
    uni.setTabBarItem({
      index: 0,
      iconPath: 'static/tabbar/home.png',
      selectedIconPath: 'static/tabbar/home-active.png',
      text: '首页'
    })
    uni.setTabBarItem({
      index: 1,
      iconPath: 'static/tabbar/community.png',
      selectedIconPath: 'static/tabbar/community-active.png',
      text: '社区'
    })
    uni.setTabBarItem({
      index: 2,
      iconPath: 'static/tabbar/quiz.png',
      selectedIconPath: 'static/tabbar/quiz-active.png',
      text: '答题'
    })
    uni.setTabBarItem({
      index: 3,
      iconPath: 'static/tabbar/profile.png',
      selectedIconPath: 'static/tabbar/profile-active.png',
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

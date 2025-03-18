<template>
  
			<view class="example-body">
				<uni-icons type="list" size="30" @click="showDrawer('showLeft')"></uni-icons>

				<uni-drawer ref="showLeft" mode="left" :width="220" @change="change($event,'showLeft')">
					<view class="close">
						<!-- <button @click="closeDrawer('showLeft')"><text class="word-btn-white">关闭Drawer</text></button> -->
            <uni-list-item title="帖子管理" @click="navigateTo('postManagement')" note="列表描述信息" link>
                <template v-slot:footer>
                  <uni-icons type="pyq" size="30"></uni-icons>
                </template>
          </uni-list-item>

          <uni-list-item @click="navigateTo('userManagement')" title="用户管理"  note="列表描述信息" link >
                <template v-slot:footer>
                  <uni-icons type="contact" size="30" ></uni-icons>
                  <!-- <image class="slot-image" src="/static/logo.png" mode="widthFix"></image> -->
                </template>
          </uni-list-item>


          <uni-list-item title="题库管理" @click="navigateTo('questionBankManagement')" note="列表描述信息" link>
                <template v-slot:footer>
                  <uni-icons type="compose" size="30"></uni-icons>
                  <!-- <image class="slot-image" src="/static/logo.png" mode="widthFix"></image> -->
                </template>
          </uni-list-item>

          <uni-list-item title="新闻/知识管理" @click="navigateTo('newsManagement')" note="列表描述信息" link>
                <template v-slot:footer>
                  <uni-icons type="wallet-filled" size="30"></uni-icons>
                  <!-- <image class="slot-image" src="/static/logo.png" mode="widthFix"></image> -->
                </template>
          </uni-list-item>

		  <uni-list-item title="登出" @click="handleLogout" note="列表描述信息" link>
                <template v-slot:footer>
                  <uni-icons type="undo-filled" size="30"></uni-icons>
                  <!-- <image class="slot-image" src="/static/logo.png" mode="widthFix"></image> -->
                </template>
          </uni-list-item>
					

          </view>
				</uni-drawer>
			</view>
	<uni-section title="系统指标" type="line" padding>


			<!-- 因为swiper特性的关系，请指定swiper的高度 ，swiper的高度并不会被内容撑开-->
			<swiper class="swiper" :indicator-dots="true">
				<swiper-item>
					<uni-grid :column="3" :highlight="true" @change="change">
						<uni-grid-item v-for="(item, index) in list" :index="index" :key="index">
							<view class="grid-item-box">
								<image :src="item.url" class="image" mode="aspectFill" />
								<text class="text">{{ item.text }}</text>
                <br/>
                <text class="text-val">{{item.badge}}</text>
							</view>
						</uni-grid-item>
					</uni-grid>
				</swiper-item>
			</swiper>
		</uni-section>
</template>
<script>
import { apiRequest } from '@/utils/api.js';
	export default {
		components: {},
		data() {
			return {
        showLeft: false,
				dynamicList: [],
				list: [{
						url: '/static/c1.png',
						text: '用户人数',
						badge: this.userCount,
						type: "primary"
					},
					{
						url: '/static/c2.png',
						text: '帖子总数',
						badge: this.postCount,
						type: "success"
					},
					{
						url: '/static/c3.png',
						text: '新闻/知识总数',
						badge: this.questionBankCount,
						type: "warning"
					},
					{
						url: '/static/c4.png',
						text: '题库总数',
						badge: '',
						type: "error"
					},
					{
						url: '/static/c5.png',
						text: '题库类型数'
					},
					{
						url: '/static/c6.png',
						text: '易错题排行'
					},
					{
						url: '/static/c7.png',
						text: '热帖排行'
					},
					{
						url: '/static/c8.png',
						text: '收藏最多'
					},
					{
						url: '/static/c9.png',
						text: '点赞最多'
					}
				],
				userCount: 0,
				postCount: 0,
				newsCount: 0,
				questionBankCount: 0,
			}
		},
		created() {
			this.fetchStatistics();
		},
		methods: {
			change(e) {
				let {
					index
				} = e.detail
				this.list[index].badge && this.list[index].badge++

				uni.showToast({
					title: `点击第${index+1}个宫格`,
					icon: 'none'
				})
			},
			add() {
				if (this.dynamicList.length < 9) {
					this.dynamicList.push({
						url:` /static/c${this.dynamicList.length+1}.png`,
						text: `Grid ${this.dynamicList.length+1}`,
						color: this.dynamicList.length % 2 === 0 ? '#f5f5f5' : "#fff"
					})
				} else {
					uni.showToast({
						title: '最多添加9个',
						icon: 'none'
					});
				}
			},
			del() {
				this.dynamicList.splice(this.dynamicList.length - 1, 1)
			},
			confirm() {},
			// 打开窗口
			showDrawer(e) {
				this.$refs[e].open()
			},
			// 关闭窗口
			closeDrawer(e) {
				this.$refs[e].close()
			},
			// 抽屉状态发生变化触发
			change(e, type) {
				console.log((type === 'showLeft' ? '左窗口' : '右窗口') + (e ? '打开' : '关闭'));
				this[type] = e
			},
      async fetchStatistics() {
  try {
    const userResponse = await apiRequest('users/allcount', 'get');
    const postResponse = await apiRequest('posts', 'get');
    const newsResponse = await apiRequest('articles/all', 'get');
    const questionsResponse = await apiRequest('questions/all', 'get');

    // 更新数据
    this.userCount = userResponse;
    this.postCount = postResponse.length;
    this.newsCount = newsResponse.length;
    this.questionBankCount = questionsResponse.length;

    console.log(this.questionBankCount, this.postCount, this.userCount, this.newsCount);

    // **更新 list 数组**
    this.list = [
      {
        url: '/static/c1.png',
        text: '用户人数',
        badge: this.userCount,
        type: "primary"
      },
      {
        url: '/static/c2.png',
        text: '帖子总数',
        badge: this.postCount,
        type: "success"
      },
      {
        url: '/static/c3.png',
        text: '新闻/知识总数',
        badge: this.newsCount,
        type: "warning"
      },
      {
        url: '/static/c4.png',
        text: '题库总数',
        badge: this.questionBankCount,
        type: "error"
      },
      {
        url: '/static/c5.png',
        text: '题库类型数'
      },
      {
        url: '/static/c6.png',
        text: '易错题排行'
      },
      {
        url: '/static/c7.png',
        text: '热帖排行'
      },
      {
        url: '/static/c8.png',
        text: '收藏最多'
      },
      {
        url: '/static/c9.png',
        text: '点赞最多'
      }
    ];
  } catch (error) {
    console.error('Error fetching statistics:', error);
  }
}
,
			navigateTo(page) {
				uni.navigateTo({
					url: `/pages/admin/${page}`
				});
			},
	  handleLogout() {
		// 清除本地存储中的用户信息
		localStorage.removeItem('userToken');
    	localStorage.removeItem('userInfo');
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.reLaunch({
              url: '/pages/login/login'
            })
          }
        }
      })
    }
		},
		onNavigationBarButtonTap(e) {
			if (this.showLeft) {
				this.$refs.showLeft.close()
			} else {
				this.$refs.showLeft.open()
			}
		},
		// app端拦截返回事件 ，仅app端生效
		onBackPress() {
			if (this.showRight || this.showLeft) {
				this.$refs.showLeft.close()
				this.$refs.showRight.close()
				return true
			}
		}
      
	}
</script>
<style lang="scss">
	.image {
		width: 25px;
		height: 25px;
	}

.text {
  position: absolute;
  top: 5px;
  left: 5px;
  font-size: 14px;
}

.text-val {
  font-size: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
}


	.grid-dynamic-box {
		margin-bottom: 15px;
	}

	.grid-item-box {
		flex: 1;
		// position: relative;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 15px 0;
	}

	.grid-item-box-row {
		flex: 1;
		// position: relative;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: center;
		padding: 15px 0;
	}

	.grid-dot {
		// position: absolute;
		top: 5px;
		right: 15px;
	}

	.swiper {
		height: 420px;
	}

	/* #ifdef H5 */
	@media screen and (min-width: 768px) and (max-width: 1425px) {
		.swiper {
			height: 630px;
		}
	}

	@media screen and (min-width: 1425px) {
		.swiper {
			height: 830px;
		}
	}
  .slot-image {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		margin-right: 10px;
		width: 30px;
		height: 30px;
	}

	/* #endif */
</style>
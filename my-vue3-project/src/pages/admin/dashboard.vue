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

          <uni-list-item title="新闻/知识/案例管理" @click="navigateTo('newsManagement')" note="列表描述信息" link>
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

		<uni-section title="应用指标" type="line" padding>
		</uni-section>
		<swiper class="swiper" :indicator-dots="true" style="height: 600rpx;">
  <!-- **每周发帖数** -->
  <swiper-item>
    <uni-grid :column="1" :highlight="true" style="height: 100%;">
      <view>
        <canvas canvas-id="postChart" id="postChart" class="charts" @touchend="tap" />
      </view>
    </uni-grid>
  </swiper-item>

  <!-- **每周新增用户数** -->
  <swiper-item>
    <uni-grid :column="1" :highlight="true" style="height: 100%;">
      <view>
        <canvas canvas-id="userChart" id="userChart" class="charts" @touchend="tap" />
      </view>
    </uni-grid>
  </swiper-item>

    <!-- **新增分类问题数量的图表** -->
	<swiper-item>
    <uni-grid :column="1" :highlight="true" style="height: 100%;">
      <view>
        <canvas canvas-id="categoryChart" id="categoryChart" class="charts" @touchend="tap" />
      </view>
    </uni-grid>
  </swiper-item>
</swiper>
</template>
<script>
import { apiRequest } from "@/utils/api.js";
import uCharts from "@qiun/ucharts/u-charts.js";

var uChartsInstance = {};

export default {
  data() {
    return {
      cWidth: 750,
      cHeight: 500,
      list: [],
      userCount: 0,
      postCount: 0,
      newsCount: 0,
      questionBankCount: 0,
	  categoryData: [],
	  categoryList:[]
    };
  },
  created() {
    this.cWidth = uni.upx2px(750);
    this.cHeight = uni.upx2px(500);
    this.fetchStatistics();
    this.fetchWeeklyPostData();
    this.fetchWeeklyUserData();
	this.fetchCategoryData();
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
						url: `/static/c${this.dynamicList.length+1}.png`,
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
			getCategoryById(id){
      const categoryVal=this.categoryList.filter(item=>item.id==id)
      console.log(categoryVal)
      return categoryVal[0].name
    },
    async fetchStatistics() {
      try {
        const userResponse = await apiRequest("users/allcount", "get");
        const postResponse = await apiRequest("posts", "get");
        const newsResponse = await apiRequest("articles/all", "get");
        const questionsResponse = await apiRequest("questions/all", "get");

        this.userCount = userResponse;
        this.postCount = postResponse.length;
        this.newsCount = newsResponse.length;
        this.questionBankCount = questionsResponse.length;
        this.list = [
          { url: "/static/c1.png", text: "用户人数", badge: this.userCount, type: "primary" },
          { url: "/static/c2.png", text: "帖子总数", badge: this.postCount, type: "success" },
          { url: "/static/c3.png", text: "新闻/知识总数", badge: this.newsCount, type: "warning" },
          { url: "/static/c4.png", text: "题库总数", badge: this.questionBankCount, type: "error" },
		  { url: "/static/c4.png", text: "题库类型数", badge: this.categoryList.length, type: "error" },
		  { url: "/static/c4.png", text: "易错题排行", badge: this.questionBankCount, type: "error" },
		  { url: "/static/c4.png", text: "热帖排行", badge: this.questionBankCount, type: "error" },
		  { url: "/static/c4.png", text: "收藏最多", badge: this.questionBankCount, type: "error" },
		  { url: "/static/c4.png", text: "点赞最多", badge: this.questionBankCount, type: "error" },
        ];
      } catch (error) {
        console.error("Error fetching statistics:", error);
      }
    },async fetchCategoryData() {
    try {
      const response = await apiRequest("questions/countByCategory", "get");
	  const categoryArray=await apiRequest('categories/all','get');
    	this.categoryList=categoryArray
      let categories = response.map(item => `${this.getCategoryById(item.category_id)}`);
      let data = response.map(item => item.count);
      this.drawCharts("categoryChart", categories, [{ name: "问题数", data }]);
    } catch (error) {
      console.error("获取分类问题数量失败:", error);
    }
  },
	drawCategoryChart() {
		const context = uni.createCanvasContext('categoryChart', this);
		const categories = this.categoryData.map(item => `类别 ${item.category_id}`);
		const counts = this.categoryData.map(item => item.count);

		// 这里用简单的柱状图逻辑，你也可以使用 uCharts 或 ECharts
		context.setFillStyle('blue');
		counts.forEach((count, index) => {
		const barHeight = count * 30; // 按比例放大
		context.fillRect(50 + index * 100, 400 - barHeight, 50, barHeight);
		});

		context.draw();
	},
    async fetchWeeklyPostData() {
      try {
        const response = await apiRequest("posts/weekly", "get");
        let categories = response.map(item => item.postDate.slice(5));
        let data = response.map(item => item.postCount);
        this.drawCharts("postChart", categories, [{ name: "每周发帖数", data }]);
      } catch (error) {
        console.error("获取发帖数据失败:", error);
      }
    },

    async fetchWeeklyUserData() {
      try {
        const response = await apiRequest("users/weekly", "get");
        let categories = response.map(item => item.userDate.slice(5));
        let data = response.map(item => item.userCount);
        this.drawCharts("userChart", categories, [{ name: "每周新增用户数", data }]);
      } catch (error) {
        console.error("获取用户数据失败:", error);
      }
    },
	drawCharts(canvasId, categories, series) {
  const ctx = uni.createCanvasContext(canvasId, this);
  uChartsInstance[canvasId] = new uCharts({
    type: "column",
    context: ctx,
    width: this.cWidth,
    height: this.cHeight,
    categories,
    series,
    animation: true,
    background: "#FFFFFF",
    padding: [20, 15, 15, 5], // ✅ 增加 padding，让图表不至于太贴边
    xAxis: { 
      disableGrid: true,
      itemRotate: 0, // ✅ 文字角度，0 为水平，45° 适合长文本
    },
    yAxis: { 
      data: [{ min: 0 }]
    },
    extra: { 
      column: { 
        type: "group",
        width: 20,  // ✅ 设置柱子宽度（值越小柱子越细）
        activeBgColor: "#FFA726", // ✅ 可选，柱子点击时的颜色
        seriesGap: 5, // ✅ 让不同组之间的柱子有点间隔
        categoryGap: 20, // ✅ 让整体分类间距增大，使柱子看起来不挤
      }
    }
  });
}
,    

    tap(e) {
      uChartsInstance[e.target.id].touchLegend(e);
      uChartsInstance[e.target.id].showToolTip(e);
    },

    navigateTo(page) {
      uni.navigateTo({ url: `/pages/admin/${page}` });
    },

    handleLogout() {
      uni.showModal({
        title: "提示",
        content: "确定要退出登录吗？",
        success: res => {
          if (res.confirm) {
            uni.removeStorageSync("token");
            uni.removeStorageSync("userInfo");
            uni.reLaunch({ url: "/pages/login/login" });
          }
        }
      });
    }
  }
};
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
	.charts{
    width: 750rpx;
    height: 500rpx;
  }

	/* #endif */
</style>
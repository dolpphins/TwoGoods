##项目名称
**贰货1+1**

##项目介绍
基于Android平台的二手商品APP，主要功能包括：浏览商品，查看商品详情，搜索商品，发布商品，私聊，个人中心等。

##项目中使用到的主要技术
 * View相关
    * 改造XListView，并实现XGridView
    * 自定义CircleImageView，EmojiTextView等
    * 修补ViewPager高度总是match_parent问题
    * 解决ViewPager与ListView滑动冲突（左右上下冲突类型）
 * 优化相关
    * ListView大量优化，包括图片加载一系列问题
    * 内存优化，包括Bitmap相关优化，防止内存泄露等
    * 网络优化
    * UI优化
 * 项目总体框架相关
    * Activity+Fragment框架设计
    * 面向对象设计，继承+组合+面向接口
    * 单例+模板+监听器解耦
    * 公共部分封装+默认操作
    * MVP简单使用，如Loginer登录逻辑与Activity分离
    * 公共数据，配置数据统一管理
 * 第三方框架使用
    * XListView
    * EventBus，用于解耦
    * OrmLite，将改造为支持List数据
    * PhotoView，图片查看

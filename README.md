###贰货1+1
---
该项目为基于Android系统的二手交易平台app。

###项目统一编码
---
UTF-8

###开发环境
---
Java1.7+Eclipse(集成ADT插件和Android SDK)

Android minSdkVersion：14(Android 4.0 Ice Cream Sandwich)

Android targetSdkVersion：19(Android 4.4 KitKat)

###运行环境
---
操作系统：Android4.0及以上

###项目工程包目录结构
---
* twogoods
    * src
        * com.lym.twogoods
        * com.lym.twogoods.adapter
        * com.lym.twogoods.adapter.base
        * com.lym.twogoods.bean
        * com.lym.twogoods.config
        * com.lym.twogoods.db
        * com.lym.twogoods.eventbus.event
        * com.lym.twogoods.fragment
        * com.lym.twogoods.fragment.base
        * com.lym.twogoods.https
        * com.lym.twogoods.index
        * com.lym.twogoods.interf
        * com.lym.twogoods.message
        * com.lym.twogoods.mine
        * com.lym.twogoods.nearby
        * com.lym.twogoods.publish
        * com.lym.twogoods.screen
        * com.lym.twogoods.settings
        * com.lym.twogoods.ui
        * com.lym.twogoods.ui.base
        * com.lym.twogoods.utils
        * com.lym.twogoods.widget
        * com.lym.twogoods.viewholder

**说明**

com.lym.twogoods:存放整个App相关的类及接口

com.lym.twogoods.adapter:存放App公共的Adapter

com.lym.twogoods.adapter.base:存放App公共的Adapter基类

com.lym.twogoods.bean:存放所有实体类

com.lym.twogoods.config:存放配置类

com.lym.twogoods.db:存放与数据库操作相关的类

com.lym.twogoods.eventbus.event:存放EventBus对象

com.lym.twogoods.fragment:存放App公共的fragment

com.lym.twogoods.fragment.base:存放App公共的fragment基类

com.lym.twogoods.https:存放与网络请求相关的类

com.lym.twogoods.index:存放与首页相关的类及接口等

com.lym.twogoods.interf:存放App公共接口

com.lym.twogoods.message:存放与消息相关的类及接口等

com.lym.twogoods.mine:存放与我的相关的类及接口等

com.lym.twogoods.nearby:存放与附近相关的类及接口等

com.lym.twogoods.publish:存放与发布相关的类及接口等

com.lym.twogoods.screen:与屏幕适配相关

com.lym.twogoods.settings:存放与设置相关的类及接口等

com.lym.twogoods.ui:存放App公共的Activity

com.lym.twogoods.ui.base:存放App公共的Activity基类

com.lym.twogoods.utils:存放App公共工具类

com.lym.twogoods.widget:存放App公共自定义View

com.lym.twogoods.viewholder:存放App公共ViewHolder

###编码规范
---
1.大部分图片放在drawable-hdpi目录下

2.大部分布局文件放在layout目录下

3.大部分值文件放在values目录下

4.第三方jar包没有特殊情况都放在libs目录下

###编码分工
---

* 2015.10.7-2015.10.10
    
    * 麦灿标
          项目主体框架，SharePreference管理类，AccessTokenKeeper类，其它实体类及greenDAO代码
    
    * 尧俊锋
          大部分工具类，包括StringUtil，ImageUtil，FileUtil，MethodCompat，NetworkHelper,EncryptHelper，DatabaseHelper，Debugger、TimeUtil
    
    * 龙宇文
          App首页，登录页面，注册页面，找回密码页面，UserBean，UserInfoBean，相应的greenDAO代码


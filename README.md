
## 测试Demo
- http://39.105.191.22:3037/dt/console
- 账户:guest 密码:1
- 账户:user1 密码:123456
- 账户:user2 密码:123456
- 账户:user3 密码:123456


## 所用框架
- MyBatis-Plus 3.0.7
- MyBatis 3.4.6
- Spring 5.0.7
- Fastjson 1.2.61
- Shiro 1.4.1
- Druid 1.1.15
- Easypoi 4.1.2
- quartz 2.3.0

## 联系方式
- Mail:maillank@qq.com


## 部署说明
### 步骤一
- 准备环境
```
- 中间件:Tomcat 8 以上
- 数据库:Mysql 5.7
```

### 步骤二
- 初始化sql
```
- SQL>CREATE DATABASE IF NOT EXISTS dt default charset utf8 COLLATE utf8_general_ci;
- SQL>set names utf8;
- SQL>source db.sql
```

### 步骤三
- 发布应用,注意:最新版本的war包已经发布,请直接下载,不需要下载源代码进行编译。
```
- 部署到Tomcat目录的webapps下,修改配置文件(webapps/dt/WEB-INF/classes)中数据库的连接信息
- jdbc.url=jdbc:mysql://ip:port/dt?useUnicode=true&characterEncoding=utf8&useSSL=false
- jdbc.username=root
- jdbc.password=root_pwd
```

### 步骤四
- 启动服务访问
```
- http://ip:port/dt/console
- 选择用户名方式登陆
- 账户:admin 
- 密码:1
```

## 效果图
![输入图片说明](https://images.gitee.com/uploads/images/2019/1112/130924_93070844_448530.jpeg "11.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1112/124506_47c9ca08_448530.jpeg "2.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1117/211517_8ba3a822_448530.jpeg "11.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1121/222157_1ae13ef1_448530.jpeg "55.jpeg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1117/211528_0797dbbb_448530.jpeg "22.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1117/211538_1e78d9a4_448530.jpeg "33.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1112/124530_b7e7847b_448530.jpeg "4.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1112/124540_62166efa_448530.jpeg "5.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2019/1205/213815_ad2975a7_448530.png "lc.png")
## 后期开发计划
- 添加资产自定义属性功能
- 添加工单流程,自定义审批流程
- 丰富资产报表
- 添加领用，资产转移等功能

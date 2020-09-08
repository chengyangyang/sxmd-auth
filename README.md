# 欢迎使用
## 设计目的
> 提供一个Java授权中心，使用 springsecurity oauth2
## 功能介绍
```
一、授权码授权 默认账号和密码是 admin,admin
1.http://localhost:40001/oauth/authorize?response_type=code&client_id=admin
2.获取token
http://localhost:40001/oauth/token?grant_type=authorization_code&client_id=admin&client_secret=admin&code=xsUAzT
二、密码模式
http://localhost:40001/oauth/token?grant_type=password&client_id=admin&client_secret=admin&code=mEbKLw&username=admin&password=admin


```
## 开发环境

- jdk 1.8+
- lombok
- idea
- ...
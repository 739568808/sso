# sso
参考某课堂视频实现SSO跨域单点登录demo


sso-client1跨域系统1

sso-client2跨域系统2

sso-client3同域系统1

sso-server统一认证中心

1、修改host文件


127.0.0.1       www.sso.com  ##跨域系统1

127.0.0.1       www.c1.com  ##跨域系统2

127.0.0.1       www.c2.com ##同域系统1

127.0.0.1       image.c1.com  ##统一认证中心


2、分别修改三个client的.yml配置文件域名对应host中修改的，加上对应的端口号
sso:
  url:
   prefix: http://www.sso.com:8080
client:
  host:
    url: http://www.c1.com:8081
    
3、清空浏览器缓存。分别访问http://www.c1.com:8081/ 和 http://www.c2.com:8082/ 和http://image.c1.com:8083 都会跳转到统一认证中心

4、清空浏览器缓存。启动以上四个服务后

    a、浏览器打开 http://www.c1.com:8081/跳转到统一认证中心
        输入用户名密码admin/123456登陆
        
    b、浏览器打开 http://www.c2.com:8082/ 登陆成功
    
    c、浏览器打开 http://image.c1.com:8083/ 登陆成功
    
5、白名单校验

sso-server .yml文件配置白名单|竖线分隔：white_list: www.sso.com|www.c1.com|www.c2.com|image.c1.com
http://www.sso.com:8080/checkLogin?redirectUrl=http://www.c4.com
跳转到404页面


7、退出操作

    a、退出 http://www.c1.com:8081/
    
    b、浏览器打开 http://www.c2.com:8082/ 退出成功
    
    c、浏览器打开 http://image.c1.com:8083/ 退出成功

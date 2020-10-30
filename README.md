# Tiny Download Station
一个极小的下载站服务器，提供配置下载根目录、文件夹浏览、文件流式下载、主页定制等功能

仅供学习与交流

### Get Start
1. 下载Releases中的web.zip
2. 解压web.zip，将web/作为Tomcat(自行安装)的执行目录。配置conf/server.xml:
````
<Context path="/" docBase="/web" debug="0" reloadable="false" />
````
3. 配置web/WEB-INF/configs.properties中的rootpath项，填写下载文件存放的根目录
4. 启动Tomcat

### 截图
主页面：

![Image text](https://github.com/RicheyJang/TinyDownloadStation/blob/master/picture/index.png)

可以通过修改web/index.jsp来自行定制(v0.1)

---
下载目录页面：

![Image text](https://github.com/RicheyJang/TinyDownloadStation/blob/master/picture/inner.png)

可以通过修改web/WEB-INF/download.html来自行定制(v0.2)
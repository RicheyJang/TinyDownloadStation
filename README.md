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
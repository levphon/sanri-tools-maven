# 9420 开发工具包
---
集合了一些常用的工具,监控工具,可用于企业应用开发; 特别是金融类应用   
另外希望身为Java牛牛的你们一起测试和完善 一起封装和完成常用的Java代码。  
节约撸码时间以方便有更多的时间去把妹子～  

---
## 如何搭建环境 
1. jdk7 及以上都可以使用

2. 除了使用 maven 下载包,还需要依赖三个第三方包,执行如下命令安装 
   
   ```shell
   mvn install:install-file -Dfile=d:\IKAnalyzer2012FF_u1.jar -DgroupId=org.wltea.analyzer -DartifactId=IKAnalyzer -Dversion=2012FF_u1 -Dpackaging=jar
   mvn install:install-file -Dfile=d:\diamond-utils-2.0.5.5.jar -DgroupId=com.taobao.diamond -DartifactId=diamond-utils -Dversion=2.0.5.5 -Dpackaging=jar
   mvn install:install-file -Dfile=d:\diamond-client-2.0.5.5.jar -DgroupId=com.taobao.diamond -DartifactId=diamond-client -Dversion=2.0.5.5 -Dpackaging=jar
   ```
   

3. 启动

```shell
mvn jetty:run
```



## 工具理念

1. 轻量级,只依赖于文件系统
2. 小工具,大作用,减少模板代码的手工编写
3. 自定义框架,加快项目启动速度 

## 已经有的工具

已经存在的工具可以在 /src/main/resources/com/sanri/config/tools.properties 中查看

1. 方法或变量取名
2. 数据提取
3. SQL 客户端,已经支持 mysql,postgresql,oracle ; 可自定义实现其它数据库 
   * 表结构查询
   * pojo,xml  生成
   * 项目模板代码生成
   * 数据导出
4. kafka  监控和 offset 设置,支持新旧版本 kafka
5. zookeeper 数据监控
6. 模板代码生成,根据列字段 
7. 列字段比较 
8. 数据库表字段,注释,名称查询,及后续模板代码操作
9. webservice 调试工具,只要输入 wsdl 地址,自动解析并构建 xml 消息 
10. 下划线转驼峰,驼峰转下划线工具
11. 生份证号码生成与验证
12. 图片转 base64 ,base64 转图片


## 扩展自己的工具

* 除前端交互 servlet 必须写在 com.sanri.app.servlet 包中以外,其它随便自己定制

* servlet 中的代码由于框架 javassist 的原因 ,不支持 java8 的 lambada 表达式
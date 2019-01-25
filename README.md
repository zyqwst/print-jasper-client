## 简介

[print-jasper-client](https://github.com/zyqwst/print-jasper-client)是一个基于jasperreport报表工具的打印客户端。用户电脑安装插件后，可以在网页直接唤醒打印，支持打印预览，打印到pdf，打印到指定打印机。

**前序准备**
客户端目前仅实现了windows系统版本，需要对windows 自定义 URL protocol有简单的了解

**参考资料**
- [自定义Protocol URL从浏览器运行本地应用](https://www.jianshu.com/p/8ba7fefeb7ad)

**流程图**
<p align="center">
  <img width="500" src="https://upload-images.jianshu.io/upload_images/2287481-918f3874d759572c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1000/format/webp">
</p>

## 开发思路

**问  题：** B/S架构下，客户在浏览器查询到数据后，打印单据到指定打印机，但是浏览器不支持个性化打印操作。
**解决思路：** 用户在浏览器点击`打印`，服务器生成报表，并提供访问报表的唯一URL响应给浏览器，浏览器通过windows的 `Protocol URL `协议，唤醒本地打印程序，传递URL到打印程序；然后打印程序访问给定的URL地址获取数据并打印。

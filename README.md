# Bing Images

每日2点下载 [https://bing.com](https://bing.com) 站点背景图片

## 构建方式
```$xslt
mvn clean package -DskipTests
```

## 运行方式
```$xslt
nohup java -jar bing-images-store.jar <target_dir> > /tmp/bing-images-store.log 2>&1 &
```
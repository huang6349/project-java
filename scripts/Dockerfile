FROM registry.cn-hangzhou.aliyuncs.com/huangyalong/amazoncorretto:17.0.15
ADD project-web-*.jar /app.jar
ADD wait /wait
RUN chmod +x /wait
EXPOSE 8088
EXPOSE 8085
CMD /wait && java -Djava.security.egd=file:/dev/./urandom -Xms2048M -Xmx2048M -Xmn512m -jar /app.jar

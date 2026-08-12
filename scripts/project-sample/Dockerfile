FROM registry.cn-hangzhou.aliyuncs.com/huangyalong/amazoncorretto:17.0.15
ADD project-web*.jar /app.jar
ADD wait /wait
RUN chmod +x /wait
EXPOSE 8088
CMD /wait && java -Djava.security.egd=file:/dev/./urandom ${JAVA_OPTS} -jar /app.jar

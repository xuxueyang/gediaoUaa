# dockerfile 基础配置
FROM java:8
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8800
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


#DOCKER_HOST=tcp://193.112.17.169:2375  mvn clean package dockerfile:build -DskipTests
#检查pom文件没有任何问题，最后发现在maven的conf/setting.xml中要加入:
#<pluginGroups>
#    <pluginGroup>com.spotify</pluginGroup>
#</pluginGroups>
#https://blog.csdn.net/a610786189/article/details/79746426
#docker run -tid --net=host --name docker_host1 qlh/uaa


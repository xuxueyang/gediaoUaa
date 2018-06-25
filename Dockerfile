# dockerfile 基础配置
FROM daocloud.io/library/java:8u40-b22
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} /usr/local/qinglonghui/uaa-0.0.1-SNAPSHOT.war
WORKDIR /opt/docker
EXPOSE 8761
ENTRYPOINT ["java","-jar","./uaa-0.0.1-SNAPSHOT.war"]


#DOCKER_HOST=tcp://193.112.17.169:2375  mvn clean package dockerfile:build -DskipTests
#检查pom文件没有任何问题，最后发现在maven的conf/setting.xml中要加入:
#<pluginGroups>
#    <pluginGroup>com.spotify</pluginGroup>
#</pluginGroups>
#https://blog.csdn.net/a610786189/article/details/79746426

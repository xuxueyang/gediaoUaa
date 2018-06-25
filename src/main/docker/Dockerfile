#FROM openjdk:8-jre-alpine

#ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
#    JHIPSTER_SLEEP=0 \
#    JAVA_OPTS=""

#CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
#    sleep ${JHIPSTER_SLEEP} && \
#    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war

#EXPOSE 9999 5701/udp

#ADD *.war /app.war

# dockerfile 基础配置
FROM daocloud.io/library/java:8u40-b22
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} /usr/local/qinglonghui/uaa.jar
WORKDIR /opt/docker
EXPOSE 8761
ENTRYPOINT ["java","-jar","./uaa.jar"]

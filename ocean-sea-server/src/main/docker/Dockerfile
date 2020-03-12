FROM openjdk:8u242-jdk

# 定义变量
ARG JAR_FILE
ARG VERSION_FILE
ARG BASE_IMAGE=openjdk:8u242-jdk

# 拷贝jar
COPY ./lib/${JAR_FILE} /var/wd/app.jar
COPY ./${VERSION_FILE} /var/wd/${VERSION_FILE}

VOLUME ${LOGGING_PATH}
VOLUME ${TOMCAT_BASE}

#EXPOSE 映射端口
EXPOSE 10000 19988

CMD [ "/bin/bash", "-c", "java ${DEFAULT_JAVA_OPTS} -jar /var/wd/app.jar" ]

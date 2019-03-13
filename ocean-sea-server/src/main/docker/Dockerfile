
FROM xxxxx.tencentyun.com/base/java8:1.0

#定义变量
ARG JAR_FILE

VOLUME ${LOGGING_PATH}
VOLUME ${TOMCAT_BASE}

CMD [ "/bin/bash", "-c", "java ${DEFAULT_JAVA_OPTS} -Dsjdbc.self.id.generator.worker.id=$RANDOM$RANDOM -Djava.security.egd=file:/dev/./urandom -jar /var/wd/app.jar" ]

# 拷贝jar
COPY ./lib/${JAR_FILE} /var/wd/app.jar

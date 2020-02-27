FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD "target/new-application-config-boot-1.0.0-SNAPSHOT.jar" app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.cloud.config.uri=http://${CONFIG_HOST}:8888 -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
EXPOSE 8888
FROM openjdk:8
ADD target/${applicationName}-boot-1.0.0-SNAPSHOT.jar ${applicationName}-boot.jar
ENTRYPOINT ["java" , "-jar", "/${applicationName}-boot.jar"]
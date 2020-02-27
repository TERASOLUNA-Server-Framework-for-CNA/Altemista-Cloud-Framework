FROM tomcat:8.0.43-jre8
ADD target/new-application-webapp-1.0.0-SNAPSHOT.war /usr/local/tomcat/webapps/
EXPOSE 8084
CMD ["catalina.sh", "run"]
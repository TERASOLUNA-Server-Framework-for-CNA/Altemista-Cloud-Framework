FROM tomcat:8.0.43-jre8
ADD target/${applicationName}-webapp-1.0.0-SNAPSHOT.war /usr/local/tomcat/webapps/${applicationName}-webapp.war
CMD ["catalina.sh", "run"]
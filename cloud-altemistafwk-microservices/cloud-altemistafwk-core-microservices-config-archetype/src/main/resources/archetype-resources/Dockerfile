FROM java:8-jre
ADD target/${applicationName}-boot-1.0.0-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/${applicationName}-boot-1.0.0-SNAPSHOT.jar"]
HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8888/actuator/health || exit 1
EXPOSE 8888
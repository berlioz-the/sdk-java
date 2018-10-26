FROM openjdk:8-jre-alpine
COPY target/*.jar /app/
RUN ls -la /app/

CMD ["/usr/bin/java", "-jar", "/app/sdk-sample-1.0-SNAPSHOT.jar"]
FROM amazoncorretto:17-alpine

EXPOSE 8081

COPY target/SpringBootDemo-0.0.1-SNAPSHOT.jar myapp.jar

ENTRYPOINT ["java", "-jar", "myapp.jar"]
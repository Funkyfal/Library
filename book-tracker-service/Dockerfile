FROM eclipse-temurin:23-jdk
WORKDIR /app
COPY target/book-tracker-service-1.0-SNAPSHOT.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/[db_name]
ENV SPRING_DATASOURCE_USERNAME=[username]
ENV SPRING_DATASOURCE_PASSWORD=[password]

ENTRYPOINT ["java", "-jar", "app.jar"]
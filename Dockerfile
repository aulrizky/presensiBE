# Builder stage
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /home/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /usr/local/lib
COPY --from=build /home/app/target/*.jar spring-boot-application.jar
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:InitialRAMPercentage=50.0 -XX:MaxRAMPercentage=80.0"
USER appuser
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar spring-boot-application.jar"]


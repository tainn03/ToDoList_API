# Step 1: Build the application
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY . /build
RUN mvn clean package -DskipTests

# Step 2: Package the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/*.jar /app/app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/app.jar ${0} ${@}" ]

# Step 1: Build the application
FROM maven:3.8.1 AS builder
WORKDIR /build
COPY . /build
RUN mvn clean package

# Step 2: Package the application
FROM openjdk:22
WORKDIR /app
COPY --from=builder /build/target/*.jar /app/app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/app.jar ${0} ${@}" ]

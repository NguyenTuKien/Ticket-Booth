#Xây dựng môi trường
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

#Chạy chương trình
FROM openjdk:21-jdk-slim
WORKDIR /app
ENV TZ=Asia/Ho_Chi_Minh
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]


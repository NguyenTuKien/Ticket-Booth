#Xây dựng môi trường
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# --- TỐI ƯU HÓA ---
# 1. Chỉ copy file pom.xml trước
COPY pom.xml .
# 2. Chạy lệnh chỉ để tải thư viện. Lớp này sẽ được cache lại.
RUN mvn dependency:go-offline
# 3. Bây giờ mới copy source code của bạn
COPY src ./src
# 4. Chạy package. Lần này Maven sẽ dùng thư viện đã được cache,
#    chỉ cần biên dịch code nên sẽ nhanh hơn rất nhiều.
RUN mvn package -DskipTests
# --- KẾT THÚC TỐI ƯU HÓA ---


#Chạy chương trình (Phần này giữ nguyên)
FROM openjdk:21-jdk-slim
WORKDIR /app
ENV TZ=Asia/Ho_Chi_Minh
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
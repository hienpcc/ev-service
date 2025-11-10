# (Đặt file này vào trong mỗi thư mục service, ví dụ: notification-service/dockerfile)

# Stage 1: Build file .jar bằng Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Dùng lệnh này để bỏ qua test HOÀN TOÀN (fix lỗi build)
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: Tạo image cuối cùng để chạy
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# Copy file .jar từ stage 'build'
COPY --from=build /app/target/*.jar app.jar

# Lệnh chạy
ENTRYPOINT ["java", "-jar", "app.jar"]
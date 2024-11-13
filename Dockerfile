# Используем JDK 17 для сборки
FROM eclipse-temurin:17-jdk as builder

WORKDIR /app

# Копирование проектных файлов
COPY gradlew /app/
COPY gradle /app/gradle
COPY build.gradle /app/
COPY src /app/src

# Предоставление прав и сборка
RUN chmod +x ./gradlew && ./gradlew build -x test


# Используем JRE 17 для выполнения
FROM eclipse-temurin:17-jre

WORKDIR /app

# Копируем только собранный jar файл из билдера
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

# 1단계: 빌드
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# 2단계: 런타임
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 타임존/UTF-8 세팅
ENV TZ=Asia/Seoul
ENV LANG=C.UTF-8

# 빌드된 JAR 파일만 런타임 이미지로 복사합니다.
COPY --from=builder /app/build/libs/*.jar app.jar

# HTTP 포트 노출
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


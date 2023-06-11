FROM openjdk:11-jdk-slim
ARG JAR_FILE=build/libs/molly-spring-0.0.1-SNAPSHOT.jar

# 터미널 접근 경로
WORKDIR /app

# ADD 압축파일.zip ./ # 이렇게 하면 압축이 풀린다.
COPY ${JAR_FILE} ./molly-application.jar

# ENV TZ=Asia/Seoul

# 실행할 명령어를 넣는다.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "molly-application.jar"]

# 명령어의 옵션
# CMD ["--server.port=8080"]
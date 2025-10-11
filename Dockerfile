# 1. 베이스 이미지 선택: Java 17 버전을 기반으로 이미지를 만듭니다.
FROM openjdk:17-jdk-slim

# 2. 빌드된 Jar 파일의 경로를 변수로 지정합니다.
ARG JAR_FILE=build/libs/*.jar

# 3. Jar 파일을 app.jar 라는 이름으로 이미지 안에 복사합니다.
COPY ${JAR_FILE} app.jar

# 4. 애플리케이션이 8080 포트를 사용함을 외부에 알립니다.
EXPOSE 8080

# 5. 컨테이너가 시작될 때 이 명령을 실행하여 애플리케이션을 구동합니다.
ENTRYPOINT ["java","-jar","/app.jar"]
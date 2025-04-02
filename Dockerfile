FROM gradle:8-jdk21
WORKDIR /app
EXPOSE 8080
EXPOSE 35729

# 安裝 inotify-tools 用於檔案監控
RUN apt-get update && apt-get install -y inotify-tools

ENV SPRING_DEVTOOLS_REMOTE_SECRET=mysecret
ENV SPRING_DEVTOOLS_RESTART_ENABLED=true

CMD ["./gradlew", "bootRun", "-PspringBoot.run.jvmArguments='-Dspring-boot.run.fork=false'"]
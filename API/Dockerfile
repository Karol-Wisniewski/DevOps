FROM gradle:jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/API
WORKDIR /home/gradle/API
RUN gradle clean build -x test

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /home/gradle/API/build/libs/API-0.0.1-SNAPSHOT.jar /app

# ENV DATABASE_URL="jdbc:postgresql://localhost:5432/devops"
# ENV DATABASE_USERNAME="user"
# ENV DATABASE_PASSWORD="password"


ENTRYPOINT ["java","-jar","API-0.0.1-SNAPSHOT.jar"]
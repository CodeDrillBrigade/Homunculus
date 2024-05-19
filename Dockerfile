FROM gradle:8.0-jdk19 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar -x ktlintMainSourceSetFormat --no-daemon

FROM amazoncorretto:21-alpine3.18
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/homunculus.jar
ENTRYPOINT ["java","-jar","/app/homunculus.jar"]
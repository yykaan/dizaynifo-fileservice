FROM maven:3.5-jdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
ADD /mvn/settings.xml /root/.m2/settings.xml
RUN mvn -f /usr/src/app/pom.xml -Drepo.pwd=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 clean package

FROM gcr.io/distroless/java
LABEL org.opencontainers.image.source="https://github.com/yykaan/tasarlasat-fileservice"
COPY --from=build /usr/src/app/target/fileservice-0.0.1.jar /usr/app/tasarlasat-fileservice-1.0.0.jar
EXPOSE 7000
ENTRYPOINT ["java","-jar","/usr/app/tasarlasat-fileservice-1.0.0.jar"]
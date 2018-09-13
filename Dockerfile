FROM maven:3.5-jdk-8 as builder
COPY . /tmp/src/
RUN mvn -f /tmp/src/pom.xml clean package


FROM openjdk:8-jre-alpine
WORKDIR /app/
COPY --from=builder /tmp/src/target/account-service-1.0.0.jar ./app.jar
EXPOSE 9998
CMD ["java", "-jar", "/app/app.jar"]  
ENTRYPOINT [""]

FROM eclipse-temurin:17-jdk-alpine

LABEL maintainer="bank-kafka-system"
LABEL description="Producer application for bank transfer system"

WORKDIR /app

COPY producer-app/target/producer-app-1.0.0.jar /app/producer-app.jar

COPY producer-app/src/main/resources/application.properties /app/config/application.properties
COPY producer-app/src/main/resources/log4j2.xml /app/config/log4j2.xml

ENV JAVA_OPTS="-Xms256m -Xmx512m -Dlog4j2.configurationFile=file:/app/config/log4j2.xml"
ENV DB_URL=jdbc:postgresql://postgres:5432/bank_db
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres
ENV KAFKA_BOOTSTRAP_SERVERS=kafka1:9092,kafka2:9093,kafka3:9094

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD pgrep -f "producer-app.jar" || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/producer-app.jar"]
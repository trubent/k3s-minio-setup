FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY target/notebook.jar notebook.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENTRYPOINT ["java","--add-opens","java.base/java.lang=ALL-UNNAMED","--enable-preview","-XX:MaxRAMPercentage=75.0","-Djava.security.egd=file:/dev/./urandom","-jar","/notebook.jar"]

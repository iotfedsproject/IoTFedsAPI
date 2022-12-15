FROM openjdk:8-jre-alpine

COPY ./build/libs/IoTFedsAPI-0.0.1-SNAPSHOT.jar ./IoTFedsAPI-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/IoTFedsAPI-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080

CMD java $JAVA_HTTP_PROXY $JAVA_HTTPS_PROXY $JAVA_NON_PROXY_HOSTS -DSPRING_BOOT_WAIT_FOR_SERVICES=symbiote-aam:8080 -jar $(ls *run.jar)
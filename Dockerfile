FROM openjdk:8-jre-alpine

COPY ./build/libs/IoTFedsAPI-1.3.4.jar ./app-run.jar
#ENTRYPOINT ["java", "-jar", "/app-run.jar"]

EXPOSE 8080

CMD java -DSPRING_BOOT_WAIT_FOR_SERVICES=symbiote-aam:8080 -jar $(ls *run.jar)
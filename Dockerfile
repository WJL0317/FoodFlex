FROM openjdk:17
COPY "./target/Restaurante-0.0.1-SNAPSHOT.jar" "app.jar" 
EXPOSE 8122
ENTRYPOINT [ "java","-jar","app.jar" ]
FROM mapps/java8

VOLUME /tmp
ADD mapps-fileservice-1.0.0.jar app.jar
RUN sh -c 'touch app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
FROM mapps/java8

VOLUME /tmp
ADD ${project.name}-${project.version}-build${svn.revision}.jar app.jar
ADD application.yml application.yml.default

RUN sh -c 'touch app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dspring.config.location=./config","-jar","app.jar"]
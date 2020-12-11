FROM java:8

VOLUME /tmp

ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", \
  "-agentlib:jdwp=transport=dt_socket,address=50505,suspend=n,server=y", \
  "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

MAINTAINER Daria Pydorenko <daria.pydorenko@nure.ua>

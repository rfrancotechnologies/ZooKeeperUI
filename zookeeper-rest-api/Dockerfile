FROM java:openjdk-8-jre-alpine

MAINTAINER mediatech <devops@mediatechsolutions.es>

EXPOSE 8080

ADD build/libs/zookeeper-rest-api.jar zookeeper-rest-api.jar
RUN sh -c 'touch /zookeeper-rest-api.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-XX:MaxHeapFreeRatio=30","-XX:MinHeapFreeRatio=10","-jar","/zookeeper-rest-api.jar"]

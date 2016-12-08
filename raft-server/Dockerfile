FROM anapsix/alpine-java
MAINTAINER Sushant Vairagade <sushant.vairagade@sjsu.edu>
COPY target/raft-consensus-1.0-SNAPSHOT.jar /home/node-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/home/node-1.0-SNAPSHOT.jar"]
ENV port = $port
EXPOSE 8080
CMD [" "]
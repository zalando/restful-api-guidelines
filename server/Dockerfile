FROM registry.opensource.zalan.do/stups/openjdk:latest

LABEL MAINTAINER "http://zalando.github.io/"

COPY build/libs/zally-1.0.0.jar /
COPY scm-source.json /scm-source.json

EXPOSE 8080

CMD java -Xmx512m $(appdynamics-agent) -jar /zally-1.0.0.jar

FROM registry.opensource.zalan.do/stups/openjdk:8-52

LABEL MAINTAINER "http://zalando.github.io/"

COPY build/libs/zally-0.0.1.jar /
COPY scm-source.json /scm-source.json

EXPOSE 8080

CMD java -Xmx512m $(appdynamics-agent) -jar /zally-0.0.1.jar

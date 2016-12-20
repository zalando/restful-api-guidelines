FROM registry.opensource.zalan.do/stups/openjdk:8-42

MAINTAINER Zalando SE

ADD build/libs/zally-0.0.1.jar /
ADD scm-source.json /scm-source.json

EXPOSE 8080

CMD java -Xmx512m $(appdynamics-agent) -jar /zally-0.0.1.jar

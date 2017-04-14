FROM registry.opensource.zalan.do/stups/node:7.7-cd45

MAINTAINER "ruben.barilani@zalando.de"

RUN mkdir -p /opt/app
WORKDIR /opt/app

# install dependencies (invalidate cache when package.json change)
COPY ./package.json ./package.json
RUN npm install -q --production

# copy source
COPY ./src ./src

COPY ./scm-source.json /scm-source.json

# Expose default port
EXPOSE 8442

ENTRYPOINT ["npm"]
CMD ["start"]

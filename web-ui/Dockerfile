FROM registry.opensource.zalan.do/stups/node:7.7-cd45

MAINTAINER "ruben.barilani@zalando.de"

RUN mkdir -p /opt/app
WORKDIR /opt/app

# Copy Sources
COPY ./package.json ./package.json
COPY ./src ./src

# Install dependencies
RUN npm install -q --production

COPY ./scm-source.json ./scm-source.json

# Expose default port
EXPOSE 8442

ENTRYPOINT ["npm"]
CMD ["start"]

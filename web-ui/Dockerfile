FROM registry.opensource.zalan.do/stups/node:latest

RUN mkdir -p /opt/app
WORKDIR /opt/app

# Copy Sources
COPY ./package.json ./package.json
COPY ./src ./src
COPY ./scm-source.json ./scm-source.json

# Install dependencies
RUN npm install -q --production

# Expose default port
EXPOSE 8442

ENTRYPOINT ["npm"]
CMD ["start"]

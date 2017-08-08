[![Build Status](https://travis-ci.org/zalando-incubator/zally.svg?branch=master)](https://travis-ci.org/zalando-incubator/zally)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/05a7515011504c06b1cb35ede27ac7d4)](https://www.codacy.com/app/zally/zally?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=zalando-incubator/zally&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/05a7515011504c06b1cb35ede27ac7d4)](https://www.codacy.com/app/zally/zally?utm_source=github.com&utm_medium=referral&utm_content=zalando-incubator/zally&utm_campaign=Badge_Coverage)

<img src="logo.png" width="200" height="200" />

### Zally - Zalando's API Linter loves you

Minimalistic API linter of awesomeness, bringing order to a sea of APIs.

- Ensures API definitions conform to
[our standard REST guidelines](http://zalando.github.io/restful-api-guidelines/).
- Does not require a deployed service, only an API definition.
- Accepts [swagger](https://swagger.io) yaml and json formats.
- Easy-to-use [CLI](cli/README.md) allows you to check your API *right now*.
- [Web UI](web-ui/README.md) provides an intuitive web interface with tons of features.

### Core Technical Concepts

- Parses swagger files using [swagger-parser](https://github.com/swagger-api/swagger-parser)
- Written in Java 8 with [Spring Boot](https://github.com/spring-projects/spring-boot)
- Zally comes with a server which lints your swagger files
- Zally CLI is handy command-line tool, it uses the server in the background
- Zally Web UI is another client build on top of React and Node.js
- Rule changes only have to be applied in the server component

**Tech Stack:** Zally server is written in Java 8 with Spring Boot.
We made rule implementation optional possible in Kotlin.
API-specific code remains in Java 8 due to better integration with Spring Boot.
Further details can be found [here](https://github.com/zalando-incubator/zally/pull/65#issuecomment-269474831).
With Spring 5 we consider using Kotlin also on API side directly.
Zally CLI is implemented in Golang.

### Dependencies

- Java 8 (Server)
- Golang 1.7+ (CLI)
- Node.js 7.6+ (Web-UI)


### Installation and Usage

You can find installation steps in the [Server Readme](server/README.md), [CLI Readme](cli/README.md) and [Web UI Readme](web-ui/README.md).

If you just wanna try it out: first run the server locally, then just use the CLI tool as it is provided.


### Roadmap

For [version 1.2](https://github.com/zalando-incubator/zally/milestone/3) we focus on these main areas:

- Consistency with [Zalando RESTful API Guidelines](http://zalando.github.io/restful-api-guidelines/)
- Referential integrity with [Zalando RESTful API Guidelines](http://zalando.github.io/restful-api-guidelines/)
- New rules
- New quick start script and better integration testing approaches

Feel free to contribute on related issues for version 1.2.

### Quick start guide

```bash
git clone git@github.com:zalando-incubator/zally.git zally
cd zally

# Disable authentication and start a local version of Zally server
cd server
./gradlew clean build
./gradlew bootRun > /dev/null &
cd ..

# Build CLI tool
go get github.com/zalando-incubator/zally/cli/zally
cd $GOPATH/src/github.com/zalando-incubator/zally/cli/zally
go build
./zally lint /path/to/swagger/definition.yaml
```

### Contributing

We are happy to accept contributions. First, take a look at our [contributing guidelines](CONTRIBUTING.md).

Please check our [Project Board](https://github.com/zalando-incubator/zally/projects/1) or [Issues Page](https://github.com/zalando-incubator/zally/issues) for contribution ideas.


### Contact

Feel free to contact one the [maintainers](MAINTAINERS)


### License

MIT license with an exception. See [license file](LICENSE).

[![Build Status](https://travis-ci.org/zalando-incubator/zally.svg?branch=master)](https://travis-ci.org/zalando-incubator/zally)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/05a7515011504c06b1cb35ede27ac7d4)](https://www.codacy.com/app/zally/zally?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=zalando-incubator/zally&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/05a7515011504c06b1cb35ede27ac7d4)](https://www.codacy.com/app/zally/zally?utm_source=github.com&utm_medium=referral&utm_content=zalando-incubator/zally&utm_campaign=Badge_Coverage)

<img src="logo.png" width="200" height="200" />

### Zally - Zalando's API Linter loves you

Minimalistic API linter of awesomeness, bringing order to a sea of APIs.

- Ensures API definitions conform to
[our standard REST guidelines](http://zalando.github.io/restful-api-guidelines/).
- Does not require a deployed service, only an API definition.
- Accepts [swagger](swagger.io) yaml and json formats.
- Easy-to-use [CLI](cli/README.md) allows you to check your API *right now*.
- Github integration to automatically lint your API anytime it changes in a Pull Request.


###Core Technical Concepts

- Parses swagger files using [swagger-parser](https://github.com/swagger-api/swagger-parser)
- Written in Java 8 with [Spring Boot](https://github.com/spring-projects/spring-boot)
- Zally comes with a server which lints your swagger files
- Zally CLI is handy command-line tool, it uses the server in the background
- Rule changes only have to be applied in the server component

**Tech Stack:** Zally server is written in Java 8 with Spring Boot. We made rule implementation
optional possible in Kotlin. API-specific code remains in Java 8 due to better integration with
Spring Boot. Further details can be found [here](https://github.com/zalando-incubator/zally/pull/65#issuecomment-269474831).


### Dependencies

- Java 8


### Installation and Usage

You can find installation steps in the [Server Readme](server/README.md) and [CLI Readme](cli/README.md).

If you just wanna try it out: first run the server locally, then just use the cli tool as it is provided.


### Roadmap

For version 1.0 we focus on these three main areas:

- improve rule implementations
- improve CLI user experience
- improve web UI

Afterwards we focus on Github integration and more sophisticated features


### Contributing

We are happy to accept contributions. First, take a look at our [contributing guidelines](CONTRIBUTING.md).

You can see our current status in [this task board](https://github.com/zalando-incubator/zally/projects/1).


### TODO

Please check the [Issues Page](https://github.com/zalando-incubator/zally/issues)
for contribution ideas.


### Contact

Feel free to contact one the [maintainers](MAINTAINERS)


### License

MIT license with an exception. See [license file](LICENSE).

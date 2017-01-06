[![Build Status](https://travis-ci.org/zalando-incubator/zally.svg?branch=master)](https://travis-ci.org/zalando-incubator/zally)

    __________      .__  .__
    \____    /____  |  | |  | ___.__.
      /     /\__  \ |  | |  |<   |  |
     /     /_ / __ \|  |_|  |_\___  |
    /_______ (____  /____/____/ ____|
            \/    \/          \/

### Zally - Zalando's API Linter loves you

Minimalistic API linter of awesomeness, bringing order to a sea of APIs.

- Ensures API definitions conform to
[our standard REST guidelines](http://zalando.github.io/restful-api-guidelines/).
- Does not require a deployed service, only an API definition.
- Accepts [swagger](swagger.io) yaml and json formats.
- Easy-to-use CLI allows you to check your API *right now*.
- Github integration to automatically lint your API anytime it changes in a Pull Request.


###Core Technical Concepts

- Parses swagger files using [swagger-parser](https://github.com/swagger-api/swagger-parser)
- Written in Java 8 with [Spring Boot](https://github.com/spring-projects/spring-boot)
- Zally comes with a server which lints your swagger files
- Zally CLI is handy command-line tool, it uses the server in the background
- Rule changes only have to be applied in the server component


### Dependencies

- Java 8


### Installation

1. Clone this repository locally

2. Download dependencies and build `Zally`:
    ```bash
    cd server
    ./gradlew build
    ```

### Running Zally locally

1. [Optional] Disable OAuth2 authentication for testing purposes (go to server subfolder, first):
    ```bash
    echo "spring.profiles.active=dev" > application.properties
    ```

2. Run Zally server using:
    ```bash
    cd server
    ./gradlew bootRun
    ```

### Checking Zally API

1. Generate OAuth2 token if needed

2. Wrap your Swagger file inside `api_definition` JSON object and save it as `test.json` file:
    ```json
    {
      "api_definition": {
            "swagger": "2.0",
            "info": {
                "version": "1.0.0",
                "title": "Swagger Petstore",
                "description": "A sample API that uses a petstore as an example to demonstrate features in the swagger-2.0 specification",
                "termsOfService": "http://swagger.io/terms/",
                "contact": {
                    "name": "Swagger API Team"
                },
                "license": {
                    "name": "MIT"
                }
            }
        }
    }
    ```

3. Run `curl` agains the API:
    ```bash
    curl -X POST \
        -H "Authorization: Bearer OAUTH2_TOKEN" \
        -H "Content-Type: application/json" \
        --data "@test.json" \
        localhost:8080/api-violations
    ```

### Command-line interface

Check CLI tool documentation in [CLI Readme](cli/README.md)


### Integrating Zally with Github

- TODO: add github integration instructions


### Contributing

We are happy to accept contributions. First, take a look at our [contributing guidelines](CONTRIBUTING.md).


### TODO

Please check the [Issues Page](https://github.com/zalando-incubator/zally/issues)
for contribution ideas.


### Contact

Feel free to contact one the [maintainers](MAINTAINERS)


### License

MIT

[![Build Status](https://travis-ci.org/zalando-incubator/zally.svg?branch=master)](https://travis-ci.org/zalando-incubator/zally)

    __________      .__  .__
    \____    /____  |  | |  | ___.__.
      /     /\__  \ |  | |  |<   |  |
     /     /_ / __ \|  |_|  |_\___  |
    /_______ (____  /____/____/ ____|
            \/    \/          \/

### Zally

Minimalistic API linter of awesomeness, bringing order to a sea of APIs.

- Ensures API definitions conform to
[our standard REST guidelines](http://zalando.github.io/restful-api-guidelines/) without needing manual reviews.
- Does not require a deployed service, only an API definition.
- Accepts [swagger](swagger.io) yaml and json formats.
- Easy-to-use CLI allows you to check your API *right now*.
- Github integration to automatically lint your API anytime it changes in a Pull Request.

###Core Technical Concepts

- Parses swagger files using [swagger-parser](https://github.com/swagger-api/swagger-parser)
- Written in Java 8 with [Spring Boot](https://github.com/spring-projects/spring-boot)

###Dependencies
- Java 8
- TODO: confirm no other dependencies

###Using the CLI
- TODO: add CLI usage instructions

###Integrating Zally with Github
- TODO: add github integration instructions

###Contributing
We are happy to accept contributions. First, take a look at our [contributing guidelines](CONTRIBUTING.md).

### How to test Zally API

1. Generate OAuth2 token

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
        localhost:8080/api_violations
    ```


### TODO
- Finish CLI
- Finish github integration

### Contact
Feel free to contact one the [maintainers](MAINTAINERS)

###License
MIT

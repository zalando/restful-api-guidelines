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
Include any essential instructions for:
- Getting it
- Installing It
- Configuring It
- Running it

###More Specific Topics (+ sample sub-categories)
- Versioning: Services, APIs, Systems
- Common Error Messages/related details
- Tests
- Is it a Swift project? Please take a look at Mattt Thompson & Nate Cook's [Swift documentation](http://nshipster.com/swift-documentation/) guide

###Contributing
- Contributor Guidelines
- Code Style/Reqts
- Format for commit messages
- Thank you (name contributors)

### TODO
- Next steps
- Features planned
- Known bugs (shortlist)

### Contact
- Email address
- Google Group/mailing list (if applicable)
- IRC or Slack (if applicable)

###License

## How to test

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
    ```

3. Run `curl` agains the API:
    ```bash
    curl -X POST \
        -H "Authorization: Bearer OAUTH2_TOKEN" \
        -H "Content-Type: application/json" \
        --data "@test.json" \
        localhost:8080/api_violations
    ```

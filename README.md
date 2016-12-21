# Zally

Zalando's API linter service, to ensure APIs conform to
[our guidelines](http://zalando.github.io/restful-api-guidelines/).

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

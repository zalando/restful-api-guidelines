# Zally Server

This is Zally server: it implements all rule checks and offers an API to request
an API check.

## Installation

1. Clone Zally repository

2. Switch to `server` folder:
	```bash
	cd server
	```

3. Build the server:
    ```bash
    ./gradlew build
    ```

4. [Optional] Disable OAuth2 authentication for testing purposes:
    ```bash
    echo "spring.profiles.active=dev" > application.properties
    ```

5. Run Zally server using:
    ```bash
    ./gradlew bootRun
    ```

## Usage

We recommend to use [Zally's CLI tool](../cli). Otherwise you can use any tool to
issue an HTTP request, see below for details.

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

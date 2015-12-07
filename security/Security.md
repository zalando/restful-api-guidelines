# Security

## {{ book.must }} Secure endpoints with OAuth 2.0

Every API endpoint needs to be secured using OAuth 2.0. Please refer to the 
[official Swagger spec](https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#security-definitions-object)
on how to specify security definitions in you API specification.

## {{ book.must }} Define/assign scopes

Every API needs to define scopes and every endpoint needs to have a at least scope assigned. Please refer to this 
[example](https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#security-definitions-object-example)
on how to define scopes.

APIs that don't require a specific scope have to fallback on requiring the `uid` scope.
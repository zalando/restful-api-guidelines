# API Discovery

## {{ book.must }} Applications Must Provide Online Access to Their API (Swagger) Definitions

In our dynamic and complex service infrastructure, it is important to provide a central place with
online access to the API definitions of all running applications. All service applications must
provide the following two API endpoints:

* endpoint(s) for GET access on its API OpenAPI definition(s), for instance
  `https://example.com/swagger.json` or `https://example.com/swagger.yaml`.
* “Twintip” discovery endpoint `https://example.com/.well-known/schema-discovery` that delivers
  the OpenAPI definition endpoint(s) above (see the link below for a description of its format).

Note, these discovery endpoints have to be supported but need not be part of the OpenAPI definition as there is no API specific information in their description.

Background: [Twintip](http://docs.stups.io/en/latest/components/twintip.html) is an API definition
crawler of the STUPS infrastructure; it checks all running applications via the endpoint above and
stores the discovered API definitions. Twintip itself provides a RESTful API as well as an
API Viewer (Swagger-UI) for central access to all discovered API definitions.

For the time being, this document is an appropriate place to mention this rule, even though it is
not a RESTful API definition rule or related to our STUPS infrastructure for application service
management.

## {{ book.should }} Enable environment specific host configuration

With [Twintip](http://docs.stups.io/en/latest/components/twintip.html) we also provide a central
Swagger UI installation from which every API can be used. Every service which implements an API
should therefore support environment specific host configuration in its OpenAPI definition:
the endpoint where the service is running should express the actual environment, e.g. for a
staging system it should be the endpoint of the service in this staging system and not the one for
production - and vice versa.

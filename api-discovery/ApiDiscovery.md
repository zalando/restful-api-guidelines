# API Discovery

## {{ book.must }} Applications Must Provide Online Access to Their API (Swagger) Definitions

In our dynamic and complex service infrastructure, it is important to provide a central place with
online access to the API definitions of all running applications. All service applications must
provide the following two API endpoints:

* endpoint(s) for GET access on its API OpenAPI definition(s), for instance
  `https://myapi.com/swagger.json`. Either .json or .yaml formats are supported.
* “Twintip” discovery endpoint `https://myapi.com/.well-known/schema-discovery` that delivers
  the OpenAPI definition endpoint(s) above

Note, these discovery endpoints have to be supported but need not be part of the OpenAPI definition as they do not provide API specific information.

Background: [Twintip](http://docs.stups.io/en/latest/components/twintip.html) is an API definition
crawler of the STUPS infrastructure; it checks all running applications via the endpoint above and
stores the discovered API definitions. Twintip itself provides a RESTful API as well as an API Viewer (OpenAPI GUI) for central access to all discovered API definitions.

For the time being, this document is an appropriate place to mention this rule, even though it is
not a RESTful API definition rule or related to our STUPS infrastructure for application service
management.

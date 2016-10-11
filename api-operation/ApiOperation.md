# API Operation

## {{ book.must }} Provide Online Access to OpenAPI Reference Definition

All service applications must support access to the OpenAPI Reference Definitions of their external APIs — it 
is optional for internal APIs — via the following two API endpoints:  

* endpoint(s) for GET access on its OpenAPI definition(s), for instance
  `https://example.com/swagger.json` or `https://example.com/swagger.yaml`.
* “Twintip” discovery endpoint `https://example.com/.well-known/schema-discovery` that delivers
  the OpenAPI definition endpoint(s) above (see the link below for a description of its format).

Hint: Though discovery endpoints have to be supported, they should not be specified in the OpenAPI definition 
as they are generic and provide no API specific information.

We distinguish between internal and external APIs of an application which is owned by a specific team and often 
implemented via small set of services. An external API is used by clients outside the team - usually 
another application owned by a different team or even an external business partner user of our platform. 
An internal API is only used within the application and only by the owning team, for instance, 
for operational or implementation internal purposes. 

Background: In our dynamic and complex service infrastructure, it is important to provide API client 
developers a central place with online access to the OpenAPI reference definitions of all running applications. 
[Twintip](http://docs.stups.io/en/latest/components/twintip.html) is an API definition
crawler of the Zalando platform infrastructure; it checks all running applications via the endpoint above and
stores the discovered API definitions. Twintip itself provides a RESTful API as well as an
API Viewer (Swagger-UI) for central access to all discovered API definitions.

Editorial: For the time being, this document is an appropriate place to mention this rule, even though it is
not a RESTful API definition rule but related to service implementation obligations to support client developer API discovery. 


Further reading:

* [Library to make your Spring Boot service crawlable via Twintip](https://github.com/zalando-stups/twintip-spring-web)

## {{ book.should }} Monitor API Usage
Owners of APIs used in production should monitor API service to get information about its using clients.
This information, for instance, is useful to identify potential review partner for API changes.

Hint: A preferred way of client detection implementation is by logging of the client-id retrieved from the OAuth token.


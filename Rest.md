
Naming Syntax
[M] Path segments must be lowercase separate words with hyphens
For instance, /shipment-orders/{shipment-order-id}. 
[M] Query parameters must be snake_case (never camelCase)
Examples: customer_number, order_id, billing_address
[M] JSON field names must be snake_case (never camelCase)
Intentionally consistent with the query parameters.
Examples see above (Query Parameters).
Why: It’s essential to establish a  consistent  look and feel. No established industry standard exists, but many popular Internet companies prefer snake_case:  e.g. GitHub, Stack Exchange, Twitter. Others, like Google and Amazon, use both — but not only camelCase.
[M] You Must Hyphenate HTTP Headers  
[S] Prefer Hyphenated-Pascal-Case for HTTP header Fields 
This is for consistency in your documentation (most other headers follow this convention). Avoid camelCase (without hyphens). Exceptions are common abbreviations like “ID.” 
Examples: Accept-Encoding, Apply-To-Redirect-Ref, Disposition-Notification-Options, Original-Message-ID
See also: HTTP Headers are case-insensitive (RFC 7230).

Headers
[C] Use Standardized Headers
Use this list and mention its support in your Swagger API definition.
URLs
[M] Always Pluralize Resource Names 
This is so that APIs can handle a collection of entities where 1 is a special case.
[M] Identify resources and Sub-Resources ia Path Segments
Basic URL structure:
/{resources}/[resource-id]/{sub-resources}/[sub-resource-id]
Examples:
/carts/1681e6b88ec1/items
/carts/1681e6b88ec1/items/1
[C] Consider Using  Nested URLs When the Access Pattern Allows
If a sub-resource is only accessible via its parent resource, consider using a nested URL structure:
/carts/1681e6b88ec1/cart-items/1
If the resource can be accessed directly, however, then the API should expose it as a top-level resource. For example, sales order items do not exist without sales orders, but some services may choose to access them directly. Orders do not exist without customers — yet it makes sense to access orders directly, without looking up the customer number.
[C] First Path segment May be /api
In most cases, all resources provided by a service are part of the public API, and therefore should be made available under the root “/” base path. If  the service should also support non-public, internal APIs — for specific operational support functions, for example — add “/api” as base path to clearly separate public and non-public API resources.
[M] Avoid Trailing Slashes
The trailing slash must not have specific semantics. Resource paths must deliver the same results whether they have the trailing  slash or not.  

Query strings
[C] Use Conventional Query Strings
If you provide query support for sorting, pagination, filtering functions or other actions, use the following standardized naming conventions: 
q — default query parameter (so that its TAB completed from browsers), may have an entity specific alias, like sku
limit — to restrict the number of entries. See Pagination section below. 
Hint: You can use size as an alternate query string.
cursor — key-based page start. See Pagination section below. 
offset — numeric offset page start. See Pagination section below.  
Hint: In combination with limit, you can use page as an alternative to offset. 
sort — comma-separated list of fields to sort. Means “descending.”
fields — to retrieve a subset of fields. See Support Filtering of Resource Fields below 
expand — to expand embedded entities (ie.: inside of an article entity, expand silhouette code into the silhouette object). Implementing “expand” correctly is difficult, so do it with care. 
[C] Use Application-Specific Content Types
For instance, application/x.zalando.article+json.For complex types, it’s better to have a specific content type. For simple use cases this isn’t necessary.
We can attach version info to media type names and support content negotiation to get different representations, e.g. application/x.zalando.article+json;version=2.

HTTP Methods
[M] Use HTTP Methods Correctly
See Best Practices
[M] Use Meaningful HTTP Status Codes
See Best Practices
Reducing Bandwidth Needs and Improving Responsiveness
APIs should support techniques for reducing bandwidth based on client needs.
This holds for APIs that (might) have high payloads and/or are used in high-traffic scenarios like the public Internet and telecommunication networks. Typical examples are APIs used by mobile web app clients with (often) less bandwidth connectivity. (Zalando is a “Mobile “First” company, so be mindful of this point.)  

Common techniques include: 
gzip compression
querying field filters to retrieve a subset of resource attributes
ETag (and if-[None-]Match) headers to avoid refetch of unchanged resources
pagination for incremental access of larger (result) lists
Each of these items is described in greater detail below. .
[S] gzip Compression
Compress the payload of your API’s responses with gzip (GNU zip), unless there’s a good reason not to — for example,  you are serving so many requests that the time to compress becomes a bottleneck. This helps to transport data faster over the network ( fewer bytes) and makes frontends respond faster.
Though gzip compression might be the default choice for server payload, the server should also support payload without compression. The client may activate or deactivate via Accept-Encoding header server compression of payload -- ee also RFC 7231 Section 5.3.4. The server should indicate used gzip compression via the Content-Encoding header. 
[S] Support Filtering of Resource Fields
Depending on your use case and payload size, you can significantly reduce network bandwidth load by allowing the client to select a subset of fields to be returned using the fields query parameter. See Google AppEngine API's partial response example. 
Hint: Swagger doesn't allow you to formally specify  whether depending on a given parameter will return different parts of the specified result schema. Explain this in English in the parameter description.
[C] Support the ETag Header
If a resource changes, the contents of the ETag header must also change. Combined with the If-Match and If-None-Match headers, the ETag header allows for:
caching of entities
optimistic locking
Its possible contents:
the entity’s version number
hash of the response body
hash of the entity’s last modified field

[M] Support Pagination
For batch processing, and for [verb] the client iteration experience, access to lists of data items must support pagination. This holds for all lists that are (potentially) larger than just a few hundred entries. 
There are two page iteration techniques:
Offset/Limit-based pagination: numeric offset identifies the first page entry
Cursor-based — aka key-based — pagination: a unique key element identifies the first page entry (see also Facebook’s guide)


[S] Prefer Cursor-Based Pagination, Avoid Offset-Based Pagination 
Cursor-based pagination is better for more efficient implementation, especially when it comes to high-data volumes and/or storage in NoSQL databases. 
Before choosing offset-based pagination,  make sure that it is feasible for efficient realization. Carefully consider the following trade-offs:
Usability/framework support
offset/limit pagination is more familiar than cursor-based, so it has more framework support and is easier to use for API clients 
Use case: Jump to a certain page
If jumping to a particular page in a range (e.g., 51 of 100) is a required use case, keyset-navigation is not feasible
Variability of data may lead to anomalies in result pages
Using offset will create duplicates or lead to missed entries if rows are inserted or  deleted, respectively, between fetching two pages 
When using cursor-based pagination, paging cannot continue when the cursor entry has been deleted while fetching two pages
Performance considerations
Efficient server =side processing using offset-based pagination is hardly feasible for 
higher data list volumes, especially if they do not reside in the database’s main memory
sharded or NoSQL data storage systeThis also holds for total count and backward iteration support
Further reading: 
Twitter
Use the Index, Luke

[C] Use Pagination Headers Where Applicable
Set X-Total-Count to send back the total count of entities. 
Set the link headers to provide information to the client about subsequent paging options. For example:
Link: <http://catalog-service.zalando.net/articles?cursor=62726863268328&limit=100>; rel="next"; title="next chunk of articles"
or 
Link: <http://catalog-service.zalando.net/articles?offset=5&limit=100>; rel="prev"; title="previous chunk of articles"
Link: <http://catalog-service.zalando.net/articles?offset=205&limit=100>; rel="next"; title="next chunk of articles"


Possible relations: next, prev, last, first

Hypermedia
[S] Use HATEOAS
This is a challenging topic. We don’t have specific recommendations yet; we expect them to emerge from trial and error, and from the API Guild’s ongoing research. For now, we strongly encourage teams to apply this principle and document results. /
Notes on aspects to be analyzed:
Security concerns with respect to  passing OAuth2 tokens 
API discovery by client programmers vs. automated hypermedia driven workflows
Additional development work vs. reduced refactoring work
Additional resources: Wikipediahttp://en.wikipedia.org/wiki/HATEOAS,
Spring.iohttps://spring.io/understanding/HATEOAS, The RESTful CookBook
[C] Use URIs for Custom Link Relations

{
  “_links”: {
    “https://docs.team.zalan.do/rels/my-entity”: [{
      “href”: “https://service.team.zalan.do/my-entities/123”
    }]
  }
}
[S] Consider Using a Standard for Links
For HTTP Link headers, consider using a format like HAL, JSON-LD with Hydra, or Siren. For a comparison of these hypermedia types, see Kevin Sookocheff’s post.

Data Formats
[M] Use JSON as the Body Payload
JSON- encode the body payload. The JSON payload must follow RFC-7158 and be compatible with RFC-4627 clients by having (if possible) a serialized object or array as the top-level object.
[M] Use Standard Date and Time Formats
Inside the JSON payload, use the date and time formats defined by RFC 3339 — e.g. 2015-05-28T14:09:17+02:00 for a point in time (note that the  Swagger format "date-time" corresponds to "date-time" in the RFC) and 2015-05-28 for a date (note that the Swagger format "date" corresponds to "full-date" in the RFC). Both are specific profiles, a subset of the international standard ISO 8601.
In HTTP headers (including the proprietary headers), use the HTTP date format defined in RFC 7231.
Timestamps are passed in UTC-related format via APIs and should be stored in UTC (i.e. without any offset information). Localization based on UTC should be done by the services that provide user interfaces, if required.
[C] Use Standard Data Formats

ISO 3166-1-alpha2 country codes

(It is “GB”, not “UK”, even though “UK” has seen some use at Zalando)
ISO 639-1 language code

BCP-47 (based on ISO 639-1) for language variants

ISO 4217 currency codes

Common Data Objects
Definitions of data objects that are good candidates for wider usage:[S] Use a Common Money Object
Use the following common money structure: 

  Money:
    type: object
    properties:
      amount:
        type: number
        format: decimal
        example: 99.95
      currency:
        type: string
        format: iso-4217
        example: EUR
    required:
      - amount
      - currency

Make sure that you don’t convert the “amount” field to float/double types when implementing this interface in a specific language or when doing calculations. Otherwise, you might lose precision. Instead, use exact formats like Java’s BigDecimal. See Stack Overflow for more info.
Some JSON parsers (NodeJS’s, for example) convert numbers to floats by default. After discussing the pros and cons, we’ve decided on "decimal" as our amount format. It is not a standard Swagger format, but should help us to avoid parsing numbers as float/doubles. 

[S] Use Common Address Fields
Address structures play a role in different functional and use-case contexts, including country variances. The address structure below should be sufficient for most of our business-related use cases. Use it in our APIs, unless it does not fit to required functionality and need — preferable compatible extension — changes:

address:
    description:
      a common address structure adequate for many use cases
    type: object
    properties:
      salutation:
        type: string
        description: A salutation and/or title which may be used for personal contacts
        example: Mr
      first_name:
        type: string
        description: given name(s) or first name(s) of a person; may also include the middle names
        example: Hans Dieter
      last_name:
        type: string
        description: family name(s) or surname(s) of a person
        example: Mustermann
      business_name:
        type: string
        description: company name of the business organization
        example: Consulting Services GmbH
      street:
        type: string
        description: full street address including house number and street name
        example: Schönhauser Allee 103
      additional:
        type: string
        description: further details like suite, apt #, etc.
        example: 2. Hinterhof rechts
      city:
        type: string
        description: name of the city
        example: Berlin
      zip:
        type: string
        description: Zip code or postal code
        example: 14265
      country_code:
        type: string
        format: iso-3166-1-alpha-2
        example: DE
    required:
      - first_name
      - last_name
      - street
      - city
      - zip
      - country_code

[M] Use Common Error Return Objects
application/x.problem+json is an  example of a common error return object derived from an Internet draft specification for error objects that have never reached registered media type status. The  media type name is prefixed with “x.” to denote its status. 
APIs may define custom problems types with extension properties, according to their specific needs.  Go here for a more detailed explanation (IAM login required). 
Problem:
 type: object
 properties:
   type:
     type: string
     format: uri
     description: |
       An absolute URI that identifies the problem type.  When dereferenced,
       it SHOULD provide human-readable documentation for the problem type
       (e.g., using HTML).
     example: http://httpstatus.es/503
   title:
     type: string
     description: |
       A short, summary of the problem type. Written in english and readable 
       for engineers (usually not suited for non technical stakeholders and 
       not localized); example: Service Unavailable
   status:
     type: integer
     format: int32
     description: |
       The HTTP status code generated by the origin server for this occurrence
       of the problem.
     example: 503
   detail:
     type: string
     description: |
       A human readable explanation specific to this occurrence of the
       problem.
     example: Connection to database timed out
   instance:
     type: string
     format: uri
     description: |
       An absolute URI that identifies the specific occurrence of the problem.
       It may or may not yield further information if dereferenced.
 required:
   - type
   - title
   - status


(!) An error message must not contain the stack trace. The reasons:
The API would leak implementation details to the outside world. Other teams would possibly rely on the stack trace and a refactoring would be hard.
Brand partners and other third parties receiving the  API,might receive leaked, sensitive information.
Common Headers 
This section shares definitions of proprietary headers that should be named consistently because they address overarching service-related concerns . Whether services support these concerns or not is  optional; therefore, the Swagger API specification is the right place to make this explicitly visible. Use the parameter definitions of the resource HTTP methods. 
Header field name
Type
Description
Example
X-Flow-Id
String
The flow id of the request, which is written into the logs and passed to called services. Helpful for operational troubleshooting and log analysis. 
X-Flow-Id: GKY7oDhpSiKY_gAAAABZ_A
X-App-Domain
Integer
The app domain (i.e. shop channel context) of the request.
X-App-Domain: 16
X-Tenant-Id
String
The tenant id for future platform multitenancy support. 
Should not be used unless new platform multitenancy is truly supported. Must be validated for external retailer, supplier, etc. tenant users via OAuth2; details in clarification. Currently only used by New Platform Prototyping services.
X-Tenant-Id: Zalando-Fashion-Store

Remember that HTTP header fields are not case-sensitive.
API Discovery
[M] Applications Must Provide Online Access to Their API (Swagger) Definitions 
In our dynamic and complex service infrastructure, it is important to provide a central place with online access to the API definitions of all running applications. All service applications must provide the following two API endpoints:
endpoint(s) for GET access on its API Swagger definition(s), for instance https://twintip.stups.zalan.do/swagger.json
Either .json or .yaml formats are supported. 
“Twintip” discovery endpoint <myapp.myteam.zalan.de>/.well-known/schema-discovery that delivers the API Swagger definition endpoint(s) above 
Background: Twintip is an API definition crawler of the STUPS infrastructure; it checks all running applications via the endpoint above and locally stores the discovered API definitions. Twintip itself provides a RESTful API as well as an API Viewer (Swagger GUI) for central access to all discovered API definitions.
For the time being, this document is an appropriate place to mention this rule, even though it is not a RESTful API definition rule or related to our STUPS infrastructure for application service management. 
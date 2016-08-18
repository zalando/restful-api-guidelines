# Hypermedia

## {{ book.must }} Use REST Maturity Level 2

We strive for a good implementation of [REST Maturity Level 2](http://martinfowler.com/articles/richardsonMaturityModel.html#level2) as it enables
us to build resource-oriented APIs that make full use of HTTP verbs and status codes.
You can see this expressed by many rules throughout these guidelines, e.g.:
- [Avoid Actions — Think About Resources](../resources/Resources.md#must-avoid-actions-—-think-about-resources)
- [Keep URLs Verb-Free](../resources/Resources.md#must-keep-urls-verbfree)
- [Use HTTP Methods Correctly](../http/Http.md#must-use-http-methods-correctly)
- [Use Meaningful HTTP Status Codes](../http/Http.md#must-use-meaningful-http-status-codes)

Although this is not HATEOAS, it should not prevent you from designing proper link relationships in your APIs as stated in rules below.

## {{ book.could }} Use REST Maturity Level 3 - HATEOAS

We do not generally recommend to implement [REST Maturity Level 3](http://martinfowler.com/articles/richardsonMaturityModel.html#level3). HATEOAS comes with additional API complexity without real value in our SOA context where client and server interact via REST APIs and provide complex business functions as part of our e-commerce platform.

Our major concerns regarding the promised advantages of HATEOAS (see also [RESTistential Crisis over Hypermedia APIs](https://www.infoq.com/news/2014/03/rest-at-odds-with-web-apis for detailed discussion), [Why I Hate HATEOAS](https://jeffknupp.com/blog/2014/06/03/why-i-hate-hateoas/) and others):
- We follow API First principle with APIs explicitly defined outside the code with standard specification language. HATEOAS does not really add value for SOA client engineers in terms of API self-descriptiveness: a client anyway finds necessary links and description how to use methods on endpoints depending on its state in the API definition.
- Generic HATEOAS clients which need no prior knowledge about APIs and explore API capabilities based on hypermedia information provided, is a theoretical concept that we haven't seen working in practise and does not fit to our SOA set-up. The OpenAPI description format (and tooling based on OpenAPI) doesn't provide sufficient support for HATEOAS either.
- In practice relevant HATEOAS approximations (e.g. following specifications like HAL or JSON API) support API navigation by abstracting from URL endpoint and HTTP method aspects via link types. So, Hypermedia does not prevent clients from required manual changes when domain model changes over time.
- Hypermedia make sense for humans, less for SOA machine clients. We would expect use cases where it may provide value more likely in the frontend and human facing service domain.
- Hypermedia does not prevent API clients to implement shortcuts and directly target resources without 'discovering' them

However, we do not forbid HATEOAS; you could use it, if you checked its limitations and still see clear value for your usage scenario that justifies its additional complexity. If you use HATEOAS please share experience and present your findings in the [API Guild \[internal link\]](https://techwiki.zalando.net/display/GUL/API+Guild).

## {{ book.must }} Use a well-defined subset of HAL

Links to other resources must be defined exclusively using [HAL](http://stateless.co/hal_specification.html) and
preferably using standard [link relations](http://www.iana.org/assignments/link-relations/link-relations.xml).

Clients and Servers are required to support `_links` with its `href` and `rel` attributes, not only at the root level
but also in nested objects. To reduce the effort needed by clients to process hypertext data from servers it's not recommended to serve data with CURIEs, URI templates or embedded resources. Nor is it required to support the HAL media type `application/hal+json`. The following snippet specifies the HAL link structure in JSONSchema:

```yaml
Link:
  type: object
  properties:
    href:
      type: string
      format: uri
      example: https://api.example.com/sales-orders/10101058747628
  required:
    - href
Links:
  type: object
  description: |
    Values can be either a single Link or an array of Links
    See https://docs.pennybags.zalan.do/ for more.
  additionalProperties:
    # keys are link relations
    type: array
    items:
      $ref: '#/definitions/Link'
  example:
    'self':
      - href: https://api.example.com/sales-orders/10101058747628
```

We opted for this subset of HAL after conducting a comparison of different hypermedia formats based on properties like:

* Simplicity: resource link syntax and concepts are easy to understand and interpret for API clients.
* Compatibility: introducing and adding links to resources is not breaking existing API clients.
* Adoption: use in open-source libraries and tools as well as other companies
* Docs: degree of good documentation

<p></p>

| Standard                                                       | Simplicity | Compatibility | Adoption | Primary Focus           | Docs |
|----------------------------------------------------------------|------------|---------------|----------|-------------------------|------|
| HAL Subset                                                     | ✓          | ✓             | ✓        | Links and relationships | ✓    |
| [HAL](http://stateless.co/hal_specification.html)              | ✗          | ✓             | ✓        | Links and relationships | ✓    |
| [JSON API](http://jsonapi.org/)                                | ✗          | ✗             | ✓        | Response format         | ✓    |
| [JSON-LD](http://json-ld.org/)                                 | ✗          | ✓             | ?        | Link data               | ?    |
| [Siren](https://github.com/kevinswiber/siren)                  | ✗          | ✗             | ✗        | Entities and navigation | ✗    |
| [Collection+JSON](http://amundsen.com/media-types/collection/) | ✗          | ✗             | ✗        | Collections and queries | ✓    |

We define HAL links to be extensible, i.e. to contain additional properties if needed. For consistency extensions should reuse attributes from the [`Link` header](https://tools.ietf.org/html/rfc5988#section-5). 

Interesting articles for comparisons of different hypermedia formats:
* [Kevin Sookocheff’s On choosing a hypermedia type for your API](http://sookocheff.com/post/api/on-choosing-a-hypermedia-format/)
* [Mike Stowe's API Best Practices: Hypermedia](http://blogs.mulesoft.com/dev/api-dev/api-best-practices-hypermedia-part-3/)

## {{ book.must }} Do Not Use Link Headers with JSON entities

We don't allow the use of the [`Link` Header defined by RFC 5988](http://tools.ietf.org/html/rfc5988#section-5)
in conjunction with JSON media types, and favor [HAL](#must-use-hal) instead. The primary reason is to have a consistent
place for links as well as the better support for links in JSON payloads compared to the uncommon link header syntax.

## {{ book.could }} Use Custom Link Relations

You should consider using a custom link relation if and only if standard [link relations](http://www.iana.org/assignments/link-relations/link-relations.xml)
are not sufficient to express a relation.
Related or even embedded (sub-) resources should be linked via “meta link attributes” within the response payload; use
the following [HAL](http://stateless.co/hal_specification.html) compliant syntax:

    {
      ...
      “_links”: {
        “my-entity”: {
          “href”: “https://service.team.zalan.do/my-entities/123”
        }
      }
    }

In earlier versions of this rule we proposed URIs but dropped that in favor of readability. Since link relations are
properties in the `_links` object you should define and describe them individually in the API specification. 

## {{ book.should }} Allow Optional Embedding of Sub-Resources

Embedding related resources (also know as *Resource expansion*) is a great way to reduce the number of requests. In
cases where clients know upfront that they need some related resources they can instruct the server to prefetch that
data eagerly. Whether this is optimized on the server, e.g. a database join, or done in a generic way, e.g. an HTTP
proxy that transparently embeds resources, is up to the implementation.

See [*Conventional Query Strings*](../naming/Naming.md#could-use-conventional-query-strings) for naming.

## {{ book.must}} Modify the Content-Type for Embedded Resources

Embedded resources requires to change the media type of the response accordingly:

```http
GET /order/123?embed=(items) HTTP/1.1
Accept: application/x.order+json
```

```http
HTTP/1.1 200 OK
Content-Type: application/x.order+json;embed=(items)
```

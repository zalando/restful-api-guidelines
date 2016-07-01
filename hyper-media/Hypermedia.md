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
- Generic HATEOAS clients which need no prior knowledge about APIs and explore API capabilities based on hypermedia information provided, is a theoretical concept that we haven't seen working in practise and does not fit to our SOA set-up. Our whole internal tooling around APIs like Twintip isn't adjusted for HATEOAS neither.
- In practice relevant HATEOAS approximations (e.g. following specifications like HAL or JSON API) support API navigation by abstracting from URL endpoint and HTTP method aspects via link types. So, Hypermedia does not prevent clients from required manual changes when domain model changes over time.
- Hypermedia make sense for humans, less for SOA machine clients. We would expect use cases where it may provide value more likely in the frontend and human facing service domain. 
- Hypermedia does not prevent API clients to implement shortcuts and directly target resources without 'discovering' them

However, we do not forbid HATEOAS; you could use it, if you checked its limitations and still see clear value for your usage scenario that justifies its additional complexity. If you use HATEOAS please share experience and present your findings in the [API Guild \[internal link\]](https://techwiki.zalando.net/display/GUL/API+Guild).

## {{ book.could }} Use URIs for Custom Link Relations

Related or even embedded (sub-) resources should be linked via “meta link attributes” within the response payload; use here the following [HAL](http://stateless.co/hal_specification.html) compliant syntax:

    {
      ...
      “_links”: {
        “https://docs.team.zalan.do/rels/my-entity”: [{
          “href”: “https://service.team.zalan.do/my-entities/123”
        }]
      }
    }

Or the [JSON API](http://jsonapi.org/) compliant one where you also embed the entities directly:

    {
      ...
      “relationships”: {
        "my-entities": {
          "data": [
            {"id": 1, "type": "my-entity"}
          ],
          "links": {
            "related": "/my-entities/123"
          }
        }
      }
    }

## {{ book.should }} Use Standards for Hypermedia Link Types, HAL or JSON API recommended

To represent hypermedia links in payload results, we should consider using a standard format like [HAL](http://stateless.co/hal_specification.html), [JSON API](http://jsonapi.org/), [JSON-LD](http://json-ld.org/) with [Hydra](http://www.hydra-cg.com/spec/latest/core/), or [Siren](https://github.com/kevinswiber/siren).  (Hint: Read [Kevin Sookocheff’s post](http://sookocheff.com/post/api/on-choosing-a-hypermedia-format/), for instance, to learn more about the hypermedia types.)

We are in an ongoing discussion in the API guild which standard should be recommended for a certain use case. In the meantime
we recommend to use [HAL](http://stateless.co/hal_specification.html) or [JSON API](http://jsonapi.org/).

## {{ book.should }} Allow Optional Embedding of Sub-Resources

Embedding related resources (also know as *Resource expansion*) is a great way to reduce the number of requests. In
cases where clients know upfront that they need some related resources they can instruct the server to prefetch that
data eagerly. Whether this is optimized on the server, e.g. a database join, or done in a generic way, e.g. an HTTP
proxy that transparently embeds resources, is up to the implementation.

See [*Conventional Query Strings*](../naming/Naming.md#could-use-conventional-query-strings) for naming.

## {{ book.must}} Modify the Content-Type for Embedded Resources

Embedded resources requires to change the media type of the response accordingly:

```http
GET /order/123?embed=(items)
Accept: application/x.order+json
```

```http
HTTP/1.1 200 OK
Content-Type: application/x.order+json;embed=(items)
```

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

## {{ book.could }} Use HATEOAS

Although we prefer [REST Maturity Level 2](http://martinfowler.com/articles/richardsonMaturityModel.html#level2), we do not forbid implementing [Level 3](http://martinfowler.com/articles/richardsonMaturityModel.html#level3), which is HATEOAS (Hypertext As the Engine Of Application State). But you should be aware of the shortcomings in our setup.

Because we are following API First principles, HATEOAS brings nothing new to the table in terms of API self-descriptiveness. Furthermore, generic HATEOAS clients which crawl and use APIs on their own are only a theoretical concept so far.
Our whole internal tooling around APIs like Twintip isn't adjusted for HATEOAS neither. For now, we have not
seen a good reason to implement HATEOAS in an API.

There are several other concerns regarding the promised advantages of HATEOAS (see [RESTistential Crisis over Hypermedia APIs](https://www.infoq.com/news/2014/03/rest-at-odds-with-web-apis for detailed discussion)):
- Hypermedia does not prevent clients from required manual changes when domain model changes over time
- Hypermedia makes sense for humans, not machines
- Hypermedia does not prevent API clients to implement shortcuts and directly target resources without 'discovering' them

If you use HATEOAS please present your findings in the [API Guild \[internal link\]](https://techwiki.zalando.net/display/GUL/API+Guild).

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

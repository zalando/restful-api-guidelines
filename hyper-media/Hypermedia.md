# Hypermedia

## {{ book.could }} Use HATEOAS

We prefer [REST Maturity Level 2](http://martinfowler.com/articles/richardsonMaturityModel.html#level2). Only if HATEOAS - as [Level 3](http://martinfowler.com/articles/richardsonMaturityModel.html#level3) - adds value to your API, consider using it. Because we are following API First, HATEOAS adds not much value for us in terms of self-descriptiveness. And we don't believe in generic HATEOAS clients which crawl and use APIs on their own. So, only use HATEOAS if your API really defines possible client actions (one part of hypermedia) and leverages other HATEOAS features.

Decoupled from HATEOAS, adding links and relationships should be considered as useful addition to APIs as it adds context and helps navigation in APIs to decent degree.

Additional resources: [Wikipedia](http://en.wikipedia.org/wiki/HATEOAS), [The RESTful CookBook](http://restcookbook.com/Basics/hateoas/)

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

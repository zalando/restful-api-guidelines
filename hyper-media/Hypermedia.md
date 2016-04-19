# Hypermedia

## {{ book.should }} Use HATEOAS

This is a challenging topic. We don’t have specific recommendations yet; we expect them to emerge
from trial and error, and from the API Guild’s ongoing research. For now, we strongly encourage
teams to apply this principle and document results.

Notes on aspects to be analyzed:

* Security concerns with respect to  passing OAuth2 tokens
* API discovery by client programmers vs. automated hypermedia driven workflows
* Additional client development work vs. reduced efforts in case of API and state handling changes

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

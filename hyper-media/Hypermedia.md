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
      “_links”: {
        “https://docs.team.zalan.do/rels/my-entity”: [{
          “href”: “https://service.team.zalan.do/my-entities/123”
        }]
      }
    }

## {{ book.should }} Use Standards for Hypermedia Link Types

To represent hypermedia links in payload results, we should consider using a standard format like [HAL](http://stateless.co/hal_specification.html), [JSON API](http://jsonapi.org/), [JSON-LD](http://json-ld.org/) with [Hydra](http://www.hydra-cg.com/spec/latest/core/), or [Siren](https://github.com/kevinswiber/siren).  (Hint: Read [Kevin Sookocheff’s post](http://sookocheff.com/post/api/on-choosing-a-hypermedia-format/), for instance, to learn more about the hypermedia types.)

## {{ book.should }} Consider Using a Standard for linked/embedded resources

For HTTP Link headers, consider using a format like [HAL](http://stateless.co/hal_specification.html),
[JSON-LD](http://json-ld.org/) with [Hydra](http://www.markus-lanthaler.com/hydra/spec/latest/core/),
or [Siren](https://github.com/kevinswiber/siren). For a comparison of these hypermedia types, see
[Kevin Sookocheff’s post](http://sookocheff.com/post/api/on-choosing-a-hypermedia-format/).

## {{ book.should }} Allow embedding of complex sub-resources

Embedding related resources (also know as *Resource expansion*) is a great way to reduce the number of requests. In
cases where clients know upfront that they need some related resources they can instruct the server to prefetch that
data eagerly. Whether this is optimized on the server, e.g. a database join, or done in a generic way, e.g. an HTTP
proxy that transparently embeds resources, is up the implementation.

See [*Conventional Query Strings*](../naming/Naming.md#could-use-conventional-query-strings) for naming.

## {{ book.must}} Modify the Content-Type for embedded resources

Embedded resources requires to change the media type of the response accordingly:

```http
GET /order/123?embed=(items)
Accept: application/x.order+json
```

```http
HTTP/1.1 200 OK
Content-Type: application/x.order+json;embed=(items)
```

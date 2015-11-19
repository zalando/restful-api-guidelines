# Hypermedia

[S] Use HATEOAS

This is a challenging topic. We don’t have specific recommendations yet; we expect them to emerge from trial and error, and from the API Guild’s ongoing research. For now, we strongly encourage teams to apply this principle and document results.

Notes on aspects to be analyzed:

* Security concerns with respect to  passing OAuth2 tokens 
* API discovery by client programmers vs. automated hypermedia driven workflows
* Additional development work vs. reduced refactoring work

Further reading:

* http://en.wikipedia.org/wiki/HATEOAS
* https://spring.io/understanding/HATEOAS

[C] Use URIs for Custom Link Relations

    {
      “_links”: {
        “https://docs.team.zalan.do/rels/my-entity”: [{
          “href”: “https://service.team.zalan.do/my-entities/123”
        }]
      }
    }
    
[S] Consider Using a Standard for Links

For HTTP Link headers, consider using a format like [HAL](http://stateless.co/hal_specification.html), [JSON-LD](http://json-ld.org/) with [Hydra](http://www.markus-lanthaler.com/hydra/spec/latest/core/), or [Siren](https://github.com/kevinswiber/siren). For a comparison of these hypermedia types, see [Kevin Sookocheff’s post](http://sookocheff.com/post/api/on-choosing-a-hypermedia-format/).
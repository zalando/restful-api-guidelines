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

## {{ book.may }} Use REST Maturity Level 3 - HATEOAS

We do not generally recommend to implement [REST Maturity Level 3](http://martinfowler.com/articles/richardsonMaturityModel.html#level3). HATEOAS comes with additional API complexity without real value in our SOA context where client and server interact via REST APIs and provide complex business functions as part of our e-commerce SaaS platform.

Our major concerns regarding the promised advantages of HATEOAS (see also [RESTistential Crisis over Hypermedia APIs](https://www.infoq.com/news/2014/03/rest-at-odds-with-web-apis for detailed discussion), [Why I Hate HATEOAS](https://jeffknupp.com/blog/2014/06/03/why-i-hate-hateoas/) and others):
- We follow the [API First principle](../general-guidelines/GeneralGuidelines.md#Must-Follow-API-First-Principle) 
with APIs explicitly defined outside the code with standard specification language. HATEOAS does not really add value for SOA client engineers in terms of API self-descriptiveness: a client engineer finds necessary links and usage description (depending on resource state) in the API reference definition anyway.
- Generic HATEOAS clients which need no prior knowledge about APIs and explore API capabilities based on hypermedia information provided, is a theoretical concept that we haven't seen working in practise and does not fit to our SOA set-up. The OpenAPI description format (and tooling based on OpenAPI) doesn't provide sufficient support for HATEOAS either.
- In practice relevant HATEOAS approximations (e.g. following specifications like HAL or JSON API) support API navigation by abstracting from URL endpoint and HTTP method aspects via link types. So, Hypermedia does not prevent clients from required manual changes when domain model changes over time.
- Hypermedia make sense for humans, less for SOA machine clients. We would expect use cases where it may provide value more likely in the frontend and human facing service domain.
- Hypermedia does not prevent API clients to implement shortcuts and directly target resources without 'discovering' them

However, we do not forbid HATEOAS; you could use it, if you checked its limitations and still see clear value for your usage scenario that justifies its additional complexity. If you use HATEOAS please share experience and present your findings in the [API Guild \[internal link\]](https://techwiki.zalando.net/display/GUL/API+Guild).

## {{ book.must }} Use common hypertext controls

When embedding links to other resources into representations you must use the common hypertext control object. It contains at least one attribute:

* `href`: The URI of the resource the hypertext control is linking to. All our API are using HTTP(s) as URI scheme. 

In API that contain any hypertext controls, the attribute name `href` is reserved for usage within hypertext controls.

The schema for hypertext controls can be derived from this model:

```yaml
HttpLink:
  description: A base type of objects representing links to resources.
  type: object
  properties:
    href:
      description: Any URI that is using http or https protocol
      type: string
      format: uri
  required: [ "href" ]
```

The name of an attribute holding such a `HttpLink` object specifies the relation between the object that 
contains the link and the linked resource. Implementations should use names from the 
[IANA Link Relation Registry](http://www.iana.org/assignments/link-relations/link-relations.xhtml) 
whenever appropriate.

Specific link objects may extend the basic link type with additional attributes, to give additional information
related to the linked resource or the relationship between the source resource and the linked one.

E.g. a service providing "Person" resources could model a person who is married with some other person with a hypertext control that contains attributes which describe the other person (`id`, `name`) but also the relationship "spouse" between between the two persons (`since`):


```json
{
  "id": "446f9876-e89b-12d3-a456-426655440000",
  "name": "Peter Mustermann",
  "spouse": {
    "href": "https://...",
    "since": "1996-12-19",
    "id": "123e4567-e89b-12d3-a456-426655440000",
    "name": "Linda Mustermann"
  }
}
```

Hypertext controls are allowed anywhere within a JSON model. While this specification would
allow [HAL](http://stateless.co/hal_specification.html), we actually don't recommend/enforce the 
usage of HAL anymore as the structural separation of meta-data and data creates more harm than value to the 
understandability and usability of an API.

## {{ book.must }} Do Not Use Link Headers with JSON entities

We don't allow the use of the [`Link` Header defined by RFC 5988](http://tools.ietf.org/html/rfc5988#section-5)
in conjunction with JSON media types, and favor [HAL](#must-use-hal) instead. The primary reason is to have a consistent
place for links as well as the better support for links in JSON payloads compared to the uncommon link header syntax.

## {{ book.may }} Use Custom Link Relations

You should consider using a custom link relation if and only if standard [link relations](http://www.iana.org/assignments/link-relations/link-relations.xml)
are not sufficient to express a relation.
Related or even embedded (sub-) resources should be linked via “meta link attributes” within the response payload; use
the following [HAL](http://stateless.co/hal_specification.html) compliant syntax:

    {
      ...
      “_links”: {
        “my-relation”: {
          “href”: “https://service.team.zalan.do/my-entities/123”
        }
      }
    }

In earlier versions of this rule we proposed URIs but dropped that in favor of readability. Since link relations are
properties in the `_links` object you should define and describe them individually in the API specification.

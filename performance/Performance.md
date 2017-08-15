# Performance

## {{ book.should }} Reduce Bandwidth Needs and Improve Responsiveness

APIs should support techniques for reducing bandwidth based on client needs. This holds for APIs
that (might) have high payloads and/or are used in high-traffic scenarios like the public Internet
and telecommunication networks. Typical examples are APIs used by mobile web app clients with
(often) less bandwidth connectivity. (Zalando is a 'Mobile First' company, so be mindful of this
point.)

Common techniques include:

* gzip compression
* querying field filters to retrieve a subset of resource attributes (see [*Support Filtering of Resource Fields*](../performance/Performance.md#should-support-filtering-of-resource-fields)
below)
* paginate lists of data items (see [*Pagination*](../pagination/Pagination.md) below)
* `ETag` and `If-(None-)Match` headers to avoid re-fetching of unchanged resources (see [*Common Headers*](../headers/CommonHeaders.md#could-consider-using-etag-together-with-ifnonematch-header)
* pagination for incremental access of larger (result) lists

Each of these items is described in greater detail below.

## {{ book.should }} Use gzip Compression

Compress the payload of your API’s responses with gzip, unless there’s a good reason not
to — for example,  you are serving so many requests that the time to compress becomes a bottleneck.
This helps to transport data faster over the network (fewer bytes) and makes frontends respond faster.

Though gzip compression might be the default choice for server payload, the server should also support payload without compression and its client control via Accept-Encoding request header -- see also [RFC 7231 Section 5.3.4](http://tools.ietf.org/html/rfc7231#section-5.3.4). The server should indicate used gzip
compression via the Content-Encoding header.

## {{ book.should }} Support Filtering of Resource Fields

Depending on your use case and payload size, you can significantly reduce network bandwidth need by supporting filtering of returned entity fields. Here, the client can determine the subset of fields he wants to receive via the fields query parameter — example see [Google AppEngine API's partial response](https://cloud.google.com/appengine/docs/python/taskqueue/rest/performance#partial-response):

### Unfiltered

```http
GET http://api.example.org/resources/123 HTTP/1.1

HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": "cddd5e44-dae0-11e5-8c01-63ed66ab2da5",
  "name": "John Doe",
  "address": "1600 Pennsylvania Avenue Northwest, Washington, DC, United States",
  "birthday": "1984-09-13",
  "partner": {
    "id": "1fb43648-dae1-11e5-aa01-1fbc3abb1cd0",
    "name": "Jane Doe",
    "address": "1600 Pennsylvania Avenue Northwest, Washington, DC, United States",
    "birthday": "1988-04-07"
  }
}
```

### Filtered

```http
GET http://api.example.org/resources/123?fields=(name,partner(name)) HTTP/1.1

HTTP/1.1 200 OK
Content-Type: application/json

{
  "name": "John Doe",
  "partner": {
    "name": "Jane Doe"
  }
}
```

As illustrated by this example, field filtering should be done via request parameter "fields" with value range defined 
by the following [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form) grammar.

```
<fields> ::= <negation> <fields_expression> | <fields_expression>

<negation> ::= "!"

<fields_expression> ::= "(" <field_set> ")"

<field_set> ::= <qualified_field> | <qualified_field> "," <field_set>

<qualified_field> ::= <field> | <field> <fields_expression>

<field> ::= <DASH_LETTER_DIGIT> | <DASH_LETTER_DIGIT> <field>

<DASH_LETTER_DIGIT> ::= <DASH> | <LETTER> | <DIGIT>

<DASH> ::= "-" | "_"

<LETTER> ::= "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z" | "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"

<DIGIT> ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
```

A `fields_expression` as defined by the grammar describes the properties of an object, i.e. `(name)` returns only the 
`name` property of the root object. `(name,partner(name))` returns the `name` and  `partner` properties where `partner` 
itself is also an object and only its `name` property is returned.

Hint: OpenAPI doesn't allow you to formally specify whether depending on a given parameter will
return different parts of the specified result schema. Explain this in English in the parameter
description.

## {{ book.should }} Allow Optional Embedding of Sub-Resources

Embedding related resources (also know as *Resource expansion*) is a great way to reduce the number of requests. In
cases where clients know upfront that they need some related resources they can instruct the server to prefetch that
data eagerly. Whether this is optimized on the server, e.g. a database join, or done in a generic way, e.g. an HTTP
proxy that transparently embeds resources, is up to the implementation.

See [*Conventional Query Parameters*](../naming/Naming.md#could-use-conventional-query-strings) for naming, e.g. 
"embed" for steering of embedded resource expansion. Please use the 
[BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form) grammar, as already defined above for filtering, when it 
comes to an embedding query syntax.

Embedding a sub-resource can possibly look like this where an order resource has its order items as sub-resource (/order/{orderId}/items):

```http
GET /order/123?embed=(items) HTTP/1.1

{
  "id": "123",
  "_embedded": {
    "items": [
      {
        "position": 1,
        "sku": "1234-ABCD-7890",
        "price": {
          "amount": 71.99,
          "currency": "EUR"
        }
      }
    ]
  }
}
```

## {{ book.should }} Not Implement Caching

While RESTful design suggests to support caching, our guidelines requires APIs to protect endpoints by SSL and [OAuth authorization](security/Security.html#must-secure-endpoints-with-oauth-2.0).
As consequence, caching is non-trivial and has to take many aspects into account, e.g. general cacheability of response information, invalidation parameters, authentication, multiple instances of consumers.
Thus, using transparent HTTP caches is difficult, in best case inefficient, and in worst case impossible.

As result API providers should always set the `Cache-Control: no-cache` header.

**Note:** as this is a technical information that is attached to each response by service frameworks, there is no need to document this header in the API specification.

**Warning:** if an API is intended to support caching, it should take care to explicitly specify this ability by defining the caching boundaries, e.g. by giving dynamically hints using the `Vary` and `Cache-Control:` headers ([RFC-7234](https://tools.ietf.org/html/rfc7234#section-4.1)).

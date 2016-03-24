# HTTP

## {{ book.must }} Use HTTP Methods Correctly

Be compliant with the standardized HTTP method semantics summarized as follows:

###GET

- reads a resource or set of resource instances, respectively
- usually returns error (404) if resource does not exist;
  however, may also be robust against non-existence, if there are sensible defaults
- must NOT have request body payload

###PUT:

- fully uploads an entity, i.e. provides a complete replacement by the resource representation
  passed as payload
- resource instance id(s) are maintained by the client and passed as input data
- usually robust against non-existence of the entity by implicit creation before update

###PATCH:

- partial upload, i.e. only a specific subset of resource fields are replaced
- partial resource representation passed as payload has either resource content type with optional
  fields or a custom content type (e.g. [RFC 5789](https://tools.ietf.org/html/rfc5789)), which should
  include instructions of how to modify the resource
- usually not robust against non existence of the entity


###DELETE:

- deletes a resource instance
- usually robust against non existence of the entity

###POST:

- creates a resource instance
- resource instance id(s) are mastered by server and returned with output payload
- more generally, POST should be used for scenarios that cannot be covered by the other methods.
  For instance, GET with complex (e.g. sql like structured) query that needs to be passed as
  request body payload

## {{book.must}}  HTTP Methods must Fulfill Safeness and Idempotency Properties

An operation can be...

- idempotent, i.e. operation will produce the same results if executed once or multiple times
- safe, i.e. must not have side effects such as state changes

Method implementations must fulfill the following basic properties:

| HTTP method  |  safe  |  idempotent |
| --           |  --    |  --         |
| POST         |  No    |  No         |
| GET          |  Yes   |  Yes        |
| PUT          |  No    |  Yes        |
| DELETE       |  No    |  Yes        |
| OPTIONS      |  Yes   |  Yes        |
| PATCH        |  No    |  No         |

Hint: Please see also [Best Practises](https://goo.gl/vhwh8a) for further practises on how to support the
different HTTP methods on resources.

## {{ book.must }} Use Meaningful HTTP Status Codes

* See [Best Practices](https://goo.gl/vhwh8a)

## Reducing Bandwidth Needs and Improving Responsiveness

APIs should support techniques for reducing bandwidth based on client needs. This holds for APIs
that (might) have high payloads and/or are used in high-traffic scenarios like the public Internet
and telecommunication networks. Typical examples are APIs used by mobile web app clients with
(often) less bandwidth connectivity. (Zalando is a “Mobile “First” company, so be mindful of this
point.)

Common techniques include:

* gzip compression
* querying field filters to retrieve a subset of resource attributes
* `ETag` (and If-[None-]Match) headers to avoid refetch of unchanged resources
* pagination for incremental access of larger (result) lists

Each of these items is described in greater detail below.

## {{ book.should }} gzip Compression

Compress the payload of your API’s responses with gzip (GNU zip), unless there’s a good reason not
to — for example,  you are serving so many requests that the time to compress becomes a bottleneck.
This helps to transport data faster over the network (fewer bytes) and makes frontends respond faster.

Though gzip compression might be the default choice for server payload, the server should also
support payload without compression. The client may activate or deactivate via `Accept-Encoding`
header server compression of payload -- see also [RFC 7231 Section
5.3.4](http://tools.ietf.org/html/rfc7231#section-5.3.4). The server should indicate used gzip
compression via the Content-Encoding header.

## {{ book.should }} Support Filtering of Resource Fields

Depending on your use case and payload size, you can significantly reduce network bandwidth load by
allowing the client to select a subset of fields to be returned using the fields query parameter.
See the following example or alternatively [Google AppEngine API's partial response](https://cloud.google.com/appengine/docs/python/taskqueue/rest/performance#partial-response):

### Unfiltered

```http
GET http://api.example.org/resources/123 HTTP/1.1

HTTP/1.1 200 OK
Content-Type: application/x.person+json

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
Content-Type: application/x.person+json;fields=(name,partner(name))

{
  "name": "John Doe",
  "partner": {
    "name": "Jane Doe"
  }
}
```

The approach we recommend for field is a Zalando Github project,
[json-fields](https://github.com/zalando/json-fields). It defines a formal grammar for the ANTLR
 parser generator and provides a ready-to use library for Java / Jackson based projects
 ([Maven link](http://mvnrepository.com/artifact/org.zalando.guild.api/json-fields-jackson)).
Teams that use other JSON serializers are encouraged to contribute to the open source project and
create their own parser / framework based on this grammar.

Other approaches we have considered are JSONPath or GraphQL. While they have advantages, neither of
them can easily be plugged into an existing serialization process, so they require an additional,
manual serialization process, whereas the above solution addresses our main filter use cases and
can easily be introduced with a minimum of effort.

Hint: OpenAPI doesn't allow you to formally specify whether depending on a given parameter will
return different parts of the specified result schema. Explain this in English in the parameter
description.

## {{ book.could }} Support the ETag Header

If a resource changes, the contents of the [`ETag`](https://en.wikipedia.org/wiki/HTTP_ETag) header
must also change. Combined with the `If-Match` and `If-None-Match` headers, the `ETag` header allows
for:

* caching of entities
* optimistic locking

Its possible contents:

* the entity’s version number
* hash of the response body
* hash of the entity’s last modified field

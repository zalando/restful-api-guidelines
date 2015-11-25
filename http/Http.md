# HTTP

## {{ book.must }} Use HTTP Methods Correctly

* See [Best Practices](https://docs.google.com/document/d/1Dqgkfxm2Jt9mVSraOYWSfKq3cKaZwf_VOgYDRuUf7kI/edit#heading=h.nkp61pevkdh6)

## {{ book.must }} Use Meaningful HTTP Status Codes

* See [Best Practices](https://docs.google.com/document/d/1Dqgkfxm2Jt9mVSraOYWSfKq3cKaZwf_VOgYDRuUf7kI/edit#heading=h.nkp61pevkdh6)

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
See [Google AppEngine API's partial response
example](https://cloud.google.com/appengine/docs/python/taskqueue/rest/performance#partial-response).

Hint: Swagger doesn't allow you to formally specify  whether depending on a given parameter will
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
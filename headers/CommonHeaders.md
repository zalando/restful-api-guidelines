# Common Headers

This section describes a handful of headers, which we found raised the most questions in our daily usage, or which are useful in particular circumstances but not widely known.

## {{ book.must }} Use Content Headers Correctly

Content or entity headers are headers with a `Content-` prefix. They describe the content of the body of the message and
they can be used in both, HTTP requests and responses. Commonly used content headers include but are not limited to:

 - [`Content-Disposition`](https://tools.ietf.org/html/rfc6266) can indicate that the representation is supposed to be saved as a file, and the proposed file name.
 - [`Content-Encoding`](https://tools.ietf.org/html/rfc7231#section-3.1.2.2) indicates compression or encryption algorithms applied to the content.
 - [`Content-Length`](https://tools.ietf.org/html/rfc7230#section-3.3.2) indicates the length of the content (in bytes).
 - [`Content-Language`](https://tools.ietf.org/html/rfc7231#section-3.1.3.2) indicates that the body is meant for people literate in some human language(s).
 - [`Content-Location`](https://tools.ietf.org/html/rfc7231#section-3.1.4.2) indicates where the body can be found otherwise ([see below for more details](#must-use-contentlocation-correctly)).
 - [`Content-Range`](https://tools.ietf.org/html/rfc7233#section-4.2) is used in responses to range requests to indicate which part of the requested resource representation is delivered with the body.
 - [`Content-Type`](https://tools.ietf.org/html/rfc7231#section-3.1.1.5) indicates the media type of the body content.

## {{ book.must }} Use Content-Location Correctly

The Content-Location header is *optional* and can be used in successful write operations (PUT, POST or PATCH) or read operations (GET, HEAD) to signal the receiver the actual location of the resource transmitted in the response body. This allows clients to identify the resource and to update their local copy when receiving a response with this header.

The Content-Location header can be used to support the following use cases:

- For reading operations GET and HEAD, a different location than the requested URI can be used to indicate that the returned resource is subject to content negotiations, and that the value provides a more specific identifier of the resource.
- For writing operations PUT and PATCH, an identical location to the requested URI, can be used to explicitly indicate that the returned resource is the current representation of the newly created or updated resource.
- For writing operations POST and DELETE, a content location can be used to indicate that the body contains a status report resource in response to the requested action, which is available at provided location.

**Note**: The standard application of the last use case is the creation of resource via POST, where the Content-Location is used to provide the location and identity of the created resource.

When using the Content-Location header, the Content-Type header has to be set as well. For example:

```http
GET /products/123/images HTTP/1.1

HTTP/1.1 200 OK
Content-Type: image/png
Content-Location: /products/123/images?format=raw
```

As the correct interpretation of Content-Location with respect to semantics and cache results is difficult, we advise to not make use of Content-Location at all. It is sufficient to direct the clients to the resource location by the Location header instead.

More details in RFC 7231 [7.1.2 Location](https://tools.ietf.org/html/rfc7231#section-7.1.2), [3.1.4.2 Content-Location](https://tools.ietf.org/html/rfc7231#section-3.1.4.2)


## {{ book.could }} Use the Prefer header to indicate processing preferences

The  `Prefer` header defined in [RFC7240](https://tools.ietf.org/html/rfc7240) allows clients to request processing behaviors from servers. [RFC7240](https://tools.ietf.org/html/rfc7240) pre-defines a number of preferences and is extensible, to allow others to be defined. Support for the Prefer header is entirely optional and at the discretion of API designers, but as an existing Internet Standard, is recommended over defining proprietary "X-" headers for processing directives. 

The `Prefer` header can defined like this in an API definition:

```yaml
  Prefer:
    name: Prefer
    description: |
      The RFC7240 Prefer header indicates that particular server 
      behaviors are preferred by the client but are not required 
      for successful completion of the request. 

      # (indicate the preferences supported by the API)

    in: header
    type: string  
    required: false
```

Supporting APIs may return the `Preference-Applied` header also defined in [RFC7240](https://tools.ietf.org/html/rfc7240) to indicate whether the preference was applied.


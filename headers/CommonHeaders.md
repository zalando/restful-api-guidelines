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

This header is used in the response of either a successful read (GET, HEAD) or successful write operation (PUT, POST or PATCH).

In the case of the GET requests it points to a location where an alternate representation of the
entity in the response body can be found. In this case one
has to set the Content-Type header as well. For example:

```http
GET /products/123/images HTTP/1.1

HTTP/1.1 200 OK
Content-Type: image/png
Content-Location: /products/123/images?format=raw
```

In the case of mutating HTTP methods, the Content-Location header can be used when there is a response body,
and then it indicates that the included response body can be found at the location indicated in the header.

If the header value is the same as the location of the created resource (as indicated by the Location header
after POST) or the modified resource (as indicated by the request URI after PUT / PATCH), then the returned
body is indeed the current representation of the entity making a subsequent GET operation from the client side
not necessary.

If your API returns the new representation after a PUT, PATCH, or POST you should include the Content-Location header
to make it explicit, that the returned resource is an up-to-date version.

More details in [rfc7231](https://tools.ietf.org/html/rfc7231#section-3.1.4.2)


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


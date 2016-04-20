# Common Headers

This section describes a handful of headers, which we found raised the most questions in our daily usage.

## Content-Location

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

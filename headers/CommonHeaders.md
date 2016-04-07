# Common Headers

This section describes a handful of headers, which we found raised the most questions in our daily usage.

## Content-Location

This header is used in the response of either a GET, HEAD, PUT, POST or PATCH request. In the case of the GET requests it
points to a location where an alternate representation of the entity in the response body can be found. In this case one
has to set the Content-Type header as well. For example:

    GET /product-images/123
    Content-Type: image/png
    Content-Location: /product-images/123?format=png

In the case of mutating HTTP methods the Content-Location header is used, when there is a response body. If the Content-Location
is the same than the Location header it means, that the returned body is indeed the current representation of the entity, making
a subsequent GET operation from the client side not necessary.

More details in [rfc7231](https://tools.ietf.org/html/rfc7231#section-3.1.4.2)

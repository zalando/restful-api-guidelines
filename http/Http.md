# HTTP

## {{ book.must }} Use HTTP Methods Correctly

Be compliant with the standardized HTTP method semantics summarized as follows:

### GET

GET requests are used to read a single resource or query set of resources.

- GET requests for individual resources will usually generate a 404 if the resource does not exist
- GET requests for collection resources may return either 200 (if the listing is empty) or 404 (if
  the list is missing)
- GET requests must NOT have request body payload

**Note:** GET requests on collection resources should provide a sufficient filter mechanism as well
as [pagination](../pagination/Pagination.md).

### PUT

PUT requests are used to create or update single resources or an entire collection resources. The
semantic is best described as »*please put the enclosed representation at the resource mentioned by
the URL*«.

- PUT requests are usually applied to single resources, and not to collection resources, as this
  would imply replacing the entire collection
- PUT requests are usually robust against non-existence of resources by implicitly creating before
  updating
- on successful PUT requests, the server will replace the entire resource addressed by the URL with
  the representation passed in the payload
- successful PUT requests will usually generate 200 or 204 (if the resource was updated - with or
  without actual content returned), and 201 (if the resource was created)

**Note:** Resource IDs with respect to PUT requests are maintained by the client and passed as a
URL path segment. Putting the same resource twice is required to be idempotent and to result in
the same single resource instance. If PUT is applied for creating a resource, only URIs should be
allowed as resource IDs. If URIs are not available POST should be preferred.

### POST

POST requests are idiomatically used to create single resources on a collection resource endpoint,
but other semantics on single resources endpoint are equally possible. The semantic for collection
endpoints is best described as »*please add the enclosed representation to the collection resource
identified by the URL*«. The semantic for single resource endpoints is best described as »*please
execute the given well specified request on the collection resource identified by the URL*«.

- POST request should only be applied to collection resources, and normally not on single resource,
  as this has an undefined semantic
- on successful POST requests, the server will create one or multiple new resources and provide
  their URI/URLs in the response
- successful POST requests will usually generate 200 (if resources have been updated), 201 (if
  resources have been created), and 202 (if the request was accepted but has not been finished yet)

**More generally:** POST should be used for scenarios that cannot be covered by the other methods
sufficiently. For instance, GET with complex (e.g. SQL like structured) query that needs to be
passed as request body payload because of the URL-length constraint. In such cases, make sure to
document the fact that POST is used as a workaround.

**Note:** Resource IDs with respect to POST requests are created and maintained by server and
returned with response payload. Posting the same resource twice is by itself **not** required to
be idempotent and may result in multiple resource instances. Anyhow, if external URIs are present
that can be used to identify duplicate requests, it is best practice to implement POST in an
idempotent way.

### PATCH

PATCH request are only used for partial update of single resources, i.e. where only a specific
subset of resource fields should be replaced. The semantic is best described as »*please change
the resource identified by the URL according to my change request*«. The semantic of the change
request is not defined in the HTTP standard and must be described in the API specification by
using suitable media types.

- PATCH requests are usually applied to single resources, and not on collection resources, as this
  would imply patching on the entire collection
- PATCH requests are usually not robust against non-existence of resource instances
- on successful PATCH requests, the server will update parts of the resource addressed by the URL
  as defined by the change request in the payload
- successful PATCH requests will usually generate 200 or 204 (if resources have been updated
  - with or without updated content returned)

**Note:** since implementing PATCH correctly is a bit tricky, we strongly suggest to choose one and
only one of the following patterns per endpoint, unless forced by a [backwards compatible change](
../compatibility/Compatibility#compatibility). In preference order:

1. use PUT with complete objects to update a resource as long as feasible (i.e. do not use PATCH
   at all).
2. use PATCH with partial objects to only update parts of a resource, when ever possible. (This is
   basically  [JSON Merge Patch](https://tools.ietf.org/html/rfc7396), a specialized media type
   `application/merge-patch+json` that is a partial resource representation.)
3. use PATCH with [JSON Patch](http://tools.ietf.org/html/rfc6902), a specialized media type
   `application/json-patch+json` that includes instructions on how to change the resource.
4. use POST (with a proper description of what is happening) instead of PATCH if the request does
   not modify the resource in a way defined by the semantics of the media type.

In practice [JSON Merge Patch](https://tools.ietf.org/html/rfc7396) quickly turns out to be too
limited, especially when trying to update single objects in large collections (as part of the
resource). In this cases [JSON Patch](http://tools.ietf.org/html/rfc6902) can shown its full power
while still showing readable patch requests
([see also](http://erosb.github.io/post/json-patch-vs-merge-patch)).

### DELETE

DELETE request are used to delete resources. The semantic is best described as »*please delete the
resource identified by the URL*«.

- DELETE requests are usually applied to single resources, not on collection resources, as this
  would imply deleting the entire collection
- successful DELETE request will usually generate 200 (if the deleted resource is returned) or 204
  (if no content is returned)
- failed DELETE request will usually generate 404 (if the resource cannot be found) or 410 (if the
  resource was already deleted before)

### HEAD

HEAD requests are used retrieve to header information of single resources and resource collections.

- HEAD has exactly the same semantics as GET, but returns headers only, no body.

### OPTIONS

OPTIONS are used to inspect the available operations (HTTP methods) of a given endpoint.

- OPTIONS requests usually either return a comma separated list of methods (provided by an
  `Allow:`-Header) or as a structured list of link templates

**Note:** OPTIONS is rarely implemented, though it could be used to self-describe the full
functionality of a resource.


## {{book.must}} Fulfill Safeness and Idempotency Properties

An operation can be...

- idempotent, i.e. operation will produce the same results if executed once or multiple times (note: this does not
  necessarily mean returning the same status code)
- safe, i.e. must not have side effects such as state changes

Method implementations must fulfill the following basic properties:

| HTTP method  |  safe  |  idempotent |
| --           |  --    |  --         |
| OPTIONS      |  Yes   |  Yes        |
| HEAD         |  Yes   |  Yes        |
| GET          |  Yes   |  Yes        |
| PUT          |  No    |  Yes        |
| POST         |  No    |  No         |
| DELETE       |  No    |  Yes        |
| PATCH        |  No    |  No         |

Please see also [Best Practices \[internal link\]](https://goo.gl/vhwh8a) for further hints on how to support the
different HTTP methods on resources.

## {{ book.must }} Use Meaningful HTTP Status Codes

### Success Codes

| Code | Meaning | Methods |
| --   | --      | --                 |
| 200  | OK - this is the standard success response | All |
| 201  | Created - Returned on successful entity creation. You are free to return either an empty response or the created resource in conjunction with the Content-Location header. (More details found in the [Common Headers section](../headers/CommonHeaders.md).) *Always* set the Location header. | POST, PUT |
| 202  | Accepted - The request was successful and will be processed asynchronously. | POST, PUT, DELETE, PATCH |
| 204  | No content - There is no response body | PUT, DELETE |

### Redirection Codes

| Code | Meaning | Methods |
| --   | --      | --                 |
| 301 | Moved Permanently - This and all future requests should be directed to the given URI. | All |
| 303 | See Other - The response to the request can be found under another URI using a GET method.  | PATCH, POST, PUT, DELETE |
| 304 | Not Modified - resource has not been modified since the date or version passed via request headers If-Modified-Since or If-None-Match. | GET |

### Client Side Error Codes

| Code | Meaning | Methods |
| --   | --      | --                 |
| 400 | Bad request - generic / unknown error | All |
| 401 | Unauthorized - the users must log in (this often means “Unauthenticated”) | All |
| 403 | Forbidden - the user is not authorized to use this resource | All |
| 404 | Not found - the resource is not found | All |
| 405 | Method Not Allowed - the method is not supported, see OPTIONS | All |
| 406 | Not Acceptable - resource can only generate content not acceptable according to the Accept headers sent in the request | All |
| 408 | Request timeout - the server times out waiting for the resource | All |
| 409 | Conflict - request cannot be completed due to conflict, e.g. when two clients try to create the same resource or if there are concurrent, conflicting updates | PUT, DELETE, PATCH |
| 410 | Gone - resource does not exist any longer, e.g. when accessing a resource that has intentionally been deleted | All |
| 412 | Precondition Failed - returned for conditional requests, e.g. If-Match if the condition failed. Used for optimistic locking. | PUT, DELETE, PATCH |
| 415 | Unsupported Media Type - e.g. clients sends request body without content type | PUT, DELETE, PATCH
| 423 | Locked - Pessimistic locking, e.g. processing states | PUT, DELETE, PATCH |
| 428 | Precondition Required - server requires the request to be conditional (e.g. to make sure that the “lost update problem” is avoided). | All |
| 429 | Too many requests - the client does not consider rate limiting and sent too many requests. See ["Use 429 with Headers for Rate Limits"](#must-use-429-with-headers-for-rate-limits). | All |

### Server Side Error Codes:

| Code | Meaning | Methods |
| --   | --      | --                 |
| 500 | Internal Server Error - a generic error indication for an unexpected server execution problem (here, client retry may be senseful) | All |
| 501 | Not Implemented -  server cannot fulfill the request (usually implies future availability, e.g. new feature). | All |
| 503 | Service Unavailable - server is (temporarily) not available (e.g. due to overload) -- client retry may be senseful. | All |

All error codes can be found in [RFC7231](https://tools.ietf.org/html/rfc7231#section-6) and [Wikipedia](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes) or via https://httpstatuses.com/<error_code>.

## {{ book.must }} Provide Error Documentation

APIs should define the functional, business view and abstract from implementation aspects. Errors become a key element providing context and visibility into how to use an API. The error object should be extended by an application-specific error identifier if and only if the HTTP status code is not specific enough to convey the domain-specific error semantic. For this reason, we use a standardized error return object definition — see [*Use Common Error Return Objects*](../common-data-objects/CommonDataObjects.md#must-use-common-error-return-objects).

The OpenAPI specification shall include definitions for error descriptions that will be returned; they are part of the interface definition and provide important information for service clients to handle exceptional situations and support troubleshooting. You should also think about a troubleshooting board — it is part of the associated online API documentation, provides information and handling guidance on application-specific errors and is referenced via links of the API definition. This can reduce service support tasks and contribute to service client and provider performance.

Service providers should differentiate between technical and functional errors. In most cases it's not useful to document technical errors that are not in control of the service provider unless the status code convey application-specific semantics. The list of status code that can be omitted from API specifications includes but is not limited to:
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found` unless it has some additional semantics
- `405 Method Not Allowed`
- `406 Not Acceptable`
- `408 Request Timeout`
- `413 Payload Too Large`
- `414 URI Too Long`
- `415 Unsupported Media Type`
- `500 Internal Server Error`
- `502 Bad Gateway`
- `503 Service Unavailable`
- `504 Gateway Timeout`

Even though they might not be documented - they may very much occur in production, so clients should be prepared for unexpected response codes, and in case of doubt handle them like they would handle the corresponding x00 code. Adding new response codes (specially error responses) should be considered a compatible API evolution.

Functional errors on the other hand, that convey domain-specific semantics, must be documented and are strongly encouraged to be expressed with [*Problem types*](../common-data-objects/CommonDataObjects.md#must-use-common-error-return-objects).

## {{ book.must }} Use 429 with Headers for Rate Limits

APIs that wish to manage the request rate of clients must use the ['429 Too Many Requests'](http://tools.ietf.org/html/rfc6585) response code if the client exceeded the request rate and therefore the request can't be fulfilled. Such responses must also contain header information providing further details to the client. There are two approaches a service can take for header information:

 - Return a ['Retry-After'](https://tools.ietf.org/html/rfc7231#section-7.1.3) header indicating how long the client ought to wait before making a follow-up request. The Retry-After header can contain a HTTP date value to retry after or the number of seconds to delay. Either is acceptable but APIs should prefer to use a delay in seconds.
 
 - Return a trio of 'X-RateLimit' headers. These headers (described below) allow a server to express a service level in the form of a number of allowing requests within a given window of time and when the window is reset. 

The 'X-RateLimit' headers are:

- `X-RateLimit-Limit`: The maximum number of requests that the client is allowed to make in this window.
- `X-RateLimit-Remaining`: The number of requests allowed in the current window.
- `X-RateLimit-Reset`: The relative time in seconds when the rate limit window will be reset.

The reason to allow both approaches is that APIs can have different needs. Retry-After is often sufficient for general load handling and request throttling scenarios and notably, does not strictly require the concept of a calling entity such as a tenant or named account. In turn this allows resource owners to minimise the amount of state they have to carry with respect to client requests. The 'X-RateLimit' headers are suitable for scenarios where clients are associated with pre-existing account or tenancy structures. 'X-RateLimit' headers are generally returned on every request and not just on a 429, which implies the service implementing the API is carrying sufficient state to track the number of requests made within a given window for each named entity.

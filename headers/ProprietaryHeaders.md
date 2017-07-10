# Proprietary Headers

This section shares definitions of proprietary headers that should be named consistently because
they address overarching service-related concerns. Whether services support these concerns or not is
optional; therefore, the OpenAPI API specification is the right place to make this explicitly
visible. Use the parameter definitions of the resource HTTP methods.

## {{ book.must }} Use Only the Specified Proprietary Zalando Headers

In general, proprietary headers should be avoided.
They can be used in cases when parameter represents a pass-through end-to-end header.
A valid use-case of a proprietary header is providing context information which is not a part of the actual API, but is used by subsequent communication.

From a conceptual point of view, the semantic of an operation should always be expressed by path and query parameters, i.e. what goes into the URL, as well as the content.
Headers are used to implement functions close to the protocol layer, such as flow control, content negotiation, and authentication.
Thus, headers are reserved for general context information ([RFC-7231](https://tools.ietf.org/html/rfc7231#section-5)).

`X-` headers were initially reserved for unstandardized parameters.
The usage of `X-` headers is deprecated ([RFC-6648](https://tools.ietf.org/html/rfc6648)).
This fact complicates the contract definition between consumer and producer of an API, since there is no aligned way of handling those headers.

The following proprietary headers have been specified by this guideline for usage so far.
Remember that HTTP header field names are not case-sensitive.

| Header field name | Type    | Description                       | Header field value example                |
| ----------------- | ------- | --------------------------------- | -------------------------- |
| X-Flow-ID         | String  | The flow id of the request, which is written into the logs and passed to called services. Helpful for operational troubleshooting and log analysis. It supports traceability of requests and identifying request flows through system of many services. It should be a string consisting of just printable ASCII characters (i.e. without whitespace). Verify in a received request that it fits to a specific format, has a sensible maximum length and possibly throw out or escape characters/bytes which could crash your log parsing (line breaks, tabs, spaces, NULL). If a legacy subsystem can only work with flow IDs of a specific format, it needs to define this in its API, or make its own ones. | GKY7oDhpSiKY_gAAAABZ_A |
| X-UID      | String | Generic user id of OpenId account that owns the passed (OAuth2) access token. E.g. additionally provided by OpenIG proxy after access token validation -- may save additional token validation round trips. | w435-dker-jdh357 |
| X-Tenant-ID       | String  | The tenant id for future platform multitenancy support. *Should not be used unless new platform multitenancy is truly supported. But should be used by New Platform Prototyping services.* Must be validated for external retailer, supplier, etc. tenant users via OAuth2; details in clarification. Currently only used by New Platform Prototyping services. | 9f8b3ca3-4be5-436c-a847-9cd55460c495  |
| X-Sales-Channel   | String | Sales channels are owned by retailers and represent a specific consumer segment being addressed with a specific product assortment that is offered via CFA retailer catalogs to consumers (see [platform glossary \[internal link\]](https://pages.github.bus.zalan.do/core-platform/docs/glossary/glossary.html)) | 101 |
| X-Frontend-Type   | String | Consumer facing applications (CFAs) provide business experience to their customers via different frontend application types, for instance, mobile app or browser. Info should be passed-through as generic aspect -- there are diverse concerns, e.g. pushing mobiles with specific coupons, that make use of it. Current range is mobile-app, browser, facebook-app, chat-app | mobile-app |
| X-Device-Type      | String | There are also use cases for steering customer experience (incl. features and content) depending on device type. Via this header info should be passed-through as generic aspect. Current range is smartphone, tablet, desktop, other | tablet |
| X-Device-OS      | String | On top of device type above, we even want to differ between device platform, e.g. smartphone Android vs. iOS. Via this header info should be passed-through as generic aspect. Current range is iOS, Android, Windows, Linux, MacOS | Android |
| X-App-Domain      | Integer | The app domain (i.e. shop channel context) of the request. Note, app-domain is a legacy concept that will be replaced in new platform by combinations of main CFA concerns like retailer, sales channel, country | 16 |

**Exception:** The only exception to this guideline are the conventional hop-by-hop `X-RateLimit-` headers which can be used as defined in [HTTP/Must-Use-429-with-Headers-For-Rate-Limits](../http/Http.md#must-use-429-with-headers-for-rate-limits).


## {{ book.must }} Propagate Proprietary Headers

All Zalando's proprietary headers are end-to-end headers.

All headers specified above must be propagated to the services down the call chain.
The header names and values must remain unchanged.

Some of the transitive services may require the meta information provided via proprietary headers and/or rely on it.
Besides, the values of the custom headers can influence the results of the queries (e.g. the device type information influences the recommendation results).

The values of the proprietary headers may be used as a part of the request body in the subsequent communication.
In such cases, the proprietary headers must also be propagated as headers to the successive calls, despite the duplication.



*Footnotes:*

The Internet Engineering Task Force's states in [RFC-6648](https://tools.ietf.org/html/rfc6648) that company specific header' names should incorporate the organization's name.
We aim for backward compatibility, and therefore keep the `X-` prefix.

HTTP/1.1 standard ([RFC-7230](https://tools.ietf.org/html/rfc7230#section-6.1)) defines two types of headers: end-to-end and hop-by-hop headers.
End-to-end headers must be transmitted to the ultimate recipient of a request or response.
Hop-by-hop headers, on the contrary, are meaningful for a single connection only.

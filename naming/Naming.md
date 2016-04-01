# Naming

## {{ book.must }} Path segments must be lowercase separate words with hyphens

Example:

    /shipment-orders/{shipment-order-id}

This applies to concrete path segments and not the names of path parameters. For example `{shipment_order_id}` would be ok as a path parameter. 

## {{ book.must }} Query parameters must be snake_case (never camelCase)

Examples:

    customer_number, order_id, billing_address

## {{ book.must }} JSON field names must be snake_case (never camelCase)

Intentionally consistent with the query parameters.
Examples see above (Query Parameters).

Why: It’s essential to establish a  consistent  look and feel. No established industry standard
exists, but many popular Internet companies prefer snake_case:  e.g. GitHub, Stack Exchange, Twitter.
Others, like Google and Amazon, use both — but not only camelCase.

## {{ book.must }} You Must Hyphenate HTTP Headers

## {{ book.should }} Prefer Hyphenated-Pascal-Case for HTTP header Fields

This is for consistency in your documentation (most other headers follow this convention). Avoid
camelCase (without hyphens). Exceptions are common abbreviations like “ID.”

Examples:

    Accept-Encoding
    Apply-To-Redirect-Ref
    Disposition-Notification-Options
    Original-Message-ID

See also: [HTTP Headers are case-insensitive
(RFC 7230)](http://tools.ietf.org/html/rfc7230#page-22).

## {{ book.could }} Use Standardized Headers

Use [this list](http://en.wikipedia.org/wiki/List_of_HTTP_header_fields) and mention its support in
your OpenAPI definition.

## {{ book.must }} Always Pluralize Resource Names

This is so that APIs can handle a collection of entities where 1 is a special case.

## {{ book.must }} Identify resources and Sub-Resources via Path Segments

Basic URL structure:

    /{resources}/[resource-id]/{sub-resources}/[sub-resource-id]

Examples:

    /carts/1681e6b88ec1/items
    /carts/1681e6b88ec1/items/1

## {{ book.could }} Consider Using  Nested URLs When the Access Pattern Allows

If a sub-resource is only accessible via its parent resource, consider using a nested URL structure:

    /carts/1681e6b88ec1/cart-items/1

If the resource can be accessed directly, however, then the API should expose it as a top-level
resource. For example, sales order items do not exist without sales orders, but some services may
choose to access them directly. Orders do not exist without customers — yet it makes sense to access
orders directly, without looking up the customer number.

## {{ book.could }} First Path segment May be /api

In most cases, all resources provided by a service are part of the public API, and therefore should
be made available under the root “/” base path. If  the service should also support non-public,
internal APIs — for specific operational support functions, for example — add “/api” as base path to
clearly separate public and non-public API resources.

## {{ book.must }} Avoid Trailing Slashes

The trailing slash must not have specific semantics. Resource paths must deliver the same results
whether they have the trailing slash or not.

## {{ book.could }} Use Conventional Query Strings

If you provide query support for sorting, pagination, filtering functions or other actions, use the
following standardized naming conventions:

* `q` — default query parameter (so that its TAB completed from browsers), may have an entity
  specific alias, like sku
* `limit` — to restrict the number of entries. See Pagination section below.
  Hint: You can use size as an alternate query string.
* `cursor` — key-based page start. See Pagination section below.
* `offset` — numeric offset page start. See Pagination section below.
  Hint: In combination with limit, you can use page as an alternative to offset.
* `sort` — comma-separated list of fields to sort. Means “descending.”
* `fields` — to retrieve a subset of fields. See Support Filtering of Resource Fields below
* `embed` — to expand embedded entities (ie.: inside of an article entity, expand silhouette code
  into the silhouette object). Implementing “expand” correctly is difficult, so do it with care. See
  [*Embedding resources*](../hyper-media/Hypermedia.md#should-allow-embedding-of-complex-subresources) for more details.

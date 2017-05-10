# API Naming

## {{ book.must }} Use lowercase separate words with hyphens for Path Segments

Example:

    /shipment-orders/{shipment-order-id}

This applies to concrete path segments and not the names of path parameters. For example `{shipment_order_id}` would be ok as a path parameter.

## {{ book.must }} Use snake_case (never camelCase) for Query Parameters

Examples:

    customer_number, order_id, billing_address

## {{ book.must }} Use Hyphenated HTTP Headers

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

## {{ book.may }} Use Standardized Headers

Use [this list](http://en.wikipedia.org/wiki/List_of_HTTP_header_fields) and mention its support in
your OpenAPI definition.

## {{ book.must }} Pluralize Resource Names

Usually, a collection of resource instances is provided (at least API should be ready here). The special case of a resource singleton is a collection with cardinality 1.

## {{ book.may }} Use /api as first Path Segment

In most cases, all resources provided by a service are part of the public API, and therefore should
be made available under the root “/” base path. If  the service should also support non-public,
internal APIs — for specific operational support functions, for example — add “/api” as base path to
clearly separate public and non-public API resources.

## {{ book.must }} Avoid Trailing Slashes

The trailing slash must not have specific semantics. Resource paths must deliver the same results
whether they have the trailing slash or not.

## {{ book.may }} Use Conventional Query Strings

If you provide query support for sorting, pagination, filtering functions or other actions, use the
following standardized naming conventions:

* `q` — default query parameter (e.g. used by browser tab completion);
  should have an entity specific alias, like sku
* `limit` — to restrict the number of entries. See Pagination section below.
  Hint: You can use size as an alternate query string.
* `cursor` — key-based page start. See Pagination section below.
* `offset` — numeric offset page start. See Pagination section below.
  Hint: In combination with limit, you can use page as an alternative to offset.
* `sort` — comma-separated list of fields to sort. To indicate sorting direction,
  fields my prefixed with + (ascending) or - (descending, default), e.g. /sales-orders?sort=+id
* `fields` — to retrieve a subset of fields. See [*Support Filtering of Resource Fields*](../performance/Performance.md#should-support-filtering-of-resource-fields) below.
* `embed` — to expand embedded entities (ie.: inside of an article entity, expand silhouette code
  into the silhouette object). Implementing “expand” correctly is difficult, so do it with care. See
  [*Embedding resources*](../hyper-media/Hypermedia.md#should-allow-embedding-of-complex-subresources) for more details.

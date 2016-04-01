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

Usually, a collection of resource instances is provided (at least API should be ready here). The special case of a resource singleton is a collection with cardinality 1.

## {{ book.must }} Identify resources and Sub-Resources via Path Segments

Basic URL structure:

    /{resources}/[resource-id]/{sub-resources}/[sub-resource-id]

Examples:

    /carts/1681e6b88ec1/items
    /carts/1681e6b88ec1/items/1

## {{ book.could }} Consider Using (Non-) Nested URLs

If a sub-resource is only accessible via its parent resource and may not exists without parent resource, consider using a nested URL structure, for instance:

    /carts/1681e6b88ec1/cart-items/1

However, if the resource can be accessed directly via its unique id, then the API should expose it as a top-level resource. For example, customer is a collection for sales orders; however, sales orders have globally unique id and some services may choose to access the orders directly, for instance:

    /customer/1681e6b88ec1
    /sales-order/5273gh3k525a

## {{ book.should }} Limit of Resources

To keep maintenance manageable, and to avoid violations of the “separation of concern” principle, an API should not expose more than 16 resources. If you exceed 16 resources, first check if you can split them into separate subdomains with distinct APIs.

## {{ book.should }} Limit of Sub-Resource Levels

There are main resources (with root url paths) and sub-resources (or “nested” resources with non-root urls paths). Use sub-resources if their life cycle is (loosely) coupled to the main resource, i.e. the main resource works as collection resource of the subresource entities. You should use <= 3 sub-resource (nesting) levels -- more levels increase API complexity and url path length. (Remember, some popular web browsers do not support URLs of more than 2000 characters)

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

* `q` — default query parameter (e.g. used by browser tab completion);
  should have an entity specific alias, like sku
* `limit` — to restrict the number of entries. See Pagination section below.
  Hint: You can use size as an alternate query string.
* `cursor` — key-based page start. See Pagination section below.
* `offset` — numeric offset page start. See Pagination section below.
  Hint: In combination with limit, you can use page as an alternative to offset.
* `sort` — comma-separated list of fields to sort. To indicate sorting direction,
  fields my prefixed with + (ascending) or - (descending, default), e.g. /sales-orders?sort=+id
* `fields` — to retrieve a subset of fields. See [*Support Filtering of Resource Fields*](../http/Http.md#should-support-filtering-of-resource-fields) below.
* `embed` — to expand embedded entities (ie.: inside of an article entity, expand silhouette code
  into the silhouette object). Implementing “expand” correctly is difficult, so do it with care. See
  [*Embedding resources*](../hyper-media/Hypermedia.md#should-allow-embedding-of-complex-subresources) for more details.

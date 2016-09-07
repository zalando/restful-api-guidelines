# Pagination

## {{ book.must }} Support Pagination

Access to lists of data items must support pagination for best client side batch processing and iteration experience. This holds true for all lists that are (potentially) larger than just a
few hundred entries.

There are two page iteration techniques:

* [Offset/Limit-based pagination](http://developer.infoconnect.com/paging-results-limit-and-offset):
  numeric offset identifies the first page entry
* [Cursor-based](https://dev.twitter.com/overview/api/cursoring) — aka key-based — pagination: a
  unique key element identifies the first page entry (see also
  [Facebook’s guide](https://developers.facebook.com/docs/graph-api/using-graph-api/v2.4#paging))

The technical conception of pagination should also consider user experience related issues. As mentioned
in this [article](https://www.smashingmagazine.com/2016/03/pagination-infinite-scrolling-load-more-buttons/),
jumping to a specific page is far less used than navigation via next/previous page links. This favours
cursor-based over offset-based pagination.

## {{ book.should }} Prefer Cursor-Based Pagination, Avoid Offset-Based Pagination

Cursor-based pagination is usually better and more efficient when compared to offset-based pagination. Especially when it comes to
high-data volumes and / or storage in NoSQL databases.

Before choosing cursor-based pagination, consider the following trade-offs:

* Usability/framework support:

    * Offset / limit based pagination is more known than cursor-based pagination, so it has more framework support and
      is easier to use for API clients

* Use case: Jump to a certain page

    * If jumping to a particular page in a range (e.g., 51 of 100) is really a required use case,
      cursor-based navigation is not feasible

* Variability of data may lead to anomalies in result pages

    * Offset-based pagination may create duplicates or lead to missing entries if rows are inserted or deleted between two subsequent paging requests.
    * When using cursor-based pagination, paging cannot continue when the cursor entry has been
      deleted while fetching two pages

* Performance considerations - efficient server-side processing using offset-based pagination is hardly feasible for:

    * Higher data list volumes, especially if they do not reside in the database’s main memory
    * Sharded or NoSQL databases

* Cursor-based navigation may not work if you need the total count of results and / or backward iteration support


Further reading:

* [Twitter](https://dev.twitter.com/rest/public/timelines)
* [Use the Index, Luke](http://use-the-index-luke.com/no-offset)
* [Paging in PostgreSQL](https://www.citusdata.com/blog/1872-joe-nelson/409-five-ways-paginate-postgres-basic-exotic)


## {{ book.could }} Use Pagination Links Where Applicable

* Set links to provide information to the client about subsequent paging options.

For example:

```http
HTTP/1.1 200 OK
Content-Type: application/x.zalando.products+json

{
  "_links": {
    "next": {
      "href": "http://catalog-service.zalando.net/products?offset=15&limit=5"
    },
    "prev": {
      "href": "http://catalog-service.zalando.net/products?offset=5&limit=5"
    }
  },
  "total_count": 42,
  "products": [
    {"id": "7e4ab218-1772-11e6-892c-836df3feeaee"},
    {"id": "9469725a-1772-11e6-83c2-ab22ac368913"},
    {"id": "a3625d80-1772-11e6-9213-d3a20f9e6bf4"},
    {"id": "a802f070-1772-11e6-a772-c3020d55eb5f"},
    {"id": "adb407ac-1772-11e6-b255-078fb28ff55b"}
  ]
}
```

[Possible relations](http://www.iana.org/assignments/link-relations/link-relations.xml):
`next`, `prev`, `last`, `first`.

Previous editions of the guidelines documented the `X-Total-Count` header to send back the total count of entities in conjunction with the `Link` header, which has since been deprecated. Instead when returning an object structure, the count can be added as a JSON property, and this is the preferred way to return count (or other result level) information. 

The `X-Total-Count` is applicable in these cases:

* The API design is still using the `Link` header.
* The API is returning a non-JSON response media type, and isn't able to carry the information.
* The JSON response is an array and not an object.

You should avoid providing a total count in your API unless there's a clear need to do so. Very often, there are systems and performance implications to supporting full counts, especially as datasets grow and requests become complex queries or filters that drive full scans (e.g., your database might need to look at all candidate items to count them). While this is an implementation detail relative to the API, it's important to consider your ability to support serving counts over the life of a service.

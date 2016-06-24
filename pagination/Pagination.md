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


## {{ book.could }} Use Pagination Headers Where Applicable

* Set `X-Total-Count` to send back the total count of entities.
* Set the [link headers](http://tools.ietf.org/html/rfc5988#section-5)
  to provide information to the client about subsequent paging options.

For example:

    Link: <http://catalog-service.zalando.net/articles?cursor=62726863268328&limit=100>;
          rel="next"; title="next chunk of articles"

or

    Link: <http://catalog-service.zalando.net/articles?offset=5&limit=100>;
          rel="prev"; title="previous chunk of articles"
    Link: <http://catalog-service.zalando.net/articles?offset=205&limit=100>;
          rel="next"; title="next chunk of articles"


[Possible relations](http://www.iana.org/assignments/link-relations/link-relations.xml):
`next`, `prev`, `last`, `first`

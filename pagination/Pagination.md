# Pagination

## {{ book.must }} Support Pagination

For batch processing, and for optimizing the client iteration experience, access to lists of data
items must support pagination. This holds for all lists that are (potentially) larger than just a
few hundred entries.

There are two page iteration techniques:

* [Offset/Limit-based pagination](http://developer.infoconnect.com/paging-results-limit-and-offset):
  numeric offset identifies the first page entry
* [Cursor-based](https://dev.twitter.com/overview/api/cursoring) — aka key-based — pagination: a
  unique key element identifies the first page entry (see also
  [Facebook’s guide](https://developers.facebook.com/docs/graph-api/using-graph-api/v2.4#paging))


## {{ book.should }} Prefer Cursor-Based Pagination, Avoid Offset-Based Pagination

Cursor-based pagination is better for more efficient implementation, especially when it comes to
high-data volumes and / or storage in NoSQL databases.

Before choosing offset-based pagination,  make sure that it is feasible for efficient realization.
Carefully consider the following trade-offs:

* Usability/framework support
* * offset/limit pagination is more familiar than cursor-based, so it has more framework support and
    is easier to use for API clients
* Use case: Jump to a certain page
* * If jumping to a particular page in a range (e.g., 51 of 100) is a required use case,
    keyset-navigation is not feasible
* Variability of data may lead to anomalies in result pages
* * Using offset will create duplicates or lead to missed entries if rows are inserted or  deleted,
    respectively, between fetching two pages
* * When using cursor-based pagination, paging cannot continue when the cursor entry has been
    deleted while fetching two pages
* Performance considerations
* * Efficient server-side processing using offset-based pagination is hardly feasible for higher
    data list volumes, especially if they do not reside in the database’s main memory
* * sharded or NoSQL data storage systeThis also holds for total count and backward iteration support

Further reading:

* [Twitter](https://dev.twitter.com/rest/public/timelines)
* [Use the Index, Luke](http://use-the-index-luke.com/no-offset)

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
#Compatibility

## {{ book.must }} Don’t Break Backward Compatibility

Change APIs, but keep all consumers
running. Consumers usually have independent release lifecycles, focus on
stability, and avoid changes that do not provide additional value. APIs are
service contracts that cannot be broken via unilateral decisions.

There are two techniques to change APIs without breaking them:

- follow rules for compatible extensions
- introduce new API versions that alsore support older versions

We strongly encourage using compatible API extensions and discourage versioning.
With Postel’s Law in mind, here are some rules for providers and consumers that
allow us to make compatible changes without versioning:

## {{ book.should }} Prefer Compatible Extensions

Apply the following rules to evolve RESTful APIs in a backward-compatible way:

* Ignore unknown fields in the payload
* Add optional fields
* Never change a field’s meaning. For values sent from the client to the server, extend enum ranges
  only with new values (i.e. a server must continue to support all values which were previously valid).
* For values sent from the server to the client, reduce enum ranges (i.e., a server can stop sending
  something), not larger (i.e., the server must not send values which previously were not in the
  OpenAPI definition). When a finite set of values can be returned from (or
  sent to) the server, or can grow in the future, use x-extensible-enum (same
  syntax as OpenAPI's enum). This is to be handled by clients as a list of example
  values (maybe with defined meanings), not a list of "only these values can be
  sent”. (If appropriate, the description should tell how unknown values are to be
  handled.)
* Support redirection in case an URL has to change
 ([301 Moved Permanently](https://en.wikipedia.org/wiki/HTTP_301))


## {{ book.must }} Prepare Clients for Compatible API Extensions (the Robustness Principle)

How to do this:

* Ignore new and unknown fields in the payload (see also Fowler’s
  “[TolerantReader](http://martinfowler.com/bliki/TolerantReader.html)” post)
* Be prepared for new enum values declared with x-extensible-enum (see above);
  provide default behavior for unknown values, if applicable
* Follow the redirect when the server returns an “HTTP 301 Moved Permanently” response code

## {{ book.should }} Avoid Versioning

When changing your RESTful APIs,
do so in a compatible way and avoid generating additional API versions. Multiple
versions can significantly complicate understanding, testing, maintaining,
evolving, operating and releasing our systems ([supplementary reading](http://martinfowler.com/articles/enterpriseREST.html)).

If changing an API can’t be done in a compatible way, then proceed in one of these
three ways:

* create a new resource (variant) in addition to the old resource variant
* create a new service endpoint — i.e. a new microservice application with a new API (variant)
* create a new API version supported in parallel with the old API by the same microservice

Avoiding versioning also means preferring the first two variants. When
versioning is unavoidable, you can follow one of two approaches to design
multi-version RESTful APIs:

URL versioning: Here, a (major) version number is
included in the path, e.g. /v1/customers. The consumer has to wait until the
provider has been released and deployed. If the consumer also supports
hypermedia links — even in their APIs — to drive workflows, this quickly becomes
complex. So does coordinating version upgrades — especially with hyperlinked
service dependencies — when using URL versioning.

Media type versioning: Here,
version information and media type are provided together via the HTTP
Content-Type header — e.g. application/x.zalando.cart+json;version=2. For
incompatible changes, a new media type version for the resource is created. To
generate the new representation version, consumer and producer can do content
negotiation using the HTTP Content-Type and Accept headers. Note: This
versioning only applies to the content schema, not to URI or method semantics.

In this example, a client wants only the new version:

    Content-Type: application/x.zalando.cart+json;version=2
    Accept: application/x.zalando.cart+json;version=2

Using header versioning should:

* include versions in request and response headers to increase visibility
* include Content-Type in the Vary header to enable proxy caches to differ between versions

Hint: [OpenAPI currently doesn’t support content
negotiation](https://github.com/OAI/OpenAPI-Specification/issues/146), though [a comment in this
issue](https://github.com/OAI/OpenAPI-Specification/issues/146#issuecomment-117288707) mentions
a workaround (using a fragment identifier that
gets stripped off).

## {{ book.must }} Use Media Type Versioning, Not URL Versioning

Why: URL versioning leads to tighter coupling of a (hyperlinked) consumer and producer
with higher complexity of release management. Media type versioning is less
tightly coupled and supports content negotiation (see above).

## {{ book.should }} Provide Version Information in OpenAPI Documentation

Only the documentation, not the API itself, needs this information. Given a version number
`MAJOR.MINOR.DRAFT`,
increment the: `MAJOR` version, when you make incompatible API changes; `MINOR`
version, when you add functionality in a backwards-compatible manner; and `DRAFT`
version, when you make changes during the review phase that are unrelated to
production releases.

Example:

    "swagger": "2.0",
    "info": {
      "title": "Parcel service API",
      "description": "API for <...>",
      "version": "1.0.0",
        <...>
    }


This is different from [semantic version information](http://semver.org/), which is used for APIs of
released services or libraries but inadequate for versioning API descriptions. During a long-running
API review phase you need different versions of the API description. These versions may include
changes that are incompatible with earlier draft versions. We recommend using the draft version only
for unreleased API definitions that are still under review .

For example:

    version 1.4.0  -- current version
    version 1.4.1  -- first draft and call for review of API extensions compatible with 1.4.0
    version 1.4.2  -- second draft and call for review of API extensions that are still
                       compatible with 1.4.0 but possibly incompatible with 1.4.1
    version 1.5.0  -- approved version for implementation and release
    version 1.5.1  -- first draft for next review and API change cycle;
                       compatible with 1.4.0 and 1.5.0
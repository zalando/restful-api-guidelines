# Data Formats

## {{ book.must }} Use JSON as the Body Payload

JSON-encode the body payload. The JSON payload must follow RFC-7158 and be compatible with RFC-4627 clients by having (if possible) a serialized object or array as the top-level object.

## {{ book.must }} Use Standard Date and Time Formats

* Inside the JSON payload, use the date and time formats defined by [RFC 3339](http://tools.ietf.org/html/rfc3339#section-5.6) — e.g. `2015-05-28T14:09:17+02:00` for a point in time (note that the  [OpenAPI format](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#data-types) "date-time" corresponds to "date-time" in the RFC) and `2015-05-28` for a date (note that the OpenAPI format "date" corresponds to "full-date" in the RFC). Both are specific profiles, a subset of the international standard [ISO 8601](http://en.wikipedia.org/wiki/ISO_8601).

* In HTTP headers (including the proprietary headers), use the [HTTP date format defined in RFC 7231](http://tools.ietf.org/html/rfc7231#section-7.1.1.1).

Timestamps are passed in UTC-related format via APIs and should be stored in UTC (i.e. without any offset information). Localization based on UTC should be done by the services that provide user interfaces, if required.

## {{ book.could }} Use Standard Data Formats

* [ISO 3166-1-alpha2 country codes](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)

* * (It's “GB”, not “UK”, even though “UK” has seen some use at Zalando)

* [ISO 639-1 language code](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)

* * [BCP-47](https://tools.ietf.org/html/bcp47) (based on ISO 639-1) for language variants

* [ISO 4217 currency codes](http://en.wikipedia.org/wiki/ISO_4217)


## {{ book.could }} Use Standards for Country, Language and Currency Codes

Use the following standard formats for country, language and currency codes:
* [ISO 3166-1-alpha2 country codes](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
* * (It is “GB”, not “UK”, even though “UK” has seen some use at Zalando)
* [ISO 639-1 language code](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)
* * [BCP-47](https://tools.ietf.org/html/bcp47) (based on ISO 639-1) for language variants
* [ISO 4217 currency codes](https://en.wikipedia.org/wiki/ISO_4217)

## {{ book.could }} Use Application-Specific Content Types

For instance, `application/x.zalando.article+json`. For complex types, it’s better to have a
specific content type. For simple use cases this isn’t necessary. We can attach version info to
media type names and support content negotiation to get different representations, e.g.
`application/x.zalando.article+json;version=2`.

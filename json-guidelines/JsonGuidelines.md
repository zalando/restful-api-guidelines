# JSON Guidelines

These guidelines provides recommendations for defining JSON data at Zalando. JSON here refers to [RFC 7159](http://www.rfc-editor.org/rfc/rfc7159.txt) (which updates [RFC 4627](https://www.ietf.org/rfc/rfc4627.txt)), the “application/json” media type and custom JSON media types defined for APIs. The guidelines clarifies some specific cases to allow Zalando JSON data to have an idiomatic form across teams and services. 

## Property Naming

### {{ book.must }} Property names must be snake_case (and never camelCase).

No established industry standard exists, but many popular Internet companies prefer snake_case: e.g. GitHub, Stack Exchange, Twitter. Others, like Google and Amazon, use both - but not only camelCase. It’s essential to establish a consistent look and feel such that JSON looks as if it came from the same hand. 

### {{ book.must }} Property names must be an ASCII subset

Property names are restricted to ASCII encoded strings. The first character must be a letter, an underscore  or a dollar sign, and subsequent characters can be a letter, an underscore, a dollar sign, or a number.

### {{ book.should }} Reserved JavaScript keywords should be avoided

Most API content is consumed by non-JavaScript clients today, but for security and sanity reasons, JavaScript (strictly, ECMAScript) keywords are worth avoiding. A list of keywords can be found in the [ECMAScript Language Specification](http://www.ecma-international.org/ecma-262/6.0/#sec-reserved-words).

### {{ book.should }} Array names should be pluralized

To indicate they contain multiple values prefer to pluralize array names. This implies that object names should in turn be singular.

## Property Values

### {{ book.must }} Boolean property values must not be null

Schema based JSON properties that are by design booleans must not be presented as nulls. A boolean is essentially a closed enumeration of two values, true and false. If the content has a meaningful null value, strongly prefer to replace the boolean with enumeration of named values or statuses - for example accepted_terms_and_conditions with true or false can be replaced with terms_and_conditions with values yes, no and unknown.

### {{ book.should }} Null values should have their fields removed

Swagger/OpenAPI, which is in common use, doesn't support null field values (it does allow omitting that field completely if it is not marked as required). However that doesn't prevent clients and servers sending and receiving those fields with null values. Also, in some cases null may be a meaningful value - for example, JSON Merge Patch [RFC 7382](https://tools.ietf.org/html/rfc7386)) using null to indicate property deletion.

### {{ book.should }} Empty array values should not be null

Empty array values can unambiguously be represented as the the empty list, `[]`.

### {{ book.should }} Enumerations should be represented as Strings

Strings are a reasonable target for values that are by design enumerations. 

### {{ book.should }} Date property values should conform to RFC 3399

Use the date and time formats defined by [RFC 3339](http://tools.ietf.org/html/rfc3339#section-5.6) — e.g. `2015-05-28T14:09:17+02:00` for a point in time (note that the  [OpenAPI format](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#data-types) "date-time" corresponds to "date-time" in the RFC) and `2015-05-28` for a date (note that the OpenAPI format "date" corresponds to "full-date" in the RFC). Both are specific profiles, a subset of the international standard [ISO 8601](http://en.wikipedia.org/wiki/ISO_8601).

A zone offset could be used, however, we encourage  restricting dates to UTC and without offsets:  for example `2015-05-28T14:07:17Z` rather than `2015-05-28T14:07:17+00:00`. From experience we have learned that zone offsets are not easy to understand and often not correctly handled. Note also that zone offsets are different from local times that might be including daylight saving time. Localization of dates should be done by the services that provide user interfaces, if required.

When it comes to storage, all dates should be consistently stored in UTC without a zone offset. 

Sometimes it can seem data is naturally represented using numerical timestamps, but this can introduce interpretation issues with precision - for example whether to represent a timestamp as 1460062925, 1460062925000 or 1460062925.000. Date strings, though more verbose and requiring more effort to parse, avoid this ambiguity.

### {{ book.could }} Time durations and intervals could conform to ISO 8601

Schema based JSON properties that are by design durations and intervals could be strings formatted as recommended by ISO 8601 ([Appendix A of RFC 3399 contains a grammar](https://tools.ietf.org/html/rfc3339#appendix-A) for durations). 

### {{ book.could }} Standards could be used for Language, Country and Currency 

- [ISO 3166-1-alpha2 country 
](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
 - (It's "GB", not "UK", even though "UK" has seen some use at Zalando)
- [ISO 639-1 language code](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)
 - [BCP-47](https://tools.ietf.org/html/bcp47) (based on ISO 639-1) for language variants
- [ISO 4217 currency codes](http://en.wikipedia.org/wiki/ISO_4217)


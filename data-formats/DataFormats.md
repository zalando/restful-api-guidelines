# Data Formats

## {{ book.must }} Use JSON as the Body Payload

JSON-encode the body payload. The JSON payload must follow [RFC-7159](https://tools.ietf.org/html/rfc7159) by having
(if possible) a serialized object or array as the top-level object.

## {{ book.must }} Use Standard Date and Time Formats

###JSON Payload
Read more about date and time format in [Json Guideline](../json-guidelines/JsonGuidelines.md#date-property-values-should-conform-to-rfc-3399).

###HTTP headers
Http headers including the proprietary headers. Use the [HTTP date format defined in RFC 7231](http://tools.ietf.org/html/rfc7231#section-7.1.1.1).

## {{ book.could }} Use Standards for Country, Language and Currency Codes

Use the following standard formats for country, language and currency codes:

* [ISO 3166-1-alpha2 country codes](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)

     * (It is “GB”, not “UK”, even though “UK” has seen some use at Zalando)

* [ISO 639-1 language code](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)

    * [BCP-47](https://tools.ietf.org/html/bcp47) (based on ISO 639-1) for language variants

* [ISO 4217 currency codes](https://en.wikipedia.org/wiki/ISO_4217)

## {{ book.could }} Use Application-Specific Content Types

For instance, `application/x.zalando.article+json`. For complex types, it’s better to have a
specific content type. For simple use cases this isn’t necessary. We can attach version info to
media type names and support content negotiation to get different representations, e.g.
`application/x.zalando.article+json;version=2`.
# Data Formats

## {{ book.must }} Use JSON as the Body Payload

JSON-encode the body payload. The JSON payload must follow [RFC-7159](https://tools.ietf.org/html/rfc7159) by having
(if possible) a serialized object as the top-level structure, since it would allow for future extension.
This also applies for collection resources where one naturally would assume an array. See the
[pagination](../pagination/Pagination.md#could-use-pagination-links-where-applicable) section for an example.

## {{ book.may }} Use other Media Types than JSON

If for given use case JSON does not make sense, for instance when providing attachments in form of PDFs, you should
use another, more sufficient media type. But only do this if you can not transfer the information in JSON.

## {{ book.must }} Use Standard Date and Time Formats

###JSON Payload
Read more about date and time format in [Json Guideline](../json-guidelines/JsonGuidelines.md#date-property-values-should-conform-to-rfc-3399).

###HTTP headers
Http headers including the proprietary headers. Use the [HTTP date format defined in RFC 7231](http://tools.ietf.org/html/rfc7231#section-7.1.1.1).

## {{ book.may }} Use Standards for Country, Language and Currency Codes

Use the following standard formats for country, language and currency codes:

* [ISO 3166-1-alpha2 country codes](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)

     * (It is “GB”, not “UK”, even though “UK” has seen some use at Zalando)

* [ISO 639-1 language code](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)

    * [BCP-47](https://tools.ietf.org/html/bcp47) (based on ISO 639-1) for language variants

* [ISO 4217 currency codes](https://en.wikipedia.org/wiki/ISO_4217)

## {{ book.should }} Prefer standard Media type name `application/json`

Previously, this guideline allowed the use of custom media types like `application/x.zalando.article+json`.
This usage is not recommended anymore and should be avoided, except where it is necessary for cases
of [media type versioning](../compatibility/Compatibility.md#must-use-media-type-versioning). Instead, the standard media type name `application/json` (or [`application/problem+json` for HTTP error details](http://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html#must-use-problem-json)) should be used for JSON-formatted data.

Custom media types with subtypes beginning with `x` bring no advantage compared to the standard media type for JSON, and make automated processing more difficult. They are also [discouraged by RFC 6838](https://tools.ietf.org/html/rfc6838#section-3.4).
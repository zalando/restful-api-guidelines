# Common Data Objects

Definitions of data objects that are good candidates for wider usage:

## {{ book.should }} Use a Common Money Object

Use the following common money structure:

    Money:
      type: object
      properties:
        amount:
          type: number
          description: Amount expressed as a decimal number of major currency units
          format: decimal
          example: 99.95
        currency:
          type: string
          description: 3 letter currency code as defined by ISO-4217
          format: iso-4217
          example: EUR
      required:
        - amount
        - currency

The decimal values for "amount" describe unit and subunit of the currency in a single value, where
the digits before the decimal point are for the major unit and the digits after the decimal point are
for the minor unit. Note that some business cases (e.g. transactions in Bitcoin) call for a higher
precision, so applications must be prepared to accept values with unlimited precision, unless
explicitly stated otherwise in the API specification.
Examples for correct representations (in EUR):

- `42.20` or `42.2` = 42 Euros, 20 Cent
- `0.23` = 23 Cent
- `42.0` or `42` = 42 Euros
- `1024.42` = 1024 Euros, 42 Cent
- `1024.4225` = 1024 Euros, 42.25 Cent

Make sure that you don’t convert the “amount” field to `float` / `double` types when implementing
this interface in a specific language or when doing calculations. Otherwise, you might lose
precision. Instead, use exact formats like
Java’s [`BigDecimal`](https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html).
See [Stack Overflow](http://stackoverflow.com/a/3730040/342852) for more info.

Some JSON parsers (NodeJS’s, for example) convert numbers to floats by default. After discussing the
[pros and cons](https://docs.google.com/spreadsheets/d/12wTj-2w39f69XZGwRDrosNc1yWPwQpGgEs_DCt5ODaQ),
we’ve decided on "decimal" as our amount format. It is not a standard OpenAPI format, but should
help us to avoid parsing numbers as float / doubles.

## {{ book.should }} Use Common Address Fields

Address structures play a role in different functional and use-case contexts, including country
variances. The address structure below should be sufficient for most of our business-related use
cases. Use it in your APIs — and compatible extend it if necessary for your API concerns:

    address:
        description:
          a common address structure adequate for many use cases
        type: object
        required:
          - first_name
          - last_name
          - street
          - city
          - zip
          - country_code
        properties:
          salutation:
            type: string
            description: |
              A salutation and/or title which may be used for personal contacts. Hint: not to be confused with the gender information that is stored per customer account
            example: Mr
          first_name:
            type: string
            description: given name(s) or first name(s) of a person; may also include the middle names
            example: Hans Dieter
          last_name:
            type: string
            description: family name(s) or surname(s) of a person
            example: Mustermann
          business_name:
            type: string
            description: company name of the business organization
            example: Consulting Services GmbH
          street:
            type: string
            description: full street address including house number and street name
            example: Schönhauser Allee 103
          additional:
            type: string
            description: further details like suite, apartment number, etc.
            example: 2. Hinterhof rechts
          city:
            type: string
            description: name of the city
            example: Berlin
          zip:
            type: string
            description: Zip code or postal code
            example: 14265
          country_code:
            type: string
            format: iso-3166-1-alpha-2
            example: DE

## {{ book.must }} Use Problem JSON

[RFC 7807](http://tools.ietf.org/html/rfc7807) defines the media type `application/problem+json`.
Operations should return that (together with a suitable status code) when any problem
occurred during processing and you can give more details than the status code itself
can supply, whether it be caused by the client or the server (i.e. both for 4xx or 5xx errors).

A previous version of this guideline (before the publication of that RFC and the
registration of the media type) told to return `application/x.problem+json` in these
cases (with the same contents).
Servers for APIs defined before this change should pay attention to the `Accept` header sent
by the client and set the `Content-Type` header of the problem response correspondingly.
Clients of such APIs should accept both media types.

APIs may define custom problems types with extension properties, according to their specific needs.

The Open API schema definition can be found [on github](https://zalando.github.io/problem/schema.yaml).
You can reference it by using:

```yaml
responses:
  503:
    description: Service Unavailable
    schema:
      $ref: 'https://zalando.github.io/problem/schema.yaml#/Problem'
        
```

## {{ book.must }} Do not expose Stack Traces

Stack traces contain implementation details that are not part of an API, and on which clients
should never rely. Moreover, stack traces can leak sensitive information that partners and third
parties are not allowed to receive and may disclose insights about vulnerabilities to attackers.

## {{ book.must }} Use common field names

There are some data fields that come up again and again in API data. We describe four here:

- `id`: the identity of the object. If used, IDs must opaque strings and not numbers. IDs are unique within some documented context, are stable and don't change for a given object once assigned, and are never recycled cross entities. 

- `created`: when the object was created. If used this must be a date-time construct.

- `modified`: when the object was updated. If used this must be a date-time construct.

- `type`: the kind of thing this object is. If used the type of this field should be a string. Types allow runtime information on the entity provided that otherwise requires examining the Open API file. 

These properties are not always strictly neccessary, but making them idiomatic allows API client developers to build up a common understanding of Zalando's resources. There is very little utility for API consumers in having different names or value types for these fields across APIs. 

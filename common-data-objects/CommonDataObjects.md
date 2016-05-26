# Common Data Objects

Definitions of data objects that are good candidates for wider usage:

## {{ book.should }} Use a Common Money Object

Use the following common money structure:

    Money:
      type: object
      properties:
        amount:
          type: number
          format: decimal
          example: 99.95
        currency:
          type: string
          format: iso-4217
          example: EUR
      required:
        - amount
        - currency

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
        required:
          - first_name
          - last_name
          - street
          - city
          - zip
          - country_code

## {{ book.must }} Use Common Error Return Objects

[RFC 7807](http://tools.ietf.org/html/rfc7807) defines the media type `application/problem+json`
for a common error return object (and the format of the contained JSON object).
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

Here is the definition in Open API 2.0 YAML format in the case you don't need any extensions:

    Problem:
     type: object
     properties:
       type:
         type: string
         format: uri
         description: |
           An absolute URI that identifies the problem type.  When dereferenced,
           it SHOULD provide human-readable documentation for the problem type
           (e.g., using HTML).
         example: http://httpstatus.es/503
       title:
         type: string
         description: |
           A short, summary of the problem type. Written in english and readable
           for engineers (usually not suited for non technical stakeholders and
           not localized); example: Service Unavailable
       status:
         type: integer
         format: int32
         description: |
           The HTTP status code generated by the origin server for this occurrence
           of the problem.
         example: 503
       detail:
         type: string
         description: |
           A human readable explanation specific to this occurrence of the
           problem.
         example: Connection to database timed out
       instance:
         type: string
         format: uri
         description: |
           An absolute URI that identifies the specific occurrence of the problem.
           It may or may not yield further information if dereferenced.
     required:
       - type
       - title
       - status


## {{ book.must }} An error message must not contain the stack trace.

Stack traces contain implementation details that are not part of an API, and on which clients
should never rely. Moreover, stack traces can leak sensitive information that partners and third
parties are not allowed to receive and may disclose insights about vulnerabilities to attackers.

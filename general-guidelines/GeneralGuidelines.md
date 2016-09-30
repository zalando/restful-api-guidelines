# General Guidelines

The titles are marked with the corresponding labels: {{ book.must }}, {{ book.should }}, {{ book.could }}.

## {{ book.must }} Provide API Reference Definition using OpenAPI

API First is one of our architecture principles, as per our Architecture Rules of Play.
Define, document and review your APIs before delivering them (also see API review procedure).
Whether you choose YAML or JSON as a format of your OpenAPI API specification file is up to you; 
however, YAML is generally preferred due to its improved readability. 
We also call the OpenAPI API specification the "API Reference Definition"; 
it provides all information needed by an experienced API user developer to use this API. 



## {{ book.should }} Provide User Manual Documentation

In addition to defining the API as OpenAPI Reference Definition, it’s good practice to provide 
further documentation to improve client developer experience, especially of engineers that 
are novices in using this API. A helpful API User Manual documentation typically describes 
the following API aspects:

- API’s scope, purpose and use cases
- concrete usage examples 
- architecture context - including figures and sequence flows
- edge cases and possible error situations

The User Manual must be posted online, e.g. via GitHub Enterprise pages, on specific 
team web servers, or as a Google doc. And don't forget to include a link to this 
user manual documentation into your OpenAPI definition using the “externalDocs” property.

## {{ book.must }} Write APIs in U.S. English

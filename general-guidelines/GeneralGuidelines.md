# General Guidelines

The titles are marked with the corresponding labels: {{ book.must }}, {{ book.should }}, {{ book.could }}.

## {{ book.must }} API First: Define APIs using OpenAPI

API First is one of our architecture principles, as per our Architecture Rules of Play.
Define, document and review your APIs before delivering them (also see API review procedure).
Whether you choose YAML or JSON as a format of your specification file is up to you.

## {{ book.should }} Provide External Documentation

In addition to defining the API with OpenAPI, it’s good practice to provide documentation that
describes the API’s scope and purpose; architecture and use-case context (including figures);
sequence flows; edge cases and possible error situations, etc. Include a link to this documentation
using the “externalDocs” property in your RESTful API OpenAPI definition and post it online on
GitHub Enterprise pages, on specific team web servers, or as a Google doc.

## {{ book.must }} Write APIs in U.S. English

# General Guidelines

The titles are marked with the corresponding labels: {{ book.must }}, {{ book.should }}, {{ book.could }}.

## {{ book.must }} API First: Define APIs using  Swagger YAML or JSON

API First is one of our architecture principles, as per our Architecture Rules of Play.
Define, document and review your APIs before delivering them (also see API review procedure).

## {{ book.should }} Provide External Documentation

In addition to defining the API with Swagger, it’s good practice to provide documentation that
describes the API’s scope and purpose; architecture and use-case context (including figures);
sequence flows; edge cases and possible error situations, etc. Include a link to this documentation
using the “externalDocs” property in your RESTful API Swagger definition and post it online on
GitHub Enterprise pages, on specific team web servers, or as a Google doc.

## {{ book.must }} Write APIs in U.S. English

## {{ book.must }} Avoid Actions — Think About Resources

REST is all about your resources, so consider the kinds of entities that take part in this action,
and aim to model your API around these. For instance, if an application has to lock articles
explicitly so that only one user may edit them, create an article lock with PUT or POST instead of
using a lock action.

Request:

    PUT /article-locks/{article-id}

The added benefit is that you already have a service for browsing and filtering article locks.

## {{ book.must }} Keep URLs Verb-Free

The API describes resources, so the only place where actions should appear is in the HTTP methods.
In URLs, use only nouns.
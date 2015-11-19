# General Guidelines

[M] Write APIs in U.S. English  

[M] Avoid Actions — Think About Resources

REST is all about your resources, so consider the kinds of entities that take part in this action,
and aim to model your API around these. For instance, if an application has to lock articles
explicitly so that only one user may edit them, create an article lock with PUT or POST instead of
using a lock action.

Request:

    PUT /article-locks/{article-id}

The added benefit is that you already have a service for browsing and filtering article locks.

[M] Keep URLs Verb-Free

The API describes resources, so the only place where actions should appear is in the HTTP methods.
In URLs, use only nouns.
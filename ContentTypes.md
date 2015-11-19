# Content Types

[C] Use Application-Specific Content Types

For instance, `application/x.zalando.article+json`. For complex types, it’s better to have a specific content type. For simple use cases this isn’t necessary.
We can attach version info to media type names and support content negotiation to get different representations, e.g. `application/x.zalando.article+json;version=2`.
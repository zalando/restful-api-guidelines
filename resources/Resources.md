# Resources

## {{ book.must }} Avoid Actions — Think About Resources

REST is all about your resources, so consider the domain entities that take part in web service interaction, and aim to model your API around these using the standard HTTP methods as operation indicators. For instance, if an application has to lock articles explicitly so that only one user may edit them, create an article lock with PUT or POST instead of using a lock action.

Request:

    PUT /article-locks/{article-id}

The added benefit is that you already have a service for browsing and filtering article locks.

## {{ book.should }} Define *useful* resources

As a rule of thumb resources should be defined to cover 90% of all its client's use cases. A *useful* resource should
contain as much information as necessary, but as little as possible. A great way to support the last 10% is to allow
clients to specify their needs for more/less information by supporting filtering and
[embedding](../hyper-media/Hypermedia.md#should-allow-embedding-of-complex-subresources).

## {{ book.must }} Keep URLs Verb-Free

The API describes resources, so the only place where actions should appear is in the HTTP methods.
In URLs, use only nouns.

## {{ book.must }} Use Domain-Specific Resource Names

API resources represent elements of the application’s domain model. Using domain-specific nomenclature for resource names helps developers to understand the functionality and basic semantics of your resources. It also reduces the need for further documentation outside the API definition. For example, “sales-order-items” is superior to “order-items” in that it clearly indicates which business object it represents. Along these lines, “items” is too general.

## {{ book.must }} Identify resources and Sub-Resources via Path Segments

Basic URL structure:

    /{resources}/[resource-id]/{sub-resources}/[sub-resource-id]

Examples:

    /carts/1681e6b88ec1/items
    /carts/1681e6b88ec1/items/1

## {{ book.could }} Consider Using (Non-) Nested URLs

If a sub-resource is only accessible via its parent resource and may not exists without parent resource, consider using a nested URL structure, for instance:

    /carts/1681e6b88ec1/cart-items/1

However, if the resource can be accessed directly via its unique id, then the API should expose it as a top-level resource. For example, customer is a collection for sales orders; however, sales orders have globally unique id and some services may choose to access the orders directly, for instance:

    /customers/1681e6b88ec1
    /sales-orders/5273gh3k525a

## {{ book.should }} Limit number of Resources

To keep maintenance and service evolution manageable, we should follow "functional segmentation" and "separation of concern" design principles and do not mix different business functionalities in same API definition. In this sense the number of resources exposed via API should be limited - our experience is that a typical range of resources for a well-designed API is between 4 and 8. There may be exceptions with more complex business domains that require more resources, but you should first check if you can split them into separate subdomains with distinct APIs.

## {{ book.should }} Limit number of Sub-Resource Levels

There are main resources (with root url paths) and sub-resources (or “nested” resources with non-root urls paths). Use sub-resources if their life cycle is (loosely) coupled to the main resource, i.e. the main resource works as collection resource of the subresource entities. You should use <= 3 sub-resource (nesting) levels -- more levels increase API complexity and url path length. (Remember, some popular web browsers do not support URLs of more than 2000 characters)

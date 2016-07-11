# Security

## {{ book.must }} Secure Endpoints with OAuth 2.0

Every API endpoint needs to be secured using OAuth 2.0. Please refer to the 
[official OpenAPI spec](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#security-definitions-object)
on how to specify security definitions in you API specification or take a look at the following example.

```yaml
securityDefinitions:
  oauth2:
    type: oauth2
    flow: implicit
    authorizationUrl: https://auth.zalando.com/oauth2/access_token?realm=services
    scopes:
      fulfillment-order-service.read: Access right needed to read from the fulfillment order service.
      fulfillment-order-service.write: Access right needed to write to the fulfillment order service.      
```

The example defines OAuth2 with implicit flow as security standard used for authentication when accessing endpoints; additionally, there are two API access rights defined via the scopes section for later endpoint authorization usage - please see next section.

## {{ book.must }} Define and Assign Access Rights (Scopes)

Every API needs to define access rights, called scopes here, and every endpoint needs to have at least one scope assigned. Scopes are defined by name and description per API specification, as shown in the previous section. Please refer to the following rules when creating scope names:

```
<api-scope> ::= <api-standard-scope> |            -- should be sufficient for majority of use cases 
                <api-resource-specific-scope> |   -- for special security access differentiation use cases 
                <api-pseudo-scope>                -- used to explicitly indicate that access is not restricted
                
<api-standard-scope>          ::= <application-id>.<access-type> 
<api-resource-specific-scope> ::= <application-id>.<resource-id>.<access-type>
<api-pseudo-scope>            ::= uid

<application-id> ::= <as defined via STUPS>
<access-type>    ::= read | write           -- might be extended in future
<resource-id>    ::= <free identifier following application-id syntax>
```

APIs should stick to standard scopes by default -- for the majority of use cases, restricting access to specific APIs (with read vs. write differentiation) is sufficient for controlling access for client types like merchant or retailer business partners, customers or operational staff. We want to avoid too many, fine grained scopes increasing governance complexity without real value add. In some situations, where the API serves different types of resources for different owners, resource specific scopes may make sense.

Some examples for standard and resource-specific scopes:

| Application ID      | Resource ID      | Access Type | Example                           |
|---------------------|------------------|-------------|-----------------------------------|
| `fulfillment-order` |                  | `read`      | `fulfillment-order.read`          |
| `fulfillment-order` |                  | `write`     | `fulfillment-order.write`         |
| `sales-order`       | `sales_order`    | `read`      | `sales-order.sales_order.read`    |
| `sales-order`       | `shipment_order` | `read`      | `sales-order.shipment_order.read` |

After scopes names are defined and the scope is declared in the security definition at the top of an API specification, it should be assigned to each API operation by specifying a [`security` requirement](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#securityRequirementObject) like this:

```yaml
paths:
  /sales-orders/{order-number}:
    get:
      summary: Retrieves a sales order
      security:
        - oauth2:
          - sales-order-service.sales_order.read
```

In very rare cases a whole API or some selected endpoints may not require specific access control. However, to make this explicit you should assign the `uid` pseudo access right scope in this case. It is the user id and always available as OAuth2 default scope. 

```yaml
paths:
  /public-information:
    get:
      summary: Provides public information about ... 
               Accessible by any user; no access rights needed. 
      security:
        - oauth2:
          - uid
```

Hint: you need not explicitly define the "Authorization" header; it is a standard header so to say implicitly defined via the security section.




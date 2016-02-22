# Security

## {{ book.must }} Secure endpoints with OAuth 2.0

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
      uid: Unique identifier of the user accessing the service.
      fulfillment-order-service.read: Allows to read from the fulfillment order service.
```

In order to see how to define and assign scopes, please refer to the next section.

## {{ book.must }} Define and assign scopes

Every API needs to define scopes and every endpoint needs to have a at least one scope assigned. Scopes are defined by a name and a description once per API specification, as shown in the previous section. Please refer to the following rules when creating scope names:

```
<scope> ::= <standard-scope> |           -- should be sufficient for majority of use cases 
            <resource-specific-scope>    -- for special security access differentiation use cases 
<standard-scope> ::= <application-id>.<access-type> 
<resource-specific-scope> ::= <application-id>.<resource-id>.<access-type>

<application-id> ::= <as defined via STUPS>
<access-type> ::= read | write            -- might be extended in future
<resource-id> ::= <free identifier following application-id syntax>
```

APIs should stick to standard scopes by default -- for the majority of use cases, restricting access to specific APIs (with read vs. write differentiation) is sufficient for controlling access for client types like merchant or retailer business partners, customers or operational staff. We want to avoid too many, fine grained scopes increasing governance complexity without real value add. In some situations, where some API serves very different types of resources interesting for different stakeholder types, resource specific scopes may make sense.

Some examples for standard and resource-specific scopes:

| Application ID              | Resource ID      | Access Type | Example                                   |
|-----------------------------|------------------|-------------|-------------------------------------------|
| `fulfillment-order-service` |                  | `read`      | `fulfillment-order-service.read`          |
| `fulfillment-order-service` |                  | `write`     | `fulfillment-order-service.write`         |
| `sales-order-service`       | `sales_order`    | `read`      | `sales-order-service.sales_order.read`    |
| `sales-order-service`       | `shipment_order` | `read`      | `sales-order-service.shipment_order.read` |

After scopes names are defined and the scope is declared in the security definition at the top of an API specification it should be assigned to each API operation by specifying a [`security` requirement](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#securityRequirementObject) like this:

```yaml
paths:
  /sales-orders/{order-number}:
    get:
      summary: Retrieves a sales order
      security:
        - oauth2:
          - sales-order-service.sales_order.read
```

In very rare cases a whole API or some selected endpoints may not require specific access control. However, to make this explicit you should assign the `uid` scope in this case. It's available to every OAuth2 account by default.

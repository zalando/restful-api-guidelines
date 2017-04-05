# Security

## {{ book.must }} Secure Endpoints with OAuth 2.0

Every API endpoint needs to be secured using OAuth 2.0. Please refer to the 
[official OpenAPI spec](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#security-definitions-object)
on how to specify security definitions in you API specification or take a look at the following example.

```yaml
securityDefinitions:
  oauth2:
    type: oauth2
    flow: password
    tokenUrl: https://auth.zalando.com/oauth2/access_token?realm=services
    scopes:
      fulfillment-order-service.read: Access right needed to read from the fulfillment order service.
      fulfillment-order-service.write: Access right needed to write to the fulfillment order service.      
```

The example defines OAuth2 with password flow as security standard used for authentication when accessing endpoints; additionally, there are two API access rights defined via the scopes section for later endpoint authorization usage - please see next section.

It makes little sense specifying the flow to retrieve OAuth tokens in the `securityDefinitions` section, as API endpoints should not care, how OAuth tokens were created. Unfortunately the `flow` field is mandatory and cannot be ommited. API endpoints should always set `flow: password` and ignore this information.

## {{ book.must }} Define and Assign Access Rights (Permissions)

Every API needs to define access permissions to it's resources. Every endpoint needs to have at least one permission assigned. Permissions are defined by name and described per API specification, as shown in the previous section.
The naming schema for permissions correspond to the naming schema for hostnames. Please refer to the following rules when creating permissions:

```
permission ::= <name-space>::<permission-name>

name-space ::= z --fix prefix for all permissions created by Zalando business partners

permission-name ::= <functional-domain>.[<functional-component>].[<resource>].<access-type>

functional-domain ::= [a-z][a-z0-9-]* ; name managed by central architecture team, defined together with you

functional-component ::= [a-z][a-z0-9-]* ; name managed by central architecture team, defined together with you

resource ::= [a-z][a-z0-9-]* ; free identifier - resource name

access-type ::= read || write ; might be extended in future
```

APIs should stick to standard permissions by default -- for the majority of use cases, restricting access to specific APIs (with read vs. write differentiation) is sufficient for controlling access for client types like merchant or retailer business partners, customers or operational staff. We want to avoid too many, fine grained scopes increasing governance complexity without real value add. In some situations, where the API serves different types of resources for different owners, resource specific scopes may make sense.

Some examples for standard and resource-specific permissions:

| Domain              | Component        | Resource           | Access Type | Example                                     |
|---------------------|------------------|--------------------|-------------|---------------------------------------------|
| `core`              | business partner |                    | `write`     | `z::core.business-partner.write`            |
| `finance`           | exchange-rate    |                    | `read`      | `z::finance.exchange-rate.read`             |
| `customer`          | address          | `shipment-address` | `read`      | `z::customer.address.shipment-address.read` |

After scopes names are defined and the scope is declared in the security definition at the top of an API specification, it should be assigned to each API operation by specifying a [`security` requirement](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#securityRequirementObject) like this:

```yaml
paths:
  /business-partners/{partner-id}:
    get:
      summary: Retrieves information about a business partner
      security:
        - oauth2:
          - z::core.business-partner.read
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




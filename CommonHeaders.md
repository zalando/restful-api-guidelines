# Common Headers 

This section shares definitions of proprietary headers that should be named consistently because
they address overarching service-related concerns. Whether services support these concerns or not is
optional; therefore, the Swagger API specification is the right place to make this explicitly
visible. Use the parameter definitions of the resource HTTP methods. 

|Header field name | Type    | Description                       | Example                |
|------------------|---------|-----------------------------------|------------------------|
| X-Flow-Id        | String  | The flow id of the request, which | GKY7oDhpSiKY_gAAAABZ_A |
|                  |         | is written into the logs and      |                        |
|                  |         | passed to called services.        |                        |
|                  |         | Helpful for operational           |                        |
|                  |         | troubleshooting and log analysis. |                        |
|------------------|---------|-----------------------------------|------------------------|
| X-App-Domain     | Integer | The app domain (i.e. shop channel | 16                     |
|                  |         | context) of the request           |                        |
|------------------|---------|-----------------------------------|------------------------|
| X-Tenant-Id      | String  | The tenant id for future platform | Zalando-Fashion-Store  |
|                  |         | multitenancy support.             |                        |

Remember that HTTP header fields are not case-sensitive.
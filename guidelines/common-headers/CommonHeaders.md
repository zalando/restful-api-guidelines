# Common Headers 

This section shares definitions of proprietary headers that should be named consistently because
they address overarching service-related concerns. Whether services support these concerns or not is
optional; therefore, the Swagger API specification is the right place to make this explicitly
visible. Use the parameter definitions of the resource HTTP methods. 

| Header field name | Type    | Description                       | Example                |
| X-Flow-Id         | String  | The flow id of the request, which is written into the logs and passed to called services. Helpful for operational troubleshooting and log analysis. | GKY7oDhpSiKY_gAAAABZ_A |
| X-App-Domain      | Integer | The app domain (i.e. shop channel context) of the request | 16 |
| X-Tenant-Id       | String  | The tenant id for future platform multitenancy support. | Zalando-Fashion-Store  |

Remember that HTTP header fields are not case-sensitive.
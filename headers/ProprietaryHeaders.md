# Proprietary Headers

This section shares definitions of proprietary headers that should be named consistently because
they address overarching service-related concerns. Whether services support these concerns or not is
optional; therefore, the OpenAPI API specification is the right place to make this explicitly
visible. Use the parameter definitions of the resource HTTP methods.

| Header field name | Type    | Description                       | Header field value example                |
| ----------------- | ------- | --------------------------------- | -------------------------- |
| X-Flow-ID         | String  | The flow id of the request, which is written into the logs and passed to called services. Helpful for operational troubleshooting and log analysis. | GKY7oDhpSiKY_gAAAABZ_A |
| X-UID      | String | Generic user id of OpenId account that owns the passed (OAuth2) access token. E.g. additionally provided by OpenIG proxy after access token validation -- may save additional token validation round trips. | w435-dker-jdh357 |
| X-Tenant-ID       | String  | The tenant id for future platform multitenancy support. *Should not be used unless new platform multitenancy is truly supported. But should be used by New Platform Prototyping services.* Must be validated for external retailer, supplier, etc. tenant users via OAuth2; details in clarification. Currently only used by New Platform Prototyping services. | 9f8b3ca3-4be5-436c-a847-9cd55460c495  |
| X-Sales-Channel   | String | Sales channels are owned by retailers and represent a specific consumer segment being addressed with a specific product assortment that is offered via CFA retailer catalogs to consumers (see [platform glossary \[internal link\]](https://pages.github.bus.zalan.do/core-platform/docs/glossary/glossary.html)) | 101 |
| X-Frontend-Type   | String | Consumer facing applications (CFAs) provide business experience to their customers via different frontend application types, for instance, mobile app or browser. Info should be passed-through as generic aspect -- there are diverse concerns, e.g. pushing mobiles with specific coupons, that make use of it. Current range is mobile-app, browser, facebook-app, chat-app | mobile-app |
| X-Device-Type      | String | There are also use cases for steering customer experience (incl. features and content) depending on device type. Via this header info should be passed-through as generic aspect. Current range is smartphone, tablet, desktop, other | tablet |
| X-Device-OS      | String | On top of device type above, we even want to differ between device platform, e.g. smartphone Android vs. iOS. Via this header info should be passed-through as generic aspect. Current range is iOS, Android, Windows, Linux, MacOS | Android |
| X-App-Domain      | Integer | The app domain (i.e. shop channel context) of the request. Note, app-domain is a legacy concept that will be replaced in new platform by combinations of main CFA concerns like retailer, sales channel, country | 16 |

Remember that HTTP header field names are not case-sensitive.

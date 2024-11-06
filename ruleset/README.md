# spectral-ruleset 

**👷🚧🛠️ in development 👷🚧🛠️**

Custom [Spectral API Linter](https://github.com/stoplightio/spectral) ruleset for NUPANO RESTful API Guidelines based on [baloise spectral-ruleset](https://github.com/baloise-incubator/spectral-ruleset)

Please refer to the table below for details.  

The ruleset `spectral:oas` is disabled by default. If you like to enable these rules again use  
```bash
echo "extends: [https://raw.githubusercontent.com/baloise-incubator/spectral-ruleset/main/zalando.yml, spectral:oas]" \
    > .spectral.yml
```

## Use it

```bash
npx @stoplight/spectral-cli lint --ruleset <path to nupano.yml> example/example-openapi.yml
```

## Currently supported rules from the nupano restful-api-guidelines
:heavy_check_mark: = rule implemented  
:grey_exclamation: = rule implementation not possible or to complex


| id             | title                                                                                                | nupano.yml | prio (A, B, C)|
|----------------|------------------------------------------------------------------------------------------------------| -- | --- |
| [#100][#100]   | [MUST follow API first principle][#100]                                                              | :grey_exclamation: | B |
| [#101][#101]   | [MUST provide API specification using OpenAPI][#101]                                                 | :grey_exclamation: | A |
| [#102][#102]   | [SHOULD provide API user manual][#102]                                                               | - | C |
| [#103][#103]   | [MUST write APIs using U.S. English][#103]                                                           | :grey_exclamation: | A |
| [#104][#104]   | [MUST secure endpoints with OAuth 2.0][#104]                                                         | - | B |
| [#105][#105]   | [MUST define and assign permissions (scopes)][#105]                                                  | - | B |
| [#106][#106]   | [MUST not break backward compatibility][#106]                                                        | - | B |
| [#107][#107]   | [SHOULD prefer compatible extensions][#107]                                                          | :grey_exclamation: | - |
| [#108][#108]   | [MUST prepare clients accept compatible API extensions][#108]                                        | :grey_exclamation: | C |
| [#109][#109]   | [SHOULD design APIs conservatively][#109]                                                            | :grey_exclamation: | - |
| [#110][#110]   | [MUST always return JSON objects as top-level data structures][#110]                                 | :heavy_check_mark: | A |
| [#111][#111]   | [MUST treat Open API specification as open for extension by default][#111]                           | :grey_exclamation: | - |
| [#112][#112]   | [SHOULD used open-ended list of values (`x-extensible-enum`) for enumerations][#112]                 | :heavy_check_mark: | - | - |
| [#113][#113]   | [SHOULD avoid versioning][#113]                                                                      | :grey_exclamation: | B |
| [#114][#114]   | [MUST use media type versioning][#114]                                                               | :grey_exclamation: | - |
| [#115][#115]   | [MUST not use URI versioning][#115]                                                                  | :heavy_check_mark: | A |
| [#115a][#115a] | [CAN use correct URI versioning: `^((?!.*\/v\d+(\/.*)?\/v\d+)\/.*)$`][#115a]                         | - | A |
| [#116][#116]   | [MUST use semantic versioning][#116]                                                                 | :heavy_check_mark: | B |
| [#118][#118]   | [MUST property names must be ASCII snake_case (and never camelCase): `^[a-z_][a-z_0-9]*$`][#118]     | :heavy_check_mark: | A |
| [#118a][#118a] | [MUST property names must be ASCII camelCase: `^[a-z]+((\d)([A-Z0-9][a-z0-9]+))*([A-Z])?$`][#118a]   | - | A |
| [#120][#120]   | [SHOULD pluralize array names][#120]                                                                 | - | B |
| [#122][#122]   | [MUST not use `null` for boolean properties][#122]                                                   | :grey_exclamation: | - |
| [#123][#123]   | [MUST use same semantics for `null` and absent properties][#123]                                     | :grey_exclamation: | - |
| [#124][#124]   | [SHOULD not use `null` for empty arrays][#124]                                                       | :grey_exclamation: | - |
| [#125][#125]   | [SHOULD represent enumerations as strings][#125]                                                     | - | ? |
| [#126][#126]   | [SHOULD define dates properties compliant with RFC 3339][#126]                                       | - | ? |
| [#127][#127]   | [SHOULD define time durations and intervals properties conform to ISO 8601][#127]                    | :grey_exclamation: | - |
| [#128][#128]   | [SHOULD use standards for country, language and currency codes][#128]                                | - | ? |
| [#129][#129]   | [MUST use lowercase separate words with hyphens for path segments][#129]                             | :heavy_check_mark: | A |
| [#130][#130]   | [MUST use snake_case (never camelCase) for query parameters][#130]                                   | :heavy_check_mark: | B |
| [#130a][#130a] | [MUST use camelCase (never snake_case) for query parameters][#130a]                                  | - | B |
| [#132][#132]   | [SHOULD prefer hyphenated-pascal-case for HTTP header fields][#132]                                  | :heavy_check_mark: | B |
| [#133][#133]   | [MAY use standardized headers][#133]                                                                 | - | - |
| [#134][#134]   | [MUST pluralize resource names][#134]                                                                | - | A |
| [#135][#135]   | [SHOULD not use /api as base path][#135]                                                             | :heavy_check_mark: | A |
| [#136][#136]   | [MUST use normalized paths without empty path segments and trailing slashes][#136]                   | :heavy_check_mark: | B |
| [#137][#137]   | [MUST stick to conventional query parameters][#137]                                                  | :grey_exclamation: | - |
| [#138][#138]   | [MUST avoid actions — think about resources][#138]                                                   | :grey_exclamation: | A |
| [#139][#139]   | [SHOULD model complete business processes][#139]                                                     | :grey_exclamation: | - |
| [#140][#140]   | [SHOULD define _useful_ resources][#140]                                                             | :grey_exclamation: | - |
| [#141][#141]   | [MUST keep URLs verb-free][#141]                                                                     | :grey_exclamation: | A |
| [#142][#142]   | [MUST use domain-specific resource names][#142]                                                      | :grey_exclamation: | - |
| [#143][#143]   | [MUST identify resources and sub-resources via path segments][#143]                                  | - | - |
| [#144][#144]   | [SHOULD only use UUIDs if necessary][#144]                                                           | :grey_exclamation: | - |
| [#145][#145]   | [MAY consider using (non-)nested URLs][#145]                                                         | :grey_exclamation: | - |
| [#146][#146]   | [SHOULD limit number of resource types][#146]                                                        | :heavy_check_mark: | B |
| [#147][#147]   | [SHOULD limit number of sub-resource levels][#147]                                                   | :heavy_check_mark: | B |
| [#148][#148]   | [MUST use HTTP methods correctly][#148]                                                              | :grey_exclamation: | A |
| [#149][#149]   | [MUST fulfill common method properties][#149]                                                        | :grey_exclamation: | - |
| [#150][#150]   | [MUST use standard HTTP status codes][#150]                                                          | :heavy_check_mark: | - |
| [#150a][#150a] | [MUST use additional standard HTTP status codes][#150a]                                              | - | - |
| [#151][#151]   | [MUST specify success and error responses][#151]                                                     | :heavy_check_mark: | B |
| [#152][#152]   | [MUST use code 207 for batch or bulk requests][#152]                                                 | :grey_exclamation: | C |
| [#153][#153]   | [MUST use code 429 with headers for rate limits][#153]                                               | :grey_exclamation: | C |
| [#154][#154]   | [MUST define collection format of header and query parameters][#154]                                 | :grey_exclamation: | - |
| [#155][#155]   | [SHOULD reduce bandwidth needs and improve responsiveness][#155]                                     | :grey_exclamation: | - |
| [#156][#156]   | [SHOULD use `gzip` compression][#156]                                                                | :grey_exclamation: | - |
| [#157][#157]   | [SHOULD support partial responses via filtering][#157]                                               | :grey_exclamation: | - |
| [#158][#158]   | [SHOULD allow optional embedding of sub-resources][#158]                                             | :grey_exclamation: | - |
| [#159][#159]   | [MUST support pagination][#159]                                                                      | - | - |
| [#160][#160]   | [SHOULD prefer cursor-based pagination, avoid offset-based pagination][#160]                         | - | - |
| [#161][#161]   | [SHOULD use pagination links where applicable][#161]                                                 | - | - |
| [#162][#162]   | [MUST use REST maturity level 2][#162]                                                               | - | - |
| [#163][#163]   | [MAY use REST maturity level 3 - HATEOAS][#163]                                                      | - | - |
| [#164][#164]   | [MUST use common hypertext controls][#164]                                                           | - | - |
| [#165][#165]   | [SHOULD use simple hypertext controls for pagination and self-references][#165]                      | - | - |
| [#166][#166]   | [MUST not use link headers with JSON entities][#166]                                                 | - | - |
| [#167][#167]   | [MUST use JSON to encode structured data][#167]                                                      | - | - |
| [#168][#168]   | [MAY use non JSON media types for binary data or alternative content representations][#168]          | :grey_exclamation: | - |
| [#169][#169]   | [MUST use standard date and time formats][#169]                                                      | :heavy_check_mark: | A |
| [#170][#170]   | [SHOULD use standards for country, language and currency codes][#170]                                | - | B |
| [#171][#171]   | [MUST define format for number and integer types][#171]                                              | :heavy_check_mark: | - |
| [#172][#172]   | [SHOULD prefer standard media type name `application/json`][#172]                                    | :heavy_check_mark: | B |
| [#173][#173]   | [MUST use the common money object][#173]                                                             | :grey_exclamation: | - |
| [#174][#174]   | [MUST use common field names and semantics][#174]                                                    | :grey_exclamation: | - |
| [#176][#176]   | [MUST support problem JSON][#176]                                                                    | :heavy_check_mark: | - |
| [#177][#177]   | [MUST not expose stack traces][#177]                                                                 | :grey_exclamation: | - |
| [#178][#178]   | [MUST use `Content-*` headers correctly][#178]                                                       | :grey_exclamation: | - |
| [#179][#179]   | [MAY use `Content-Location` header][#179]                                                            | :grey_exclamation: | - |
| [#180][#180]   | [SHOULD use `Location` header instead of `Content-Location` header][#180]                            | - | - |
| [#181][#181]   | [MAY consider to support `Prefer` header to handle processing preferences][#181]                     | :grey_exclamation: | - |
| [#182][#182]   | [MAY consider to support `ETag` together with `If-Match`/`If-None-Match` header][#182]               | :grey_exclamation: | - |
| [#183][#183]   | [SHOULD use only the specified proprietary Zalando headers][#183]                                    | - | - |
| [#184][#184]   | [MUST propagate proprietary headers][#184]                                                           | - | - |
| [#185][#185]   | [MUST obtain approval of clients before API shut down][#185]                                         | :grey_exclamation: | - |
| [#186][#186]   | [MUST collect external partner consent on deprecation time span][#186]                               | :grey_exclamation: | - |
| [#187][#187]   | [MUST reflect deprecation in API specifications][#187]                                               | :grey_exclamation: | - |
| [#188][#188]   | [MUST monitor usage of deprecated API scheduled for sunset][#188]                                    | :grey_exclamation: | - |
| [#189][#189]   | [SHOULD add `Deprecation` and `Sunset` header to responses][#189]                                    | :grey_exclamation: | - |
| [#190][#190]   | [SHOULD add monitoring for `Deprecation` and `Sunset` header][#190]                                  | :grey_exclamation: | - |
| [#191][#191]   | [MUST not start using deprecated APIs][#191]                                                         | :grey_exclamation: | B |
| [#192][#192]   | [MUST publish Open API specification][#192]                                                          | :grey_exclamation: | A |
| [#193][#193]   | [SHOULD monitor API usage][#193]                                                                     | :grey_exclamation: | - |
| [#194][#194]   | [MUST treat events as part of the service interface][#194]                                           | - | - |
| [#195][#195]   | [MUST make event schema available for review][#195]                                                  | - | - |
| [#196][#196]   | [MUST ensure event schema conforms to Open API schema object][#196]                                  | - | - |
| [#197][#197]   | [MUST ensure that events are registered as event types][#197]                                        | - | - |
| [#198][#198]   | [MUST ensure that events conform to a well-known event category][#198]                               | - | - |
| [#199][#199]   | [MUST ensure that events define useful business resources][#199]                                     | - | - |
| [#200][#200]   | [MUST ensure that events do not provide sensitive data][#200]                                        | - | - |
| [#201][#201]   | [MUST use the general event category to signal steps and arrival points in business processes][#201] | - | - |
| [#202][#202]   | [MUST use data change events to signal mutations][#202]                                              | - | - |
| [#203][#203]   | [SHOULD provide means for explicit event ordering][#203]                                             | - | - |
| [#204][#204]   | [SHOULD use the hash partition strategy for data change events][#204]                                | - | - |
| [#205][#205]   | [SHOULD ensure that data change events match the APIs resources][#205]                               | - | - |
| [#207][#207]   | [MUST indicate ownership of event types][#207]                                                       | - | - |
| [#208][#208]   | [MUST define event payloads compliant with overall API guidelines][#208]                             | - | - |
| [#209][#209]   | [MUST maintain backwards compatibility for events][#209]                                             | - | - |
| [#210][#210]   | [SHOULD avoid `additionalProperties` in event type definitions][#210]                                | - | - |
| [#211][#211]   | [MUST use unique event identifiers][#211]                                                            | - | - |
| [#212][#212]   | [SHOULD design for idempotent out-of-order processing][#212]                                         | - | - |
| [#213][#213]   | [MUST follow naming convention for event type names][#213]                                           | - | - |
| [#214][#214]   | [MUST prepare event consumers for duplicate events][#214]                                            | - | - |
| [#215][#215]   | [MUST provide API identifiers][#215]                                                                 | :heavy_check_mark: | - | - |
| [#216][#216]   | [SHOULD define maps using `additionalProperties`][#216]                                              | - | - |
| [#217][#217]   | [MUST use full, absolute URI][#217]                                                                  | - | - |
| [#218][#218]   | [MUST contain API meta information][#218]                                                            | :heavy_check_mark: | - | - |
| [#219][#219]   | [MUST provide API audience][#219]                                                                    | :heavy_check_mark: | - | - |
| [#219a][#219a] | [MUST provide baloise API audience][#219a]                                                           | - | - | - |
| [#220][#220]   | [MUST use most specific HTTP status codes][#220]                                                     | :grey_exclamation: | - |
| [#223][#223]   | [MUST-SHOULD use functional naming schema][#223]                                                     | :grey_exclamation: | - |
| [#224][#224]   | [MUST follow naming convention for hostnames][#224]                                                  | :grey_exclamation: | - |
| [#225][#225]   | [MUST follow naming convention for permissions (scopes)][#225]                                       | :grey_exclamation: | - |
| [#226][#226]   | [MUST document implicit filtering][#226]                                                             | :grey_exclamation: | - |
| [#227][#227]   | [MUST document cachable `GET`, `HEAD`, and `POST` endpoints][#227]                                   | :grey_exclamation: | - |
| [#228][#228]   | [MUST use URL-friendly resource identifiers: [a-zA-Z0-9:._\-/]*][#228]                               | :grey_exclamation: | A |
| [#229][#229]   | [SHOULD consider to design `POST` and `PATCH` idempotent][#229]                                      | :grey_exclamation: | - |
| [#230][#230]   | [MAY consider to support `Idempotency-Key` header][#230]                                             | :grey_exclamation: | - |
| [#231][#231]   | [Should use secondary key for idempotent `POST` design][#231]                                        | :grey_exclamation: | - |
| [#233a][#233a] | [MUST request must use tracing headers][#233a]                                                    | - | - |
| [#234][#234]   | [MUST only use durable and immutable remote references][#234]                                        | :grey_exclamation: | - |
| [#235][#235]   | [SHOULD name date/time properties with `_at` suffix][#235]                                           | - | - |
| [#236][#236]   | [SHOULD design simple query languages using query parameters][#236]                                  | :grey_exclamation: | - |
| [#237][#237]   | [SHOULD design complex query languages using JSON][#237]                                             | :grey_exclamation: | - |
| [#238][#238]   | [SHOULD use standardized property formats][#238]                                                     | :grey_exclamation: | - |
| [#239][#239]   | [SHOULD encode embedded binary data in `base64url`][#239]                                            | :grey_exclamation: | - |
| [#240][#240]   | [SHOULD declare enum values using UPPER_SNAKE_CASE format][#240]                                     | :heavy_check_mark: | - | - |
| [#241][#241]   | [MAY expose compound keys as resource identifiers][#241]                                             | :grey_exclamation: | - |
[#100]: https://opensource.zalando.com/restful-api-guidelines/#100
[#101]: https://opensource.zalando.com/restful-api-guidelines/#101
[#102]: https://opensource.zalando.com/restful-api-guidelines/#102
[#103]: https://opensource.zalando.com/restful-api-guidelines/#103
[#104]: https://opensource.zalando.com/restful-api-guidelines/#104
[#105]: https://opensource.zalando.com/restful-api-guidelines/#105
[#106]: https://opensource.zalando.com/restful-api-guidelines/#106
[#107]: https://opensource.zalando.com/restful-api-guidelines/#107
[#108]: https://opensource.zalando.com/restful-api-guidelines/#108
[#109]: https://opensource.zalando.com/restful-api-guidelines/#109
[#110]: https://opensource.zalando.com/restful-api-guidelines/#110
[#111]: https://opensource.zalando.com/restful-api-guidelines/#111
[#112]: https://opensource.zalando.com/restful-api-guidelines/#112
[#113]: https://opensource.zalando.com/restful-api-guidelines/#113
[#114]: https://opensource.zalando.com/restful-api-guidelines/#114
[#115]: https://opensource.zalando.com/restful-api-guidelines/#115
[#115a]: ./doc/rules/can-use-correct-URI-versioning.test.md
[#116]: https://opensource.zalando.com/restful-api-guidelines/#116
[#118]: https://opensource.zalando.com/restful-api-guidelines/#118
[#118a]: ./doc/rules/property-names-must-be-ascii-camel-case.md
[#120]: https://opensource.zalando.com/restful-api-guidelines/#120
[#122]: https://opensource.zalando.com/restful-api-guidelines/#122
[#123]: https://opensource.zalando.com/restful-api-guidelines/#123
[#124]: https://opensource.zalando.com/restful-api-guidelines/#124
[#125]: https://opensource.zalando.com/restful-api-guidelines/#125
[#126]: https://opensource.zalando.com/restful-api-guidelines/#126
[#127]: https://opensource.zalando.com/restful-api-guidelines/#127
[#128]: https://opensource.zalando.com/restful-api-guidelines/#128
[#129]: https://opensource.zalando.com/restful-api-guidelines/#129
[#130]: https://opensource.zalando.com/restful-api-guidelines/#130
[#130a]: ./doc/rules/query-parameter-names-must-be-ascii-camel-case.md
[#132]: https://opensource.zalando.com/restful-api-guidelines/#132
[#133]: https://opensource.zalando.com/restful-api-guidelines/#133
[#134]: https://opensource.zalando.com/restful-api-guidelines/#134
[#135]: https://opensource.zalando.com/restful-api-guidelines/#135
[#136]: https://opensource.zalando.com/restful-api-guidelines/#136
[#137]: https://opensource.zalando.com/restful-api-guidelines/#137
[#138]: https://opensource.zalando.com/restful-api-guidelines/#138
[#139]: https://opensource.zalando.com/restful-api-guidelines/#139
[#140]: https://opensource.zalando.com/restful-api-guidelines/#140
[#141]: https://opensource.zalando.com/restful-api-guidelines/#141
[#142]: https://opensource.zalando.com/restful-api-guidelines/#142
[#143]: https://opensource.zalando.com/restful-api-guidelines/#143
[#144]: https://opensource.zalando.com/restful-api-guidelines/#144
[#145]: https://opensource.zalando.com/restful-api-guidelines/#145
[#146]: https://opensource.zalando.com/restful-api-guidelines/#146
[#147]: https://opensource.zalando.com/restful-api-guidelines/#147
[#148]: https://opensource.zalando.com/restful-api-guidelines/#148
[#149]: https://opensource.zalando.com/restful-api-guidelines/#149
[#150]: https://opensource.zalando.com/restful-api-guidelines/#150
[#150a]: ./doc/rules/must-use-additional-standard-http-status-codes.md
[#151]: https://opensource.zalando.com/restful-api-guidelines/#151
[#152]: https://opensource.zalando.com/restful-api-guidelines/#152
[#153]: https://opensource.zalando.com/restful-api-guidelines/#153
[#154]: https://opensource.zalando.com/restful-api-guidelines/#154
[#155]: https://opensource.zalando.com/restful-api-guidelines/#155
[#156]: https://opensource.zalando.com/restful-api-guidelines/#156
[#157]: https://opensource.zalando.com/restful-api-guidelines/#157
[#158]: https://opensource.zalando.com/restful-api-guidelines/#158
[#159]: https://opensource.zalando.com/restful-api-guidelines/#159
[#160]: https://opensource.zalando.com/restful-api-guidelines/#160
[#161]: https://opensource.zalando.com/restful-api-guidelines/#161
[#162]: https://opensource.zalando.com/restful-api-guidelines/#162
[#163]: https://opensource.zalando.com/restful-api-guidelines/#163
[#164]: https://opensource.zalando.com/restful-api-guidelines/#164
[#165]: https://opensource.zalando.com/restful-api-guidelines/#165
[#166]: https://opensource.zalando.com/restful-api-guidelines/#166
[#167]: https://opensource.zalando.com/restful-api-guidelines/#167
[#168]: https://opensource.zalando.com/restful-api-guidelines/#168
[#169]: https://opensource.zalando.com/restful-api-guidelines/#169
[#170]: https://opensource.zalando.com/restful-api-guidelines/#170
[#171]: https://opensource.zalando.com/restful-api-guidelines/#171
[#172]: https://opensource.zalando.com/restful-api-guidelines/#172
[#173]: https://opensource.zalando.com/restful-api-guidelines/#173
[#174]: https://opensource.zalando.com/restful-api-guidelines/#174
[#176]: https://opensource.zalando.com/restful-api-guidelines/#176
[#177]: https://opensource.zalando.com/restful-api-guidelines/#177
[#178]: https://opensource.zalando.com/restful-api-guidelines/#178
[#179]: https://opensource.zalando.com/restful-api-guidelines/#179
[#180]: https://opensource.zalando.com/restful-api-guidelines/#180
[#181]: https://opensource.zalando.com/restful-api-guidelines/#181
[#182]: https://opensource.zalando.com/restful-api-guidelines/#182
[#183]: https://opensource.zalando.com/restful-api-guidelines/#183
[#184]: https://opensource.zalando.com/restful-api-guidelines/#184
[#185]: https://opensource.zalando.com/restful-api-guidelines/#185
[#186]: https://opensource.zalando.com/restful-api-guidelines/#186
[#187]: https://opensource.zalando.com/restful-api-guidelines/#187
[#188]: https://opensource.zalando.com/restful-api-guidelines/#188
[#189]: https://opensource.zalando.com/restful-api-guidelines/#189
[#190]: https://opensource.zalando.com/restful-api-guidelines/#190
[#191]: https://opensource.zalando.com/restful-api-guidelines/#191
[#192]: https://opensource.zalando.com/restful-api-guidelines/#192
[#193]: https://opensource.zalando.com/restful-api-guidelines/#193
[#194]: https://opensource.zalando.com/restful-api-guidelines/#194
[#195]: https://opensource.zalando.com/restful-api-guidelines/#195
[#196]: https://opensource.zalando.com/restful-api-guidelines/#196
[#197]: https://opensource.zalando.com/restful-api-guidelines/#197
[#198]: https://opensource.zalando.com/restful-api-guidelines/#198
[#199]: https://opensource.zalando.com/restful-api-guidelines/#199
[#200]: https://opensource.zalando.com/restful-api-guidelines/#200
[#201]: https://opensource.zalando.com/restful-api-guidelines/#201
[#202]: https://opensource.zalando.com/restful-api-guidelines/#202
[#203]: https://opensource.zalando.com/restful-api-guidelines/#203
[#204]: https://opensource.zalando.com/restful-api-guidelines/#204
[#205]: https://opensource.zalando.com/restful-api-guidelines/#205
[#207]: https://opensource.zalando.com/restful-api-guidelines/#207
[#208]: https://opensource.zalando.com/restful-api-guidelines/#208
[#209]: https://opensource.zalando.com/restful-api-guidelines/#209
[#210]: https://opensource.zalando.com/restful-api-guidelines/#210
[#211]: https://opensource.zalando.com/restful-api-guidelines/#211
[#212]: https://opensource.zalando.com/restful-api-guidelines/#212
[#213]: https://opensource.zalando.com/restful-api-guidelines/#213
[#214]: https://opensource.zalando.com/restful-api-guidelines/#214
[#215]: https://opensource.zalando.com/restful-api-guidelines/#215
[#216]: https://opensource.zalando.com/restful-api-guidelines/#216
[#217]: https://opensource.zalando.com/restful-api-guidelines/#217
[#218]: https://opensource.zalando.com/restful-api-guidelines/#218
[#219]: https://opensource.zalando.com/restful-api-guidelines/#219
[#219a]: ./doc/rules/must-provide-baloise-api-audience.md
[#220]: https://opensource.zalando.com/restful-api-guidelines/#220
[#223]: https://opensource.zalando.com/restful-api-guidelines/#223
[#224]: https://opensource.zalando.com/restful-api-guidelines/#224
[#225]: https://opensource.zalando.com/restful-api-guidelines/#225
[#226]: https://opensource.zalando.com/restful-api-guidelines/#226
[#227]: https://opensource.zalando.com/restful-api-guidelines/#227
[#228]: https://opensource.zalando.com/restful-api-guidelines/#228
[#229]: https://opensource.zalando.com/restful-api-guidelines/#229
[#230]: https://opensource.zalando.com/restful-api-guidelines/#230
[#231]: https://opensource.zalando.com/restful-api-guidelines/#231
[#233]: https://opensource.zalando.com/restful-api-guidelines/#233
[#233a]: ./doc/rules/requests-must-use-tracing.md
[#234]: https://opensource.zalando.com/restful-api-guidelines/#234
[#235]: https://opensource.zalando.com/restful-api-guidelines/#235
[#236]: https://opensource.zalando.com/restful-api-guidelines/#236
[#237]: https://opensource.zalando.com/restful-api-guidelines/#237
[#238]: https://opensource.zalando.com/restful-api-guidelines/#238
[#239]: https://opensource.zalando.com/restful-api-guidelines/#239
[#240]: https://opensource.zalando.com/restful-api-guidelines/#240
[#241]: https://opensource.zalando.com/restful-api-guidelines/#241

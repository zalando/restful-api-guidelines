# General Guidelines

The titles are marked with the corresponding labels: {{ book.must }}, {{ book.should }}, {{ book.could }}.

## {{ book.must }} Follow API First Principle

As mentioned in the introduction, API First is one of our architecture principles, as per our Architecture Rules of Play. 
In a nutshell API First has to aspects:

- define APIs outside the code first using a standard specification language
- get early review feedback from peers and client developers

By defining APIs outside the code, we want to facilitate early review feedback and also development 
discipline to early focus service interface design on...

- profound understanding of the domain and required functionality
- generalized business entities / resources — avoidance of avoid use case specific APIs
- clear separation of WHAT vs. HOW concerns, i.e. abstraction from implementation aspects

Moreover, API definitions with standardized specification format also facilitate...

- single source of truth for API reference specification; 
basically, it is a crucial part of a contract between service provider and client users
- infrastructure tooling for API discovery, API GUIs, API documents, automated quality checks etc.

An element of API First are also this API Guidelines and a [lightweight API review process \[internal link\]](https://github.bus.zalan.do/ApiGuild/ApiReviewProcedure) as to get early review feedback from peers and client developers. 
Peer review is an important tool for us to get high quality APIs, to enable architectural and design alignment 
and to supported development of client applications decoupled from service provider engineering life cycle. 


Designing high-quality, long-lasting APIs is hard work and takes time. 
It is important to learn, that API First principle is **not in conflict with the agile development principles** that we love. 
Service applications should evolve iteratively — and so its APIs. So, of course, API specification will 
and should evolve iteratively in different cycles, each starting with draft status and early team 
and peer review feedback. 

API may change and profit from implementation concerns and automated testing feedback. 
API evolution during design and development may include breaking changes for not yet productive features 
and as long as we have aligned the changes with the clients. 
Hence, API First does *not* mean that you must have 100% domain and requirement understanding and can never produce code 
before you have defined the complete API and get it confirmed by peer review. On the other hand, API First obviously is 
in conflict with the practise of publishing API reference definition and asking for peer review after the service integration 
or even the service productive operation has started. It is crucial to request and get early feedback — as early as possible,
unless the API changes are is focussed to the next evolution step and have a certain quality, 
already confirmed by team internal reviews. 


## {{ book.must }} Provide API Reference Definition using OpenAPI

We use [OpenAPI specification](http://swagger.io/specification/) (aka Swagger spec) as standard for our REST API definitions. 
You may choose YAML or JSON as a format of your OpenAPI API specification file; 
however, YAML is generally preferred due to its improved readability. 

We also call the OpenAPI API specification the "API Reference Definition"; 
it provides all information needed by an experienced API client developer to use this API.

The OpenAPi API specification file should be subject of version control together with source code management. 
Service also have to provide 
[online access to the API Reference definition](../api-operation/ApiOperation.md#must-Provide-Online-Access-to-OpenAPI-Reference-Definition). 
The API definition is the source of truth; basically, it is part of a contract between service provider and client users. 


## {{ book.should }} Provide User Manual Documentation

In addition to defining the API as OpenAPI Reference Definition, it’s good practice to provide 
further documentation to improve client developer experience, especially of engineers that 
are novices in using this API. A helpful API User Manual documentation typically describes 
the following API aspects:

- API’s scope, purpose and use cases
- concrete usage examples 
- architecture context - including figures and sequence flows
- edge cases and possible error situations

The User Manual must be posted online, e.g. via GitHub Enterprise pages, on specific 
team web servers, or as a Google doc. And don't forget to include a link to this 
user manual documentation into your OpenAPI definition using the “externalDocs” property.

## {{ book.must }} Write APIs in U.S. English

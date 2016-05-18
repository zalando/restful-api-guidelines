# Events

Zalando’s architecture centers around decoupled microservices and in that context we favour asynchronous event driven approaches. The [Nakadi](https://github.com/zalando/nakadi) framework provides an event publish and subscribe system, abstracting exchanges through a RESTful API. 

Nakadi is a key element for data integration in the architecture, and the standard system for inter-service event messaging. The guidelines in this section are for use with the Nakadi framework and focus on how to design and publish events intended to be shared for others to consume. Critically, once events pass service boundaries they are considered part of the service API and are subject to the API guidelines. 

## {{ book.must }} Treat Events as part of the service interface

Events are part of a service’s interface to the outside world equivalent in standing to a service’s REST API. Services publishing data for integration must treat their events as a first class design concern, just as they would an API. For example this means approaching events with the "API first" principle in mind [as described in the Introduction](../Introduction.md) and making them available for review.

## {{ book.must }} Events should define useful business resources
 
 Events are intended to be used by other services including business process/data analytics and monitoring. They should be based around the resources and business processes you have defined for your service domain and adhere to its natural lifecycle (see also "Should: Define useful resources" in the [General Guidelines](../general-guidelines/GeneralGuidelines.md)).
 
 As there is a cost in creating an explosion of event types and topics, prefer to define event types that are abstract/generic enough to be valuable for multiple use cases, and avoid publishing event types without a clear need.

## {{ book.must }} Event Types must conform to Nakadi's API

Nakadi defines a structure called an _EventType_, which describes details for a particular kind of event. The EventType declares standard information, such as a name, an owning application (and by implication, an owning team), a well known event category (business process or data change), and a schema defining the event payload. It also allows the declaration of validation and enrichment strategies for events, along with supplemental information such as how events are partitioned in the stream. 

An EventType is registered with Nakadi via its _Schema Registry API_. Once the EventType is created, individual events that conform to the type and its payload schema can be published, and consumers can access them as stream of Events. [Nakadi's Open API definitions](https://github.com/zalando/nakadi/blob/nakadi-jvm/api/nakadi-event-bus-api.yaml) include the definitions for two main categories, business events (BusinessEvent), and data change events (DataChangeEvent), as well as a generic 'undefined' type. 

The service specific data defined for an event is called the _payload_. Only this non-Nakadi defined part of the event is expected to be submitted with the EventType schema.  When defining an EventType's payload schema as part of your API and for review purposes, it's ok to declare the payload as an object definition using Open API, but please note that Nakadi currently expects the EventType's schema to be submitted as JSON Schema and not as Open API JSON or YAML. Further details on how to register EventTypes are available in the Nakadi project's documentation.

## {{ book.must }} Use the Business Events to signal steps and arrival points in business processes

Nakadi defines a specific event type for business processes, called [BusinessEvent](https://github.com/zalando/nakadi/blob/nakadi-jvm/api/nakadi-event-bus-api.yaml#/definitions/BusinessEvent). When publishing events that represent steps in a business process, event types must be based on the BusinessEvent type.

All your events of a single business process will conform to the following rules:

- Business events must contain a specific identifier field (a business process id or "bp-id") similar to flow-id to allow for efficient aggregation of all events in a business process execution.

- Business events must contain a means to correctly order events in a business process execution. In distributed settings where monotically increasing values (such as a high precision timestamp that is assured to move forwards) cannot be obtained, Nakadi provides a `parent_eids` data structure that allows causal relationships to be declared between events.

- Business events should only contain information that is new to the business process execution at the specific step/arrival point.

- Each business process sequence should be started by a business event containing all relevant context information.

- Business events must be published reliably by the service.

At the moment we cannot state whether it's best practice to publish all the events for a business process using a single event type and represent the specific steps with a state field, or whether to use multiple event types to represent each step. For now we suggest assessing each option and sticking to one for a given business process.

## {{ book.must }} Use the Data Change Event structure to signal mutations

Nakadi defines an event for signalling data changes, called a [DataChangeEvent](https://github.com/zalando/nakadi/blob/nakadi-jvm/api/nakadi-event-bus-api.yaml#/definitions/DataChangeEvent). When publishing events that represents created, updated, or deleted data, change event types must be based on the DataChangeEvent category.

- Change events must identify the changed entity to allow aggregation of all related events for the entity.

- Change events should contain a means of ordering events for a given entity (such as created or updated timestamps that are assured to move forwards with respect to the entity, or version identifiers provided by some databases). Note that basing events on data structures that can be converged upon (such as [CRDTs](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type) or [logical clocks](https://en.wikipedia.org/wiki/Logical_clock)) in a distributed setting are outside the scope of this guidance.

- Change events must be published reliably by the service.

- When the `hash` partition strategy is used, change events should identify a partition key that allows all related events for the entity to be consistently to assigned to a partition.

## {{ book.should }} Data Change Events should match API representations

A data change event's representation of an entity should correspond to the REST API representation. 

There's value in having the fewest number of published structures for a service. Consumers of the service will be working with fewer representations, and the service owners will have less API surface to maintain. In particular, you should only publish events that are interesting in the domain and abstract away from implementation or local details - there's no need to reflect every change that happens within your system.

There are cases where it could make sense to define data change events that don't directly correspond to your API resource representations. Some examples are -
 
 - Where the API resource representations are very different from the datastore representation, but the physical data are easier to reliably process for data integration.

 - Publishing aggregated data. For example a data change to an individual entity might cause an event to be published that contains a coarser representation than that defined for an API

 - Events that are the result of a computation, such as a matching algorithm, or the generation of enriched data, and which might not be stored as entity by the service. 


## {{ book.must }} Event Types must indicate ownership

Event definitions must have clear ownership - this can be indicated via the `owning_application` field of the EventType. 

Typically there is one producer application, which owns the EventType and is responsible for its definition, akin to how RESTful API definitions are managed. However, the owner may also be a particular service from a set of multiple services that are producing the same kind of event. Please note indicating that a specific application is producing or consuming a specific event type is not yet defined explicitly, but a subject of Nakadi service discovery support functions.

## {{ book.must }} Event Payloads must be defined in accordance with the overall Guidelines

Events must be consistent with other API data and the API Guidelines in general. 

Everything expressed in the [Introduction to these Guidelines](../Introduction.md) is applicable to event data interchange between services. This is because our events, just like our APIs, represent a commitment to express what our systems do and designing high-quality, useful events allows us to develop new and interesting products and services. 

What distinguishes events from other kinds of data is the delivery style used, asynchronous publish-subscribe messaging. But there is no reason why they could not be made available using a REST API, for example via a search request or as a paginated feed, and it will be common to base events on the models created for the service’s REST API. 

The following existing guidelines for API data are applicable to events -

[General Guidelines](../general-guidelines/GeneralGuidelines.md) -

- Must: API First: Define APIs using OpenAPI YAML or JSON
- Must: Write in U.S. English

[Naming](../naming/Naming.md) -

- Must: JSON field names must be snake_case (never camelCase)

[Data Formats](../data-formats/DataFormats.md) -

- Must: Use JSON as the Body Payload
- Must: Use Standard Date and Time Formats
- Could: Use Standards for Country, Language and Currency Codes
- Could: Use Application-Specific Content Types

[Common Data Objects](../common-data-objects/CommonDataObjects.md) -

- Should: Use a Common Money Object
- Should: Use Common Address Fields

[Hypermedia](../hyper-media/Hypermedia.md) - 

- Should: Use HATEOAS. The use of hypermedia techniques are equally applicable to events.
- Could: Use URIs for Custom Link Relations
- Should: Consider Using a Standard for linked/embedded resources. 

There are a couple of technical considerations to bear in mind when defining event schema -

Note that Nakadi currently requires Open API event definitions to be submitted in JSON Schema syntax, and does not yet support Open API YAML. It's ok to define your events for peer review in YAML to avoid maintaining duplicate representations. This is best understood as point in time guidance - as the project develops it may support consuming Open API YAML (or other) structures directly, and the guidance here will be updated. 

## {{ book.must }} Maintain backwards compatibility for Events

Changes to events must be based around making additive and backward compatible changes. This follows the guideline, "Must: Don’t Break Backward Compatibility" from the [Compatibility guidelines](../compatibility/Compatibility.md). 

In the context of events, compatibility issues are complicated by the fact that producers and consumers of events are highly asynchronous and can’t use content-negotiation techniques that are available to REST style clients and servers. This places a higher bar on producers to maintain compatibility as they will not be in a position to serve versioned media types on demand. 

For event schema, these are considered backward compatible changes, as seen by consumers  -

- Adding new optional fields to JSON objects.
- Changing the order of fields (field order in objects is arbitrary).
- Changing the order of values with same type in an array.
- Removing optional fields.
- Removing an individual value from an enumeration.

These are considered backwards-incompatible changes, as seen by consumers  -

- Removing required fields from JSON objects.
- Changing the default value of a field.
- Changing the type of a field, object, enum or array.
- Changing the order of values with different type in an array (also known as a tuple).
- Adding a new optional field to redefine the meaning of an existing field (also known as a co-occurrence constraint).
- Adding a value to an enumeration (note that `x-extensible-enum` is not available in JSON Schema).

## {{ book.must }} Event identifiers must be unique

The `eid` (event identifier) value of an event must be unique.

The `eid` property is part of the standard metadata for an event and gives the event an identifier. Producing clients must generate this value when sending an event and it must be guaranteed to be unique from the perspective of the owning application. In particular events within a given event type's stream must have unique identifiers. This allows consumers to process the `eid` to assert the event is unique and use it as an idempotency check. 

Note that uniqueness checking of the `eid` is not enforced by Nakadi at the event type or global levels and it is the responsibility of the producer to ensure event identifiers do in fact distinctly identify events. A straightforward way to create a unique identifier for an event is to generate a UUID value.

## {{ book.must }} Event Type names must follow conventions

Event types can follow these naming conventions (each convention has its own should, must or could conformance level) -
 
 - Event type names must be url-safe. This is because the event type names are used by Nakadi as part of the URL for the event type and its stream.

 - Event type names should be lowercase words and numbers, using hypens, underscores or periods as separators. 

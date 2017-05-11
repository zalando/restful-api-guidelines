# Events

Zalando’s architecture centers around decoupled microservices and in that context we favour asynchronous event driven approaches. The [Nakadi](https://github.com/zalando/nakadi) framework provides an event publish and subscribe system, abstracting exchanges through a RESTful API. 

Nakadi is a key element for data integration in the architecture, and the standard system for inter-service event messaging. The guidelines in this section are for use with the Nakadi framework and focus on how to design and publish events intended to be shared for others to consume. Critically, once events pass service boundaries they are considered part of the service API and are subject to the API guidelines. 

## {{ book.must }} Treat Events as part of the service interface

Events are part of a service’s interface to the outside world equivalent in standing to a service’s REST API. Services publishing data for integration must treat their events as a first class design concern, just as they would an API. For example this means approaching events with the "API first" principle in mind [as described in the Introduction](../Introduction.md) and making them available for review.

## {{ book.must }} Ensure that Events define useful business resources
 
 Events are intended to be used by other services including business process/data analytics and monitoring. They should be based around the resources and business processes you have defined for your service domain and adhere to its natural lifecycle (see also "Should: Define useful resources" in the [General Guidelines](../general-guidelines/GeneralGuidelines.md)).
 
 As there is a cost in creating an explosion of event types and topics, prefer to define event types that are abstract/generic enough to be valuable for multiple use cases, and avoid publishing event types without a clear need.

## {{ book.must }} Ensure that Event Types conform to Nakadi's API

Nakadi defines a structure called an _EventType_, which describes details for a particular kind of event. The EventType declares standard information, such as a name, an owning application (and by implication, an owning team), a well known event category (business process or data change), and a schema defining the event payload. It also allows the declaration of validation and enrichment strategies for events, along with supplemental information such as how events are partitioned in the stream. 

An EventType is registered with Nakadi via its _Schema Registry API_. Once the EventType is created, individual events that conform to the type and its payload schema can be published, and consumers can access them as stream of Events. [Nakadi's Open API definitions](https://github.com/zalando/nakadi/blob/master/api/nakadi-event-bus-api.yaml) include the definitions for two main categories, business events (BusinessEvent), and data change events (DataChangeEvent), as well as a generic 'undefined' type. 

The service specific data defined for an event is called the _payload_. Only this non-Nakadi defined part of the event is expected to be submitted with the EventType schema.  When defining an EventType's payload schema as part of your API and for review purposes, it's ok to declare the payload as an object definition using Open API, but please note that Nakadi currently expects the EventType's schema to be submitted as JSON Schema and not as Open API JSON or YAML. Further details on how to register EventTypes are available in the Nakadi project's documentation.

## {{ book.must }} Events must not provide sensitive customer personal data.

Similar to API permission scopes, there will be Event Type permissions passed via an OAuth token supported in near future by the Nakadi API. Hence, Nakadi will restrict event data access to clients with sufficient authorization. However, teams are asked to note the following:

 - Sensitive data, such as (e-mail addresses, phone numbers, etc) are subject to strict access and data protection controls. 
 
 - Event type owners **must not** publish sensitive information unless it's mandatory or neccessary to do so. For example, events sometimes need to provide personal data, such as delivery addresses in shipment orders  (as do other APIs), and this is fine.

## {{ book.must }} Use Business Events to signal steps and arrival points in business processes

Nakadi defines a specific event type for business processes, called [BusinessEvent](https://github.com/zalando/nakadi/blob/nakadi-jvm/api/nakadi-event-bus-api.yaml#/definitions/BusinessEvent). When publishing events that represent steps in a business process, event types must be based on the BusinessEvent type.

All your events of a single business process will conform to the following rules:

- Business events must contain a specific identifier field (a business process id or "bp-id") similar to flow-id to allow for efficient aggregation of all events in a business process execution.

- Business events must contain a means to correctly order events in a business process execution. In distributed settings where monotically increasing values (such as a high precision timestamp that is assured to move forwards) cannot be obtained, Nakadi provides a `parent_eids` data structure that allows causal relationships to be declared between events.

- Business events should only contain information that is new to the business process execution at the specific step/arrival point.

- Each business process sequence should be started by a business event containing all relevant context information.

- Business events must be published reliably by the service.

At the moment we cannot state whether it's best practice to publish all the events for a business process using a single event type and represent the specific steps with a state field, or whether to use multiple event types to represent each step. For now we suggest assessing each option and sticking to one for a given business process.

## {{ book.must }} Use Data Change Events to signal mutations

Nakadi defines an event for signalling data changes, called a [DataChangeEvent](https://github.com/zalando/nakadi/blob/nakadi-jvm/api/nakadi-event-bus-api.yaml#/definitions/DataChangeEvent). When publishing events that represents created, updated, or deleted data, change event types must be based on the DataChangeEvent category.

- Change events must identify the changed entity to allow aggregation of all related events for the entity.
- Change events should [contain a means of ordering](#should-provide-a-means-of-event-ordering) events for a given entity.
- Change events must be published reliably by the service.

## {{ book.should }} Provide a means for explicit event ordering

While Nakadi guarantees ordering per partition, some common error cases may require your consumers to reconstruct message ordering and 
their last read position within the ordered stream. Events *should* therefore contain a way to restore their partial order of occurence. 

This can be done - among other ways -  by adding
- a strictly monotonically increasing entity version (e.g. as created by a database) to allow for partial ordering of all events for an entity
- a strictly monotonically increasing message counter

System timestamps are not necessarily a good choice, since exact synchronization of clocks in distributed systems is difficult, two events may occur in the same microsecond and system clocks may jump backward or forward to compensate drifts or leap-seconds. If you use system timestamps to indicate event ordering, you must carefully ensure that your designated event order is not messed up by these effects. 

**Note** that basing events on data structures that can be converged upon in a distributed setting (such as [CRDTs](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type), [logical clocks](https://en.wikipedia.org/wiki/Logical_clock) and [vector clocks](https://en.wikipedia.org/wiki/Vector_clock)) is outside the scope of this guidance.

## {{ book.should }} Use the hash partition strategy for Data Change Events

The `hash` partition strategy allows a producer to define which fields in an event are used as input to compute the partition the event should be added to. 

The `hash` option is particulary useful for data changes as it allows all related events for an entity to be consistently assigned to a partition, providing an ordered stream of events for that entity as they arrive at Nakadi. This is because while each partition has a total ordering, ordering across partitions is not assured, thus it is possible for events sent across partitions to appear in a different order to consumers that the order they arrived at the server. Note that as of the time of writing, random is the default option in Nakadi and thus the `hash` option must be declared when creating the event type.

When using the `hash` strategy the partition key in almost all cases should represent the entity being changed and not a per event or change identifier such as the `eid` field or a timestamp. This ensures data changes arrive at the same partition for a given entity and can be consumed effectively by clients.

There may be exceptional cases where data change events could have their partition strategy set to be the producer defined or random options, but generally `hash` is the right option - that is while the guidelines here are a "should", they can be read as "must, unless you have a very good reason". 

## {{ book.should }} Ensure that Data Change Events match API representations

A data change event's representation of an entity should correspond to the REST API representation. 

There's value in having the fewest number of published structures for a service. Consumers of the service will be working with fewer representations, and the service owners will have less API surface to maintain. In particular, you should only publish events that are interesting in the domain and abstract away from implementation or local details - there's no need to reflect every change that happens within your system.

There are cases where it could make sense to define data change events that don't directly correspond to your API resource representations. Some examples are -
 
 - Where the API resource representations are very different from the datastore representation, but the physical data are easier to reliably process for data integration.

 - Publishing aggregated data. For example a data change to an individual entity might cause an event to be published that contains a coarser representation than that defined for an API

 - Events that are the result of a computation, such as a matching algorithm, or the generation of enriched data, and which might not be stored as entity by the service. 


## {{book.must}} Permissions on events must correspond to API permissions
If a resource can be read synchronously via a REST API and read asynchronously via an event, the same read-permission must apply: We want to protect access to data, not the way data is accessed.


## {{ book.must }} Indicate ownership of Event Types

Event definitions must have clear ownership - this can be indicated via the `owning_application` field of the EventType. 

Typically there is one producer application, which owns the EventType and is responsible for its definition, akin to how RESTful API definitions are managed. However, the owner may also be a particular service from a set of multiple services that are producing the same kind of event. Please note indicating that a specific application is producing or consuming a specific event type is not yet defined explicitly, but a subject of Nakadi service discovery support functions.

## {{ book.must }} Define Event Payloads in accordance with the overall Guidelines

Events must be consistent with other API data and the API Guidelines in general. 

Everything expressed in the [Introduction to these Guidelines](../Introduction.md) is applicable to event data interchange between services. This is because our events, just like our APIs, represent a commitment to express what our systems do and designing high-quality, useful events allows us to develop new and interesting products and services. 

What distinguishes events from other kinds of data is the delivery style used, asynchronous publish-subscribe messaging. But there is no reason why they could not be made available using a REST API, for example via a search request or as a paginated feed, and it will be common to base events on the models created for the service’s REST API. 

The following existing guideline sections are applicable to events -

- [General Guidelines](../general-guidelines/GeneralGuidelines.md) 

- [Naming](../naming/Naming.md)

- [Data Formats](../data-formats/DataFormats.md)

- [Common Data Objects](../common-data-objects/CommonDataObjects.md)

- [Hypermedia](../hyper-media/Hypermedia.md) - 

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
- Adding a value to an enumeration (note that [`x-extensible-enum`](../compatibility/Compatibility.md#should-used-openended-list-of-values-xextensibleenum-instead-of-enumerations) is not available in JSON Schema).

## {{ book.should }} Avoid `additionalProperties` in event type definitions

Event type schema should avoid using `additionalProperties` declarations, in 
order to support schema evolution.

Events are often intermediated by publish/subscribe systems and are 
commonly captured in logs or long term storage to be read later. In 
particular, the schemas used by publishers and consumers can  
drift over time. As a result, compatibility and extensibility issues 
that happen less frequently with client-server style APIs become important 
and regular considerations for event design. The guidelines recommend the 
following to enable event schema evolution:

- Publishers who intend to provide compatibility and allow their schemas 
  to evolve safely over time must define new optional fields 
  (as additive changes) and update their schemas in advance of publishing 
  those fields. In doing so, they **must not** declare an `additionalProperties` 
  field as a wildcard extension point. 

- Consumers must ignore fields they cannot process and not raise 
  errors, just as they would as an API client. This can happen if they are 
  processing events with an older copy of the event schema than the one
  containing the new definitions specified by the publishers. 

Requiring event publishers to define their fields ahead of publishing avoids 
the problem of _field redefinition_. This is when a publisher defines a 
field to be of a different type that was already being emitted, or, is 
changing the type of an undefined field. Both of these are prevented by 
not using `additionalProperties`. 

Avoiding `additionalProperties` as described here also aligns with the approach
 taken by the Nakadi API's ["compatible mode"](http://zalando.github.io/nakadi-manual/docs/using/event-types.html#compatible) for event type schema.

See also "Treat Open API Definitions As Open For Extension By Default"  
in the [Compatibility](../compatibility/Compatibility.md#must-treat-open-api-definitions-as-open-for-extension-by-default) 
section for further guidelines on the use of `additionalProperties`. 

## {{ book.must }} Use unique Event identifiers

The `eid` (event identifier) value of an event must be unique.

The `eid` property is part of the standard metadata for an event and gives the event an identifier. Producing clients must generate this value when sending an event and it must be guaranteed to be unique from the perspective of the owning application. In particular events within a given event type's stream must have unique identifiers. This allows consumers to process the `eid` to assert the event is unique and use it as an idempotency check. 

Note that uniqueness checking of the `eid` is not enforced by Nakadi at the event type or global levels and it is the responsibility of the producer to ensure event identifiers do in fact distinctly identify events. A straightforward way to create a unique identifier for an event is to generate a UUID value.

## {{ book.should }} Design for idempotent out-of-order processing

Events that are designed for [idempotent](#must-use-unique-event-identifiers) out-of-order processing allow for extremely resilient systems: If processing an event fails, consumers and producers can skip/delay/retry it without stopping the world or corrupting the processing result. 

To enable this freedom of processing, you must explicitly design for idempotent out-of-order processing: Either your events must contain enough information to infer their original order during consumption or your domain must be designed in a way that order becomes irrelevant.

As common example similar to data change events, idempotent out-of-order processing can be supported by sending the following information:

- the process/resource/entity identifier,
- a [monotonically increasing ordering key](#should-provide-a-means-of-event-ordering) and
- the process/resource state after the change.

A receiver that is interested in the current state can then ignore events that are older than the last processed event of each resource. A receiver interested in the history of a resource can use the ordering key to recreate a (partially) ordered sequence of events.

## {{ book.must }} Follow conventions for Event Type names

Event types can follow these naming conventions (each convention has its own should, must or could conformance level) -
 
 - Event type names must be url-safe. This is because the event type names are used by Nakadi as part of the URL for the event type and its stream.

 - Event type names should be lowercase words and numbers, using hyphens, underscores or periods as separators.

## {{ book.must }} Prepare for duplicate Events

Event consumers must be able to process duplicate events.

Most of message broker and event bus systems, like Nakadi, guarantee “at-least-once” delivery. That is, one particular event is delivered to the consumers one or more times. Other circumstances can also cause duplicate events.

For example, these situations occur if the publisher sends an event and doesn't receive the acknowledgment (e.g. due to a network issue). In this case, the publisher will try to send the same event again. This leads to two identical events in the event bus which have to be processed by the consumers. Similar conditions can appear on consumer side: an event has been processed successfully, but the consumer fails to confirm the processing.

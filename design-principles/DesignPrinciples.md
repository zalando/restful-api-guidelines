# Principles

## API as a Product

As mentioned above, Zalando is transforming from an online shop into an expansive fashion platform 
comprising a rich set of products following a Software as a Platform (SaaS) model for our business partners. 
As a company we want to deliver products to our (internal and external) customers which can be consumed like a service. 

Platform products provide their functionality via (public) APIs; 
hence, the design of our APIs should be based on the API as a Product principle:

* Treat your API as product and understand the needs of its customers
* Take ownership and advocate for the customer and continuous improvement
* Emphasize easy understanding, discovery and usage of APIs; design APIs irresistible for client engineers
* Actively improve and maintain API consistency over the long term
* Make use of customer feedback and provide service level support

RESTful API as a Product makes the difference between enterprise integration business and agile, 
innovative product service business built on a platform of APIs.


Based on your concrete customer use cases, you should carefully check the trade-offs of API design variants 
and avoid short-term server side implementation optimizations at the expense of unnecessary client side 
obligations and have a high attention on API quality and client developer experience. 

API as a Product is closely related to our 
[API First principle](../general-guidelines/GeneralGuidelines.md#Must-Follow-API-First-Principle) 
(see next chapter) which is more focussed on how we engineer high quality APIs. 


## API Design Principles

Comparing SOA web service interfacing style of SOAP vs. REST,
the former tend to be centered around operations that are usually use-case specific and specialized.
In contrast, REST is centered around business (data) entities exposed as resources 
that are identified via URIs and can be manipulated via standardized CRUD-like methods 
using different representations, self-descriptive messages and hypermedia. 
RESTful APIs tend to be less use-case specific and comes with less rigid client / server coupling 
and are more suitable as a platform interface being open for diverse client applications. 

* We prefer REST-based APIs with JSON payloads
* We prefer systems to be truly RESTful

We apply the RESTful web service principles to all kind of application components, whether they provide functionality via the Internet or via the intranet as larger application elements. We strive to build interoperating distributed systems that different teams can evolve in parallel.

An important principle for (RESTful) API design and usage is Postel's Law, aka [the
Robustness Principle](http://en.wikipedia.org/wiki/Robustness_principle) (RFC 1122):

* Be liberal in what you accept, be conservative in what you send


*Readings:* Read the following to gain additional insight on the RESTful service architecture paradigm and
general RESTful API design style:

* Book: [Irresistable APIs: Designing web APIs that developers will love](https://www.amazon.de/Irresistible-APIs-Designing-that-developers/dp/1617292559)
* Book: [REST in Practice: Hypermedia and Systems
  Architecture](http://www.amazon.de/REST-Practice-Hypermedia-Systems-Architecture/dp/0596805829)
* Book: [Build APIs You Won't Hate](https://leanpub.com/build-apis-you-wont-hate)
* InfoQ eBook: [Web APIs: From Start to Finish](http://www.infoq.com/minibooks/emag-web-api)
* Lessons-learned blog: [Thoughts on RESTful API Design](http://restful-api-design.readthedocs.org/en/latest/)
* Fielding Dissertation: [Architectural Styles and the Design of Network-Based Software Architectures](http://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm)

*Footnote:* 
Per definition of R.Fielding REST APIs have to support HATEOAS (maturity level 3). Our guidelines do not strongly advocate for full REST compliance, but limited hypermedia usage, e.g. for pagination (see [Hypermedia Section](../hyper-media/Hypermedia.md#Hypermedia) below). However, we still use the term "RESTful API", due to the absence of an alternative established term and to keep it like the very majority of web service industry that also use the term for their REST approximations — in fact, in today's industry full HATEOAS compliant APIs are a very rare exception.



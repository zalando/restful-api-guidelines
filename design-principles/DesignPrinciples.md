# Design Principles

To compare the interface design approaches of SOAP-based web services to those of REST services,
the former tend to be centered around operations that are usually use-case specific and specialized.
In contrast, REST is all about (business) entities found in the system and exposed as resource
endpoints — leading to interfaces that are more broadly usable (here, clients sometimes have to
filter out unnecessary information).

## API Design Principles

1. We prefer REST-based APIs with JSON payloads
2. We prefer systems to be truly RESTful
3. We strive to build interoperating distributed systems that different teams can evolve in parallel

An important principle for (RESTful) API design and usage is Postel's Law, aka [the
Robustness Principle](http://en.wikipedia.org/wiki/Robustness_principle) (RFC 1122):
“Be liberal in what you accept, be conservative in what you send.”

Read the following to gain additional insight on the RESTful service architecture paradigm and
general RESTful API design style:

* Fielding Dissertation: [Architectural Styles and the Design of Network-Based Software
  Architectures](http://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm)
* Book: [REST in Practice: Hypermedia and Systems
  Architecture](http://www.amazon.de/REST-Practice-Hypermedia-Systems-Architecture/dp/0596805829)
* Book: [Build APIs You Won't
  Hate](https://leanpub.com/build-apis-you-wont-hate)
* InfoQ eBook: [Web APIs: From Start to
  Finish](http://www.infoq.com/minibooks/emag-web-api)
* Lessons-learned blog: [Thoughts on RESTful API
  Design](http://restful-api-design.readthedocs.org/en/latest/)

We apply the RESTful web service principles to all kind of application components, whether they provide functionality via the Internet or via the intranet as larger application elements. We strive to build interoperating distributed systems that different teams can evolve in parallel.

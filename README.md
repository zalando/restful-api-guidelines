# Developing Restful APIs: A Comprehensive Set of Guidelines by Zalando

[![Build Status](https://travis-ci.org/zalando/restful-api-guidelines.svg?branch=master)](https://travis-ci.org/zalando/restful-api-guidelines)
Latest published version: **[HTML](http://zalando.github.io/restful-api-guidelines)**

Purpose
-------
Great RESTful APIs look like they were designed by a single team. This promotes API adoption, reduces friction, and enables clients to use them properly. To build APIs that meet this standard, and to answer many common questions encountered along the way of RESTful API development, the Zalando Tech team has created this comprehensive set of guidelines. We have shared it with you to inspire additional discussion and refinement within and among your teams, and contribute our learnings and suggestions to the tech community at large.

Usage
-----
Feel free to use these guidelines as a guidance for your own development. Note that we encourage our own teams to use them in order to challenge their APIs. As such, you should consider this to be a living, evolving document. We will revise and update based on our learnings and experiences.

E-Book Support
-----

You can easily generate PDF, ePub, Mobi files out of our guidelines. Please refer to this [explanation](https://toolchain.gitbook.com/ebook.html) - you have to install Callibre.
You have to use node version 5.1.1 to build the guidelines.
CAUTION: you need to add version 2.6.7 identifier to every build command:

  ```bash
  $ npm install

  # Generate a PDF file
  $ ./node_modules/.bin/gitbook -v 2.6.7 pdf ./ ./api-guidelines.pdf

  # Generate an ePub file
  $ ./node_modules/.bin/gitbook -v 2.6.7 epub ./ ./api-guidelines.epub

  # Generate a Mobi file
  $ ./node_modules/.bin/gitbook -v 2.6.7 mobi ./ ./api-guidelines.mobi
  ```

Table of Contents
-------
After a short Introduction, these guidelines include chapters on the following topics:
- [Design Principles](http://zalando.github.io/restful-api-guidelines/design-principles/DesignPrinciples.html)
- [General Guidelines](http://zalando.github.io/restful-api-guidelines/general-guidelines/GeneralGuidelines.html)
- [Security](http://zalando.github.io/restful-api-guidelines/security/Security.html)
- [Compatibility](http://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html)
- [JSON Guidelines](http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html)
- [Naming](http://zalando.github.io/restful-api-guidelines/naming/Naming.html)
- [Resources](http://zalando.github.io/restful-api-guidelines/resources/Resources.html)
- [HTTP](http://zalando.github.io/restful-api-guidelines/http/Http.html)
- [Performance](http://zalando.github.io/restful-api-guidelines/performance/Performance.html)
- [Pagination](http://zalando.github.io/restful-api-guidelines/pagination/Pagination.html)
- [Hypermedia](http://zalando.github.io/restful-api-guidelines/hyper-media/Hypermedia.html)
- [Data Formats](http://zalando.github.io/restful-api-guidelines/data-formats/DataFormats.html)
- [Common Data Objects](http://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html)
- [Common Headers](http://zalando.github.io/restful-api-guidelines/headers/CommonHeaders.html)
- [Proprietary Headers](http://zalando.github.io/restful-api-guidelines/headers/ProprietaryHeaders.html)
- [API Operation](http://zalando.github.io/restful-api-guidelines/api-operation/ApiOperation.html)
- [Events](http://zalando.github.io/restful-api-guidelines/events/events.html)
- [Tooling](http://zalando.github.io/restful-api-guidelines/tooling/Tooling.html)

License
-------
We have published these guidelines under the CC-BY (Creative commons Attribution 4.0) license. Please see [LICENSE file](LICENSE).

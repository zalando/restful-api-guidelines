= Build

== Create HTML site
```
docker run --interactive --volume=.:/documents/ asciidoctor/docker-asciidoctor:latest asciidoctor -D /documents/output index.adoc
```
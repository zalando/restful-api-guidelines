#!/usr/bin/env bash
# Script to build Zalando RESTful Guidelines (static HTML, PDF)

set -ex #print commands, exit on failure

BUILD_DIR=docs

rm -rf ${BUILD_DIR}/* #clean

cp -r assets ${BUILD_DIR}/ #copy assets

asciidoctor -D ${BUILD_DIR} index.adoc #generate HTML

asciidoctor-pdf -D ${BUILD_DIR} index.adoc #generate PDF
asciidoctor-epub3 -D ${BUILD_DIR} index.adoc #generate EPUB3

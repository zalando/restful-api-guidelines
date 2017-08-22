#!/usr/bin/env bash
# Script to build Zalando RESTful Guidelines (static HTML, PDF)

set -ex #print commands, exit on failure

BUILD_DIR=output

rm -rf ${BUILD_DIR}/* #clean
cp -r assets ${BUILD_DIR}/ #copy assets
cp -r legacy/* ${BUILD_DIR}/ #copy assets
asciidoctor -D ${BUILD_DIR} index.adoc #generate HTML
asciidoctor-pdf -D ${BUILD_DIR} index.adoc #generate PDF
asciidoctor-epub3 -D ${BUILD_DIR} index.adoc #generate EPUB3

mv ${BUILD_DIR}/index.pdf ${BUILD_DIR}/zalando-restful-guidelines.pdf
mv ${BUILD_DIR}/index.epub ${BUILD_DIR}/zalando-restful-guidelines.epub

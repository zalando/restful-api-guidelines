#!/usr/bin/env bash
# Script to build ValiMail RESTful Guidelines (static HTML, PDF)

#set -ex

pushd `dirname $0` > /dev/null
SCRIPT_DIR=`pwd -P`
popd > /dev/null
BUILD_DIR=${SCRIPT_DIR}/output

rm -rf ${BUILD_DIR}
mkdir ${BUILD_DIR}
docker pull asciidoctor/docker-asciidoctor

./check_rule_ids.sh

docker run -v ${SCRIPT_DIR}:/documents/ asciidoctor/docker-asciidoctor asciidoctor -D /documents/output index.adoc
docker run -v ${SCRIPT_DIR}:/documents/ asciidoctor/docker-asciidoctor asciidoctor-pdf -D /documents/output index.adoc
docker run -v ${SCRIPT_DIR}:/documents/ asciidoctor/docker-asciidoctor asciidoctor-epub3 -D /documents/output index.adoc

cp -r assets ${BUILD_DIR}/
cp -r -n legacy/* ${BUILD_DIR}/

./generate_rules_json.sh

mv ${BUILD_DIR}/index.pdf ${BUILD_DIR}/valimail-guidelines.pdf
mv ${BUILD_DIR}/index.epub ${BUILD_DIR}/valimail-guidelines.epub

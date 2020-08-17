#!/usr/bin/env bash
# Script to build Zalando RESTful Guidelines (static HTML, PDF)

DOCKER_ASCIIDOCTOR=asciidoctor/docker-asciidoctor:1.1.0

set -ex

pushd `dirname $0` > /dev/null
SCRIPT_DIR=`pwd -P`
popd > /dev/null
BUILD_DIR=${SCRIPT_DIR}/output

rm -rf ${BUILD_DIR}
mkdir ${BUILD_DIR}
docker pull ${DOCKER_ASCIIDOCTOR}

./check_rule_ids.sh

docker run -v ${SCRIPT_DIR}:/documents/ ${DOCKER_ASCIIDOCTOR} asciidoctor -D /documents/output index.adoc
docker run -v ${SCRIPT_DIR}:/documents/ ${DOCKER_ASCIIDOCTOR} asciidoctor-pdf -D /documents/output index.adoc
docker run -v ${SCRIPT_DIR}:/documents/ ${DOCKER_ASCIIDOCTOR} asciidoctor-epub3 -D /documents/output index.adoc

cp models/money-1.0.0.yaml ${BUILD_DIR}/
cp -r assets ${BUILD_DIR}/
cp -r -n legacy/* ${BUILD_DIR}/

./generate_rules_json.sh

mv ${BUILD_DIR}/index.pdf ${BUILD_DIR}/zalando-guidelines.pdf
mv ${BUILD_DIR}/index.epub ${BUILD_DIR}/zalando-guidelines.epub

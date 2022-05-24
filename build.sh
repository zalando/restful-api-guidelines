#!/usr/bin/env bash
# Script to build RESTful Guidelines (static HTML only)

set -ex

pushd `dirname $0` > /dev/null
SCRIPT_DIR=`pwd -P`
popd > /dev/null
BUILD_DIR=${SCRIPT_DIR}/docs

rm -rf ${BUILD_DIR}
mkdir ${BUILD_DIR}
docker pull asciidoctor/docker-asciidoctor

./check_rule_ids.sh

docker run -v ${SCRIPT_DIR}:/documents/ asciidoctor/docker-asciidoctor asciidoctor -D /documents/docs index.adoc

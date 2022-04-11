#!/usr/bin/env bash
# Script to build Pon Guidelines (static HTML, PDF)

set -ex

./check_rule_ids.sh

pushd $(dirname "$0") > /dev/null
SCRIPT_DIR=`pwd -P`
popd > /dev/null
BUILD_DIR=${SCRIPT_DIR}/output

rm -rf ${BUILD_DIR}
mkdir "${BUILD_DIR}"

if command -v docker &> /dev/null
then
    docker pull asciidoctor/docker-asciidoctor

    docker run -v "${SCRIPT_DIR}:/documents/" asciidoctor/docker-asciidoctor asciidoctor --verbose --trace -D /documents/output index.adoc
    docker run -v "${SCRIPT_DIR}:/documents/" asciidoctor/docker-asciidoctor asciidoctor-pdf --verbose --trace -D /documents/output index.adoc
    # docker run -v ${SCRIPT_DIR}:/documents/ asciidoctor/docker-asciidoctor asciidoctor-epub3 --verbose --trace -D /documents/output index.adoc
else
    gem install asciidoctor
    gem install asciidoctor-pdf

    asciidoctor --verbose --trace -D ${BUILD_DIR} index.adoc
    asciidoctor-pdf --verbose --trace -D ${BUILD_DIR} index.adoc
fi

cp models/money-1.0.0.yaml "${BUILD_DIR}/"
cp -r assets "${BUILD_DIR}/"
# cp -r -n legacy/* ${BUILD_DIR}/

./generate_rules_json.sh

mv "${BUILD_DIR}/index.pdf" "${BUILD_DIR}/pon-guidelines.pdf"   
# mv ${BUILD_DIR}/index.epub ${BUILD_DIR}/pon-guidelines.epub

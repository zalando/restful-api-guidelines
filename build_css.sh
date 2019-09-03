#!/usr/bin/env bash
# Script to generate CSS for the Guidelines

set -ex #print commands, exit on failure

pushd `dirname $0` > /dev/null
GUIDELINES_DIR=`pwd`
popd > /dev/null

STYLESHEET_FACTORY_DIR=`mktemp -d`
mkdir -p ${STYLESHEET_FACTORY_DIR}

git clone git@github.com:asciidoctor/asciidoctor-stylesheet-factory.git ${STYLESHEET_FACTORY_DIR}
cd ${STYLESHEET_FACTORY_DIR} && bundle install

cp -r ${GUIDELINES_DIR}/sass/* ${STYLESHEET_FACTORY_DIR}/sass
cd ${STYLESHEET_FACTORY_DIR} && compass compile
cp ${STYLESHEET_FACTORY_DIR}/stylesheets/stylesheet.css ${GUIDELINES_DIR}/

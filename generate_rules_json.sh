#!/usr/bin/env bash
# This script generates a JSON with information about the guideline rules.

set -x

build_dir=${1:-output}

grep -Pzoh "(?s)\[(#[0-9]+)\]\n[=]+\s+.*?\n" chapters/* | \
        sed '$!N;s/\n/ /' | tr -d "[]{}\000" | sed '/^\s*$/d' | \
        sort | perl -p -e 'chomp if eof' | \
        jq -Rs '{rules: split("\n") }' | \
        jq '{rules: .rules | map(. | {id: split(" == ")[0], title: split(" == ")[1]})}' \
        > ${build_dir}/rules

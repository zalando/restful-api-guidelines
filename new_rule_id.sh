#!/usr/bin/env bash
# This script generates a new (unused) rule id.

CONTENT_DIR=chapters
rule_ids=()


IFS=$'\r\n' GLOBIGNORE='*' command eval "rule_ids=($(grep -r -h '^.*\[#[0-9]\{1,5\}.*$' ${CONTENT_DIR} | sort -r))"
last_used_rule_id=`echo ${rule_ids[0]} | tr -d '\[' | tr -d '\]' | tr -d '#'`

echo $((last_used_rule_id +1 ))
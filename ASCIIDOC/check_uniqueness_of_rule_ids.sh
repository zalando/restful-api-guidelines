#!/usr/bin/env bash
# This script checks if any rule id has been used more than once

CONTENT_DIR=chapters

duplicated_ids=`grep -r -h '^.*\[#[0-9]\{1,5\}.*$' ${CONTENT_DIR} | uniq -d`

if [ -z "$duplicated_ids" ]; then
    exit 0
else
    echo "Duplicated Rule IDs: `echo ${duplicated_ids} | tr -d '\n'`"
    exit 1
fi

#!/usr/bin/env bash
# This script checks if any rule id has been used more than once; can be also used as a pre-commit git hook ;)

CONTENT_DIR=chapters

duplicated_ids=`grep -r -h '^.*\[#[0-9]\{1,5\}.*$' ${CONTENT_DIR} | sort |uniq -d`

if [ -z "$duplicated_ids" ]; then
    exit 0
else
    echo "Duplicated Rule IDs: `echo ${duplicated_ids} | tr -d '\n'`"
    echo "Please make sure the Rule IDs are unique"
    exit 1
fi

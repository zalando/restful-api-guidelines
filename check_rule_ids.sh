#!/usr/bin/env bash
# This script checks if any rule id has been used more than once; can be also used as a pre-commit git hook ;)

CONTENT_DIR=chapters

duplicated_ids=`grep -r -o -E '\[#[0-9]+]' ${CONTENT_DIR} | sort |uniq -d`

if [ -n "$duplicated_ids" ]; then
    echo "Duplicated Rule IDs: `echo ${duplicated_ids} | tr -d '\n'`"
    echo "Please make sure the Rule ID anchors are unique"
    exit 1
fi

incorrect_ids=`grep -r -n -E '.*(\[[0-9]+\]|\[ +#?[0-9]+ +\]).*' ${CONTENT_DIR}`

if [ -n "${incorrect_ids}" ]; then
    echo -e "Incorrect Rule IDs:\n ${incorrect_ids}"
    echo "Please make sure that the Rule ID anchors conform to '\[#[0-9]+\]'"
    exit 1
fi

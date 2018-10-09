#!/usr/bin/env bash
# This script generates a JSON with information about the guideline rules.

build_dir=${1:-output}

cat chapters/*.adoc | \
	awk '
	BEGIN {	printf "{\"rules\": [";	}
	($1 ~ /\[#[0-9]+\]/) {
		if (_state == 2) {
			printf ","
		}
		gsub("[\]\[]+","")
		printf "{\"id\":\"" $1 "\"";
		_state=1;
		next;
	}
	(_state == 1) {
		gsub("[= ]+\{","")
		gsub("\}","")
		printf ", \"title\": \"" $0 "\"}";
		_state=2;
		next;
	}
	END { printf "]}" }' | \
	jq . > ${build_dir}/rules

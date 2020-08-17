#!/usr/bin/env bash
# This script generates a JSON with information about the guideline rules.

cat chapters/*.adoc | \
	awk '
	($1 ~ /\[#[0-9]+\]/) {
		gsub("[\\]\\[]+", "");
		keys[$1]=$1;
		_state=1;
		next;
	}
	(_state == 1) {
		gsub("[= ]+\\{", "");
		gsub("\\}","");
        gsub("\\\\","\\\\");
        gsub("\"","\\\"");
		for (key in keys) {
            printf "{\"id\":\"" key "\", \"title\": \"" $0 "\"}\n";
            delete keys[key];
		}
		_state=2;
		next;
	}';

#! /bin/bash

readonly DIR="${1}";
readonly SOURCES=("models/headers-1.0.0.yaml");
readonly HEADERS=($(cat "${SOURCES[@]}" | awk '
    BEGIN { mode = "parameters" }
    ($0 ~ "^#.*response headers") {
        mode = "headers"; next
    }
    ($0 ~ "^#.*request headers$") {
        mode = "parameters"; next
    }
    ($0 ~ "^#.*common headers$") {
        mode = "parameters"; next
    }
    ($0 ~ "^[A-Z][A-Za-z-]*:$") {
        print $0 mode
    }'))

for HEADER in "${HEADERS[@]}"; do
    NAME="${HEADER%%:*}"; MODE="${HEADER##*:}";
    if [ -n "${DIR}" ]; then
      FILE="${DIR}/$(echo ${NAME} | tr '[A-Z]' '[a-z]').yaml";
    else FILE="/dev/stdout"; fi;
    awk -v name="${NAME}" -v mode="${MODE}" '
        (($0 ~ "^[A-Z][A-Za-z-]*:$" || $0 ~ "^#.*$") && content) {
            print content; content = ""
        }

        (content) {
            if (mode == "headers" && ($1 == "in:" || $1 == "name:")) next;
            if ($0 != "") {
                content = content "\n      " $0; empty = 0
            } else if (!empty) {
                content = content "\n"; empty = 1
            }
        }

        ($0 ~ "^" name ":$") {
            content = "components:\n  " mode ":\n    " $0; empty = 0
        }
        
        END {
            if (content) { print content }
        }' "${SOURCES[@]}" > "${FILE}";
done;

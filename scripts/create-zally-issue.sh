#!/usr/bin/env bash

set -ex

GH_REPO="github.com/zalando/restful-api-guidelines.git"
GH_REPO_URL="https://api.github.com/repos/zalando/restful-api-guidelines"
ZALLY_REPO_URL="https://api.github.com/repos/zalando/zally"

create_zally_issue () {
    local commit message count=0;
    while [ "${count}" -lt 6 ]; do
        commit=$(curl -s "${GH_REPO_URL}/commits/${GITHUB_SHA}");
        message="$(echo "${commit}" | jq --raw-output '.message' || true)";
        if [ "${message}" != "No commit found for SHA: ${GITHUB_SHA}" ]; then
            break;
        fi;
        count=$((count + 1)); sleep 10;
    done;

    local files=($(echo "${commit}" | jq --raw-output '.files[].filename' || true))
    local chapters=false;
    for file in "${files[@]}"; do
        if [[ ${file} == chapters/* ]]; then chapters=true; break; fi;
    done;
    if [ ${chapters} = false ]; then
        echo "No changes, aborting issue creation (${GITHUB_SHA}}"; return;
    fi;

    local pull origin count=0;
    while [ "${count}" -lt 6 ]; do
         pull="$(curl -s "${GH_REPO_URL}/commits/${GITHUB_SHA}/pulls" | jq '.[0]')";
         origin="$(echo "${pull}" | jq --raw-output '.number')";
         if [ "${origin}" != "null" ]; then break; fi;
         count=$((count + 1)); sleep 10;
    done;

    if [ "${origin}" == "null" ]; then
        origin="$(echo "${commit}" | \
            jq --raw-output '.commit.message | capture("#(?<n>[0-9]+)( |$)") | .n')";
        pull="$(curl -s ${GH_REPO_URL}/pulls/${origin})";
    fi;

    local title url body;
    if [ "${origin}" != "null" ]; then
        title="$(echo "${pull}" | jq --raw-output '.title' | \
            sed 's/"/'"'"'/g' || true)";
        url="$(echo "${pull}" | jq --raw-output '.html_url' )";
        body="Please check if the PR ${url} introduces changes which are relevant to the Zally project.";
    else
        title="$(echo "${commit}" | jq --raw-output '.commit.message' | \
            sed 's/"/'"'"'/g' | head -n 1)";
        url="$(echo "${commit}" | jq --raw-output '.html_url')"
        body="Please check if the COMMIT ${url} introduces changes which are relevant to the Zally project.";
    fi;
    local data="{\"title\":\"${title}\", \"body\": \"${body}\", \"labels\": [\"guidelines-update\"]}";

    echo "Changes require zally issue creation (${GITHUB_SHA}}";
    curl -X POST --data "${data}" \
         -H 'Content-Type: application/json' \
         -H "Authorization: token ${GH_TOKEN}" \
         "${ZALLY_REPO_URL}/issues";
}

create_zally_issue


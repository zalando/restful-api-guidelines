#!/usr/bin/env bash
# This script runs on Travis CI and does the following things:
# 1. deploys generated files to gh-pages branch of the repository
# 2. creates a new issue in the Zally project if guideline content has been changed
# Both happens only on master branch update

set -ex

USER="ZalandoGhPages"
EMAIL="no-reply@zalando.de"
DEPLOY_MESSAGE="auto-deployment to the gh-branch"
GH_REPO="github.com/zalando/restful-api-guidelines.git"
GH_REPO_URL="https://api.github.com/repos/zalando/restful-api-guidelines"
ZALLY_REPO_URL="https://api.github.com/repos/zalando/zally"

deploy_gh_pages () {
    echo "Deploying to gh-pages branch"
    cd output
    git init
    git config user.name "${USER}"
    git config user.email "${EMAIL}"
    git add -A
    git commit -m "${DEPLOY_MESSAGE}"
    git push --force --quiet "https://${GH_TOKEN}@${GH_REPO}" master:gh-pages
    echo "Deployed successfully to gh-pages branch"
}

create_zally_issue () {
    local commit message count=0
    while [ "${count}" -lt 30 ]; do
	commit=$(curl -s "${GH_REPO_URL}/commits/${TRAVIS_COMMIT}");
	message="$(echo "${commit}" | jq --raw-output '.message' || true)";
	if [ "${message}" != "No commit found for SHA: ${TRAVIS_COMMIT}" ]; then
	    break;
	fi;
	count=$((count + 1)); sleep 1;
    done;

    local files=($(echo "${commit}" | jq --raw-output '.files[].filename' || true))
    local chapters=false;
    for file in "${files[@]}"; do
        if [[ ${file} == chapters/* ]]; then chapters=true; break; fi;
    done;
    if [ ${chapters} = false ]; then
	echo "No changes, aboring issue creation (${TRAVIS_COMMIT}}"; return;
    fi;

    local pulls="$(curl -s "-H Accept: application/vnd.github.groot-preview+json" \
		       "${GH_REPO_URL}/commits/${TRAVIS_COMMIT}/pulls")";
    local origin="$(echo "${pulls}" | jq --raw-output '.[0].number')";
    if [ -n "${origin}" ]; then
	local title="$(echo "${pulls}" | jq --raw-output '.[0].title' | sed "s/\"/\'/g" || true)";
	local url="$(echo "${pulls}" | jq --raw-output '.[0].html_url' )";
	local body="Please check if the PR ${url} introduces changes which are relevant to the Zally project.";
    else
	local title="$(echo "${commit}" | jq --raw-output '.commit.message' | head 1)";
	local url="$(echo "${commit}" | jq --raw-output '.html_url')"
	local body="Please check if the COMMIT ${url} introduces changes which are relevant to the Zally project.";
    fi;
    local data="{\"title\":\"${title}\", \"body\": \"${body}\", \"labels\": [\"guidelines-update\"]}";

    echo "Changes require zally issue reation (${TRAVIS_COMMIT}}";
    curl -X POST --data "${data}" \
         -H 'Content-Type: application/json' \
         -H "Authorization: token ${GH_TOKEN}" \
         "${ZALLY_REPO_URL}/issues";
}

if [[ "${TRAVIS}" = "true" && "${TRAVIS_SECURE_ENV_VARS}" = "true" && "${TRAVIS_PULL_REQUEST}" = "false" && "${TRAVIS_BRANCH}" = "master" ]]; then
    deploy_gh_pages
    create_zally_issue
else
    echo "It's not an update of the master branch, skipping the deployment"
fi

#!/usr/bin/env bash
# This script runs on Travis CI and does the following things:
# 1. deploys generated files to gh-pages branch of the repository
# 2. creates a new issue in the Zally project if guideline content has been changed
# Both happens only on master branch update

set -ex

USER="dewayne.richardson"
EMAIL="no-reply@valimail.com"
DEPLOY_MESSAGE="auto-deployment to the gh-branch"
GH_REPO="github.com/ValiMail/restful-api-guidelines.git"
GH_REPO_URL="https://api.github.com/repos/ValiMail/restful-api-guidelines"
GH_REPO_LINK="https://github.com/ValiMail/restful-api-guidelines"
ZALLY_REPO_URL="https://api.github.com/repos/ValiMail/zally"

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
    local pr_number count=0
    while [ -z "${pr_number}" -a "${count}" -lt 30 ]; do
	pr_number=$(curl -s "${GH_REPO_URL}/commits/${TRAVIS_COMMIT}" | \
            jq -r '.commit.message | capture("#(?<n>[0-9]+) ") | .n' || true)
	count=$((count + 1)); sleep 1
    done
    local changed_files=($(curl -s "${GH_REPO_URL}/pulls/${pr_number}/files" | \
        jq -r '.[] | .filename'))

    local content_changed=false
    for f in "${changed_files[@]}"
    do
        if [[ $f == chapters/* ]]; then
            content_changed=true
        fi
    done

    if [ "$content_changed" = true ]; then
        local title=$(curl -s ${GH_REPO_URL}/pulls/${pr_number} | jq -r '.title' | sed s/\"/\'/g)
        local body="Please check if the PR ${GH_REPO_LINK}/pull/${pr_number} introduces changes which are relevant to the Zally project."
        curl -X POST \
            -H 'Content-Type: application/json' \
            -H "Authorization: token ${GH_TOKEN}" \
            --data "{\"title\":\"${title}\", \"body\": \"${body}\", \"labels\": [\"guidelines-update\"]}" \
            "${ZALLY_REPO_URL}/issues"
    fi
}

if [[ "${TRAVIS}" = "true" && "${TRAVIS_SECURE_ENV_VARS}" = "true" && "${TRAVIS_PULL_REQUEST}" = "false" && "${TRAVIS_BRANCH}" = "master" ]]; then
    deploy_gh_pages
    create_zally_issue
else
    echo "It's not an update of the master branch, skipping the deployment"
fi

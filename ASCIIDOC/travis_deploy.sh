#!/usr/bin/env bash
# This script runs on Travis CI and deploy the generated files to gh-pages branch of the repository iff it's triggered
# by a merge into the master branch

set -ex

USER="ZalandoGhPages"
EMAIL="no-reply@zalando.de"
DEPLOY_MESSAGE="Deploy to GitHub Pages (auto)"

if [[ "${TRAVIS}" -eq "true" && "${TRAVIS_SECURE_ENV_VARS}" -eq "true" && "${TRAVIS_PULL_REQUEST}" -eq "false" && "${TRAVIS_BRANCH}" -eq "master" ]]; then
    echo "Deploying to gh-pages branch"
    cp -r assets output/
    cd output
    git init
    git config user.name "${USER}"
    git config user.email "${EMAIL}"
    git add -A
    git commit -m "${DEPLOY_MESSAGE}"
    git push --force --quiet "https://${GH_TOKEN}@${GH_REF}" master:gh-pages
    echo "Deployed successfully to gh-pages branch"
else
    echo "It's not an update of the master branch, skipping the deployment"
fi
#
# Makefile
#
# Requires:
#   npm
#
NODE_MODULES=node_modules

# tools
#
gitbook=$(NODE_MODULES)/.bin/gitbook
gh_pages=$(NODE_MODULES)/.bin/gh-pages

url=http://zalando.github.io/restful-api-guidelines

all: help

help:
	@echo ""
	@echo "make setup   - install dependencies"
	@echo "make init    - initialize book"
	@echo "make build   - update the '_book/' files"
	@echo "make publish - update pages served by GitHub: $(url)"
	@echo ""

setup: | $(gitbook)

init: | $(gitbook)
	$(gitbook) init

build: | $(gitbook)
	$(gitbook) build -v 2.6.7

publish: build | $(gh_pages)
	$(gh_pages) -d _book

#---
$(NODE_MODULES)/gitbook-plugin-%/package.json: | $(gitbook)
	$(gitbook) install

#---
$(NODE_MODULES)/.bin/%:
	npm install

#---
echo:
	@echo $aaa

.PHONY: all help setup init build publish echo

# Remove output file if task fails
#
.DELETE_ON_ERROR:

SHELL := /bin/bash 
DOCKER := asciidoctor/docker-asciidoctor:latest
DIRMOUNTS := /documents
DIRCONTENTS := chapters
DIRSCRIPTS := scripts
DIRBUILDS := output
DIRINCLUDES := includes
DIRWORK := $(shell pwd -P)

.PHONY: all clean install lint format pull assets rules html pdf epub force
.PHONY: check check-rules check-rules-duplicates check-rules-incorrects
.PHONY: next-rule-id

all: clean html rules
clean:
	rm -rf $(DIRBUILDS) $(DIRINCLUDES);

install: $(NVM_BIN)/markdownlint
$(NVM_BIN)/markdownlint:
	npm install --global markdownlint-cli;
lint: $(NVM_BIN)/markdownlint
	markdownlint --config .markdownlint.yaml chapters/*.adoc;
format: $(NVM_BIN)/markdownlint
	markdownlint --config .markdownlint.yaml --fix chapters/*.adoc;

pull:
	docker pull $(DOCKER);

check: check-rules
check-rules: check-rules-duplicates check-rules-incorrects
check-rules-duplicates:
	@DUPLICATED="$$(grep -roE "\[#[0-9]+]" $(DIRCONTENTS) | sort |uniq -d)"; \
	if [ -n "$${DUPLICATED}" ]; then \
	    echo "Duplicated Rule IDs: $$(echo "$${DUPLICATED}" | tr -d '\n')"; \
	    echo "Please make sure the Rule ID anchors are unique"; \
	    exit 1; \
	fi;

check-rules-incorrects:
	@INCORRECT="$$(grep -rnE "(\[[0-9]+\]|\[ +#?[0-9]+ +\])" $(DIRCONTENTS))"; \
	if [ -n "$${INCORRECT}" ]; then \
	    echo -e "Incorrect Rule IDs:\n $${INCORRECT}"; \
	    echo "Please make sure that the Rule ID anchors conform to '\[#[0-9]+\]'"; \
	    exit 1; \
	fi;

next-rule-id:
	@IFS=$$'\r\n' GLOBIGNORE='*' command eval \
	"RULE_IDS=($$(grep -rh "^.*\[#[0-9]\{1,5\}.*$$" $(DIRCONTENTS) | sort -r))"; \
	echo $$(($$(echo $${RULE_IDS[0]} | tr -d '\[' | tr -d '\]' | tr -d '#') + 1));

assets:
	mkdir -p $(DIRBUILDS);
	cp -r assets $(DIRBUILDS)/;
	cp -r models $(DIRBUILDS)/;
	cp -r models/{problem-1.0.{0,1},money-1.0.0}.yaml $(DIRBUILDS);

rules: check-rules
	$(DIRSCRIPTS)/generate-rules-json.sh  | \
	  jq -s '{rules: . | sort}' | tee $(DIRBUILDS)/rules >$(DIRBUILDS)/rules.json;

$(DIRINCLUDES): models/headers-1.0.0.yaml $(DIRSCRIPTS)/generate-includes.sh
	mkdir -p $(DIRINCLUDES); $(DIRSCRIPTS)/generate-includes.sh "$(DIRINCLUDES)";

html: $(DIRINCLUDES) check assets pull
	docker run -v $(DIRWORK):$(DIRMOUNTS)/ ${DOCKER} asciidoctor \
	  -D $(DIRMOUNTS)/$(DIRBUILDS) index.adoc;

# Not used any longer.
pdf: $(DIRINCLUDES) check pull
	docker run -v $(DIRWORK):$(DIRMOUNTS)/ ${DOCKER} asciidoctor-pdf -v \
		-a pdf-fontsdir=$(DIRMOUNTS)/resources/fonts \
		-a pdf-theme=$(DIRMOUNTS)/resources/themes/pdf-theme.yml \
	  -D $(DIRMOUNTS)/$(DIRBUILDS) index.adoc;
	mv -f $(DIRBUILDS)/index.pdf $(DIRBUILDS)/zalando-guidelines.pdf;

# Not used any longer.
epub: $(DIRINCLUDES) check pull
	docker run -v $(DIRWORK):$(DIRMOUNTS)/ ${DOCKER} asciidoctor-epub3 \
	  -D $(DIRMOUNTS)/$(DIRBUILDS) index.adoc;
	mv -f $(DIRBUILDS)/index.epub $(DIRBUILDS)/zalando-guidelines.epub;

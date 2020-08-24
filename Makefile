DOCKER := asciidoctor/docker-asciidoctor:latest
DIRMOUNTS := /documents
DIRCONTENTS := chapters
DIRSCRIPTS := scripts
DIRBUILDS := output
DIRWORK := $(shell pwd -P)

.PHONY: all clean pull check assets
.PHONY: rules html pdf epub force

all: clean html pdf epub rules
clean:
	rm -rf $(DIRBUILDS);

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
	cp -r assets $(DIRBUILDS)/assets;
	cp -r models/* $(DIRBUILDS);
	cp -r -n legacy/* $(DIRBUILDS);

rules: check-rules
	$(DIRSCRIPTS)/generate-rules-json.sh  | \
	  jq -s '{rules: . | sort}' | tee $(DIRBUILDS)/rules >$(DIRBUILDS)/rules.json;

html: check assets pull
	docker run -v $(DIRWORK):$(DIRMOUNTS)/ ${DOCKER} asciidoctor \
	  -D $(DIRMOUNTS)/$(DIRBUILDS) index.adoc;

pdf: check pull
	docker run -v $(DIRWORK):$(DIRMOUNTS)/ ${DOCKER} asciidoctor-pdf \
	  -D $(DIRMOUNTS)/$(DIRBUILDS) index.adoc;
	mv -f $(DIRBUILDS)/index.pdf $(DIRBUILDS)/zalando-guidelines.pdf;

epub: check pull
	docker run -v $(DIRWORK):$(DIRMOUNTS)/ ${DOCKER} asciidoctor-epub3 \
	  -D $(DIRMOUNTS)/$(DIRBUILDS) index.adoc;
	mv -f $(DIRBUILDS)/index.epub $(DIRBUILDS)/zalando-guidelines.epub;

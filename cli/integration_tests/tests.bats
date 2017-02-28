#!/usr/bin/env bats

@test "CLI requires filename or URL to be specified" {
	run cli/bin/zally
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "Please provide a swagger file path or URL" ]
	[ "${lines[1]}" = "Validates the given swagger specification using Zally service" ]
}

@test "CLI works with local .yaml file" {
	run cli/bin/zally server/src/test/resources/fixtures/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "[31m" ]
	[ "${lines[1]}" = "Found the following MUST violations" ]
	[ "${lines[2]}" = "===================================" ]
	[ "${lines[52]}" = "[0mMUST violations: 6" ]
	[ "${lines[53]}" = "SHOULD violations: 0" ]
	[ "${lines[54]}" = "COULD violations: 0" ]
	[ "${lines[55]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 56 ]
}

@test "CLI works with local .json file" {
	run cli/bin/zally server/src/test/resources/fixtures/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "[31m" ]
	[ "${lines[1]}" = "Found the following MUST violations" ]
	[ "${lines[2]}" = "===================================" ]
	[ "${lines[41]}" = "[33m" ]
	[ "${lines[42]}" = "Found the following SHOULD violations" ]
	[ "${lines[43]}" = "=====================================" ]
	[ "${lines[57]}" = "[32m" ]
	[ "${lines[58]}" = "Found the following COULD violations" ]
	[ "${lines[59]}" = "====================================" ]
	[ "${lines[70]}" = "[0mMUST violations: 2" ]
	[ "${lines[71]}" = "SHOULD violations: 1" ]
	[ "${lines[72]}" = "COULD violations: 1" ]
	[ "${lines[73]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 74 ]
}

@test "CLI works with remote .yaml file" {
	run cli/bin/zally https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "[31m" ]
	[ "${lines[1]}" = "Found the following MUST violations" ]
	[ "${lines[2]}" = "===================================" ]
	[ "${lines[52]}" = "[0mMUST violations: 6" ]
	[ "${lines[53]}" = "SHOULD violations: 0" ]
	[ "${lines[54]}" = "COULD violations: 0" ]
	[ "${lines[55]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 56 ]
}

@test "CLI works with remote .json file" {
	run cli/bin/zally https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "[31m" ]
	[ "${lines[1]}" = "Found the following MUST violations" ]
	[ "${lines[2]}" = "===================================" ]
	[ "${lines[41]}" = "[33m" ]
	[ "${lines[42]}" = "Found the following SHOULD violations" ]
	[ "${lines[43]}" = "=====================================" ]
	[ "${lines[57]}" = "[32m" ]
	[ "${lines[58]}" = "Found the following COULD violations" ]
	[ "${lines[59]}" = "====================================" ]
	[ "${lines[70]}" = "[0mMUST violations: 2" ]
	[ "${lines[71]}" = "SHOULD violations: 1" ]
	[ "${lines[72]}" = "COULD violations: 1" ]
	[ "${lines[73]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 74 ]
}

@test "Zally API Specification is valid" {
	run cli/bin/zally server/src/main/resources/api/zally-api.yaml
	[ "$status" -eq 0 ]
	[ "${lines[10]}" = "[0mMUST violations: 0" ]
	[ "${lines[11]}" = "SHOULD violations: 0" ]
	[ "${lines[12]}" = "COULD violations: 0" ]
	[ "${lines[13]}" = "HINT violations: 1" ]
	[ "${#lines[@]}" -eq 14 ]
}

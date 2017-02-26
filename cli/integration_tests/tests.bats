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
	[ "${lines[121]}" = "[0mMUST violations: 24" ]
	[ "${lines[122]}" = "SHOULD violations: 0" ]
	[ "${lines[123]}" = "COULD violations: 0" ]
	[ "${lines[124]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 125 ]
}

@test "CLI works with local .json file" {
	run cli/bin/zally server/src/test/resources/fixtures/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "[31m" ]
	[ "${lines[1]}" = "Found the following MUST violations" ]
	[ "${lines[2]}" = "===================================" ]
	[ "${lines[58]}" = "[33m" ]
	[ "${lines[59]}" = "Found the following SHOULD violations" ]
	[ "${lines[60]}" = "=====================================" ]
	[ "${lines[96]}" = "[32m" ]
	[ "${lines[97]}" = "Found the following COULD violations" ]
	[ "${lines[98]}" = "====================================" ]
	[ "${lines[107]}" = "[0mMUST violations: 11" ]
	[ "${lines[108]}" = "SHOULD violations: 7" ]
	[ "${lines[109]}" = "COULD violations: 1" ]
	[ "${lines[110]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 111 ]
}

@test "CLI works with remote .yaml file" {
	run cli/bin/zally https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "[31m" ]
	[ "${lines[1]}" = "Found the following MUST violations" ]
	[ "${lines[2]}" = "===================================" ]
	[ "${lines[121]}" = "[0mMUST violations: 24" ]
	[ "${lines[122]}" = "SHOULD violations: 0" ]
	[ "${lines[123]}" = "COULD violations: 0" ]
	[ "${lines[124]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 125 ]
}

@test "CLI works with remote .json file" {
	run cli/bin/zally https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "[31m" ]
	[ "${lines[1]}" = "Found the following MUST violations" ]
	[ "${lines[2]}" = "===================================" ]
	[ "${lines[58]}" = "[33m" ]
	[ "${lines[59]}" = "Found the following SHOULD violations" ]
	[ "${lines[60]}" = "=====================================" ]
	[ "${lines[96]}" = "[32m" ]
	[ "${lines[97]}" = "Found the following COULD violations" ]
	[ "${lines[98]}" = "====================================" ]
	[ "${lines[107]}" = "[0mMUST violations: 11" ]
	[ "${lines[108]}" = "SHOULD violations: 7" ]
	[ "${lines[109]}" = "COULD violations: 1" ]
	[ "${lines[110]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 111 ]
}

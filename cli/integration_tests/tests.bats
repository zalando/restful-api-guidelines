#!/usr/bin/env bats

# TODO: fix paths

@test "CLI requires filename or URL to be specified" {
	run ../bin/zally
	[ "$status" -eq 1 ]
	[ "$output" = "Please provide a swagger file path or URL" ]
}

@test "CLI works with local .yaml file" {
	run ../bin/zally ../../server/src/test/resources/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "Found the following MUST violations" ]
	[ "${lines[75]}" = "COULD violations: 0" ]
	[ "${lines[76]}" = "SHOULD violations: 0" ]
	[ "${lines[77]}" = "MUST violations: 24" ]
	[ "${#lines[@]}" -eq 78 ]
}

@test "CLI works with local .json file" {
	run ../bin/zally ../../server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "Found the following MUST violations" ]
	[ "${lines[65]}" = "COULD violations: 1" ]
	[ "${lines[66]}" = "SHOULD violations: 7" ]
	[ "${lines[67]}" = "MUST violations: 11" ]
	[ "${#lines[@]}" -eq 68 ]
}

@test "CLI works with remote .yaml file" {
	run ../bin/zally https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "Found the following MUST violations" ]
	[ "${lines[75]}" = "COULD violations: 0" ]
	[ "${lines[76]}" = "SHOULD violations: 0" ]
	[ "${lines[77]}" = "MUST violations: 24" ]
	[ "${#lines[@]}" -eq 78 ]
}

@test "CLI works with remote .json file" {
	run ../bin/zally https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "Found the following MUST violations" ]
	[ "${lines[65]}" = "COULD violations: 1" ]
	[ "${lines[66]}" = "SHOULD violations: 7" ]
	[ "${lines[67]}" = "MUST violations: 11" ]
	[ "${#lines[@]}" -eq 68 ]
}
